package pro.network.madina;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.network.moeidbannerlibrary.banner.BannerBean;
import com.network.moeidbannerlibrary.banner.BannerLayout;
import com.network.moeidbannerlibrary.banner.BaseBannerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pro.network.madina.ad.AdimgAdapter;
import pro.network.madina.app.AppConfig;
import pro.network.madina.app.AppController;
import pro.network.madina.product.AllOffersAdapter;
import pro.network.madina.product.BannerActivity;
import pro.network.madina.product.ProductActivity;
import pro.network.madina.product.ProductItemClick;
import pro.network.madina.product.ProductListAdapter;
import pro.network.madina.product.ProductListBean;

public class HomeActivity extends Fragment implements ProductItemClick, OnCategoryClick {

    ProgressDialog pDialog;
    RecyclerView categories;
    CategoryAdapter categoryAdapter;
    ProductListAdapter productListAdapter;
    RecyclerView rec_all_offers, recycler_product;
    SharedPreferences sharedpreferences;
    BannerLayout banner;
    AdimgAdapter shopAdapter;
    List<String> shopList = new ArrayList<>();
    private final ArrayList<Category> categoryList = new ArrayList<>();
    private List<ProductListBean> productList = new ArrayList<>();

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_home, container, false);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        sharedpreferences = getActivity().getSharedPreferences(AppConfig.mypreference, Context.MODE_PRIVATE);


        SharedPreferences.Editor edit = sharedpreferences.edit();
        edit.putString(AppConfig.userId, sharedpreferences.getString(AppConfig.user_id, ""));
        edit.commit();


        if (sharedpreferences.contains(AppConfig.usernameKey)) {
            // getSupportActionBar().setSubtitle(sharedpreferences.getString(AppConfig.usernameKey, ""));
        }

        recycler_product = view.findViewById(R.id.all_products);

        productList = new ArrayList<>();

        productListAdapter = new ProductListAdapter(getActivity(), productList, this, sharedpreferences);
        final GridLayoutManager addManager1 = new GridLayoutManager(getActivity(), 2);
        recycler_product.setLayoutManager(addManager1);
        recycler_product.setAdapter(productListAdapter);

        rec_all_offers= view.findViewById(R.id.recycler_view);
        shopAdapter = new AdimgAdapter(shopList, getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                if (getActivity() != null) {
                    LinearSmoothScroller smoothScroller = new LinearSmoothScroller(getActivity()) {
                        private static final float SPEED = 3500f;// Change this value (default=25f)

                        @Override
                        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                            return SPEED / displayMetrics.densityDpi;
                        }
                    };
                    smoothScroller.setTargetPosition(position);
                    startSmoothScroll(smoothScroller);
                }
            }

        };
        rec_all_offers.setHasFixedSize(true);
        rec_all_offers.setItemViewCacheSize(1000);
        rec_all_offers.setDrawingCacheEnabled(true);
        rec_all_offers.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rec_all_offers.setLayoutManager(layoutManager);
        rec_all_offers.setAdapter(shopAdapter);


        categoryList.add(new Category("Lite Used Mobiles", "#a4d7ed", "#B2E2F8", R.drawable.oldphone));
        categoryList.add(new Category("New Mobiles", "#f5bfd0", "#FAD4E1", R.drawable.phone));
        categoryList.add(new Category("Tablet", "#cdf59f", "#D8FAB0", R.drawable.tablet));
        categoryList.add(new Category("Smart watches", "#eeaffa", "#E9A7F4", R.drawable.watch));
        categoryList.add(new Category("Headphones", "#faca8e", "#F6DBB9", R.drawable.headphone));
        categoryList.add(new Category("Laptop", "#a4d7ed", "#B2E2F8", R.drawable.labtop));
        categoryList.add(new Category("TV", "#f7abc3", "#FAD4E1", R.drawable.tv));
        categoryList.add(new Category("Home Theatre", "#f2e299", "#FBE584", R.drawable.hometheatre));
        categoryList.add(new Category("AC", "#c4f788", "#D8FAB0", R.drawable.ac));
        categoryList.add(new Category("Service", "#ea9ef7", "#E9A7F4", R.drawable.service));
        categoryList.add(new Category("Accessories", "#ffe18a", "#D8FAB0", R.drawable.accessories));
        categories = view.findViewById(R.id.categories);
        categoryAdapter = new CategoryAdapter(getActivity(), categoryList, this);
        final LinearLayoutManager addManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        categories.setLayoutManager(addManager2);
        categories.setAdapter(categoryAdapter);


        banner = view.findViewById(R.id.Banner);

        fetchBanner();
        getAllStaff();

        fetchProductList("");
        return view;
    }

    private void startAllProduct(String view) {
        Intent intent = new Intent(getActivity(), AllProductActivity.class);
        intent.putExtra("type", view);
        startActivity(intent);
    }


    private void fetchProductList(final String searchKey) {
        String tag_string_req = "req_register";
        // showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.PRODUCT_GET_ALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    if (success == 1) {
                        JSONArray jsonArray = jObj.getJSONArray("data");
                        productList = new ArrayList<>();
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
                            productList.add(productListBean);
                            if (i == 6) {
                                break;
                            }
                        }
                        productListAdapter.notifyData(productList);
                        // viewAllBtn.setText("View " + jsonArray.length() + " Products");
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
                localHashMap.put("searchKey", searchKey);
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(AppConfig.getTimeOut());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void fetchBanner() {
        String tag_string_req = "req_register";
        // showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.BANNERS_GET_ALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");

                    if (success == 1) {
                        JSONArray jsonArray = jObj.getJSONArray("data");
                        List<BannerBean> bannerBeans = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            BannerBean bannerBean = new BannerBean();
                            bannerBean.setImages(jsonObject.getString("banner"));
                            bannerBean.setId(jsonObject.getString("id"));
                            bannerBean.setDescription(jsonObject.getString("description"));
                            bannerBeans.add(bannerBean);
                        }
                        BaseBannerAdapter webBannerAdapter = new BaseBannerAdapter(getContext(), bannerBeans);
                        webBannerAdapter.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
                            @Override
                            public void onItemClick(BannerBean bannerBean) {
                                Intent intent = new Intent(getActivity(), BannerActivity.class);
                                intent.putExtra("description", bannerBean.getDescription());
                                intent.putExtra("image", bannerBean.getImages());
                                startActivityForResult(intent, 100);
                            }
                        });
                        banner.setAdapter(webBannerAdapter);

                    } else {
                        Toast.makeText(getContext(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("xxxxxxxxxxx", e.toString());
                    Toast.makeText(getContext(), "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(AppConfig.getTimeOut());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void addtocart(int position) {
        productListAdapter.notifyDataSetChanged();
    }


    @Override
    public void onProductClick(int position) {
        Intent intent = new Intent(getActivity(), ProductActivity.class);
        intent.putExtra("data", productList.get(position));
        startActivity(intent);

    }

    @Override
    public void onCartClick(int position) {
        addtocart(position);
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    private void getAllStaff() {
        String tag_string_req = "req_register";
        pDialog.setMessage("Fetching ...");
        // showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.ALL_AD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response);
                //hideDialog();
                try {
                    shopList = new ArrayList<>();
                    JSONObject jObj = new JSONObject(response);
                    String files = jObj.getString("files");
                    String[] filesStrings = files.split(",");
                    for (int i = 0; i < filesStrings.length; i++) {
                        if (filesStrings[i] != null &&
                                (filesStrings[i].contains(".jpg")
                                        || filesStrings[i].contains(".png")
                                        || filesStrings[i].contains(".PNG")
                                        || filesStrings[i].contains(".JPEG"))) {
                            shopList.add(filesStrings[i]);
                        }
                    }
                    shopAdapter.notifyData(shopList);
                    autoScroll();
                } catch (JSONException e) {
                    Log.e("xxxxxxxxxxx", e.toString());
                    Toast.makeText(getActivity(), "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("Registration Error: ", error.getMessage());
                Toast.makeText(getActivity(),
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
                // hideDialog();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(AppConfig.getTimeOut());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onCategoryItem(int position) {
        String title = categoryList.get(position).title;
        String mobiles = title;
        if (title.equalsIgnoreCase("Lite used mobiles")) {
            mobiles = "Old Mobiles";
        } else if (title.equalsIgnoreCase("Mobiles")) {
            mobiles = "New Mobiles";
        }
        startAllProduct(mobiles);
    }

    public void autoScroll() {
        final int speedScroll = 2000;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int count = 0;
            @Override
            public void run() {
                if (count == shopAdapter.getItemCount())
                    count = 0;
                if (count < shopAdapter.getItemCount()) {
                    rec_all_offers.smoothScrollToPosition(++count);
                    handler.postDelayed(this, speedScroll);
                }
            }
        };
        handler.postDelayed(runnable, 100);
    }

}
