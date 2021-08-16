package pro.network.madina;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pro.network.madina.app.AppConfig;
import pro.network.madina.app.AppController;
import pro.network.madina.app.BaseActivity;
import pro.network.madina.app.DatabaseHelperMadina;
import pro.network.madina.cart.CartActivity;
import pro.network.madina.chip.ChipAdapter;
import pro.network.madina.chip.ChipBean;
import pro.network.madina.chip.OnChip;
import pro.network.madina.product.ProductActivity;
import pro.network.madina.product.ProductItemClick;
import pro.network.madina.product.ProductListAdapter;
import pro.network.madina.product.ProductListBean;

public class AllProductActivity extends BaseActivity implements ProductItemClick, OnChip {

    RecyclerView recycler_product;
    ProductListAdapter productListAdapter;
    SharedPreferences sharedpreferences;
    CartActivity.OnCartItemChange onCartItemChange;
    RecyclerView recycler_chips;
    private final String TAG = getClass().getSimpleName();
    private DatabaseHelperMadina db;
    private List<ProductListBean> productList = new ArrayList<>();
    private final List<ProductListBean> permanantList = new ArrayList<>();
    private SearchView searchView;
    private TextView cart_badge;
    private Set<String> subCategories = new HashSet<>();
    private ArrayList<ChipBean> chipBeans = new ArrayList<>();
    private String selectedType = "ALL";
    ChipAdapter chipAdapter;

    @Override
    protected void startDemo() {
        setContentView(R.layout.activity_all_product);

        sharedpreferences = getApplicationContext().getSharedPreferences(AppConfig.mypreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedpreferences.edit();
        edit.putString(AppConfig.userId, sharedpreferences.getString(AppConfig.user_id, ""));
        edit.commit();

        db = new DatabaseHelperMadina(getApplicationContext());
        recycler_product = findViewById(R.id.recycler_product);

        productList = new ArrayList<>();

        productListAdapter = new ProductListAdapter(getApplication(), productList, this, sharedpreferences);
        final GridLayoutManager addManager1 = new GridLayoutManager(getApplication(), 2);
        recycler_product.setLayoutManager(addManager1);
        recycler_product.setAdapter(productListAdapter);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String type = getIntent().getStringExtra("type");
        getSupportActionBar().setTitle(type != null ? type : getString(R.string.app_name_home));
        getSupportActionBar().setSubtitle(selectedType);

        chipBeans = new ArrayList<>();
        recycler_chips = findViewById(R.id.recycler_chips);
        chipAdapter = new ChipAdapter(AllProductActivity.this, chipBeans, this, selectedType);
        final LinearLayoutManager addManager2 = new LinearLayoutManager(AllProductActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recycler_chips.setLayoutManager(addManager2);
        recycler_chips.setAdapter(chipAdapter);

    }


    private void fetchProductList(final String searchKey) {
        String tag_string_req = "req_register";
        pDialog.setMessage("Loading...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.PRODUCT_GET_ALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                Log.d("Register Response: ", response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    productList = new ArrayList<>();
                    subCategories = new HashSet<>();
                    if (success == 1) {
                        JSONArray jsonArray = jObj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            ProductListBean productListBean = new ProductListBean();
                            productListBean.setId(jsonObject.getString("id"));
                            productListBean.setBrand(jsonObject.getString("brand"));
                            productListBean.setPrice(jsonObject.getString("price"));
                            productListBean.setRam(jsonObject.getString("ram"));
                            productListBean.setRom(jsonObject.getString("rom"));
                            productListBean.setModel(jsonObject.getString("model"));
                            productListBean.setImage(jsonObject.getString("image"));
                            productListBean.setDescription(jsonObject.getString("description"));
                            productListBean.setStock_update(jsonObject.getString("stock_update"));
                            productListBean.setCreatedOn(jsonObject.getString("createdon"));
                            subCategories.add(productListBean.getBrand().toUpperCase());
                            permanantList.add(productListBean);
                            productList.add(productListBean);
                        }
                        productListAdapter.notifyData(productList);
                        if (chipBeans.size() <= 0) {
                            chipBeans = new ArrayList<>();
                            chipBeans.add(new ChipBean("ALL"));
                            chipBeans.add(new ChipBean("IPHONE"));
                            for (String e : subCategories) {
                                if(!e.equalsIgnoreCase("IPHONE")) {
                                    chipBeans.add(new ChipBean(e));
                                }
                            }
                            chipAdapter.notifyData(chipBeans);
                            selectedType = "ALL";
                            getSupportActionBar().setSubtitle(selectedType);
                            chipAdapter.notifyData(selectedType);
                        }

                    } else {
                        Toast.makeText(getApplication(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    getSupportActionBar().setSubtitle(productList.size() + " Products");
                } catch (JSONException e) {
                    Log.e("xxxxxxxxxxx", e.toString());
                    Toast.makeText(getApplication(), "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(getApplication(),
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                localHashMap.put("searchKey", searchKey);
                String type = getIntent().getStringExtra("type");
                if (type != null) {
                    localHashMap.put("category", type);
                }
                if (selectedType != null && !selectedType.equalsIgnoreCase("All")) {
                    localHashMap.put("scategory", selectedType);
                }
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(AppConfig.getTimeOut());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void addToCart(int position) {
        productList.get(position).setQty("1");
        db.insertMainbeanyalu(productList.get(position), sharedpreferences.getString(AppConfig.user_id, ""));
        Toast.makeText(getApplication(), "Item added successfully", Toast.LENGTH_SHORT).show();
        if (onCartItemChange != null) {
            onCartItemChange.onCartChange();
        }
        setupBadge();
        productListAdapter.notifyDataSetChanged();
    }


    @Override
    public void onProductClick(int position) {
        try {
            Intent intent = new Intent(getApplication(), ProductActivity.class);
            intent.putExtra("data", productList.get(position));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Please Wait, Try again", Toast.LENGTH_SHORT).show();
            Log.e("xxxxxxxxxxxxx", e.toString());
        }
    }

    @Override
    public void onCartClick(int position) {
        addToCart(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.product_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                fetchProductList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                fetchProductList(query);
                return false;
            }
        });

        final MenuItem menuItem = menu.findItem(R.id.cart);

        View actionView = menuItem.getActionView();
        cart_badge = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        if (getIntent() != null && getIntent().getBooleanExtra("isSearch", false)) {
            searchView.setIconified(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.cart:
                Intent intent = new Intent(AllProductActivity.this, CartActivity.class);
                startActivity(intent);
                return true;
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupBadge() {
        int cartCountYalu = db.getCartCountYalu(sharedpreferences.getString(AppConfig.user_id, ""));
        if (cart_badge != null) {
            if (cartCountYalu == 0) {
                if (cart_badge.getVisibility() != View.GONE) {
                    cart_badge.setVisibility(View.GONE);
                }
            } else {
                cart_badge.setText(String.valueOf(Math.min(cartCountYalu, 99)));
                if (cart_badge.getVisibility() != View.VISIBLE) {
                    cart_badge.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchProductList("");
        setupBadge();
    }

    @Override
    public void onItemClick(String type) {
        selectedType = type;
        getSupportActionBar().setSubtitle(selectedType);
        chipAdapter.notifyData(selectedType);
        productList = new ArrayList<>();
        for (int i = 0; i < permanantList.size(); i++) {
            ProductListBean productListBean = permanantList.get(i);
            if (selectedType.equalsIgnoreCase("all")
                    || selectedType.equalsIgnoreCase(productListBean.brand)) {
                productList.add(productListBean);
            }
        }
        productListAdapter.notifyData(productList);
    }
}
