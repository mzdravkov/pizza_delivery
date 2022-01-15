package com.example.pizza_f103255.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.pizza_f103255.PizzaApp;
import com.example.pizza_f103255.R;
import com.example.pizza_f103255.adapters.ProductItemAdapter;
import com.example.pizza_f103255.entities.ProductItem;
import com.example.pizza_f103255.entities.ProductList;

/**
 * A fragment representing a list of products.
 */
public class ProductListFragment extends Fragment {
    public ProductListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.products_list, container, false);
        Context context = view.getContext();

        ListView listView = (ListView) view.findViewById(R.id.productList);

        PizzaApp app = (PizzaApp) getActivity().getApplication();
        ProductList productList = app.productList;

        ArrayAdapter<ProductItem> adapter = new ProductItemAdapter(context, R.layout.products_list, productList.products);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, clickedView, position, id) -> {
            ProductItem clickedProduct = productList.products.get(position);
            ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment(clickedProduct);
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_view, productDetailsFragment, "productDetails")
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}