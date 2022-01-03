package com.example.pizza_f103255.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pizza_f103255.DBHandler;
import com.example.pizza_f103255.PizzaApp;
import com.example.pizza_f103255.R;
import com.example.pizza_f103255.adapters.ProductItemAdapter;
import com.example.pizza_f103255.entities.ProductItem;
import com.example.pizza_f103255.entities.ProductList;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Shows the user's favourite products.
 */
public class FavouritesFragment extends Fragment {

    public FavouritesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favourites, container, false);

        ListView listView = (ListView) view.findViewById(R.id.productList);

        PizzaApp app = (PizzaApp) getActivity().getApplication();
        ProductList productList = app.productList;

        DBHandler db = new DBHandler(getContext());
        Set<Integer> productIDs = db.getFavourites();

        List<ProductItem> favouriteProducts = productList
                .products
                .stream()
                .filter(product -> productIDs.contains(product.id))
                .collect(Collectors.toList());

        Context context = view.getContext();
        ArrayAdapter<ProductItem> adapter = new ProductItemAdapter(context, R.layout.products_list, favouriteProducts);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, clickedView, position, id) -> {
            String productIdStr = ((TextView) clickedView.findViewById(R.id.id)).getText().toString();
            int productId = Integer.parseInt(productIdStr);
            ProductItem clickedProduct = productList.products
                    .stream()
                    .filter(product -> product.id == productId)
                    .findFirst()
                    .get();
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