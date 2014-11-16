package com.pathofthefood.flyingburger.Address;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.pathofthefood.flyingburger.R;

import java.util.ArrayList;

/**
 * Created by Juan Acosta on 11/10/2014.
 */
public class AddressAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Address> address_list;

    public AddressAdapter(Context context, ArrayList<Address> address_list) {
        this.context = context;
        this.address_list = address_list;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.address, null);
        }

        TextView TVAN = (TextView) convertView.findViewById(R.id.TextViewAddresName);
        TextView TVAD = (TextView) convertView.findViewById(R.id.textViewDescriptionAddres);
        TextView TVTA = (TextView) convertView.findViewById(R.id.textViewTextAddres);
        TVAN.setText(address_list.get(position).getLabel());
        Log.d("LABEL", address_list.get(position).getLabel());
        TVAD.setText(address_list.get(position).getDescription());
        TVTA.setText(address_list.get(position).getTextaddress());
        return convertView;
    }
}
