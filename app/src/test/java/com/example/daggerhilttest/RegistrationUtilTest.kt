package com.example.daggerhilttest


import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RegistrationUtilTest {

    @Test
    fun `empty username returns false`(){
        val result = RegistrationUtil.validateRegistrationInput(
            "",
            "123",
            "123",
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `valid username and correctly repeated password returns true`(){
        val result = RegistrationUtil.validateRegistrationInput(
            "Philipp",
            "123",
            "123",
        )
        assertThat(result).isTrue()
        assertThat("hello").isEqualTo("hello")
    }

    @Test
    fun `username already exists returns false`(){
        val result = RegistrationUtil.validateRegistrationInput(
            "Carl",
            "123",
            "123",
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `incorrectly confirmed password returns false`(){
        val result = RegistrationUtil.validateRegistrationInput(
            "Phillipp",
            "123456",
            "abcdefg",
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `empty password returns false`(){
        val result = RegistrationUtil.validateRegistrationInput(
            "Phillipp",
            "",
            "",
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `less than 2 digit password returns false`(){
        val result = RegistrationUtil.validateRegistrationInput(
            "Phillipp",
            "abcdefg5",
            "abcdefg5",
        )
        assertThat(result).isFalse()
    }

    //empty password
    //password was repeated incorrectly
    //password contains less than 2 digits
}