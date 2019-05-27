package jp.co.mitsuhori_y.firebaseanalyticssample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class MainActivity : AppCompatActivity() {

    private val mFirebaseAnalytics: FirebaseAnalytics by lazy {
        FirebaseAnalytics.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeFirebaseRemoteConfig()
    }

    private fun initializeFirebaseRemoteConfig() {

        val config = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds((if (BuildConfig.DEBUG) 0 else 60 * 60 * 12))
                .build()
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        remoteConfig.apply {
            setConfigSettingsAsync(config)
            setDefaults(R.xml.remote_config_default_value)
            activate().addOnCompleteListener { fetchedTask ->
                if (fetchedTask.isSuccessful) {
                    val userGroup = getString("user_group")
                    findViewById<TextView>(R.id.remote_config).text = userGroup
                    mFirebaseAnalytics.setUserProperty("user_group", userGroup)
                }
            }
        }
    }
}
