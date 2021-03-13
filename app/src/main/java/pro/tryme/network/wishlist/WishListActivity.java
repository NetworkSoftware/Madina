package pro.tryme.network.wishlist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pro.tryme.network.R;
import pro.tryme.network.app.AppConfig;
import pro.tryme.network.app.AppController;
import pro.tryme.network.app.DatabaseHelperYalu;
import pro.tryme.network.app.DbWishList;
import pro.tryme.network.cart.CartActivity;
import pro.tryme.network.orders.MyOrderListAdapter;
import pro.tryme.network.orders.MyorderBean;
import pro.tryme.network.product.ProductListBean;

import static pro.tryme.network.app.AppConfig.ORDER_CREATE;
import static pro.tryme.network.app.AppConfig.decimalFormat;
import static pro.tryme.network.app.AppConfig.mypreference;

public class WishListActivity extends Fragment implements WishListClick {

    RecyclerView cart_list;
    private List<ProductListBean> productList = new ArrayList<>();
    WishListAdapter wishListAdapter;
    ProgressDialog pDialog;
    private String TAG = getClass().getSimpleName();
    private DatabaseHelperYalu db;
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
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        db = new DatabaseHelperYalu(getActivity());
        dbWishList = new DbWishList(getActivity());
        sharedpreferences = getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        cart_list = view.findViewById(R.id.cart_list);
        wishListAdapter = new WishListAdapter(getActivity(), productList, this);
        final LinearLayoutManager addManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        cart_list.setLayoutManager(addManager1);
        cart_list.setAdapter(wishListAdapter);

        order = view.findViewById(R.id.order);
        order.setVisibility(View.GONE);


        nestedScrollView = view.findViewById(R.id.nested);
        continueCard = view.findViewById(R.id.continueCard);
        empty_product = view.findViewById(R.id.empty_product);
        emptyText = view.findViewById(R.id.emptyText);
        emptyText.setText("No Items in WishList");
        onCartItemChange = (CartActivity.OnCartItemChange) getActivity();
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
        ((TextView) view.findViewById(R.id.subtotal)).setText("₹" + decimalFormat.format(grandTotal) + ".00");
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

    private void hideDialog() {

        if (this.pDialog.isShowing()) this.pDialog.dismiss();
    }

    private void showDialog() {

        if (!this.pDialog.isShowing()) this.pDialog.show();
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
}