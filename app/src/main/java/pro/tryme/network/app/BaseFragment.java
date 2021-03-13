package pro.tryme.network.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import pro.tryme.network.LoginActivity;

import static pro.tryme.network.app.AppConfig.auth_key;
import static pro.tryme.network.app.AppConfig.configKey;
import static pro.tryme.network.app.AppConfig.mypreference;
import static pro.tryme.network.app.AppConfig.user_id;

public abstract class BaseFragment extends Fragment {

    protected SharedPreferences sharedpreferences;
    protected ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        sharedpreferences = getActivity().getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        return startDemo(inflater,container);
    }

    protected void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    protected void logout() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove(configKey);
        editor.remove(auth_key);
        editor.remove(user_id);
        editor.commit();
        editor.apply();
        startActivity(new Intent(getActivity(), LoginActivity.class));
        //   getfinishAffinity();
    }

    protected abstract View startDemo(LayoutInflater inflater, ViewGroup container);

    protected void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    protected void hideDialog() {
        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();
    }
}
