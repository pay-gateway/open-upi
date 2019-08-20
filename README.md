# open-upi
A simple UPI payment gateway with zero cost. Made for Android.

<img src="https://raw.githubusercontent.com/theapache64/open-upi/master/demo.gif" width="300">

### Installation

```groovy
implementation "com.theapache64.open-upi:open-upi:1.0.0-alpha01"
```

### Usage

**STEP 1** 

Init default `payeeAddress` and `payeeName` in your `Application` class. 

```kotlin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        OpenUPI.init(
            "john.doe@okicici",
            "John Doe"
        )
    }
}
```

**STEP 2**

Start transaction

```kotlin
OpenUPI.newTransaction(amount) 
       .start(this)
```

**STEP 3**

Override `onActivityResult` to handle transaction result

```kotlin

if (requestCode == OpenUPI.REQUEST_CODE) {

    OpenUPI.handleActivityResult(
        amount,
        requestCode,
        resultCode,
        data,
        object : TransactionCallback {

            override fun onSubmitted(transactionResult: TransactionResult) {
                // Transaction submitted/pending
            }

            override fun onSuccess(transactionResult: TransactionResult) {
                // Transaction fully succeeded
            }

            override fun onFailure(message: String, transactionResult: TransactionResult?) {
                // Transaction failed
            }
        })
}
```

All done!! :thumbsup:  