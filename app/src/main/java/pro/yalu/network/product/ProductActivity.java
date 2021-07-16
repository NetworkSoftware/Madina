package pro.yalu.network.product;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import pro.yalu.network.R;
import pro.yalu.network.app.AppConfig;
import pro.yalu.network.app.BaseActivity;
import pro.yalu.network.app.DatabaseHelperYalu;
import pro.yalu.network.app.DbWishList;
import pro.yalu.network.cart.CartActivity;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.gson.Gson;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.network.moeidbannerlibrary.banner.BannerLayout;
import com.shuhart.bubblepagerindicator.BubblePageIndicator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static pro.yalu.network.app.AppConfig.mypreference;
import static pro.yalu.network.app.AppConfig.user_id;

public class ProductActivity extends BaseActivity implements ViewClick {
    private DatabaseHelperYalu db;
    SharedPreferences sharedpreferences;
    public ArrayList<ProductListBean> productBeans;

    TextView product_price, product_descrpition, product_name;
    //    TextInputEditText quantity;
//    TextInputLayout quantityText;
    ExtendedFloatingActionButton cart;
    ImageView call, chat;
    ImageView wishlist;
    int currentImage = 0;
    private PhotoView photoView;
    private RecyclerView baseList;
    private AttachmentViewAdapter attachmentBaseAdapter;

    private TextView basketCount, wishlistBadge,createdon;
    DbWishList dbWishList;

    LikeButton wish_button;
    private TextView cart_badge;
    private ArrayList<String> urls = new ArrayList<>();
    ProductListBean productBean;
    BannerLayout banner;

    ViewPager pager;
    BubblePageIndicator indicator;
    ViewPagerAdapter adapter;

    @Override
    protected void startDemo() {
        setContentView(R.layout.activity_product_new);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
      /*  Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
*/
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        productBean = (ProductListBean) getIntent().getSerializableExtra("data");

        call = findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:9500413999"));
                startActivity(intent);
            }
        });
        product_name = findViewById(R.id.product_name);

        createdon = findViewById(R.id.createdon);
        createdon.setText(AppConfig.date2DayTime(productBean.getCreatedOn()));
        chat = findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=919500413999"
                            + "&text=" + "Hi YALU MOBILES, I would like buy this *" + product_name.getText().toString() + "* product"));
                    intent.setPackage("com.whatsapp.w4b");
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=919500413999"
                            + "&text=" + "Hi YALU MOBILES, I would like buy this *" + product_name.getText().toString() + "* product"));
                    intent.setPackage("com.whatsapp");
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Whatsapp Not found", Toast.LENGTH_SHORT).show();
                }


            }
        });
        db = new DatabaseHelperYalu(this);
        dbWishList = new DbWishList(this);

        sharedpreferences = this.getSharedPreferences(mypreference, Context.MODE_PRIVATE);


        product_price = findViewById(R.id.product_price);
        product_descrpition = findViewById(R.id.product_descrpition);
