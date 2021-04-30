package pro.yalu.network;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import pro.yalu.network.about.AboutusActivity;
import pro.yalu.network.app.AppConfig;
import pro.yalu.network.app.DatabaseHelperYalu;
import pro.yalu.network.app.DbWishList;
import pro.yalu.network.cart.CartActivity;
import pro.yalu.network.orders.MyOrderPage;
import pro.yalu.network.wishlist.WishListActivity;

import static pro.yalu.network.app.AppConfig.mypreference;

public class MainActivityYalu extends AppCompatActivity implements CartActivity.OnCartItemChange {

    private ChipNavigationBar chipNavigationBar;
    private Fragment fragment = null;
    ImageView search;
    SharedPreferences sharedpreferences;
    TextView basketCount, wishListCount;
    ImageView wishlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_yalu);
        sharedpreferences = this.getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        chipNavigationBar = findViewById(R.id.chipNavigation);
        search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityYalu.this, AllProductActivity.class);
                intent.putExtra("isSearch", true);
                startActivity(intent);
            }
        });
        ImageView navigation_notifications123 = findViewById(R.id.profile);
        navigation_notifications123.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivityYalu.this, CartActivity.class));
            }
        });
        ImageView wishlist = findViewById(R.id.wishlist);
        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivityYalu.this, WishListActivity.class));
            }
        });
        chipNavigationBar.setItemSelected(R.id.navigation_home, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeActivity()).commit();
        basketCount = findViewById(R.id.basketCount);
        wishListCount = findViewById(R.id.wishListCount);
        updateCart();
        updateWishList();
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.navigation_home:
                        fragment = new HomeActivity();
                        break;
                    case R.id.myorders:
                        fragment = new MyOrderPage();
                        break;
                    case R.id.profile:
                        fragment = new AboutusActivity();
                        break;
                    case R.id.map:
                        fragment = new MapActivity();
                        break;

                    default:
                        fragment = new HomeActivity();
                        break;
                }
                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                }
            }
        });
    }


    private void updateCart() {
        DatabaseHelperYalu helperYalu = new DatabaseHelperYalu(this);
        if (basketCount != null) {
            if (helperYalu.getCartCountYalu(sharedpreferences.getString(AppConfig.userId, "")) == 0) {
                basketCount.setVisibility(View.GONE);
            } else {
                basketCount.setVisibility(View.VISIBLE);
            }
            basketCount.setText(helperYalu.getCartCountYalu(sharedpreferences.getString(AppConfig.userId, "")) + "");
        }
    }

    private void updateWishList() {
        DbWishList dbWishList = new DbWishList(this);
        if (wishListCount != null) {
            if (dbWishList.getWishListCount(sharedpreferences.getString(AppConfig.userId, "")) == 0) {
                wishListCount.setVisibility(View.GONE);
            } else {
                wishListCount.setVisibility(View.VISIBLE);
            }
            wishListCount.setText(dbWishList.getWishListCount(sharedpreferences.getString(AppConfig.userId, "")) + "");
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        updateWishList();
        updateCart();
    }

    @Override
    public void onCartChange() {
        updateCart();
        updateWishList();
    }
}