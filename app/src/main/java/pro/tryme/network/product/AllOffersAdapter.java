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

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.network.moeidbannerlibrary.banner.BannerBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import pro.tryme.network.LoginActivity;
import pro.tryme.network.R;
import pro.tryme.network.app.AppConfig;
import pro.tryme.network.app.DbWishList;


public class AllOffersAdapter extends RecyclerView.Adapter<AllOffersAdapter.MyViewHolder> {

    private Context mainActivityUser;
    private List<BannerBean> allOffs;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView news;

        public MyViewHolder(View view) {
            super((view));
            news = view.findViewById(R.id.news);
        }
    }

    public AllOffersAdapter(Context mainActivityUser, List<BannerBean> allOffs) {
        this.mainActivityUser = mainActivityUser;
        this.allOffs = allOffs;

    }


    public void notifyData(List<BannerBean> allOffs) {
        this.allOffs = allOffs;
        notifyDataSetChanged();
    }


    public AllOffersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_all_off, parent, false);

        return new AllOffersAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final BannerBean allOff = allOffs.get(position);
        Glide.with(mainActivityUser).load(allOff.getImages()).into(holder.news);

        // holder.news.setText(allOff.data);


    }

    public int getItemCount() {
        return allOffs.size();
    }

}


