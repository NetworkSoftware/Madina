package pro.tryme.network;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import pro.tryme.network.app.AppConfig;
import pro.tryme.network.app.AppController;
import pro.tryme.network.app.DatabaseHelperYalu;
import pro.tryme.network.app.DbWishList;
import pro.tryme.network.cart.CartActivity;
import pro.tryme.network.product.AllOffersAdapter;
import pro.tryme.network.product.BannerActivity;
import pro.tryme.network.product.ProductActivity;
import pro.tryme.network.product.ProductItemClick;
import pro.tryme.network.product.ProductListAdapter;
import pro.tryme.network.product.ProductListBean;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
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

public class HomeActivity extends Fragment implements ProductItemClick, OnMapReadyCallback {

    ProgressDialog pDialog;
    private String TAG = getClass().getSimpleName();

    LinearLayout mobiles, oldmobiles, headphone, tv,laptops,smartwatches,ac,cameraandLens,tshirts,service,tablet;

    RecyclerView recycler_product;
    private List<ProductListBean> productList = new ArrayList<>();
    ProductListAdapter productListAdapter;

    RecyclerView rec_all_offers;
    private List<BannerBean> allOffs = new ArrayList<>();
    AllOffersAdapter allOffersAdapter;

    SharedPreferences sharedpreferences;
    CartActivity.OnCartItemChange onCartItemChange;
    BannerLayout banner;

    private View view;
    GoogleMap googleMap;

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

        recycler_product = view.findViewById(R.id.recycler_product);

        productList = new ArrayList<>();

        productListAdapter = new ProductListAdapter(getActivity(), productList, this, sharedpreferences);
        final GridLayoutManager addManager1 = new GridLayoutManager(getActivity(), 2);
        recycler_product.setLayoutManager(addManager1);
        recycler_product.setAdapter(productListAdapter);

        allOffs = new ArrayList<>();


        rec_all_offers = view.findViewById(R.id.rec_all_offers);
        allOffersAdapter = new AllOffersAdapter(getContext(), allOffs);
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
        rec_all_offers.setAdapter(allOffersAdapter);


        mobiles = view.findViewById(R.id.mobiles);
        mobiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAllProduct(view);
            }
        });

        tv = view.findViewById(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAllProduct(view);
            }
        });
        oldmobiles = view.findViewById(R.id.oldmobiles);
        oldmobiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAllProduct(view);
            }
        });
        headphone = view.findViewById(R.id.headphone);
        headphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAllProduct(view);
            }
        });

        smartwatches = view.findViewById(R.id.smartwatches);
        smartwatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAllProduct(view);
            }
        });
        laptops = view.findViewById(R.id.laptops);
        laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAllProduct(view);
            }
        });

        ac = view.findViewById(R.id.ac);
        ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAllProduct(view);
            }
        });
        tablet = view.findViewById(R.id.tablet);
        tablet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAllProduct(view);
            }
        });
        cameraandLens = view.findViewById(R.id.cameraandLens);
        cameraandLens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAllProduct(view);
            }
        });
        tshirts = view.findViewById(R.id.tshirts);
        tshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAllProduct(view);
            }
        });
        service = view.findViewById(R.id.service);
        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAllProduct(view);
            }
        });


        banner = view.findViewById(R.id.Banner);


        fetchBanner();
        fetchProductList("");

        onCartItemChange = (CartActivity.OnCartItemChange) getActivity();

        FragmentManager fm = getActivity().getSupportFragmentManager();/// getChildFragmentManager();
        SupportMapFragment supportMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (supportMapFragment == null) {
            supportMapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, supportMapFragment).commit();
        }
        supportMapFragment.getMapAsync(this);


        return view;
    }

    private void startAllProduct(View view) {
        Intent intent = new Intent(getActivity(), AllProductActivity.class);
        intent.putExtra("type", view.getTag().toString());
        startActivity(intent);
    }

    public void autoScroll() {
        final int speedScroll = 2000;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int count = 0;

            @Override
            public void run() {
                if (count == allOffersAdapter.getItemCount())
                    count = 0;
                if (count < allOffersAdapter.getItemCount()) {
                    rec_all_offers.smoothScrollToPosition(++count);
                    handler.postDelayed(this, speedScroll);
                }
            }
        };
        handler.postDelayed(runnable, 100);
    }

    private void fetchProductList(final String searchKey) {
        String tag_string_req = "req_register";
        // showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.PRODUCT_GET_ALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response.toString());
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
                Log.d("Register Response: ", response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");

                    if (success == 1) {
                        JSONArray jsonArray = jObj.getJSONArray("data");
                        List<BannerBean> bannerBeans = new ArrayList<>();
                        allOffs = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            BannerBean bannerBean = new BannerBean();
                            bannerBean.setImages(jsonObject.getString("banner"));
                            bannerBean.setId(jsonObject.getString("id"));
                            bannerBean.setDescription(jsonObject.getString("description"));
                            if ("Slider".equalsIgnoreCase(jsonObject.getString("type"))) {
                                allOffs.add(bannerBean);
                            } else {
                                bannerBeans.add(bannerBean);
                            }
                        }
                        BaseBannerAdapter webBannerAdapter = new BaseBannerAdapter(getActivity(), bannerBeans);
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
                        banner.setAutoPlaying(true);
                        banner.setAutoPlayDuration(2000);

                        allOffersAdapter.notifyData(allOffs);
                        autoScroll();

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
        if (onCartItemChange != null) {
            onCartItemChange.onCartChange();
        }
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        initLocation();

    }

    private void initLocation() {
        if(googleMap!=null){
        LatLng sydney = new LatLng(11.1017492, 77.3499228);
        googleMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("TRYME MOBILES"));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(11.1017492, 77.3499228), 15f));
        }
    }
}
