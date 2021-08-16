package pro.network.madina.about;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pro.network.madina.ChangePassword;
import pro.network.madina.FeedbackMainbean;
import pro.network.madina.R;
import pro.network.madina.app.AppConfig;
import pro.network.madina.app.AppController;
import pro.network.madina.app.BaseFragment;
import pro.network.madina.app.DatabaseHelperMadina;
import pro.network.madina.app.DbWishList;
import pro.network.madina.product.ProductListBean;
import pro.network.madina.web.WebActivity;

import static pro.network.madina.app.AppConfig.FEEDBACk_CREATE;
import static pro.network.madina.app.AppConfig.REGISTER_USER;
import static pro.network.madina.app.AppConfig.user_id;
import static pro.network.madina.app.AppConfig.user_phone;
import static pro.network.madina.app.AppConfig.usernameKey;

public class AboutusActivity extends BaseFragment implements OnAboutListener {
    RecyclerView listView;
    AboutusAdapter aboutusAdapter;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 401;

    SharedPreferences sharedpreferences;


    private ProgressDialog progressDialog;
    private RoundedBottomSheetDialog mBottomSheetDialog;

    ArrayList<AboutUs> aboutUsArrayList = new ArrayList<AboutUs>();
    View view;
    DatabaseHelperMadina dbHelper;
    DbWishList dbWishList;

    @Override
    protected View startDemo(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.activity_settings, container, false);

