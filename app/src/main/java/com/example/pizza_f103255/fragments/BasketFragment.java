package com.example.pizza_f103255.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.pizza_f103255.DBHandler;
import com.example.pizza_f103255.PizzaApp;
import com.example.pizza_f103255.R;
import com.example.pizza_f103255.entities.Basket;
import com.example.pizza_f103255.entities.ProductList;
import com.example.pizza_f103255.entities.Supplement;
import com.example.pizza_f103255.entities.SupplementList;

import java.math.BigDecimal;

/**
 * Shows the user's basket.
 */
public class BasketFragment extends Fragment {

    public BasketFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.basket, container, false);

        PizzaApp app = (PizzaApp) getActivity().getApplication();
        ProductList productList = app.productList;
        SupplementList supplementList = app.supplementList;

        DBHandler db = new DBHandler(getContext());
        Basket basket = db.getBasket(productList, supplementList);

        TableLayout table = view.findViewById(R.id.basket_table);

        TableRow titleRow = titleRow();
        table.addView(titleRow);

        BigDecimal total = BigDecimal.ZERO;
        for (Basket.ProductInBasket product : basket.products) {
            TableRow productRow = productToRow(product);
            table.addView(productRow);
            if (product.productSupplements != null) {
                for (Supplement supplement : product.productSupplements) {
                    TableRow supplementRow = supplementToRow(supplement, product.number);
                    table.addView(supplementRow);
                    total = total.add(
                            supplement.price
                                    .multiply(BigDecimal.valueOf(product.number)));
                }
            }

            total = total.add(
                    product.product.price
                            .multiply(BigDecimal.valueOf(product.number)));
        }

        TableRow totalRow = totalRow(total);
        table.addView(totalRow);

        Button clearBasket = view.findViewById(R.id.clear_basket_btn);
        clearBasket.setOnClickListener(v -> {
            db.clearBasket();
            refreshFragment();
        });

        return view;
    }

    private TableRow productToRow(Basket.ProductInBasket product) {
        TableRow row = new TableRow(getContext());

        TextView name = new TextView(getContext());
        name.setText(product.product.name);
        name.setPadding(5, 5, 5, 5);
        row.addView(name);

        TextView pricePerItem = new TextView(getContext());
        pricePerItem.setText(product.product.price.toString());
        pricePerItem.setPadding(5, 5, 5, 5);
        row.addView(pricePerItem);

        TextView number = new TextView(getContext());
        number.setText(Integer.toString(product.number));
        number.setPadding(5, 5, 5, 5);
        row.addView(number);

        TextView price = new TextView(getContext());
        price.setText(product.product.price.multiply(BigDecimal.valueOf(product.number)).toString());
        price.setPadding(5, 5, 5, 5);
        row.addView(price);

        return row;
    }

    private TableRow supplementToRow(Supplement supplement, int number) {
        TableRow row = new TableRow(getContext());

        TextView name = new TextView(getContext());
        name.setText(" + " + supplement.name);
        name.setPadding(5, 5, 5, 5);
        row.addView(name);

        TextView pricePerItem = new TextView(getContext());
        pricePerItem.setText(supplement.price.toString());
        pricePerItem.setPadding(5, 5, 5, 5);
        row.addView(pricePerItem);

        TextView numberView = new TextView(getContext());
        numberView.setText(Integer.toString(number));
        numberView.setPadding(5, 5, 5, 5);
        row.addView(numberView);

        TextView price = new TextView(getContext());
        price.setText(supplement.price.multiply(BigDecimal.valueOf(number)).toString());
        price.setPadding(5, 5, 5, 5);
        row.addView(price);

        return row;
    }

    private TableRow titleRow() {
        TableRow row = new TableRow(getContext());

        TextView name = new TextView(getContext());
        name.setText("Product name");
        name.setPadding(10, 10, 10, 10);
        row.addView(name);

        TextView pricePerItem = new TextView(getContext());
        pricePerItem.setText("Price per item");
        pricePerItem.setPadding(10, 10, 10, 10);
        row.addView(pricePerItem);

        TextView numberView = new TextView(getContext());
        numberView.setText("Number");
        numberView.setPadding(10, 10, 10, 10);
        row.addView(numberView);

        TextView price = new TextView(getContext());
        price.setText("Total");
        price.setPadding(10, 10, 10, 10);
        row.addView(price);

        return row;
    }

    private TableRow totalRow(BigDecimal total) {
        TableRow row = new TableRow(getContext());

        TextView name = new TextView(getContext());
        name.setText("Total");
        name.setPadding(5, 5, 5, 5);
        row.addView(name);

        TextView pricePerItem = new TextView(getContext());
        pricePerItem.setText("");
        pricePerItem.setPadding(5, 5, 5, 5);
        row.addView(pricePerItem);

        TextView numberView = new TextView(getContext());
        numberView.setText("");
        numberView.setPadding(5, 5, 5, 5);
        row.addView(numberView);

        TextView price = new TextView(getContext());
        price.setText(total.toString());
        price.setPadding(5, 5, 5, 5);
        row.addView(price);

        return row;
    }

    private void refreshFragment() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction().detach(this).commit();
        fm.beginTransaction().attach(this).commit();
    }
}