package com.example.pokemonsearch

import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.SearchView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.pokemonsearch.ui.favourites.FavouritePokemonListItem
import okhttp3.mockwebserver.MockResponse
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.BufferedReader


@RunWith(AndroidJUnit4::class)
@LargeTest
class SearchPokemonTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity>
            = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun prepareMock() {
        val mock = PokemonSearchApplication.mockWebServer

        var r = MockResponse()
        r.setResponseCode(200)
        r.setBody(readResponseFromAssets("alakazam.json"))
        mock.enqueue(r)

        r.setResponseCode(200)
        r.setBody(readResponseFromAssets("alakazam_species.json"))
        mock.enqueue(r)

        r.setResponseCode(200)
        r.setBody(readResponseFromAssets("alakazam_translation.json"))
        mock.enqueue(r)

        r.setResponseCode(200)
        r.setBody(readResponseFromAssets("seel.json"))
        mock.enqueue(r)

        r.setResponseCode(200)
        r.setBody(readResponseFromAssets("seel_species.json"))
        mock.enqueue(r)

        r.setResponseCode(200)
        r.setBody(readResponseFromAssets("seel_translation.json"))
        mock.enqueue(r)
    }

    @After
    fun shutdown() {
        PokemonSearchApplication.mockWebServer.shutdown()
    }

    @Test
    fun searchForPokemons() {
        // Navigate to search screen
        onView(withId(R.id.navigation_search)).perform(click())




        //search for alakazam and ad it into favourites
        onView(withId(R.id.searchView)).perform(click());
        onView(withId(R.id.searchView)).perform(typeSearchViewText("alakazam"));
        Thread.sleep(1000)
        onView(withId(R.id.custom_pokemon_details)).check(matches(isDisplayed()))
        onView(withId(R.id.add_to_favourites)).perform(click())


        //search for seel and ad it into favourites
        onView(withId(R.id.searchView)).perform(typeSearchViewText("seel"));
        Thread.sleep(1000)
        onView(withId(R.id.custom_pokemon_details)).check(matches(isDisplayed()))
        onView(withId(R.id.add_to_favourites)).perform(click())

        // navigate to favourites screen
        onView(withId(R.id.navigation_favourites)).perform(click())

        // validate that alakazam and seel are on the lists
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollTo<FavouritePokemonListItem>(
            withChild(withText("alakazam"))))
        onView(withChild(withText("alakazam"))).perform(longClick())
        onView(withChild(withText("alakazam"))).check(doesNotExist())

        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollTo<FavouritePokemonListItem>(
            withChild(withText("seel"))))
        onView(withChild(withText("seel"))).perform(longClick())
        onView(withChild(withText("seel"))).check(doesNotExist())

    }

    private fun typeSearchViewText(text: String): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String {
                return "Change view text"
            }

            override fun getConstraints(): Matcher<View> {
                return allOf(isDisplayed(), isAssignableFrom(SearchView::class.java))
            }

            override fun perform(uiController: UiController?, view: View?) {
                (view as SearchView).setQuery(text, true)
            }
        }
    }

    private fun readResponseFromAssets(assetName: String) : String {
        val context = InstrumentationRegistry.getInstrumentation().context
        val inputStream = context.assets.open(assetName)
        return inputStream.bufferedReader().use(BufferedReader::readText)
    }
}