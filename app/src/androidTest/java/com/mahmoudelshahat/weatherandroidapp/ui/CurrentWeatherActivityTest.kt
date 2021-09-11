

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.mahmoudelshahat.weatherandroidapp.R
import com.mahmoudelshahat.weatherandroidapp.ui.current_weather.CurrentWeatherActivity
import com.mahmoudelshahat.weatherandroidapp.utils.EspressoIdlingResource
import com.mahmoudelshahat.weatherandroidapp.utils.RecyclerViewItemCountAssertion
import com.mahmoudelshahat.weatherandroidapp.utils.WeatherConstants
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith



@RunWith(AndroidJUnit4ClassRunner::class)
class CurrentWeatherActivityTest {
    @Rule
    @JvmField
    val mActivityRule = ActivityTestRule(CurrentWeatherActivity::class.java,false,false)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)

        val intent = Intent()
        val citiesList= arrayListOf<String>()
        citiesList.add("Egypt")
        citiesList.add("Germany")
        citiesList.add("Italy")
        intent.putStringArrayListExtra(WeatherConstants.CITIES_KEY, citiesList)
        mActivityRule.launchActivity(intent)
    }

    @Test
    fun allItemsDisplayedRightAfterLoading() {
        onView(withText(R.string.specific_cities))
            .check(matches(isDisplayed()))
        onView(withId(R.id.shimmerFrameLayout)).check(matches(Matchers.not(isDisplayed())))
        onView(withId(R.id.current_weather_recyclerview)).check(matches(isDisplayed()))

        onView(withId(R.id.current_weather_recyclerview)).check( RecyclerViewItemCountAssertion(3));
    }


    @After
    fun unRegisterIdlingResource()
    {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }
}