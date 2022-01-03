package com.example.pizza_f103255.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.pizza_f103255.activities.MainActivity;
import com.example.pizza_f103255.PizzaApp;
import com.example.pizza_f103255.R;
import com.example.pizza_f103255.entities.ProductList;
import com.example.pizza_f103255.entities.SupplementList;
import com.example.pizza_f103255.tasks.LoadProducts;
import com.example.pizza_f103255.tasks.LoadSupplements;

/**
 * A fragment that shows a loading screen
 * while it loads the data from the back-end.
 */
public class LoadDataFragment extends Fragment {

    public LoadDataFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.load_data, container, false);

        LoadProducts loadProducts = new LoadProducts((ProductList productList) -> {
            PizzaApp app = (PizzaApp) getActivity().getApplication();
            app.productList = productList;

            LoadSupplements loadSupplements = new LoadSupplements((SupplementList supplements) -> {
                app.supplementList = supplements;

                Intent intent = new Intent(getActivity(), MainActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
                return null;
            });
            loadSupplements.execute("http://10.0.2.2:5000/supplements");

            return null;
        });
        loadProducts.execute("http://10.0.2.2:5000/pizzas");

        return view;
    }
}