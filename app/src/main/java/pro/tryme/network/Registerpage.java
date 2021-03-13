package pro.tryme.network;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import pro.tryme.network.R;

import pro.tryme.network.app.AppConfig;
import pro.tryme.network.app.AppController;
import pro.tryme.network.app.BaseActivity;
import pro.tryme.network.app.Session_management;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static pro.tryme.network.app.AppConfig.REGISTER_USER;

public class Registerpage extends BaseActivity {
    private static final int FINE_LOCATION_CODE = 199;

    CountryCodePicker phonedial;
    ProgressDialog progressDialog;
    String token;
    private Session_management session_management;

    BetterSpinner country;
    BetterSpinner state;

    EditText name;
    EditText mobile;

    EditText password;

    TextInputLayout nameText;
    TextInputLayout mobileText;
    TextInputLayout passwordText;

    RegMainbean RegMainbean = null;

    private String TAG = getClass().getSimpleName();
    Map<String, String> countryCodeMap = new HashMap<>();
    Map<String, String> stateCodeMap = new HashMap<>();

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void startDemo() {
        setContentView(R.layout.activity_register);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }


        nameText = (TextInputLayout) findViewById(R.id.nameText);
        mobileText = (TextInputLayout) findViewById(R.id.mobileText);
        passwordText = (TextInputLayout) findViewById(R.id.passwordText);

        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        password = findViewById(R.id.password);

        ExtendedFloatingActionButton submit = findViewById(R.id.submit);


        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    nameText.setError(null);
                }
            }
        });


        mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    mobileText.setError(null);
                }
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


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sharedpreferences.contains("user_id")) {
                    Toast.makeText(getApplicationContext(), "The User Already Registered", Toast.LENGTH_SHORT).show();

                } else if (name.getText().toString().length() <= 0) {
                    nameText.setError("Enter the name");
                } else if (mobile.getText().toString().length() <= 0) {
                    mobileText.setError("Enter the Mobile Number");
                } else if (password.getText().toString().length() <= 0) {
                    passwordText.setError("Enter the Password");
                } else if (password.length() < 8) {
                    passwordText.setError("Enter Password at least 8 digits");
                } else {
                    registerUser();
                }
            }

        });

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Account");

    }

    private void registerUser() {
        String tag_string_req = "req_register";
        pDialog.setMessage("Processing ...");
        showDialog();
        // showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                REGISTER_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response.toString());
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");
                    String msg = jsonObject.getString("message");
                    if (success == 1) {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(AppConfig.configKey, mobile.getText().toString());
                        editor.commit();
                        finish();
                    }
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                localHashMap.put("name", name.getText().toString());
                localHashMap.put("phone", mobile.getText().toString());
                localHashMap.put("password", password.getText().toString());

                return localHashMap;
            }
        };
        strReq.setRetryPolicy(AppConfig.getTimeOut());
        AppController.getInstance().addToRequestQueue(strReq);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
