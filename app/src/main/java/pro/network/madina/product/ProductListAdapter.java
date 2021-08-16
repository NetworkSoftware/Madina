package pro.network.madina.product;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import pro.network.madina.R;
import pro.network.madina.app.AppConfig;
import pro.network.madina.app.DbWishList;


public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {

    public ProductItemClick productItemClick;
    SharedPreferences preferences;
    int selectedPosition = 0;
    DbWishList dbWishList;
    SharedPreferences sharedpreferences;
    private final Context mainActivityUser;
    private List<ProductListBean> productBeans;

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

        holder.product_name.setText(productBean.getModel());
        holder.product_price.setText("Rs. " + productBean.getPrice());
        holder.brand.setText(productBean.getBrand());
        holder.createdon.setText(AppConfig.date2DayTime(productBean.getCreatedOn()));
       // String description = productBean.getDescription();
      //  holder.product_descrpition.setText(description.length() > 15 ? description.substring(0, 14) + "..." : description);
        ArrayList<String> urls = new Gson().fromJson(productBean.image, (Type) List.class);
        Glide.with(mainActivityUser)
                .load(AppConfig.getResizedImage(urls.get(0), true))
                .into(holder.product_image);

        holder.wish_button.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                dbWishList.insertWishList(productBean, sharedpreferences.getString(AppConfig.user_id, ""));
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                dbWishList.deleteWishList(productBean, sharedpreferences.getString(AppConfig.user_id, ""));
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

        holder.wish_button.setLiked(dbWishList.isInWishList(productBean.id, sharedpreferences.getString(AppConfig.user_id, "")));

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

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final View outOfStock;
        CardView product_card;
        LikeButton wish_button;
        private final ImageView product_image;
        private final TextView product_name;
        private final TextView product_price;
        private final TextView brand;
        private final TextView createdon;

        public MyViewHolder(View view) {
            super((view));
            product_card = view.findViewById(R.id.product_card);
            product_image = (ImageView) view.findViewById(R.id.product_image);
            wish_button = view.findViewById(R.id.wish_button);
            product_name = (TextView) view.findViewById(R.id.product_name);
            product_price = (TextView) view.findViewById(R.id.product_price);
            outOfStock = view.findViewById(R.id.outOfStock);
            createdon = view.findViewById(R.id.createdon);
            brand = view.findViewById(R.id.brand);
        }
    }

}


