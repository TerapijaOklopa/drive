package com.mobile

import com.mobile.util.coroutines.CoroutineScopeRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
open class BaseTest {

    @get:Rule
    val coroutineScope = CoroutineScopeRule()
}
