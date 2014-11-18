package com.pathofthefood.flyingburger.Menu;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.pathofthefood.flyingburger.ImageLoader.ImageLoader;
import com.pathofthefood.flyingburger.R;

import java.lang.reflect.Field;
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

    public ProductAdapter getInstance() {
        return this;
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
        final ViewItem item;
        View vi = convertView;
        if (vi == null) {
            vi = inflater.inflate(R.layout.item, null);
            item = new ViewItem();

            item.productQuantityPick = (NumberPicker)
                    vi.findViewById(R.id.numberPicker);

            item.productImageView = (ImageView) vi
                    .findViewById(R.id.ImageViewItem);

            item.productTitle = (TextView) vi
                    .findViewById(R.id.TextViewItem);
            item.description = (TextView) vi
                    .findViewById(R.id.textViewDescriptionItem);
            item.price = (TextView) vi
                    .findViewById(R.id.textViewPrice);

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
        item.description.setText(mProductList.get(position).getDescription());
        item.price.setText("$"+String.valueOf(mProductList.get(position).getPrice()));
        setNumberPickerTextColor(item.productQuantityPick,R.color.primary);
        item.productQuantityPick.setMinValue(0);
        item.productQuantityPick.setMaxValue(5);

        item.productQuantityPick.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal > 0) {
                    ShoppingCartHelper.setQuantity(mProductList.get(position), newVal,mProductList.get(position).getId());
                }
            }
        });

        // Show the quantity in the cart or not
        if (mShowQuantity) {
            item.productQuantity.setText("Quantity: "
                    + ShoppingCartHelper.getProductQuantity(mProductList.get(position)));
            item.productQuantityPick.setValue(ShoppingCartHelper.getProductQuantity(mProductList.get(position)));
        } else {
            // Hid the view
            item.productQuantity.setVisibility(View.GONE);
        }

        return vi;
    }

    private class ViewItem {
        ImageView productImageView;
        TextView productTitle;
        TextView description;
        TextView price;
        TextView productQuantity;
        NumberPicker productQuantityPick;
    }

    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color)
    {
        final int count = numberPicker.getChildCount();
        for(int i = 0; i < count; i++){
            View child = numberPicker.getChildAt(i);
            if(child instanceof EditText){
                try{
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint)selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText)child).setTextColor(color);
                    numberPicker.invalidate();
                    return true;
                }
                catch(NoSuchFieldException e){
                    Log.w("setNumberPickerTextColor", e);
                }
                catch(IllegalAccessException e){
                    Log.w("setNumberPickerTextColor", e);
                }
                catch(IllegalArgumentException e){
                    Log.w("setNumberPickerTextColor", e);
                }
            }
        }
        return false;
    }

}
