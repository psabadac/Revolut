package com.example.revolut.utils

import android.app.Application
import android.util.Log
import com.example.revolut.R
import com.flurry.android.FlurryAgent
import com.flurry.android.FlurryPerformance

class RevolutApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        FlurryAgent.Builder()
            .withDataSaleOptOut(false) //CCPA - the default value is false
            .withCaptureUncaughtExceptions(true)
            .withIncludeBackgroundSessionsInMetrics(true)
            .withLogLevel(Log.VERBOSE)
            .withPerformanceMetrics(FlurryPerformance.ALL)
            .build(this, getString(R.string.flurry_api_key));
    }
}