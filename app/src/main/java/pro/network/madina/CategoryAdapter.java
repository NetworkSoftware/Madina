package pro.network.madina;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private final Context mainActivityUser;
    private List<Category> categories;
    public OnCategoryClick onCategoryClick;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView title;
        LinearLayout linear;

        public MyViewHolder(View view) {
            super((view));
            linear = view.findViewById(R.id.linear);
            image = view.findViewById(R.id.image);
            title = view.findViewById(R.id.title);
        }
    }

    public CategoryAdapter(Context mainActivityUser, List<Category> productBeans, OnCategoryClick onCategoryClick) {
        this.mainActivityUser = mainActivityUser;
        this.categories = productBeans;
        this.onCategoryClick = onCategoryClick;
    }

    public void notifyData(List<Category> productBeans) {
        this.categories = productBeans;
        notifyDataSetChanged();
    }


    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Category productBean = categories.get(position);

        holder.title.setText(productBean.getTitle());
        holder.image.setImageDrawable(mainActivityUser.getResources().getDrawable(productBean.image));
        holder.title.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(productBean.subColor)));
        holder.linear.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(productBean.color)));
        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCategoryClick.onCategoryItem(position);
            }
        });
    }

    public int getItemCount() {
        return categories.size();
    }

}


