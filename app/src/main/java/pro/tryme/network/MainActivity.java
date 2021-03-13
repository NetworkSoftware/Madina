package pro.tryme.network;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import pro.tryme.network.app.AppConfig;
import pro.tryme.network.app.DatabaseHelperYalu;
import pro.tryme.network.app.DbWishList;
import pro.tryme.network.cart.CartActivity;
import pro.tryme.network.orders.MyOrderPage;
import pro.tryme.network.product.AllProductFragment;
import pro.tryme.network.wishlist.WishListActivity;

import static pro.tryme.network.app.AppConfig.isLogin;
import static pro.tryme.network.app.AppConfig.mypreference;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        CartActivity.OnCartItemChange {

    public BottomNavigationView navigation;
    TextView basketCount;
    DrawerLayout drawer;
    Toolbar toolbar;
    private ImageView searchView;
    SharedPreferences sharedpreferences;
    FloatingActionButton profileEdit;
    private Menu nav_menu;
    private ImageView iv_profile;
    private TextView tv_name;
    private int wishListCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedpreferences = this.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }
        navigation = findViewById(R.id.nav_view12);

        NavigationView navigationView = findViewById(R.id.nav_view);


        drawer = findViewById(R.id.drawer_layout);

        ImageView navigation_notifications123 = findViewById(R.id.profile);
        navigation_notifications123.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedpreferences.contains(AppConfig.isLogin)) {
                    startActivity(new Intent(MainActivity.this, CartActivity.class));
                } else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }
        });

        ImageView search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AllProductActivity.class);
                intent.putExtra("isSearch", true);
                startActivity(intent);
            }
        });


        basketCount = findViewById(R.id.basketCount);
        updateCart();


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, new HomeActivity()).commit();

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_home) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.flContent, new HomeActivity()).commit();
                } else if (item.getItemId() == R.id.myorders) {
                    if (sharedpreferences.contains(AppConfig.isLogin)) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.flContent, new MyOrderPage()).commit();
                    } else {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));

                    }

                } else if (item.getItemId() == R.id.wishlistBottom) {
                    if (sharedpreferences.contains(AppConfig.isLogin)) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.flContent, new WishListActivity()).commit();
                    } else {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }

                } else if (item.getItemId() == R.id.profile) {
                    if (sharedpreferences.contains(AppConfig.isLogin)) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.flContent, new SettingsActivity()).commit();
                    } else {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }

                }else if (item.getItemId() == R.id.map) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.flContent, new MapActivity()).commit();

                }
                return true;
            }
        });

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.home) {
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, new HomeActivity()).commit();
        } else if (id == R.id.about) {
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.flContent, new AboutUsActivity()).commit();
//            address.setText("About Us");
        } else if (id == R.id.category) {
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.flContent, new CategoryListActivity()).commit();
//            address.setText("Categories");

        } else if (id == R.id.product) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, new AllProductFragment()).commit();

        } else if (id == R.id.contact) {
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.flContent, new ContactUsActivity()).commit();
//            address.setText("Contact Us");

        } else if (id == R.id.changepassword) {
            Intent intent = new Intent(getApplicationContext(), ChangePassword.class);
            startActivity(intent);

        } else if (id == R.id.exit) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.remove(isLogin);
            editor.commit();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finishAffinity();
        }
        drawer.closeDrawers();
        return true;
    }

    @Override
    public void onBackPressed() {

        finish();

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
        if (navigation != null) {
            wishListCount = dbWishList.getWishListCount(sharedpreferences.getString(AppConfig.userId, ""));
            if (wishListCount == 0) {
                navigation.removeBadge(navigation.getMenu().getItem(2).getItemId());
            } else {
                navigation.getOrCreateBadge(navigation.getMenu().getItem(2).getItemId()).setNumber(wishListCount);
            }
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