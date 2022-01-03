package com.example.pizza_f103255.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.pizza_f103255.DBHandler;
import com.example.pizza_f103255.R;
import com.example.pizza_f103255.entities.ProductItem;
import com.example.pizza_f103255.widgets.HorizontalNumberPicker;

import java.util.Collections;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_details, container, false);

        TextView nameView = view.findViewById(R.id.name);
        nameView.setText(product.name);

        TextView descriptionView = view.findViewById(R.id.description);
        descriptionView.setText(product.description);

        TextView sizeView = view.findViewById(R.id.size);
        sizeView.setText(product.size);

        TextView priceView = view.findViewById(R.id.price);
        priceView.setText(product.price.toString() + " лв.");

        HorizontalNumberPicker numberPicker = view.findViewById(R.id.number_picker);
        numberPicker.setMin(0);
        numberPicker.setMax(100);

        DBHandler db = new DBHandler(getContext());

        Button addToBasket = view.findViewById(R.id.add_to_basket_btn);
        addToBasket.setOnClickListener(v -> db.addProductToBasket(1, product, Collections.emptyList()));

        Button addToFavourites = view.findViewById(R.id.add_favourite_btn);
        boolean isFavourite = db.getFavourites().contains(product.id);

        if (isFavourite) {
            ViewManager parent = ((ViewManager) addToFavourites.getParent());
            parent.removeView(addToFavourites);
            parent.removeView(addToBasket);
            Button btnTag = new Button(getContext());
            btnTag.setId(View.generateViewId());
            btnTag.setText("Remove from favourites");
            btnTag.setOnClickListener(v -> {
                db.removeProductFromFavourites(product);
                refreshFragment();
            });
            ViewGroup.LayoutParams layoutParams = addToFavourites.getLayoutParams();
//            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            parent.addView(btnTag, layoutParams);
            parent.addView(addToBasket, layoutParams);
        } else {
            addToFavourites.setOnClickListener(v -> {
                db.addProductToFavourites(product);
                refreshFragment();
            });
        }

        return view;
    }

    private void refreshFragment() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction().detach(this).commit();
        fm.beginTransaction().attach(this).commit();
    }
}