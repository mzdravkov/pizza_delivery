package com.example.pizza_f103255.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pizza_f103255.R;
import com.example.pizza_f103255.entities.ProductItem;

/**
 * Shows the details of a single product.
 * Allows adding it to the basket or favourites.
 */
public class ProductDetails extends Fragment {
    private ProductItem product;

    public ProductDetails(ProductItem product) {
        this.product = product;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_details, container, false);

        TextView nameView = view.findViewById(R.id.name);
        nameView.setText(product.name);

        TextView descriptionView =  view.findViewById(R.id.description);
        descriptionView.setText(product.description);

        TextView sizeView =  view.findViewById(R.id.size);
        sizeView.setText(product.size);

        TextView priceView =  view.findViewById(R.id.price);
        priceView.setText(product.price.toString() + " лв.");

        return view;
    }
}