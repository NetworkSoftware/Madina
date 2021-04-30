package pro.yalu.network.web;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import pro.yalu.network.R;


public class WebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_webview);

        WebView webView = findViewById(R.id.webview);
        final ProgressBar webProgress = findViewById(R.id.webProgress);
        webProgress.setVisibility(View.VISIBLE);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new MyWebViewClient() {

            public void onPageFinished(WebView view, String url) {
                webProgress.setVisibility(View.GONE);
            }
        });
        webView.loadUrl(getIntent().getStringExtra("url"));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setSubtitle(getIntent().getStringExtra("name"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
