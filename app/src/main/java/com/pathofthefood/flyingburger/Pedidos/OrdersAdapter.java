package com.pathofthefood.flyingburger.Pedidos;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.pathofthefood.flyingburger.ImageLoader.ImageLoader;
import com.pathofthefood.flyingburger.R;

import java.util.ArrayList;

public class OrdersAdapter extends BaseAdapter {
    View convertView;
    private Activity activity;
    private String[] data;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader;
    private Context context;
    private ArrayList<Orders> address_list;

    public OrdersAdapter(Context context, ArrayList<Orders> address_list) {
        this.context = context;
        this.address_list = address_list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.address_list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.address_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        View vi = convertView;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.order, null);
            holder = new ViewHolder();
            holder.TVAN = (TextView) vi.findViewById(R.id.TextViewOrderName);
            holder.TVAD = (TextView) vi.findViewById(R.id.textViewDescriptionOrder);
            holder.TVTA = (TextView) vi.findViewById(R.id.textViewTextOrder);
            holder.RLOGO = (ImageView) vi.findViewById(R.id.restarantLogo);
            holder.RLOGO.setVisibility(View.INVISIBLE);
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }
        holder.TVAN.setText(address_list.get(position).getStatus());
        holder.TVAD.setText(address_list.get(position).getUpdated_at());
        holder.TVTA.setText(address_list.get(position).getDelivery_at());

        //RLOGO.setImageDrawable(drawable);
        return vi;
    }

    public static class ViewHolder {
        TextView TVAN;
        TextView TVAD;
        TextView TVTA;
        ImageView RLOGO;
    }

}
