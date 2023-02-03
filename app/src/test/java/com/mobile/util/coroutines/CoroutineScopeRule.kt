package com.mobile.util.coroutines

import com.mobile.drive.mobile.utils.coroutines.CoroutineDispatcherProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class CoroutineScopeRule(
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher(),
    var dispatcherProvider: CoroutineDispatcherProvider = CoroutineDispatcherProvider()
) : TestWatcher() {

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(dispatcher)
        dispatcherProvider = CoroutineDispatcherProvider(
            main = dispatcher,
            default = dispatcher,
            io = dispatcher
        )
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}