//        quantity = findViewById(R.id.quantity);
//        quantityText = findViewById(R.id.quantityText);
        cart = findViewById(R.id.cart);
        //  quantity.setHintTextColor(getResources().getColor(R.color.white));
        basketCount = findViewById(R.id.cartImgBadge);
        wishlistBadge = findViewById(R.id.wishlistBadge);
        wishlist = findViewById(R.id.wishlist);


        urls = new Gson().fromJson(productBean.image, (Type) List.class);

        /*pager = findViewById(R.id.pager);
        indicator = findViewById(R.id.indicator);*/
    /*    adapter = new ViewPagerAdapter(ProductActivity.this,urls);
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);*/

        photoView = findViewById(R.id.product_image);
        Glide.with(ProductActivity.this)
                .load(AppConfig.getResizedImage(urls.get(currentImage), false))
                .into(photoView);
        product_price.setText("Rs. " + productBean.getPrice());
        product_name.setText(productBean.getModel());
        product_descrpition.setText(productBean.getDescription());

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (db.isInCartyalu(productBean.id, sharedpreferences.getString(AppConfig.user_id, ""))) {
                    startActivity(new Intent(ProductActivity.this, CartActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Item added Successfully", Toast.LENGTH_SHORT).show();
                    productBean.setQty("1");
                    db.insertMainbeanyalu(productBean, sharedpreferences.getString(AppConfig.user_id, ""));
                    cart.setText("Go to Bag");
                    updateCart();
                }
            }
        });

        if (db.isInCartyalu(productBean.id, sharedpreferences.getString(AppConfig.user_id, ""))) {
            cart.setText("Go to Bag");
        } else {
            cart.setText("Add to Bag");
        }

        wish_button = findViewById(R.id.wish_button);
        if (dbWishList.isInWishList(productBean.id, sharedpreferences.getString(AppConfig.user_id, ""))) {
            wish_button.setLiked(true);
        } else {
            wish_button.setLiked(false);
        }
        wish_button.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                dbWishList.insertWishList(productBean, sharedpreferences.getString(AppConfig.user_id, ""));
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                dbWishList.deleteWishList(productBean, sharedpreferences.getString(AppConfig.user_id, ""));
            }
        });


        baseList = (RecyclerView) findViewById(R.id.attachmentList);
        attachmentBaseAdapter = new AttachmentViewAdapter(this, urls, this);
        baseList.setLayoutManager(new GridLayoutManager(this, 3));
        baseList.setAdapter(attachmentBaseAdapter);

        getSupportActionBar().setTitle(productBean.getModel());
        getSupportActionBar().setSubtitle(getString(R.string.app_name));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.cart:
                startActivity(new Intent(ProductActivity.this, CartActivity.class));
                return true;
            case android.R.id.home:
                finish();
                return true;
            case R.id.share:
                if (ContextCompat.checkSelfPermission(ProductActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ProductActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                } else if (productBean != null && urls != null && urls.size() > 0) {
                    Uri bitmap = getLocalBitmapUri(urls.get(0), ProductActivity.this);
                    if (bitmap != null) {
                        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_STREAM, bitmap);
                        intent.putExtra(Intent.EXTRA_TEXT, productBean.getBrand()+
                                " "+productBean.getModel() + "\nVisit more products like this\nhttps://play.google.com/store/apps/details?id=pro.yalu.network");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setType("image/png");
                        startActivity(intent);
                    }
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void updateCart() {


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

        if (basketCount != null) {
            if (db.getCartCountYalu(sharedpreferences.getString(AppConfig.userId, "")) == 0) {
                basketCount.setVisibility(View.GONE);
            } else {
                basketCount.setVisibility(View.VISIBLE);
            }
            basketCount.setText(db.getCartCountYalu(sharedpreferences.getString(AppConfig.userId, "")) + "");
        }
    }

    private void updateWishList() {
        if (wishlistBadge != null) {
            if (dbWishList.getWishListCount(sharedpreferences.getString(AppConfig.userId, "")) == 0) {
                wishlistBadge.setVisibility(View.GONE);
            } else {
                wishlistBadge.setVisibility(View.VISIBLE);
            }
            wishlistBadge.setText(dbWishList.getWishListCount(sharedpreferences.getString(AppConfig.userId, "")) + "");
        }
    }

    public Uri getLocalBitmapUri(String urlL, Context context) {
        // Extract Bitmap from ImageView drawable
        Uri bmpUri = null;
        try {
            URL url = new URL(urlL);
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            // Store image to default external storage directory
            File file = new File(getExternalCacheDir().getPath(), "product_image" + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bmpUri = FileProvider.getUriForFile(context,
                        getPackageName() + ".provider", file);
            } else {
                bmpUri = Uri.fromFile(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCart();
        updateWishList();
    }

    @Override
    public void onViewClick(String s) {
        Glide.with(ProductActivity.this)
                .load(AppConfig.getResizedImage(s, false))
                .into(photoView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.product_share, menu);

        final MenuItem menuItem = menu.findItem(R.id.cart);

        View actionView = menuItem.getActionView();
        cart_badge = (TextView) actionView.findViewById(R.id.cart_badge);

        updateWishList();
        updateCart();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }



}