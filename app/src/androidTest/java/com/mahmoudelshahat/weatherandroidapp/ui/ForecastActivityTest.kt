
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.mahmoudelshahat.weatherandroidapp.R
import com.mahmoudelshahat.weatherandroidapp.ui.LandingActivity
import com.mahmoudelshahat.weatherandroidapp.ui.city_forecast.ForecastActivity
import com.mahmoudelshahat.weatherandroidapp.utils.EspressoIdlingResource
import com.mahmoudelshahat.weatherandroidapp.utils.WeatherConstants
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.security.AccessController.getContext


@RunWith(AndroidJUnit4ClassRunner::class)
class ForecastActivityTest {
    @Rule
    @JvmField
    val mainActivityRule = ActivityTestRule(ForecastActivity::class.java,false,false)
    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)

        val intent = Intent()
        intent.putExtra(WeatherConstants.LOCATION_CITY_KEY, "Egypt")
        mainActivityRule.launchActivity(intent)
    }

    @Test
    fun allItemsDisplayedRightAfterLoading() {
        onView(withText("Egypt"))
            .check(matches(isDisplayed()))
        onView(withId(R.id.shimmerFrameLayout)).check(matches(not(isDisplayed())))
        onView(withId(R.id.forecast_recyclerview)).check(matches(isDisplayed()))
    }

    @After
    fun unRegisterIdlingResource()
    {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }
}