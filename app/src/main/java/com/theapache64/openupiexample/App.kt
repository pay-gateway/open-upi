package com.theapache64.openupiexample

import android.app.Application
import com.theapache64.openupi.OpenUPI

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        OpenUPI.init(
            "theapache64@ybl",
            "MOHAMMED SIFAR K"
        )
    }
}