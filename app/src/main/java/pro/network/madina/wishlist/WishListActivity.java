package pro.network.madina.wishlist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pro.network.madina.R;
import pro.network.madina.app.AppConfig;
import pro.network.madina.app.DatabaseHelperMadina;
import pro.network.madina.app.DbWishList;
import pro.network.madina.cart.CartActivity;
import pro.network.madina.product.ProductListBean;

import static pro.network.madina.app.AppConfig.decimalFormat;
import static pro.network.madina.app.AppConfig.mypreference;

public class WishListActivity extends Fragment implements WishListClick {

    RecyclerView cart_list;
    private List<ProductListBean> productList = new ArrayList<>();
    WishListAdapter wishListAdapter;
    ProgressDialog pDialog;
    private String TAG = getClass().getSimpleName();
    private DatabaseHelperMadina db;
    private DbWishList dbWishList;
    Button order;
    CartActivity.OnCartItemChange onCartItemChange;
    NestedScrollView nestedScrollView;
    CardView continueCard;
    LinearLayout empty_product;
    TextView emptyText;
    SharedPreferences sharedpreferences;
View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_cart, container, false);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }

        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        db = new DatabaseHelperMadina(getActivity());
        dbWishList = new DbWishList(getActivity());
        sharedpreferences = getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cart_list = view.findViewById(R.id.cart_list);
        wishListAdapter = new WishListAdapter(getActivity(), productList, this);
        final LinearLayoutManager addManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        cart_list.setLayoutManager(addManager1);
        cart_list.setAdapter(wishListAdapter);

        order = view.findViewById(R.id.order);
        order.setVisibility(View.GONE);
        /*order.setText("Order Page");
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WishListActivity.this, CartActivity.class));
            }
        });*/

        nestedScrollView = view.findViewById(R.id.nested);
        continueCard = view.findViewById(R.id.continueCard);
        empty_product = view.findViewById(R.id.empty_product);
        emptyText = view.findViewById(R.id.emptyText);
        emptyText.setText("No Items in WishList");
        //  onCartItemChange = (CartActivity.OnCartItemChange) this;
        getAllCart();
        return view;
    }


    private void getAllCart() {
        productList.clear();
        productList = dbWishList.getAllWishList(sharedpreferences.getString(AppConfig.user_id, ""));
        float grandTotal = 0f;
        for (int i = 0; i < productList.size(); i++) {
            String qty = productList.get(i).qty;
            if (qty == null || qty.length() <= 0) {
                qty = "1";
            }
            float startValue = Float.parseFloat(productList.get(i).price) * Integer.parseInt(qty);
            grandTotal = grandTotal + startValue;
        }
        ((TextView) view.findViewById(R.id.grandtotal)).setText("₹" + decimalFormat.format(grandTotal) + ".00");
        ((TextView) view.findViewById(R.id.total)).setText("₹" + decimalFormat.format(grandTotal) + ".00");
        cart_list.setAdapter(wishListAdapter);
        wishListAdapter.notifyData(productList);
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


    @Override
    public void onMoveToBag(int position) {
        dbWishList.deleteWishList(productList.get(position), sharedpreferences.getString(AppConfig.user_id, ""));
        ProductListBean productListBean = productList.get(position);
        productListBean.setQty("1");
        db.insertMainbeanyalu(productListBean, sharedpreferences.getString(AppConfig.user_id, ""));
        if (onCartItemChange != null) {
            onCartItemChange.onCartChange();
        }
        getAllCart();
    }

    @Override
    public void ondeleteClick(int position) {
        dbWishList.deleteWishList(productList.get(position), sharedpreferences.getString(AppConfig.user_id, ""));
        if (onCartItemChange != null) {
            onCartItemChange.onCartChange();
        }
        getAllCart();
    }

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }*/

}