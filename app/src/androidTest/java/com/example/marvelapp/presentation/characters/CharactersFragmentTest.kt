package com.example.marvelapp.presentation.characters

import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.marvelapp.R
import com.example.marvelapp.extension.asJsonString
import com.example.marvelapp.framework.di.BaseUrlModule
import com.example.marvelapp.framework.di.CoroutinesModule
import com.example.marvelapp.launchFragmentInHiltContainer
import com.example.marvelapp.presentation.characters.adapters.CharactersViewHolder
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@UninstallModules(BaseUrlModule::class, CoroutinesModule::class)
@HiltAndroidTest
class CharactersFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var server: MockWebServer

    private val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

    @Before
    fun setUp() {
        server = MockWebServer().apply {
            start(8080)
        }

        launchFragmentInHiltContainer<CharactersFragment>(navHostController = navController)
    }

    @Test
    fun shouldShowCharacters_whenViewIsCreated(): Unit = runBlocking {
        //
        // deve mostrar Characters quando a exibição é criada
        //

        // Arrange
        server.enqueue(MockResponse().setBody("characters_p1.json".asJsonString()))

        delay(700) // delay porque utilizamos cache.

        // Action
        onView(withId(R.id.recyclerCharacters)).check(matches(isDisplayed()))

        // Assert
    }

    @Test
    fun shouldLoadMoreCharacters_whenNewPageIsRequested(): Unit = runBlocking {
        //
        // deve carregar mais Characters quando uma nova página for solicitada
        //

        delay(2000) // test delay

        // Arrange
        with(server) {
            enqueue(MockResponse().setBody("characters_p1.json".asJsonString()))
            enqueue(MockResponse().setBody("characters_p2.json".asJsonString()))
        }

        delay(700) // delay porque utilizamos cache.

        // Action
        onView(
            withId(R.id.recyclerCharacters),
        ).perform(
            RecyclerViewActions.scrollToPosition<CharactersViewHolder>(20),
        )

        // Assert
        onView(withText("Amora")).check(matches(isDisplayed()))
    }

    @Test
    fun shouldShowErrorView_whenReceivesAnErrorFromApi(): Unit = runBlocking {
        //
        // deve mostrar visualização de erro quando recebe um erro da API
        //

        delay(2000) // test delay

        // Arrange
        server.enqueue(MockResponse().setResponseCode(404))

        delay(700) // delay porque utilizamos cache.

        // Action
        onView(withId(R.id.textInitialLoadingError)).check(matches(isDisplayed()))

        // Assert
    }

    @After
    fun tearDown() {
        server.shutdown()
    }
}
