package com.pathofthefood.flyingburger;

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
import com.pathofthefood.flyingburger.Restaurant.Restaurants;

import java.util.ArrayList;

/**
 * Created by Juan Acosta on 11/10/2014.
 */
public class DrawerAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<DrawerItem> navDrawerItems;

        public DrawerAdapter(Context context, ArrayList<DrawerItem> navDrawerItems){
            this.context = context;
            this.navDrawerItems = navDrawerItems;
        }

        @Override
        public int getCount() {
            return navDrawerItems.size();
        }

        @Override
        public Object getItem(int position) {
            return navDrawerItems.get(position);
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
                convertView = mInflater.inflate(R.layout.drawer_adapter, null);
            }

            ImageView imgIcon = (ImageView) convertView.findViewById(R.id.drawer_image);
            TextView txtTitle = (TextView) convertView.findViewById(R.id.drawerText);

            imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
            txtTitle.setText(navDrawerItems.get(position).getTitle());


            return convertView;
        }

}
