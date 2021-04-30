package pro.yalu.network.about;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pro.yalu.network.R;


public class AboutusAdapter extends RecyclerView.Adapter<AboutusAdapter.MyViewHolder> {

    private Context mainActivityUser;
    private ArrayList<AboutUs> moviesList;
    private OnAboutListener onAboutListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView list_item_icon;
        LinearLayout itemLinear;
        TextView title, summary;

        public MyViewHolder(View view) {
            super(view);
            list_item_icon = view.findViewById(R.id.list_item_icon);
            itemLinear = view.findViewById(R.id.itemLinear);
            title = view.findViewById(R.id.title);
            summary = view.findViewById(R.id.summary);
        }
    }


    public AboutusAdapter(Context mainActivityUser, ArrayList<AboutUs> moviesList, OnAboutListener onAboutListener) {
        this.moviesList = moviesList;
        this.mainActivityUser = mainActivityUser;
        this.onAboutListener = onAboutListener;

    }

    public void notifyData(ArrayList<AboutUs> myList) {
        this.moviesList = myList;
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final AboutUs bean = moviesList.get(position);
        holder.title.setText(bean.getTitle());
        holder.summary.setText(bean.getSummary());
        holder.list_item_icon.setImageDrawable(bean.getImage());
        holder.itemLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAboutListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
