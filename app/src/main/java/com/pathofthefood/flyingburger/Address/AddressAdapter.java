package com.pathofthefood.flyingburger.Address;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.pathofthefood.flyingburger.R;

import java.util.ArrayList;

/**
 * Created by Juan Acosta on 11/10/2014.
 */
public class AddressAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Address> address_list;

    public AddressAdapter(Context context,ArrayList<Address> address_list) {
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

        TextView CF = (TextView) convertView.findViewById(R.id.TextViewAddresName);
        TextView min = (TextView) convertView.findViewById(R.id.textViewDescriptionAddres);
        CF.setText(address_list.get(position).getLabel());
        Log.d("LABEL",address_list.get(position).getLabel());
        min.setText(address_list.get(position).getTextaddress());
        return convertView;
    }
}
