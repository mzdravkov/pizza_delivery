package com.example.pizza_f103255.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.pizza_f103255.R;
import com.example.pizza_f103255.entities.ProductItem;
import com.example.pizza_f103255.entities.ProductList;
import com.example.pizza_f103255.tasks.LoadProducts;

/**
 * A fragment representing a list of products.
 */
public class ProductListFragment extends Fragment {
    private static final String PRODUCTS = "products";

    private ProductList productList;

    public ProductListFragment() {
    }

    public void setProductList(ProductList productList) {
        this.productList = productList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (getArguments() != null) {
//            productList = (ProductList) getArguments().getSerializable(PRODUCTS);
//        } else {
//            Bundle args = new Bundle();
//            args.putSerializable(PRODUCTS, productList);
//            setArguments(args);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.products_list, container, false);
        Context context = view.getContext();

        ListView listView = (ListView) view.findViewById(R.id.productList);
//        if (getArguments() != null) {
//            productList = (ProductList) getArguments().getSerializable(PRODUCTS);
//            ArrayAdapter<ProductItem> adapter = new ProductItemAdapter(context, R.layout.fragment_item_list, productList.products);
//            listView.setAdapter(adapter);
//        } else {
//            Bundle args = new Bundle();
//            args.putSerializable(PRODUCTS, productList);
//            setArguments(args);
//        }
        LoadProducts loadProducts = new LoadProducts(context, listView, this);
        loadProducts.execute("http://10.0.2.2:5000");

        listView.setOnItemClickListener((parent, clickedView, position, id) -> {
            ProductItem clickedProduct = productList.products.get(position);
            ProductDetails productDetailsFragment = new ProductDetails(clickedProduct);
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