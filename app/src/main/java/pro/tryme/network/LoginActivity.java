package pro.tryme.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import pro.tryme.network.R;

import pro.tryme.network.app.AppConfig;
import pro.tryme.network.app.AppController;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText username;
    TextInputLayout usernameTxt;
    TextInputEditText password;
    TextInputLayout passwordText;
    Button request;
    ProgressDialog dialog;
    protected SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        setContentView(R.layout.activity_login_page);

        sharedpreferences = getSharedPreferences(AppConfig.mypreference,
                Context.MODE_PRIVATE);

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

        username = findViewById(R.id.username);
        usernameTxt = findViewById(R.id.usernameTxt);
        password = findViewById(R.id.password);
        passwordText = findViewById(R.id.passwordText);

        request = findViewById(R.id.request);
        if (sharedpreferences.contains(AppConfig.configKey)) {
            username.setText(sharedpreferences.getString(AppConfig.configKey, ""));
            password.requestFocus();
        }


        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, Registerpage.class));
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() < 8) {
                    passwordText.setError("Enter Password at least 8 digits");
                } else if (password.length() > 0) {
                    passwordText.setError(null);

                }
            }
        });


        FloatingActionButton submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().length() > 0 && password.getText().toString().length() > 0) {
                } else if (password.length() < 8) {
                    passwordText.setError("Enter Password at least 8 digits");
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
                checkLogin();

            }
        });

    }

    private void checkLogin() {
        String tag_string_req = "req_register";
        dialog.setMessage("Login ...");
        showDialog();
        // showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.LOGIN_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    String msg = jObj.getString("message");
                    if (success == 1) {
                        String auth_key = jObj.getString("auth_key");
                        String user_id = jObj.getString("user_id");
                        String name = jObj.getString("name");

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(AppConfig.isLogin, true);
                        editor.putString(AppConfig.configKey, username.getText().toString());
                        editor.putString(AppConfig.usernameKey, name);
                        editor.putString(AppConfig.auth_key, auth_key);
                        editor.putString(AppConfig.user_id, user_id);
                        editor.commit();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finishAffinity();

                    }
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Log.e("xxxxxxxxxx", e.toString());
                }
                hideDialog();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),
                        "Slow network found.Try again later", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                localHashMap.put("phone", username.getText().toString());
                localHashMap.put("password", password.getText().toString());
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(AppConfig.getTimeOut());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!dialog.isShowing())
            dialog.show();
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        hideDialog();
        super.onDestroy();

    }
}
