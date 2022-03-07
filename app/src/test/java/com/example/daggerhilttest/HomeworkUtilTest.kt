package com.example.daggerhilttest

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class HomeworkUtilTest {

    @Test
    fun `input zero returns zero`(){
        val result = HomeworkUtil.fib(0)
        assertThat(result).isEqualTo(0)
    }

    @Test
    fun `input one returns one`(){
        val result = HomeworkUtil.fib(1)
        assertThat(result).isEqualTo(1)
    }

    @Test
    fun `input two returns one`(){
        val result = HomeworkUtil.fib(2)
        assertThat(result).isEqualTo(1)
    }

    @Test
    fun `input five returns one`(){
        val result = HomeworkUtil.fib2(10)
        assertThat(result).isEqualTo(55)
    }
}