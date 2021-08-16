package pro.network.madina.orders;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import pro.network.madina.R;
import pro.network.madina.app.AppController;
import pro.network.madina.app.BaseFragment;
import pro.network.madina.product.ProductListBean;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pro.network.madina.app.AppConfig;

public class MyOrderPage extends BaseFragment {
    private static final int FINE_LOCATION_CODE = 199;
    ProgressDialog pDialog;

    RecyclerView myorders_list;
    private ArrayList<MyorderBean> myorderBeans = new ArrayList<>();
    MyOrderListAdapter myOrderListAdapter;
    SharedPreferences sharedpreferences;
    NestedScrollView scroll;
    LinearLayout empty_product;
    private View view;

    @Override
    protected View startDemo(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.activity_myorder, container, false);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        sharedpreferences = getActivity().getSharedPreferences(AppConfig.mypreference, Context.MODE_PRIVATE);

        myorders_list = view.findViewById(R.id.myorders_list);
        myOrderListAdapter = new MyOrderListAdapter(getActivity(), myorderBeans);
        final LinearLayoutManager addManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        myorders_list.setLayoutManager(addManager1);
        myorders_list.setAdapter(myOrderListAdapter);
        scroll = view.findViewById(R.id.scroll);
        empty_product = view.findViewById(R.id.empty_product);

        getAllOrder();

        return view;
    }

    private void getAllOrder() {
        String tag_string_req = "req_register";
        pDialog.setMessage("fetching..");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.ORDER_GET_ALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");

                    if (success == 1) {
                        JSONArray jsonArray = jObj.getJSONArray("data");
                        myorderBeans = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            MyorderBean order = new MyorderBean();
                            order.setAmount(jsonObject.getString("price"));
                            order.setQuantity(jsonObject.getString("quantity"));
                            order.setStatus(jsonObject.getString("status"));
                            order.setitems(jsonObject.getString("items"));
                            order.setCreatedon(jsonObject.getString("createdon"));
                            try {
                                String log = jsonObject.getString("items");

                                String regex = "\"description\":\"";

                                String regexTo = "\",";

                                Pattern pattern = Pattern.compile(regex);
                                Matcher matcher = pattern.matcher(log);

                                ArrayList<String> strings = new ArrayList<>();
                                while (matcher.find()) {
                                    Matcher matcherTo = Pattern.compile(regexTo).matcher(log);
                                    if ((matcherTo.find(matcher.end())) || matcherTo.find()) {
                                        strings.add(log.substring(matcher.start(), matcherTo.end()));
                                    }
                                }
                                for (int k = 0; k < strings.size(); k++) {
                                    log = log.replace(strings.get(k), "");
                                }


                                ObjectMapper mapper = new ObjectMapper();
                                Object listBeans = new Gson().fromJson(log,
                                        Object.class);
                                ArrayList<ProductListBean> accountList = mapper.convertValue(
                                        listBeans,
                                        new TypeReference<ArrayList<ProductListBean>>() {
                                        }
                                );
                                order.setProductBeans(accountList);
                            } catch (Exception e) {
                                order.setProductBeans(new ArrayList<ProductListBean>());
                            }
                            myorderBeans.add(order);

                        }
                        myOrderListAdapter.notifyData(myorderBeans);
                        if (myorderBeans.size() == 0) {
                            empty_product.setVisibility(View.VISIBLE);
                            scroll.setVisibility(View.GONE);
                        } else {
                            empty_product.setVisibility(View.GONE);
                            scroll.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("xxxxxxxxxxx", e.toString());
                    Toast.makeText(getActivity(), "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                localHashMap.put("user", sharedpreferences.getString(AppConfig.user_id, ""));
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(AppConfig.getTimeOut());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


}
