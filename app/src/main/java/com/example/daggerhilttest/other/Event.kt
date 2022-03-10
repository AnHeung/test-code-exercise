package com.example.daggerhilttest.other

// 만약 통신을해서 에러가 났고 해당 에러의 이벤트를 보내서 SnackBar 가 동작했다고 가정했을때
// 화면을 회전시킬경우 해당 이벤트가 한번 더 발생할 수 있기 때문에 쓰는 클래스
open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set //

    /**
     * content 값을 반환하고 재사용을 막는다.
     */
    fun getContentIfNotHandled(): T? =
        if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }

    /**
     * 한번 다뤄졌던 content 라도 반환한다.
     */
    fun peekContent(): T = content
}