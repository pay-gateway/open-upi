package com.theapache64.openupi

import android.app.Activity
import java.util.*

/**
 * To represent an independent transaction
 */
class Transaction(
    amount: Float,
    payeeAddr: String,
    payeeName: String,
    merchantCode: String? = null
) {

    companion object {
        private const val CURRENCY_INR = "INR"
    }

    private val params = mutableMapOf<String, String>()

    init {
        params["am"] = amount.toString()
        setPayeeAddress(payeeAddr)
        setPayeeName(payeeName)
        setMerchantCode(merchantCode)
        setCurrencyCode(CURRENCY_INR)

        val randomTransactionId = UUID.randomUUID().toString().replace("-", "")
        val randomTransactionRefId = UUID.randomUUID().toString().replace("-", "")
        val transactionNote = "tId:$randomTransactionId,tRefId:$randomTransactionRefId"

        setTransactionId(randomTransactionId)
        setTransactionRefId(randomTransactionRefId)
        setTransactionNote(transactionNote)
    }

    /**
     * Payee VPA
     */
    fun setPayeeAddress(payeeAddr: String): Transaction {
        this.params["pa"] = payeeAddr
        return this
    }

    /**
     * Payee name
     */
    fun setPayeeName(payeeName: String): Transaction {
        this.params["pn"] = payeeName
        return this
    }

    /**
     * Payee merchant code If present then needs to be passed as it is.
     *
     */
    fun setMerchantCode(merchantCode: String?): Transaction {
        if (merchantCode != null) {
            this.params["mc"] = merchantCode
        }
        return this
    }

    /**
     * This must be PSP generated id when present.
     * In the case of Merchant payments, merchant may acquire the txn id from his PSP.
     * If present then needs to be passed as it is.
     */
    fun setTransactionId(transactionId: String): Transaction {
        this.params["tid"] = transactionId
        return this
    }

    /**
     * Transaction reference ID.
     * This could be order number, subscription number, Bill ID, booking ID, insurance renewal reference, etc.
     * This field is Mandatory for Merchant transactions and dynamic URL generation
     */
    fun setTransactionRefId(transactionRefId: String): Transaction {
        this.params["tr"] = transactionRefId
        return this
    }

    /**
     * Transaction note providing a short description of the transaction.
     */
    fun setTransactionNote(transactionNote: String): Transaction {
        this.params["tn"] = transactionNote
        return this
    }

    /**
     * This should be a URL when clicked provides customer with further transaction details
     * like complete bill details, bill copy, order copy, ticket details, etc.
     * This can also be used to deliver digital goods such as mp3 files etc. after payment.
     * This URL, when used, MUST BE related to the particular transaction and MUST NOT be used
     * to send unsolicited information that are not relevant to the transaction.
     */
    fun setRefUrl(refUrl: String): Transaction {
        this.params["url"] = refUrl
        return this
    }


    /**
     * Currency code. Currently ONLY "INR" is the supported value.
     */
    private fun setCurrencyCode(currencyCode: String): Transaction {
        this.params["cu"] = currencyCode
        return this
    }

    fun getParams(): Map<String, String> = params

    fun start(activity: Activity) {
        OpenUPI.start(activity, this)
    }

}