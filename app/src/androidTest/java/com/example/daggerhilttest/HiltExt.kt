package com.example.daggerhilttest

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.core.internal.deps.guava.base.Preconditions
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
inline fun <reified T : Fragment> launchFragmentInHiltContainer(
    fragmentArgs: Bundle? = null,
    themeResId: Int = R.style.FragmentScenarioEmptyFragmentActivityTheme,
    fragmentFactory : FragmentFactory? = null,
    crossinline action: T.() -> Unit = {}
) {
    //각 어플리케이션은 메인 액티비티가 필요하고 테스트 환경에서 기본적으로 없으므로 여기다가 명시해서 사용한다.
    //실제 프래그먼트를 붙일 액티비티를 실행하기 위해서 (메인으로 가정하고 띄울 액티비티)
    val mainActivityIntent = Intent.makeMainActivity(
        ComponentName(
            ApplicationProvider.getApplicationContext(),
            HiltTestActivity::class.java
        )
    ).putExtra("androidx.fragment.app.testing.FragmentScenario.EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY", themeResId)

    //액티비티 시나리오를 통해 인텐트와 함께 액티비티를 띄우고 액티비티 참조를 얻을 수 있다.
    ActivityScenario.launch<HiltTestActivity>(mainActivityIntent).onActivity { activity->

        fragmentFactory?.let {
            activity.supportFragmentManager.fragmentFactory = it
        }
        val fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
            Preconditions.checkNotNull(T::class.java.classLoader),
            T::class.java.name
        ).apply { arguments = fragmentArgs }

        activity.supportFragmentManager.beginTransaction()
            .add(android.R.id.content , fragment, "")
            .commitNow()

        (fragment as T).action()
    }

}