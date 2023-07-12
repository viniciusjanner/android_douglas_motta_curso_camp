package com.example.testing

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class MainCoroutineRule(
    // private val dispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher(), // Deprecated
    private val dispatcher: TestDispatcher = StandardTestDispatcher(),
) : TestWatcher() {

    // TestCoroutineScope by TestCoroutineScope(dispatcher) { // Deprecated

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        // cleanupTestCoroutines() // Deprecated
        Dispatchers.resetMain()
    }
}
