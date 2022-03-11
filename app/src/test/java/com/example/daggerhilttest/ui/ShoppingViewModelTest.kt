package com.example.daggerhilttest.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.daggerhilttest.MainCoroutineRule
import com.example.daggerhilttest.getOrAwaitValueTest
import com.example.daggerhilttest.other.Constants
import com.example.daggerhilttest.other.Status
import com.example.daggerhilttest.repositories.FakeShoppingRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShoppingViewModelTest {

    @get:Rule
    var instantTaskExecutorRule  = InstantTaskExecutorRule() //모든 작업이 동일스레드에서 동작하도록 보장한다.

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel : ShoppingViewModel

    @Before
    fun setup(){
        viewModel = ShoppingViewModel(FakeShoppingRepository()) //unit 테스트를 할거기 때문에 실제 API 통신이나 DB 쪽 작업을 하지 않는다. 즉 DefaultShoppingRepository 는 여기선 필요없다.
    }

    @Test
    fun `insert shopping item with empty field, returns error`(){
        viewModel.insertShoppingItem("name" , "", "3.0")
        val value = viewModel.insertShoppingItemsStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)

    }

    @Test
    fun `insert shopping item with too long name, returns error`(){
        val overTextString = buildString {      //20 자가 MAX_LENGTH 이므로 21번 담아서 오류 유발
            for(i in 1..Constants.MAX_NAME_LENGTH + 1){ 
                append(1)
            }
        }
        viewModel.insertShoppingItem(overTextString , "5", "3.0")
        val value = viewModel.insertShoppingItemsStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too long price, returns error`(){
        val overTextString = buildString {      //10 자가 MAX_PRICE_LENGTH 이므로 11 번 담아서 오류 유발
            for(i in 1..Constants.MAX_PRICE_LENGTH + 1){
                append(1)
            }
        }
        viewModel.insertShoppingItem("name" , overTextString, "3.0")
        val value = viewModel.insertShoppingItemsStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too high amount, returns error`(){
        viewModel.insertShoppingItem("name" , "999999999999", "3.0")
        val value = viewModel.insertShoppingItemsStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with valid input, returns success`(){
        viewModel.insertShoppingItem("name" , "5", "3.0")
        val value = viewModel.insertShoppingItemsStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }
}