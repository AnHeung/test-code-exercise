package com.example.daggerhilttest.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.daggerhilttest.getOrAwaitValue
import com.example.daggerhilttest.launchFragmentInHiltContainer
import com.example.daggerhilttest.ui.ShoppingFragment
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
//@RunWith(AndroidJUnit4::class) // 여기는 java 환경이 아니라 안드로이드 환경이기 떄문에 계측테스트를 하려면 실제 에뮬기기나 실제기기로 돌려야한다.
@SmallTest // UnitTest
@HiltAndroidTest
class ShoppingDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this) // 테스트 인스턴스 삽입에 사용

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db") // TestAppModule 과 AppModule 과 같은 인스턴스를 제공하기 때문에 식별자를 설정
    lateinit var database: ShoppingItemDatabase
    private lateinit var dao: ShoppingDao

    @Before
    fun setup() {
        //ram 에 저장되는 데이터 베이스
        hiltRule.inject() //해당 명령어를 통해 annotation 붙은 변수들에게 인스턴스가 생성된다.
        dao = database.shoppingDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun testLaunchFragmentInHiltContainer(){
        launchFragmentInHiltContainer<ShoppingFragment> {

        }
    }


    @Test
    fun insertShoppingItem() =
        runBlockingTest { // test 에 특화된 runBlocking 으로 delay 같은 함수가 있을시 skip 해줌
            val shoppingItem = ShoppingItem("name", 1, 1f, "url", id = 1)
            //아이템을 추가했으니 잘 들어갔는지를 체크하기 위해 dao 에서 해당 id 값으로 검색하는 함수를 추가하면 좋을것 같지만 실제앱에서 해당 함수가 필요하지 않다면 구지 만들 필요는 X
            //차라리 observeAllShoppingItems 함수로 불러서 확인하면 됨.
            dao.insertShoppingItem(shoppingItem)

            // 다 좋은데 livedata 고(실제 리스트가 아니라) 비동기적으로 동작하므로 구글에서 제공하는 함수를 사용하는것이 좋다.
            val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

            assertThat(allShoppingItems).contains(shoppingItem) //만약 그냥 돌릴경우 This job has not completed yet 에러가 발생한다. 기본적으로 JUnit 은 같은 스레드에서 동작하는것을 원하므로 명확하게 룰을 명시해야한다.
        }

    @Test
    fun deleteShoppingItem() = runBlockingTest {
        val shoppingItem = ShoppingItem("name", 1, 1f, "url", id = 1)
        dao.insertShoppingItem(shoppingItem)
        dao.deleteShoppingItem(shoppingItem) //각각 독립적으로 돌아야 하므로 새로운 database 에 값을 넣고 삭제함으로 테스트 진행

        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

        assertThat(allShoppingItems).doesNotContain(shoppingItem)
    }

    @Test
    fun observeTotalPriceSum() = runBlockingTest {
        val shoppingItem1 = ShoppingItem("name", 2, 10f, "url", id = 1)
        val shoppingItem2 = ShoppingItem("name", 4, 5.5f, "url", id = 2)
        val shoppingItem3 = ShoppingItem("name", 0, 100f, "url", id = 3)
        dao.insertShoppingItem(shoppingItem1)
        dao.insertShoppingItem(shoppingItem2)
        dao.insertShoppingItem(shoppingItem3)

        val totalPriceSum = dao.observeTotalPrice().getOrAwaitValue()

        assertThat(totalPriceSum).isEqualTo(2 * 10f + 4 * 5.5f)

    }
}