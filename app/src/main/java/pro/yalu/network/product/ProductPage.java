package pro.yalu.network.product;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import pro.yalu.network.app.AppConfig;
import pro.yalu.network.R;
import pro.yalu.network.app.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import static pro.yalu.network.app.AppConfig.mypreference;

public class ProductPage extends AppCompatActivity implements ProductItemClick {

    //    List<CategoryGrid> model = new ArrayList<>();
    ProgressDialog pDialog;
    RecyclerView recycler_product;
    private List<ProductBean> productList = new ArrayList<>();
    ProductListAdapter productListAdapter;
    LinearLayout empty_product;

    LinearLayout back;
    private DatabaseHelper db;
    SharedPreferences sharedpreferences;

    private String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_page);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        sharedpreferences = this.getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        recycler_product = findViewById(R.id.recycler_product);
        empty_product = findViewById(R.id.empty_product);
        db = new DatabaseHelper(this);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

      //  productListAdapter = new ProductListAdapter(getApplicationContext(), productList, this,sharedpreferences);
        final GridLayoutManager addManager1 = new GridLayoutManager(this, 2);
        recycler_product.setLayoutManager(addManager1);
        recycler_product.setAdapter(productListAdapter);

       // productURL();
    }


    /*private void productURL() {
        *//*this.pDialog.setMessage("Creating...");
        showDialog();*//*
        JSONObject jsonObject = new JSONObject();
        JsonObjectRequest local16 = new JsonObjectRequest(1, ProductList, jsonObject,
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
                                        if (getIntent().getStringExtra("cat_id").equals(object.getString("category"))) {
                                            ProductBean model = new ProductBean();
                                            model.setId(object.getString("id"));
                                            //model.setName("Name");
                                            model.setName(object.getString("pro_name"));
                                            if(!object.isNull("weight")){
                                                model.setWeight(object.getString("weight"));
                                            }else {
                                                model.setWeight("");
                                            }

                                            model.setProductrupeemrp(object.getString("price"));
                                            model.setProductrupeefinal(object.getString("final_price"));
                                            model.setSubCategory(object.getString("subcategory"));
                                            model.setTax(object.getString("tax"));
                                            model.setQty(object.getString("qty"));
                                            model.setP_no(object.getString("p_no"));
                                            model.setCurrency(object.getString("currency"));
                                            model.setOffer(object.getString("offer"));
                                            model.setImg1(object.getString("img1"));
                                            model.setImg2(object.getString("img2"));
                                            model.setImg3(object.getString("img3"));
                                            model.setImg4(object.getString("img4"));
                                            model.setImg5(object.getString("img5"));
                                            model.setImg6(object.getString("img6"));
                                            model.setDescription(object.getString("description"));
                                            model.setVideo(object.getString("video"));
                                            model.setTax_type(object.getString("tax_type"));
                                            if (!object.isNull("sub_cat_status")) {
                                                model.setSub_cat_status(object.getString("sub_cat_status"));
                                            } else {
                                                model.setSub_cat_status(null);

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
    private void addtocart(int position, final ProductBean productBean) {

    }

    private void hideDialog() {

        if (this.pDialog.isShowing()) this.pDialog.dismiss();
    }

    private void showDialog() {

        if (!this.pDialog.isShowing()) this.pDialog.show();
    }

    @Override
    public void onProductClick(int position) {
        if (productList.get(position).getSub_cat_status() == null) {
            Intent intent = new Intent(ProductPage.this, ProductActivity.class);
            intent.putExtra("data", productList.get(position));
            startActivity(intent);
        } else {
            Intent intent = new Intent(ProductPage.this, SubProductPage.class);
            intent.putExtra("id", productList.get(position).id);

            startActivity(intent);
        }
    }

    @Override
    public void onCartClick(int position) {
       long insert= db.insertMainbean(productList.get(position),"1",sharedpreferences.getString(AppConfig.user_id, ""));
        if(insert==1) {
            Toast.makeText(getApplicationContext(), "Item added successfully", Toast.LENGTH_SHORT).show();
            productListAdapter.notifyDataSetChanged();
        }else{
            Toast.makeText(getApplication(), getString(R.string.sign), Toast.LENGTH_SHORT).show();
        }
    }
}
