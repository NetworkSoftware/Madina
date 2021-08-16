package pro.network.madina;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import pro.network.madina.R;

import pro.network.madina.app.AppConfig;

import com.google.android.material.textfield.TextInputLayout;

public class ForgotPassword extends AppCompatActivity {

    EditText newPassword;
    EditText confirmNewPassword;
    TextInputLayout confirmNewPasswordText;
    TextInputLayout newPasswordText;
    TextInputLayout oldpasswordText;
    ProgressDialog pDialog;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot);
        sharedpreferences = this.getSharedPreferences(AppConfig.mypreference, Context.MODE_PRIVATE);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        newPassword = findViewById(R.id.newPassword);
        confirmNewPassword = findViewById(R.id.confirmNewPassword);
        confirmNewPasswordText = findViewById(R.id.confirmNewPasswordText);
        newPasswordText = findViewById(R.id.newPasswordText);
        oldpasswordText = findViewById(R.id.oldpasswordText);

        LinearLayout ll1 = findViewById(R.id.ll1);
        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    newPasswordText.setError(null);
                } else {
                    newPasswordText.setError("Enter valid Password");
                }
            }
        });
        confirmNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    confirmNewPasswordText.setError(null);
                } else {
                    confirmNewPasswordText.setError("Enter valid Password");
                }
            }
        });

        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (newPassword.getText().toString().length() <= 0 ) {
                    newPasswordText.setError("Enter valid Password");
                }else if (newPassword.length() < 8 ) {
                    newPasswordText.setError("Enter Password at least 8 digits");
                } else if (confirmNewPassword.getText().toString().length() <= 0) {
                    confirmNewPasswordText.setError("Enter valid Password");
                } else if (confirmNewPassword.length() < 8 ) {
                    confirmNewPasswordText.setError("Enter Password at least 8 digits");
                }else if (!newPassword.getText().toString().equals(confirmNewPassword.getText().toString())) {
                    confirmNewPasswordText.setError("Password does not match");
                } else {
                }
            }
        });
    }
    private void hideDialog() {

        if (this.pDialog.isShowing()) this.pDialog.dismiss();
    }

    private void showDialog() {

        if (!this.pDialog.isShowing()) this.pDialog.show();
    }
}
