package pro.tryme.network.product;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import pro.tryme.network.app.AppConfig;
import pro.tryme.network.app.AppController;
import pro.tryme.network.R;
import pro.tryme.network.app.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pro.tryme.network.app.AppConfig.mypreference;

public class SubProductPage extends AppCompatActivity implements ProductItemClick {

    //    List<CategoryGrid> model = new ArrayList<>();
    ProgressDialog pDialog;
    RecyclerView recycler_product;
    private List<ProductBean> productList = new ArrayList<>();
    ProductListAdapter productListAdapter;
    LinearLayout empty_product;
    LinearLayout ll1;
    private DatabaseHelper db;
    SharedPreferences sharedpreferences;

    private String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_page);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        db = new DatabaseHelper(this);
        recycler_product = findViewById(R.id.recycler_product);
        empty_product = findViewById(R.id.empty_product);
        ll1 = findViewById(R.id.ll1);
        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        sharedpreferences = this.getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        //productListAdapter = new ProductListAdapter(getApplicationContext(), productList, this, sharedpreferences);
        final GridLayoutManager addManager1 = new GridLayoutManager(this, 2);
        recycler_product.setLayoutManager(addManager1);
        recycler_product.setAdapter(productListAdapter);

        //productURL();
    }


/*
    private void productURL() {
        */
/*this.pDialog.setMessage("Creating...");
        showDialog();*//*

        JSONObject jsonObject = new JSONObject();
        JsonObjectRequest local16 = new JsonObjectRequest(1, SubProductList, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("categdrytyguioj", response.toString());
                        pDialog.hide();

                        try {
                            if (response != null && response.length() > 0) {
                                productList.clear();
                                // String str = response.getString("status");
                                if (response.getInt("code") == 200) {
                                    JSONArray array = response.getJSONArray("data");


                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object = array.getJSONObject(i);
                                        if (getIntent().getStringExtra("id").equals(object.getString("subcategory"))) {
                                            ProductBean model = new ProductBean();
                                            model.setId(object.getString("id"));
                                            //model.setName("Name");
                                            model.setName(object.getString("pro_name"));
                                            if (!object.isNull("weight")) {
                                                model.setWeight(object.getString("weight"));
                                            } else {
                                                model.setWeight("");
                                            }
                                            model.setProductrupeemrp(object.getString("price"));
                                            model.setProductrupeefinal(object.getString("final_price"));
                                            model.setTax(object.getString("tax"));
                                            model.setTax_type(object.getString("tax_type"));
                                            model.setCurrency(object.getString("currency"));
                                            model.setOffer(object.getString("offer"));
                                            model.setDescription(object.getString("description"));
                                            model.setImg1(object.getString("img1"));
                                            model.setImg2(object.getString("img2"));
                                            model.setImg3(object.getString("img3"));
                                            model.setImg4(object.getString("img4"));
                                            model.setImg5(object.getString("img5"));
                                            model.setImg6(object.getString("img6"));
                                            if (!object.isNull("video")) {
                                                model.setVideo(object.getString("video"));
                                            } else {
                                                model.setVideo("");
                                            }
                                            productList.add(model);
                                        }
                                    }
                                    if (productList.size() > 0) {
                                        recycler_product.setAdapter(productListAdapter);
                                        productListAdapter.notifyDataSetChanged();
                                        empty_product.setVisibility(View.GONE);
                                        recycler_product.setVisibility(View.VISIBLE);
                                    } else {
                                        empty_product.setVisibility(View.VISIBLE);
                                        recycler_product.setVisibility(View.GONE);
                                    }
                                }
                                // Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException localJSONException) {
                            localJSONException.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError paramVolleyError) {
                Toast.makeText(getApplicationContext(), paramVolleyError.getMessage(), Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap<String, String> localHashMap = new HashMap<String, String>();
                return localHashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(local16, TAG);
    }
*/

    private void hideDialog() {

        if (this.pDialog.isShowing()) this.pDialog.dismiss();
    }

    private void showDialog() {

        if (!this.pDialog.isShowing()) this.pDialog.show();
    }

    @Override
    public void onProductClick(int position) {
        Intent intent = new Intent(SubProductPage.this, ProductActivity.class);
        intent.putExtra("data", productList.get(position));
        startActivity(intent);
    }

    @Override
    public void onCartClick(int position) {
        long insert = db.insertMainbean(productList.get(position), "1", sharedpreferences.getString(AppConfig.userId, ""));
        if (insert == 1) {
            Toast.makeText(getApplicationContext(), "Item added successfully", Toast.LENGTH_SHORT).show();
            productListAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getApplication(), getString(R.string.sign), Toast.LENGTH_SHORT).show();
        }
    }
}
