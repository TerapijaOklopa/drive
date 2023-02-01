@file:Suppress("REDUNDANT_INLINE_SUSPEND_FUNCTION_TYPE")

package com.mobile.drive.mobile.utils

import kotlinx.coroutines.TimeoutCancellationException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Attempts [block], returning a successful [Result] if it succeeds, otherwise a [Result.Failure]
 * taking care not to break structured concurrency
 */
suspend inline fun <T, R> T.suspendRunCatching(
    block: suspend T.() -> R
): Result<R> = try {
    Result.success(block())
} catch (t: TimeoutCancellationException) {
    Result.failure(t)
} catch (cancellationException: CancellationException) {
    throw cancellationException
} catch (t: Throwable) {
    Result.failure(t)
}
