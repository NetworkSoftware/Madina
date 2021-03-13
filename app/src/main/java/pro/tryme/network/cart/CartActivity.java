package pro.tryme.network.cart;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import pro.tryme.network.R;
import pro.tryme.network.app.AppConfig;
import pro.tryme.network.app.AppController;
import pro.tryme.network.app.BaseActivity;
import pro.tryme.network.app.DatabaseHelperYalu;

import pro.tryme.network.orders.MyOrderListAdapter;
import pro.tryme.network.orders.MyorderBean;
import pro.tryme.network.product.ProductListBean;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pro.tryme.network.app.AppConfig.ORDER_CREATE;
import static pro.tryme.network.app.AppConfig.decimalFormat;
import static pro.tryme.network.app.AppConfig.mypreference;

public class CartActivity extends BaseActivity implements CartItemClick {

    public interface OnCartItemChange {
        void onCartChange();
    }

    private List<MyorderBean> myorderBeans = new ArrayList<>();
    MyOrderListAdapter myOrderListAdapter;
    RecyclerView cart_list;
    private List<ProductListBean> productList = new ArrayList<>();
    CartListAdapter cartListAdapter;
    ProgressDialog pDialog;
    private String TAG = getClass().getSimpleName();
    private DatabaseHelperYalu db;
    TextView total;
    Button order;
    View view;
    OnCartItemChange onCartItemChange;
    NestedScrollView nestedScrollView;
    CardView continueCard;
    LinearLayout empty_product;
    SharedPreferences sharedpreferences;

    @Override
    protected void startDemo() {
        setContentView(R.layout.activity_cart);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        db = new DatabaseHelperYalu(this);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cart_list = findViewById(R.id.cart_list);
        cartListAdapter = new CartListAdapter(this, productList, this);
        final LinearLayoutManager addManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        cart_list.setLayoutManager(addManager1);
        cart_list.setAdapter(cartListAdapter);

        order = findViewById(R.id.order);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(CartActivity.this, R.style.RoundShapeTheme);
                LayoutInflater inflater = CartActivity.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.alert_dialog, null);
                TextView title = dialogView.findViewById(R.id.title);
                title.setText("Do you want to confirm this order? If yes Order will be Placed and TRYME MOBILES admin will contact you shortly.");
                dialogBuilder.setTitle("Alert")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                orderpage();
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                dialogBuilder.setView(dialogView);
                final AlertDialog b = dialogBuilder.create();
                b.setCancelable(false);
                b.show();
            }
        });


        nestedScrollView = findViewById(R.id.nested);
        continueCard = findViewById(R.id.continueCard);
        empty_product = findViewById(R.id.empty_product);

        getAllCart();
    }


    private void getAllCart() {
        productList.clear();
        productList = db.getAllMainbeansyalu(sharedpreferences.getString(AppConfig.user_id, ""));
        float grandTotal = 0f;
        for (int i = 0; i < productList.size(); i++) {
            String qty = productList.get(i).qty;
            if (qty == null || qty.length() <= 0) {
                qty = "1";
            }
            float startValue = Float.parseFloat(productList.get(i).price) * Integer.parseInt(qty);
            grandTotal = grandTotal + startValue;
        }
        ((TextView) findViewById(R.id.subtotal)).setText("₹" + decimalFormat.format(grandTotal) + ".00");
        ((TextView) findViewById(R.id.grandtotal)).setText("₹" + decimalFormat.format(grandTotal) + ".00");
        ((TextView) findViewById(R.id.total)).setText("₹" + decimalFormat.format(grandTotal) + ".00");
        cart_list.setAdapter(cartListAdapter);
        cartListAdapter.notifyData(productList);
        if (productList.size() == 0) {
            empty_product.setVisibility(View.VISIBLE);
            nestedScrollView.setVisibility(View.GONE);
            continueCard.setVisibility(View.GONE);
        } else {
            empty_product.setVisibility(View.GONE);
            nestedScrollView.setVisibility(View.VISIBLE);
            continueCard.setVisibility(View.VISIBLE);
        }

    }

    private void orderpage() {
        String tag_string_req = "req_register";
        pDialog.setMessage("Processing ...");
        showDialog();
        // showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                ORDER_CREATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response.toString());
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response.split("0000")[1]);
                    boolean success = jsonObject.getBoolean("success");
                    String msg = jsonObject.getString("message");
                    if (success) {
                        db.deleteAllyalu(sharedpreferences.getString(AppConfig.user_id, ""));
                        if (onCartItemChange != null) {
                            onCartItemChange.onCartChange();
                        }
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
                localHashMap.put("quantity", String.valueOf(productList.size()));
                localHashMap.put("price", ((TextView) findViewById(R.id.grandtotal)).getText().toString());
                localHashMap.put("status", "ordered");
                localHashMap.put("user", sharedpreferences.getString(AppConfig.user_id, ""));
                ArrayList<ProductListBean> productListBeans = new ArrayList<>();
                for (int i = 0; i < productList.size(); i++) {
                    ProductListBean productListBean = productList.get(i);
                    ArrayList<String> urls = new Gson().fromJson(productListBean.image, (Type) List.class);
                    productListBean.setImage(urls.get(0));
                    productListBeans.add(productListBean);
                }
                localHashMap.put("items", new Gson().toJson(productListBeans));
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(AppConfig.getTimeOut());
        AppController.getInstance().addToRequestQueue(strReq);
    }


    @Override
    public void OnQuantityChange(int position, int qty) {

        productList.get(position).setQty(qty + "");
        db.updateMainbeanyalu(productList.get(position), sharedpreferences.getString(AppConfig.user_id, ""));
        if (onCartItemChange != null) {
            onCartItemChange.onCartChange();
        }

        getAllCart();
    }

    @Override
    public void ondeleteClick(int position) {
        db.deleteMainbeanyalu(productList.get(position), sharedpreferences.getString(AppConfig.user_id, ""));
        productList.remove(position);
        cartListAdapter.notifyData(position);
        Toast.makeText(getApplication(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
        getAllCart();
        if (onCartItemChange != null) {
            onCartItemChange.onCartChange();
        }
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