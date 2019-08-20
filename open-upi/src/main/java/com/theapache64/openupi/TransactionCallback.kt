package com.theapache64.openupi

interface TransactionCallback {
    fun onSubmitted(transactionResult: TransactionResult)
    fun onSuccess(transactionResult: TransactionResult)
    fun onFailure(message: String, transactionResult: TransactionResult?)
}