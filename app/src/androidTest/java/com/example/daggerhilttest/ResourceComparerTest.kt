package com.example.daggerhilttest

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

class ResourceComparerTest {

//    private val resourceComparer = ResourceComparer() //bad practice 글로벌로 전역선언해서 쓰이고 있는데 좋은 테스트 케이스는 각각 독립적으로 돌아야 하고 밑에 두 테스트다 같은 인스턴스를 사용하는데 더이상 독립적이지 않음
    private lateinit var resourceComparer : ResourceComparer

    //모든 테스트 케이스 동작전에 동작해서 초기화 및 기타 작업
    @Before
    fun setup(){
        resourceComparer = ResourceComparer()
    }

    // Room 같은걸 사용한다고 가정하면 모든 테스트 케이스마다 close 함수를 호출해야할텐데 그런 부분을 여기서 할 수 있다.
    @After
    fun teardown(){

    }


    @Test
    fun stringResourceSameAsGivenString_returnsTrue(){
//        resourceComparer  = ResourceComparer() 매번 해당 초기화 동작을 하는것은 수많은 보일러 플레이트 코드를 유발할 수 있다. 그래서 JUnit 이 이부분을 위해 제공해주는 셋업 함수가 있다.
        val context =  ApplicationProvider.getApplicationContext<Context>()
        val result = resourceComparer.isEqual(context , R.string.app_name , "DaggerHiltTest")
        assertThat(result).isTrue()
    }

    @Test
    fun stringResourceDifferenceAsGivenString_returnsFalse(){
//        resourceComparer  = ResourceComparer()
        val context =  ApplicationProvider.getApplicationContext<Context>()
        val result = resourceComparer.isEqual(context , R.string.app_name , "Hello")
        assertThat(result).isFalse()
    }
}