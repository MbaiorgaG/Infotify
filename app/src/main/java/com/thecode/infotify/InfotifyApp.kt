package com.thecode.infotify

import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
import com.thecode.infotify.utils.NetworkChangeReceiver
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

class InfotifyApp : MultiDexApplication() {
    override fun onCreate() {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true) // Allow to load drawable dynamiccaly to solve RessourceNotFoundException with drawable vectors
        super.onCreate()
        instance = this
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/SF-Regular.otf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onTerminate() {
        super.onTerminate()
    }

    fun initStetho() {
        Stetho.initializeWithDefaults(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    val isInternetAvailable: Boolean
        get() {
            val connectivityManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var activeNetworkInfo: NetworkInfo? = null
            if (connectivityManager != null) {
                activeNetworkInfo = connectivityManager.activeNetworkInfo
            }
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

    val appContext: Context
        get() = instance!!.applicationContext

    fun setConnectivityListener(listener: NetworkChangeReceiver.ConnectivityReceiverListener?) {
        NetworkChangeReceiver.connectivityReceiverListener = listener
    }

    val context: Context
        get() = applicationContext

    companion object {
        @get:Synchronized
        var instance: InfotifyApp? = null
            private set
    }
}