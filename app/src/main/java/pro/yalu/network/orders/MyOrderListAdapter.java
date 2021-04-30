package pro.yalu.network.orders;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import pro.yalu.network.R;

import java.util.ArrayList;


public class MyOrderListAdapter extends RecyclerView.Adapter<MyOrderListAdapter.MyViewHolder> {

    private Context mainActivityUser;
    private ArrayList<MyorderBean> myorderBeans;
    SharedPreferences preferences;
    int selectedPosition = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name,quantity , amount, status,createdon;
        RecyclerView cart_sub_list;

        public MyViewHolder(View view) {
            super((view));
            name = (TextView) view.findViewById(R.id.name);
            quantity = (TextView) view.findViewById(R.id.quantity);
            amount = (TextView) view.findViewById(R.id.amount);
            status = (TextView) view.findViewById(R.id.status);
            createdon = (TextView) view.findViewById(R.id.createdon);
            cart_sub_list = view.findViewById(R.id.cart_sub_list);

        }
    }

    public MyOrderListAdapter(Context mainActivityUser, ArrayList<MyorderBean> myorderBeans) {
        this.mainActivityUser = mainActivityUser;
        this.myorderBeans = myorderBeans;
    }

    public void notifyData(ArrayList<MyorderBean> myorderBeans) {
        this.myorderBeans = myorderBeans;
        notifyDataSetChanged();
    }

    public void notifyData(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    public MyOrderListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myorders_list, parent, false);

        return new MyOrderListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final MyorderBean myorderBean = myorderBeans.get(position);


        holder.amount.setText(myorderBean.getAmount());
        holder.quantity.setText( myorderBean.getQuantity());
        holder.status.setText( myorderBean.getStatus());
        holder.createdon.setText(myorderBean.getCreatedon());

        MyOrderListSubAdapter myOrderListAdapter = new MyOrderListSubAdapter(mainActivityUser, myorderBean.getProductBeans());
        final LinearLayoutManager addManager1 = new LinearLayoutManager(mainActivityUser, LinearLayoutManager.HORIZONTAL, false);
        holder.cart_sub_list.setLayoutManager(addManager1);
        holder.cart_sub_list.setAdapter(myOrderListAdapter);


    }

    public int getItemCount() {
        return myorderBeans.size();
    }

}


