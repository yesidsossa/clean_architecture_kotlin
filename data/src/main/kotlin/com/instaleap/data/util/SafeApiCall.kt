package com.instaleap.data.util

import com.instaleap.domain.util.Result

suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> = try {
    Result.Success(apiCall.invoke())
} catch (e: Exception) {
    Result.Error(e)
}
