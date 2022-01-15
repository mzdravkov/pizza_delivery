package com.example.pizza_f103255.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.pizza_f103255.R;
import com.example.pizza_f103255.entities.ProductItem;
import com.example.pizza_f103255.tasks.DownloadProductImage;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class ProductItemAdapter extends ArrayAdapter<ProductItem> {
    ConcurrentMap<Integer, Bitmap> bitmaps;

    public ProductItemAdapter(
            @NonNull Context context,
            int resource,
            @NonNull List<ProductItem> products,
            ConcurrentMap<Integer, Bitmap> bitmaps) {
        super(context, resource, products);
        this.bitmaps = bitmaps;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(
                    getContext()).inflate(R.layout.product_list_item, parent, false);
        }

        ProductItem currentProduct = getItem(position);

        TextView idView = listItemView.findViewById(R.id.id);
        idView.setText(Integer.toString(currentProduct.id));

        TextView nameView = listItemView.findViewById(R.id.name);
        nameView.setText(currentProduct.name);

        TextView descriptionView =  listItemView.findViewById(R.id.description);
        descriptionView.setText(currentProduct.description);

        TextView sizeView =  listItemView.findViewById(R.id.size);
        sizeView.setText(currentProduct.size);

        TextView priceView =  listItemView.findViewById(R.id.price);
        priceView.setText(currentProduct.price.toString() + " лв.");

        listItemView.setTag(R.id.product_id, currentProduct.id);
        ImageView imageView = listItemView.findViewById(R.id.product_image);
        imageView.setImageBitmap(null);
        new DownloadProductImage(
                listItemView, currentProduct.id, bitmaps).execute(currentProduct.imageUrl);


        return listItemView;
    }
}
