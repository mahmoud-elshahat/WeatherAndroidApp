
import android.os.SystemClock
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.mahmoudelshahat.weatherandroidapp.R
import com.mahmoudelshahat.weatherandroidapp.location_manager.OnLocationReceivedListener
import com.mahmoudelshahat.weatherandroidapp.ui.LandingActivity
import com.mahmoudelshahat.weatherandroidapp.utils.PermissionUtils
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockkObject
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class LandingActivityTest {
    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(LandingActivity::class.java)

    @Before
    fun setUp() {
        ActivityScenario.launch(LandingActivity::class.java)
    }

    @Test
    fun allItemsDisplayedRight() {
        onView(withId(R.id.main_activity_edit_text_cities))
            .check(matches(isDisplayed()))
        onView(withId(R.id.main_activity_button_cities_weather))
            .check(matches(isDisplayed()))
        onView(withId(R.id.main_activity_button_current_location_weather))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testLocationCityButton(){
        mockkObject(PermissionUtils)
        every{PermissionUtils.isPermissionsGranted(any(),any()) } returns false
        every{PermissionUtils.requestGrantedPermissions(any(),any(),any()) } returns false


        onView(withId(R.id.main_activity_button_current_location_weather))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withText(R.string.app_name))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testWeatherForSpecificCitiesWrongText(){
        onView(withId(R.id.main_activity_edit_text_cities)).perform(typeText("Egypt,Russia"))
        onView(withId(R.id.main_activity_button_cities_weather))
            .check(matches(isDisplayed()))
            .perform(click())
        onView(withText(R.string.app_name))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testWeatherForSpecificCitiesRightText(){
        onView(withId(R.id.main_activity_edit_text_cities)).perform(typeText("Egypt,Russia,Canada"))
        onView(withId(R.id.main_activity_button_cities_weather))
            .check(matches(isDisplayed()))
            .perform(click())
        onView(withText(R.string.specific_cities))
            .check(matches(isDisplayed()))
    }
    @After
    fun afterTest(){
        clearAllMocks()
    }
}