        sharedpreferences = getActivity().getSharedPreferences(AppConfig.mypreference,
                Context.MODE_PRIVATE);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);

        dbHelper = new DatabaseHelperMadina(getActivity());
        dbWishList = new DbWishList(getActivity());

        listView = view.findViewById(R.id.listview_flavor);
        aboutusAdapter = new AboutusAdapter(getActivity(), aboutUsArrayList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                getActivity(), RecyclerView.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        listView.setAdapter(aboutusAdapter);
        setlistItems();

        return view;
    }

    private void setlistItems() {

        aboutUsArrayList = new ArrayList<>();

        String version = "1.0.1";
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            version = pInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (sharedpreferences.getString(user_id, "").equalsIgnoreCase("guest")) {
            aboutUsArrayList.add(new AboutUs("Guest",
                    "Click here to Login", getResources().getDrawable(R.drawable.ic_account_circle_black_24dp)));
        } else {
            aboutUsArrayList.add(new AboutUs(sharedpreferences.getString(usernameKey, ""),
                    "Logout", getResources().getDrawable(R.drawable.ic_account_circle_black_24dp)));
            aboutUsArrayList.add(new AboutUs("Change Password",
                    "Change Password to secure stocks", getResources().getDrawable(R.drawable.ic_outline_lock_24)));
        }

        aboutUsArrayList.add(new AboutUs("Contact us", "Buy, Service, Exchange & Others", getResources().getDrawable(R.drawable.ic_call_black_24dp)));
        aboutUsArrayList.add(new AboutUs("Whatsapp", "Buy, Service, Exchange & Others", getResources().getDrawable(R.drawable.ic_icons_whatsapp)));
        aboutUsArrayList.add(new AboutUs("Facebook", "Madina", getResources().getDrawable(R.drawable.ic_icons_facebook)));
        aboutUsArrayList.add(new AboutUs("Web Site", "Madina Business Site", getResources().getDrawable(R.drawable.ic_baseline_web_24)));
        aboutUsArrayList.add(new AboutUs("Youtube", "Follow us on Youtube", getResources().getDrawable(R.drawable.ic_outline_videocam_24)));
        aboutUsArrayList.add(new AboutUs("Instagram", "Madina", getResources().getDrawable(R.drawable.ic_icons_instagram)));
        aboutUsArrayList.add(new AboutUs("We are located at", "9.9242109, 78.1127871", getResources().getDrawable(R.drawable.ic_location_on_black_24dp)));
        aboutUsArrayList.add(new AboutUs("Feedback", "Leave your feedback here..", getResources().getDrawable(R.drawable.ic_outline_feedback_24)));
        aboutUsArrayList.add(new AboutUs("Version", version, getResources().getDrawable(R.drawable.ic_phone_android_black_24dp)));
        aboutusAdapter.notifyData(aboutUsArrayList);

    }


    private void openWhatsApp() {
        try {
            Intent intent6 = new Intent(Intent.ACTION_VIEW);
            intent6.setData(Uri.parse("http://api.whatsapp.com/send?phone=916374864026"
                    + "&text=" + "Hi MADINA Mobiles Store"));
            intent6.setPackage("com.whatsapp.w4b");
            startActivity(intent6);
        } catch (ActivityNotFoundException e) {
            Intent intent6 = new Intent(Intent.ACTION_VIEW);
            intent6.setData(Uri.parse("http://api.whatsapp.com/send?phone=916374864026"
                    + "&text=" + "Hi MADINA Mobiles Store"));
            intent6.setPackage("com.whatsapp");
            startActivity(intent6);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Whatsapp Not found", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkAndRequestPermissions() {
        int externalStoragePermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (externalStoragePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            doCall();
        }
    }


    @Override
    public void onItemClick(int position) {
        String title = aboutUsArrayList.get(position).getTitle();
        if (title.equalsIgnoreCase("Facebook")) {
            Intent intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtra("url", "https://www.facebook.com/MadinaMobileStore/");
            intent.putExtra("name", "Facebook");
            startActivity(intent);
        } else if (title.equalsIgnoreCase("Web Site")) {
            Intent intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtra("url", "https://madinamobilestore.business.site/");
            intent.putExtra("name", "Business Site");
            startActivity(intent);
        }else if (title.equalsIgnoreCase("Youtube")) {
            Intent intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtra("url", "https://www.youtube.com/channel/UC1_aTabhyQOMOtmvMqevWgw/");
            intent.putExtra("name", "Youtube");
            startActivity(intent);
        } else if (title.equalsIgnoreCase("Instagram")) {
            Intent intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtra("url", "https://www.instagram.com/madinamobilestore/");
            intent.putExtra("name", "Instagram");
            startActivity(intent);
        } else if (title.equalsIgnoreCase("We are located at")) {
            Intent intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtra("url", "https://www.google.com/maps/place/Madina+Mobile+Store/@10.2817969,79.3164463,17z/data=!4m13!1m7!3m6!1s0x3b0003c89c811e9f:0x6ed13ca6f108e03a!2sMadina+Mobile+Store!8m2!3d10.2819257!4d79.3163745!10e1!3m4!1s0x3b0003c89c811e9f:0x6ed13ca6f108e03a!8m2!3d10.2819257!4d79.3163745");
            intent.putExtra("name", "We are located at");
            startActivity(intent);
        } else if (title.equalsIgnoreCase("Whatsapp")) {
            openWhatsApp();
        } else if (title.equalsIgnoreCase("Contact us")) {
            doCall();
        } else if (title.equalsIgnoreCase("Feedback")) {
            feedback();
        } else if (title.equalsIgnoreCase("Change Password")) {
            Intent intent = new Intent(getActivity(), ChangePassword.class);
            startActivity(intent);
        } else if (title.equalsIgnoreCase("guest")) {
            showBottomDialog();
        } else if (title.equalsIgnoreCase(sharedpreferences.getString(usernameKey, ""))) {
            logout();
        }
    }

    private void feedback() {

        mBottomSheetDialog = new RoundedBottomSheetDialog(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
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
                localHashMap.put("feedback", feedbackMainbean.getFeedback());
                localHashMap.put("user_name", sharedpreferences.getString(usernameKey, ""));
                localHashMap.put("user_phone", sharedpreferences.getString(user_phone, ""));

                return localHashMap;
            }
        };
        strReq.setRetryPolicy(AppConfig.getTimeOut());
        AppController.getInstance().addToRequestQueue(strReq);
    }

    private void doCall() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:916374864026"));
        startActivity(intent);
    }

    private void bottomSheetCancel() {
        if (mBottomSheetDialog != null) {
            mBottomSheetDialog.cancel();
        }
    }

    private void showBottomDialog() {
        final RoundedBottomSheetDialog mBottomSheetDialog = new RoundedBottomSheetDialog(getActivity());
        LayoutInflater inflater = AboutusActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.bottom_sheet_layout, null);


        final TextInputEditText username = dialogView.findViewById(R.id.username);
        final TextInputEditText phoneNumber = dialogView.findViewById(R.id.phoneNumber);
        final TextInputEditText password = dialogView.findViewById(R.id.password);

        TextInputLayout phoneNumberTxt = dialogView.findViewById(R.id.phoneNumberTxt);
        final TextInputLayout usernameTxt = dialogView.findViewById(R.id.usernameTxt);
        TextInputLayout passwordText = dialogView.findViewById(R.id.passwordText);

        final TextView accountTxt = dialogView.findViewById(R.id.accountTxt);
        final Button login = dialogView.findViewById(R.id.login);
        final Button request = dialogView.findViewById(R.id.request);

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accountTxt.getText().toString().equalsIgnoreCase("No account? ")) {
                    accountTxt.setText("Have Account? ");
                    request.setText("Login");
                    login.setText("Sign up");
                    usernameTxt.setVisibility(View.VISIBLE);
                } else {
                    accountTxt.setText("No Account? ");
                    login.setText("Login");
                    request.setText("Sign up");
                    usernameTxt.setVisibility(View.GONE);
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accountTxt.getText().toString().equalsIgnoreCase("Have Account? ") &&
                        username.getText().toString().length() <= 0) {
                    Toast.makeText(getActivity(), "Enter Valid name", Toast.LENGTH_LONG).show();
                    return;
                } else if (phoneNumber.getText().toString().length() <= 0) {
                    Toast.makeText(getActivity(), "Enter Phone number", Toast.LENGTH_LONG).show();
                    return;
                } else if (password.getText().toString().length() <= 0) {
                    Toast.makeText(getActivity(), "Enter Password", Toast.LENGTH_LONG).show();
                    return;
                }
                if (accountTxt.getText().toString().equalsIgnoreCase("No account? ")) {
                    checkLogin(phoneNumber.getText().toString(),
                            password.getText().toString(), mBottomSheetDialog);
                } else {
                    registerUser(phoneNumber.getText().toString(),
                            username.getText().toString(), password.getText().toString(), mBottomSheetDialog);
                }
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
                        RoundedBottomSheetDialog d = (RoundedBottomSheetDialog) dialog;
                        FrameLayout bottomSheet = d.findViewById(R.id.design_bottom_sheet);
                        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }, 0);
            }
        });
        mBottomSheetDialog.show();
    }

    private void checkLogin(final String username, final String password, final RoundedBottomSheetDialog mBottomSheetDialog) {
        String tag_string_req = "req_register";
        pDialog.setMessage("Login ...");
        showDialog();
        // showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.LOGIN_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response);
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
                        editor.putString(AppConfig.configKey, username);
                        editor.putString(usernameKey, name);
                        editor.putString(AppConfig.auth_key, auth_key);
                        editor.putString(AppConfig.user_id, user_id);
                        editor.commit();
                        mBottomSheetDialog.dismiss();

                        ArrayList<ProductListBean> productListTemp = dbHelper.getAllMainbeansyalu("guest");
                        for (int k = 0; k < productListTemp.size(); k++) {
                            dbHelper.insertMainbeanyalu(productListTemp.get(k), user_id);
                        }
                        dbHelper.deleteAllyalu("guest");

                        ArrayList<ProductListBean> wishListListTemp = dbWishList.getAllWishList("guest");
                        for (int k = 0; k < wishListListTemp.size(); k++) {
                            dbWishList.insertWishList(wishListListTemp.get(k), user_id);
                        }
                        dbWishList.deleteAllWishList("guest");

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setlistItems();
                            }
                        }, 500);
                    }
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Log.e("xxxxxxxxxx", e.toString());
                }
                hideDialog();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),
                        "Slow network found.Try again later", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                localHashMap.put("phone", username);
                localHashMap.put("password", password);
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(AppConfig.getTimeOut());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void registerUser(final String phoneNumber, final String username, final String password,
                              final RoundedBottomSheetDialog mBottomSheetDialog) {
        String tag_string_req = "req_register";
        pDialog.setMessage("Processing ...");
        showDialog();
        // showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                REGISTER_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response);
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");
                    String msg = jsonObject.getString("message");
                    if (success == 1) {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(AppConfig.configKey, phoneNumber);
                        editor.commit();
                        checkLogin(username, password, mBottomSheetDialog);
                    }
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                localHashMap.put("name", username);
                localHashMap.put("phone", phoneNumber);
                localHashMap.put("password", password);

                return localHashMap;
            }
        };
        strReq.setRetryPolicy(AppConfig.getTimeOut());
        AppController.getInstance().addToRequestQueue(strReq);
    }


}
