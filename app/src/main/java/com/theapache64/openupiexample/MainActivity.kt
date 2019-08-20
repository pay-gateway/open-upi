package com.theapache64.openupiexample

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.theapache64.openupi.OpenUPI
import com.theapache64.openupi.TransactionCallback
import com.theapache64.openupi.TransactionResult

class MainActivity : AppCompatActivity() {

    companion object {

        // For the sake of a simple example, we're using constant amount
        private const val AMOUNT = 9596f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))


        findViewById<Button>(R.id.b_pay).setOnClickListener {
            // Pay button clicked, starting transaction
            OpenUPI.newTransaction(AMOUNT)
                .start(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == OpenUPI.REQUEST_CODE) {

            OpenUPI.handleActivityResult(
                AMOUNT,
                requestCode,
                resultCode,
                data,
                object : TransactionCallback {

                    override fun onSubmitted(transactionResult: TransactionResult) {
                        // Transaction submitted/pending
                        toast("Transaction submitted")
                    }

                    override fun onSuccess(transactionResult: TransactionResult) {
                        // Transaction fully succeeded
                        toast("Transaction succeeded")
                    }

                    override fun onFailure(message: String, transactionResult: TransactionResult?) {
                        // Transaction failed
                        toast("Transaction failed : '$message'")
                    }
                })
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}
