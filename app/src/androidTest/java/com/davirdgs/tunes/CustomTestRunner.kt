package com.davirdgs.tunes

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

class CustomTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        Log.d("CustomTestRunner", "newApplication - $className")
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }

    override fun newActivity(cl: ClassLoader?, className: String?, intent: Intent?): Activity {
        Log.d("CustomTestRunner", "newActivity - $className")
        return super.newActivity(cl, className, intent)
    }
}
