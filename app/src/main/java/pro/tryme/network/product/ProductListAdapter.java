package pro.tryme.network.product;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import pro.tryme.network.LoginActivity;
import pro.tryme.network.R;
import pro.tryme.network.app.AppConfig;
import pro.tryme.network.app.DatabaseHelperYalu;
import pro.tryme.network.app.DbWishList;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {

    private Context mainActivityUser;
    private List<ProductListBean> productBeans;
    SharedPreferences preferences;
    public ProductItemClick productItemClick;
    int selectedPosition = 0;
    DbWishList dbWishList;
    SharedPreferences sharedpreferences;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final View outOfStock;
        private ImageView product_image;
        private TextView product_name,product_price, product_descrpition;
        LinearLayout product_card;
        LikeButton wish_button;

        public MyViewHolder(View view) {
            super((view));
            product_card = (LinearLayout) view.findViewById(R.id.product_card);
            product_image = (ImageView) view.findViewById(R.id.product_image);
            wish_button = view.findViewById(R.id.wish_button);
            product_name = (TextView) view.findViewById(R.id.product_name);
            product_price = (TextView) view.findViewById(R.id.product_price);
            outOfStock = view.findViewById(R.id.outOfStock);
            product_descrpition = view.findViewById(R.id.product_descrpition);
        }
    }

    public ProductListAdapter(Context mainActivityUser, List<ProductListBean> productBeans, ProductItemClick productItemClick, SharedPreferences sharedPreferences) {
        this.mainActivityUser = mainActivityUser;
        this.productBeans = productBeans;
        this.productItemClick = productItemClick;
        sharedpreferences = mainActivityUser.getSharedPreferences(AppConfig.mypreference, Context.MODE_PRIVATE);
        dbWishList = new DbWishList(mainActivityUser);
    }


    public void notifyData(List<ProductListBean> productBeans) {
        this.productBeans = productBeans;
        notifyDataSetChanged();
    }

    public void notifyData(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    public ProductListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_single_product, parent, false);

        return new ProductListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ProductListBean productBean = productBeans.get(position);

        holder.product_name.setText(productBean.getBrand() + " " + productBean.getModel());
        holder.product_price.setText("Rs. "+productBean.getPrice());
        String description = productBean.getDescription();
        holder.product_descrpition.setText(description.length()>15? description.substring(0,14)+"...": description);
        ArrayList<String> urls = new Gson().fromJson(productBean.image, (Type) List.class);
        Glide.with(mainActivityUser)
                .load(AppConfig.getResizedImage(urls.get(0), true))
                .into(holder.product_image);

        holder.wish_button.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                if (sharedpreferences.contains(AppConfig.user_id)) {
                    dbWishList.insertWishList(productBean, sharedpreferences.getString(AppConfig.user_id, ""));
                    productItemClick.onCartClick(position);
                } else {
                    mainActivityUser.startActivity(new Intent(mainActivityUser, LoginActivity.class));
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                if (sharedpreferences.contains(AppConfig.user_id)) {
                    dbWishList.deleteWishList(productBean, sharedpreferences.getString(AppConfig.user_id, ""));
                    productItemClick.onCartClick(position);
                } else {
                    mainActivityUser.startActivity(new Intent(mainActivityUser, LoginActivity.class));
                }
            }
        });


        if (productBean.getStock_update().equalsIgnoreCase("In Stock")) {
            holder.outOfStock.setVisibility(View.GONE);
        } else {
            holder.outOfStock.setVisibility(View.VISIBLE);
        }
        holder.outOfStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do nothing
            }
        });

        if (dbWishList.isInWishList(productBean.id, sharedpreferences.getString(AppConfig.user_id, ""))) {
            holder.wish_button.setLiked(true);
        } else {
            holder.wish_button.setLiked(false);
        }

        holder.product_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productItemClick.onProductClick(position);
            }
        });


    }

    public int getItemCount() {
        return productBeans.size();
    }

}


