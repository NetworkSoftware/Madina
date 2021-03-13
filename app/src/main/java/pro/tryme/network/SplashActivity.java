package pro.tryme.network;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import pro.tryme.network.R;


import pro.tryme.network.app.AppConfig;
import pro.tryme.network.app.FirebaseMessageReceiver;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }
        sharedpreferences = getSharedPreferences(AppConfig.mypreference,
                Context.MODE_PRIVATE);

        Log.d("TOken ", "" + FirebaseInstanceId.getInstance().getToken());
        FirebaseMessaging.getInstance().subscribeToTopic("allDevices");


        Thread logoTimer = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (Exception e) {
                    Log.e("Error", e.toString());
                } finally {

                    SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));

                    finish();
                }
            }
        };
        logoTimer.start();

    }


}
