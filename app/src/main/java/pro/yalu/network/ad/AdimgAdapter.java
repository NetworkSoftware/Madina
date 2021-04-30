package pro.yalu.network.ad;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import pro.yalu.network.R;
import pro.yalu.network.app.AppConfig;


public class AdimgAdapter extends RecyclerView.Adapter<AdimgAdapter.MyViewHolder> {

    private List<String> moviesList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView3;

        public MyViewHolder(View view) {
            super(view);
            imageView3 = (ImageView) view.findViewById(R.id.imageView3);


        }
    }


    public AdimgAdapter(List<String> moviesList, Context context) {
        this.moviesList = moviesList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ad_img, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        String shop = moviesList.get(position);
        final String file = AppConfig.ip+"/feed/" + shop;
        Glide.with(context)
                .load(file)
                .apply(new RequestOptions().placeholder(R.drawable.yalulogo))
                .into(holder.imageView3);

        holder.imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent localIntent = new Intent(context, ActivityMediaOnline.class);
                localIntent.putExtra("filePath", file);
                localIntent.putExtra("isImage", true);
                context.startActivity(localIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public void notifyData(List<String> moviesList) {
        this.moviesList = moviesList;
        notifyDataSetChanged();
    }


}
