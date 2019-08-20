package com.theapache64.openupi

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import java.net.URLEncoder


/**
 * This API compliance to https://www.npci.org.in/sites/all/themes/npcl/images/PDF/UPI_Linking_Specs_ver_1.5.1.pdf
 */
object OpenUPI {
    private val TAG = OpenUPI::class.java.simpleName


    const val REQUEST_CODE = 324

    /**
     * Payee VPA
     */
    lateinit var payeeAddr: String

    /**
     *  Payee Name
     */
    lateinit var payeeName: String

    /**
     * Payee merchant code
     */
    var merchantCode: String? = null

    /**
     * Currency code - For now UPI only supports INR, but may change in future.
     */
    private const val CURRENCY_CODE = "INR"

    /**
     * To set default values
     */
    fun init(
        payeeAddr: String,
        payeeName: String,
        merchantCode: String? = null
    ) {
        OpenUPI.payeeAddr = payeeAddr
        OpenUPI.payeeName = payeeName
        OpenUPI.merchantCode = merchantCode
    }

    fun newTransaction(
        amount: Float
    ): Transaction {
        return Transaction(
            amount,
            payeeAddr,
            payeeName,
            merchantCode
        )
    }


    private fun logIntent(data: Intent) {
        val bundle = data.extras
        if (bundle != null) {
            for (key in bundle.keySet()) {
                val value = bundle.get(key)
            }
        }
    }

    /**
     * To initiate a payment
     */
    fun start(activity: Activity, transaction: Transaction) {

        val paramsBuilder = StringBuilder()

        for (param in transaction.getParams()) {
            paramsBuilder.append("${param.key}=${URLEncoder.encode(param.value, "UTF-8")}&")
        }

        val upiString = "upi://pay?$paramsBuilder"

        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.parse(upiString)
        val chooser = Intent.createChooser(intent, "Pay with...")
        activity.startActivityForResult(chooser, OpenUPI.REQUEST_CODE, null)
    }

    /**
     * To handle the result returned from the UPI app
     */
    fun handleActivityResult(
        amount: Float,
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        callback: TransactionCallback
    ) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {

                    try {
                        val status =
                            data.getStringExtra("Status")
                                ?: throw TransactionException("Status is null")

                        val txnId = data.getStringExtra("txnId")
                            ?: throw TransactionException("Transaction ID is null")

                        val responseCode = data.getStringExtra("responseCode")
                            ?: throw TransactionException("responseCode is null")

                        val txnRef =
                            data.getStringExtra("txnRef")
                                ?: throw TransactionException("txnRef is null")

                        val transaction = TransactionResult(
                            amount,
                            txnId,
                            responseCode,
                            status,
                            txnRef
                        )

                        when (transaction.statusCode) {

                            TransactionResult.STATUS_SUCCESS -> {
                                callback.onSuccess(transaction)
                            }

                            TransactionResult.STATUS_SUBMITTED -> {
                                callback.onSubmitted(transaction)
                            }

                            TransactionResult.STATUS_FAILURE -> {
                                callback.onFailure("Transaction failed", transaction)
                            }
                        }

                    } catch (e: TransactionException) {
                        e.printStackTrace()
                        callback.onFailure(e.message!!, null)
                    }

                } else {
                    callback.onFailure("Returned data is null", null)
                }
            } else {
                callback.onFailure("Transaction failed", null)
            }
        } else {
            Log.e(TAG, "Activity result is not from OpenUPI")
        }
    }

}