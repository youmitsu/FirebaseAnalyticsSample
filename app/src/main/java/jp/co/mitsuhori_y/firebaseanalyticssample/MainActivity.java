package jp.co.mitsuhori_y.firebaseanalyticssample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        initializeFirebaseRemoteConfig();

        Bundle params = new Bundle();
        params.putString("image_name", "hoge");
        params.putString("full_text", "fuga");
        mFirebaseAnalytics.logEvent("share_image", params);
    }

    private void initializeFirebaseRemoteConfig() {

        FirebaseRemoteConfigSettings config = new FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(BuildConfig.DEBUG).build();
        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        remoteConfig.setConfigSettings(config);
        remoteConfig.setDefaults(R.xml.remote_config_default_value);
        remoteConfig.activateFetched();

        Task<Void> task;
        if (config.isDeveloperModeEnabled()) {
            // 開発モード：キャッシュの有効時間を 0 で設定
            task = remoteConfig.fetch(0);
        } else {
            // 本番モード：キャッシュの有効期間はデフォルト 12 時間
            task = remoteConfig.fetch();
        }
        task.addOnCompleteListener(fetchedTask -> {
            if (fetchedTask.isSuccessful()) {
                remoteConfig.activateFetched();
                String userGroup = remoteConfig.getString("user_group");
                ((TextView) findViewById(R.id.remote_config)).setText(userGroup);
                mFirebaseAnalytics.setUserProperty("user_group", userGroup);
            }
        });
    }
}
