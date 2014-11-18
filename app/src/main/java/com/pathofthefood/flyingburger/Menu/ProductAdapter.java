package com.pathofthefood.flyingburger.Menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.pathofthefood.flyingburger.ImageLoader.ImageLoader;
import com.pathofthefood.flyingburger.R;

import java.util.List;

public class ProductAdapter extends BaseAdapter {

    private List<Products> mProductList;
    private static LayoutInflater inflater = null;
    private boolean mShowQuantity;
    public ImageLoader imageLoader;
    Context context;

    public ProductAdapter(List<Products> list, Context context, boolean showQuantity) {
        this.context = context;
        mProductList = list;
        mShowQuantity = showQuantity;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(context.getApplicationContext());
    }

    @Override
    public int getCount() {
        return mProductList.size();
    }

    @Override
    public Object getItem(int position) {
        return mProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewItem item;
        View vi = convertView;
        if (vi == null) {
            vi = inflater.inflate(R.layout.item, null);
            item = new ViewItem();

            item.productImageView = (ImageView) vi
                    .findViewById(R.id.ImageViewItem);

            item.productTitle = (TextView) vi
                    .findViewById(R.id.TextViewItem);

            item.productQuantity = (TextView) vi
                    .findViewById(R.id.textViewQuantity);

            vi.setTag(item);
        } else {
            item = (ViewItem) vi.getTag();
        }

        ImageView image = item.productImageView;
        String image_url = "http://pof.marinsalinas.com/uploads/" + mProductList.get(position).getImage_url();
        imageLoader.DisplayImage(image_url, image);
        item.productTitle.setText(mProductList.get(position).getProduct());

        // Show the quantity in the cart or not
        if (mShowQuantity) {
            item.productQuantity.setText("Quantity: "
                    + ShoppingCartHelper.getProductQuantity(mProductList.get(position)));
        } else {
            // Hid the view
            item.productQuantity.setVisibility(View.GONE);
        }

        return vi;
    }

    private class ViewItem {
        ImageView productImageView;
        TextView productTitle;
        TextView productQuantity;
    }

}
