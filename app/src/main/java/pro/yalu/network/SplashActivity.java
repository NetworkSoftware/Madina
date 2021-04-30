package pro.yalu.network;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sanojpunchihewa.updatemanager.UpdateManager;
import com.sanojpunchihewa.updatemanager.UpdateManagerConstant;

import pro.yalu.network.R;


import pro.yalu.network.app.AppConfig;

import static pro.yalu.network.app.AppConfig.skipKey;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    UpdateManager mUpdateManager;
    MaterialButton flexUpdate, skipUpdate;
    TextView latestVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mUpdateManager = UpdateManager.Builder(this);
        Log.d("TOken ", "" + FirebaseInstanceId.getInstance().getToken());
        FirebaseMessaging.getInstance().subscribeToTopic("allDevices");

        flexUpdate = findViewById(R.id.flexUpdate);
        skipUpdate = findViewById(R.id.skipUpdate);
        latestVersion = findViewById(R.id.latestVersion);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }
        sharedpreferences = getSharedPreferences(AppConfig.mypreference,
                Context.MODE_PRIVATE);
        String version = "1.0.8";
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView versionid = findViewById(R.id.versionid);
        versionid.setText("Version " + version);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mUpdateManager.addUpdateInfoListener(new UpdateManager.UpdateInfoListener() {
                    @Override
                    public void onReceiveVersionCode(final int code) {
                        if (code > BuildConfig.VERSION_CODE) {
                            skipUpdate.setVisibility(View.VISIBLE);
                            latestVersion.setVisibility(View.VISIBLE);
                            latestVersion.setText("Get More with new Update " + code);
                            flexUpdate.setVisibility(View.VISIBLE);
                        } else {
                            goToNext();
                        }
                    }

                    @Override
                    public void onNoUpdate() {
                        goToNext();
                    }

                    @Override
                    public void onReceiveStalenessDays(final int days) {
                        Log.e("xxxxxxxxxx", days + "");
                    }
                });

            }
        }, 2000);

        mUpdateManager.addFlexibleUpdateDownloadListener(new UpdateManager.FlexibleUpdateDownloadListener() {
            @Override
            public void onDownloadProgress(final long bytesDownloaded, final long totalBytes) {
                latestVersion.setText("Downloading: " + bytesDownloaded + " / " + totalBytes);
            }
        });

        flexUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUpdateManager.mode(UpdateManagerConstant.FLEXIBLE).start();
                latestVersion.setVisibility(View.VISIBLE);
            }
        });


    }

    private void goToNext() {
        if (!(sharedpreferences.contains(AppConfig.isLogin)
                && sharedpreferences.getBoolean(AppConfig.isLogin, false))) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(AppConfig.isLogin, true);
            editor.putString(AppConfig.configKey, "guest");
            editor.putString(AppConfig.usernameKey, "guest");
            editor.putString(AppConfig.auth_key, "guest");
            editor.putString(AppConfig.user_id, "guest");
            editor.commit();
        }
        SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivityYalu.class));
        finish();
    }

}
