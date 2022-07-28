package eParticipation.mobile

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class CertificadoTestSinDatos {
    @Rule
    var mActivityTestRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun certificadosTestSinDatos() {
        val materialButton = Espresso.onView(
            allOf(
                withId(R.id.login), ViewMatchers.withText("Login"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        )
        materialButton.perform(ViewActions.click())
        val cardView = Espresso.onView(
            allOf(
                withId(R.id.certificado),
                childAtPosition(
                    allOf(
                        withId(R.id.linearLayout),
                        childAtPosition(
                            ViewMatchers.withClassName(Matchers.`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            4
                        )
                    ),
                    0
                )
            )
        )
        cardView.perform(ViewActions.click())
        Espresso.pressBack()
        val appCompatImageButton = Espresso.onView(
            childAtPosition(
                allOf(
                    withId(R.id.navbar),
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                        3
                    )
                ),
                0
            )
        )
        appCompatImageButton.perform(ViewActions.click())
    }

    companion object {
        private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int
        ): Matcher<View> {
            return object : TypeSafeMatcher<View>() {
                override fun describeTo(description: Description) {
                    description.appendText("Child at position $position in parent ")
                    parentMatcher.describeTo(description)
                }

                public override fun matchesSafely(view: View): Boolean {
                    val parent = view.parent
                    return (parent is ViewGroup && parentMatcher.matches(parent)
                            && view == parent.getChildAt(position))
                }
            }
        }
    }
}
