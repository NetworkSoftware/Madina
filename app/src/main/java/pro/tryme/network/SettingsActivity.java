package pro.tryme.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pro.tryme.network.R;

import pro.tryme.network.app.AppConfig;
import pro.tryme.network.app.AppController;
import pro.tryme.network.app.BaseActivity;

import static pro.tryme.network.app.AppConfig.FEEDBACk_CREATE;
import static pro.tryme.network.app.AppConfig.termsNo;
import static pro.tryme.network.app.AppConfig.termsValue;

public class SettingsActivity extends Fragment {

    protected SharedPreferences sharedpreferences;
    LinearLayout title;
    private RoundedBottomSheetDialog mBottomSheetDialog;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_settings, container, false);

        sharedpreferences = getActivity().getSharedPreferences(AppConfig.mypreference,
                Context.MODE_PRIVATE);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);

        ((LinearLayout) view.findViewById(R.id.changePassword)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChangePassword.class));
            }
        });

        ((LinearLayout) view.findViewById(R.id.help)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:919715439506"));
                startActivity(intent);
            }
        });
        ((LinearLayout) view.findViewById(R.id.contactus)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:919715439506"));
                startActivity(intent);
            }
        });
        ((TextView) view.findViewById(R.id.profileName))
                .setText(sharedpreferences.getString(AppConfig.usernameKey, ""));

        ((TextView) view.findViewById(R.id.logout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.remove(AppConfig.isLogin);
                editor.remove(AppConfig.configKey);
                editor.remove(AppConfig.user_id);
                editor.remove(AppConfig.auth_key);
                editor.commit();

                startActivity(new Intent(getActivity(), LoginActivity.class));

            }
        });


        ((LinearLayout) view.findViewById(R.id.title)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        mBottomSheetDialog = new RoundedBottomSheetDialog(getActivity());
                        LayoutInflater inflater = SettingsActivity.this.getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.add_feedback, null);

                        TextInputLayout feedbackText = dialogView.findViewById(R.id.feedbackText);

                        final TextInputEditText feedback = dialogView.findViewById(R.id.feedback);

                        Button submitBtn = dialogView.findViewById(R.id.submitBtn);
                        Button cancelBtn = dialogView.findViewById(R.id.cancelBtn);


                        feedback.requestFocus();
                        feedback.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });

                        submitBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                FeedbackMainbean feedbackMainbean = new FeedbackMainbean(
                                        feedback.getText().toString());
                                createPurchase(feedbackMainbean);
                                bottomSheetCancel();
                            }
                        });
                        cancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                bottomSheetCancel();
                            }
                        });
                        mBottomSheetDialog.setContentView(dialogView);
                        mBottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        mBottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(final DialogInterface dialog) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        BottomSheetDialog d = (BottomSheetDialog) dialog;
                                        FrameLayout bottomSheet = d.findViewById(R.id.design_bottom_sheet);
                                        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                    }
                                }, 0);
                            }
                        });
                        mBottomSheetDialog.show();
            }

        });
        return view;
    }

    private void createPurchase(final FeedbackMainbean feedbackMainbean) {
        String tag_string_req = "req_register";
        progressDialog.setMessage("Processing ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                FEEDBACk_CREATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response.toString());
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    String msg = jsonObject.getString("message");
                    if (success == true) {
                    }
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Registration Error: ", error.getMessage());
                Toast.makeText(getActivity(),
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                localHashMap.put("feedback", feedbackMainbean.feedback);
                localHashMap.put("user_name", sharedpreferences.getString(AppConfig.usernameKey, ""));
                localHashMap.put("user_phone", sharedpreferences.getString(AppConfig.user_id, ""));

                return localHashMap;
            }
        };
        strReq.setRetryPolicy(AppConfig.getTimeOut());
        AppController.getInstance().addToRequestQueue(strReq);
    }

    public void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    public void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void bottomSheetCancel() {
        if (mBottomSheetDialog != null) {
            mBottomSheetDialog.cancel();
        }
    }
}
