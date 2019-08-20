package com.theapache64.openupi

import java.io.Serializable
import java.util.*

class TransactionResult(
    val amount: Float,
    val transactionId: String,
    val responseCode: String,
    val status: String,
    val transactionRefId: String
) : Serializable {
    companion object {
        const val STATUS_FAILURE = 0
        const val STATUS_SUCCESS = 1
        const val STATUS_SUBMITTED = 2
    }

    val statusCode = when (status.toLowerCase(Locale.getDefault())) {
        "success" -> STATUS_SUCCESS
        "submitted" -> STATUS_SUBMITTED
        "failed", "failure" -> STATUS_FAILURE
        else -> throw TransactionException("Undefined statusCode $status")
    }
}