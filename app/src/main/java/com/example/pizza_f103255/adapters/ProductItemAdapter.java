package com.example.pizza_f103255.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.pizza_f103255.R;
import com.example.pizza_f103255.entities.ProductItem;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class ProductItemAdapter extends ArrayAdapter<ProductItem> {

    public ProductItemAdapter(@NonNull Context context, int resource, @NonNull List<ProductItem> products) {
        super(context, resource, products);
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

//        ImageView imageView = listItemView.findViewById(R.id.productImage);
//        imageView.setImageURI();

        return listItemView;
    }

    public static Drawable loadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
}
