package com.pathofthefood.flyingburger.Restaurant;

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

/**
 * Created by Juan Acosta on 11/10/2014.
 */
public class RestaurantAdapter extends BaseAdapter {
    View convertView;
    private Activity activity;
    private String[] data;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader;
    private Context context;
    private ArrayList<Restaurants> address_list;

    public RestaurantAdapter(Context context, ArrayList<Restaurants> address_list,String[] d) {
        this.context = context;
        data = d;
        this.address_list = address_list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(context.getApplicationContext());
    }

    @Override
    public int getCount() {
        return this.address_list.size();
    }

    public RestaurantAdapter getInstance() {
        return this;
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
            vi = inflater.inflate(R.layout.restaurants, null);
            holder = new ViewHolder();
            holder.TVAN = (TextView) vi.findViewById(R.id.TextViewRestaurantName);
            holder.TVAD = (TextView) vi.findViewById(R.id.textViewDescriptionRestaurant);
            holder.TVTA = (TextView) vi.findViewById(R.id.textViewTextRestaurant);
            holder.RLOGO = (ImageView) vi.findViewById(R.id.restarantLogo);
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }
        holder.TVAN.setText(address_list.get(position).getName());
        Log.d("LABEL", address_list.get(position).getName());
        holder.TVAD.setText(address_list.get(position).getDescription());
        holder.TVTA.setText(address_list.get(position).getTextaddress());
        ImageView image = holder.RLOGO;
        String image_url = "http://pof.marinsalinas.com/uploads/"+address_list.get(position).getImage_url();
        imageLoader.DisplayImage(image_url,image);

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
