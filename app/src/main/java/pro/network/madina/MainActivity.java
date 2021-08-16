package pro.network.madina;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import np.com.susanthapa.curved_bottom_navigation.CbnMenuItem;
import np.com.susanthapa.curved_bottom_navigation.CurvedBottomNavigationView;
import pro.network.madina.about.AboutusActivity;
import pro.network.madina.app.AppConfig;
import pro.network.madina.app.DatabaseHelperMadina;
import pro.network.madina.cart.CartActivity;
import pro.network.madina.orders.MyOrderPage;
import pro.network.madina.wishlist.WishListActivity;

import static pro.network.madina.app.AppConfig.mypreference;

public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener,
        CartActivity.OnCartItemChange {

    public CurvedBottomNavigationView navigation;
    private Fragment fragment = null;
    ImageView search;
    SharedPreferences sharedpreferences;
    TextView basketCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedpreferences = this.getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        navigation = findViewById(R.id.nav_view12);

        search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AllProductActivity.class);
                intent.putExtra("isSearch", true);
                startActivity(intent);
            }
        });
        ImageView navigation_notifications123 = findViewById(R.id.cart);
        navigation_notifications123.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            }
        });

        View nav_header = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeActivity()).commit();

        CbnMenuItem[] cbnMenuItems = new CbnMenuItem[5];
        cbnMenuItems[0] = new CbnMenuItem(R.drawable.avd_home,
                R.drawable.avd_home, R.id.navigation_home);
        cbnMenuItems[1] = new CbnMenuItem(R.drawable.avd_bag,
                R.drawable.avd_bag, R.id.myorders);
        cbnMenuItems[2] = new CbnMenuItem(R.drawable.avd_heart,
                R.drawable.avd_heart, R.id.wishlistBottom);
        cbnMenuItems[3] = new CbnMenuItem(R.drawable.avd_nearme,
                R.drawable.avd_nearme, R.id.map);
        cbnMenuItems[4] = new CbnMenuItem(R.drawable.avd_profile,
                R.drawable.avd_profile, R.id.profile);
        navigation.setMenuItems(cbnMenuItems, 0);

        navigation.setOnMenuItemClickListener(new Function2<CbnMenuItem, Integer, Unit>() {
            @Override
            public Unit invoke(CbnMenuItem cbnMenuItem, Integer integer) {
                if (integer == 0) {
                    androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, new HomeActivity()).commit();
                } else if (integer == 1) {
                    androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, new MyOrderPage()).commit();
                } else if (integer == 2) {
                    androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, new WishListActivity()).commit();
                } else if (integer == 3) {
                    androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, new MapActivity()).commit();
                } else if (integer == 4) {
                    androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, new AboutusActivity()).commit();
                }
                return null;
            }
        });
        basketCount = findViewById(R.id.basketCount);
        updateCart();
        }


    private void updateCart() {
        DatabaseHelperMadina helperYalu = new DatabaseHelperMadina(this);
        if (basketCount != null) {
            if (helperYalu.getCartCountYalu(sharedpreferences.getString(AppConfig.userId, "")) == 0) {
                basketCount.setVisibility(View.GONE);
            } else {
                basketCount.setVisibility(View.VISIBLE);
            }
            basketCount.setText(helperYalu.getCartCountYalu(sharedpreferences.getString(AppConfig.userId, "")) + "");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCart();
    }

    @Override
    public void onCartChange() {
        updateCart();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        return false;
    }
}