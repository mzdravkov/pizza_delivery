package com.example.pizza_f103255.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Shows the user's basket.
 */
public class BasketFragment extends Fragment {
    public static final String RESTAURANT_EMAIL = "orders@examplerestaurant.bg";

    final Calendar calendar = Calendar.getInstance();

    public BasketFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.basket, container, false);

        PizzaApp app = (PizzaApp) getActivity().getApplication();
        ProductList productList = app.productList;
        SupplementList supplementList = app.supplementList;

        DBHandler db = new DBHandler(getContext());
        Basket basket = db.getBasket(productList, supplementList);

        addBasketContentTable(view, basket);

        Spinner citySpinner = view.findViewById(R.id.city_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(), R.array.cities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapter);

        EditText city = view.findViewById(R.id.address_city_field);

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String[] cities = getResources().getStringArray(R.array.cities);
                city.setText(cities[position]);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        EditText deliveryDate = view.findViewById(R.id.delivery_date_field);
        DatePickerDialog.OnDateSetListener date = (ignored, year, month, day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            String format = "dd/MM/yy";
            SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
            deliveryDate.setText(dateFormat.format(calendar.getTime()));
        };
        deliveryDate.setOnClickListener(
                view1 -> new DatePickerDialog(
                        getContext(),
                        date,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                ).show());

        Button clearBasket = view.findViewById(R.id.clear_basket_btn);
        clearBasket.setOnClickListener(v -> {
            db.clearBasket();
            refreshFragment();
        });

        Button submitButton = view.findViewById(R.id.finish_order_btn);
        submitButton.setOnClickListener(v -> showConfirmationDialog(basket, view));

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showConfirmationDialog(Basket basket, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder
                .setTitle("Are you sure you want to complete your order?")
                .setMessage("Pressing \"Yes\" will send the order to the restaurant")
                .setPositiveButton("Yes", (dialog, id) -> completeOrder(basket, view))
                .setNegativeButton("Cancel", (dialog, id) -> {});

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void completeOrder(Basket basket, View view) {
        StringBuilder contentBuilder = new StringBuilder();

        for (Basket.ProductInBasket product : basket.products) {
            String productText = String.format("%dx %s", product.number, product.product.name);
            String supplementsText = "";

            if (product.productSupplements != null && product.productSupplements.size() > 0) {
               List<String> supplementNames =
                       product.productSupplements
                               .stream()
                               .map(supplement -> supplement.name)
                               .collect(Collectors.toList());
               supplementsText = " (" + String.join(", ", supplementNames) + ")";
            }
           contentBuilder
                   .append(productText)
                   .append(supplementsText)
                   .append("\n");
        }

        contentBuilder.append("\n\n");

        EditText city = view.findViewById(R.id.address_city_field);
        contentBuilder.append("City: " + city.getText() + "\n");
        EditText address = view.findViewById(R.id.detailed_address_field);
        contentBuilder.append("Address: " + address.getText() + "\n");
        EditText date = view.findViewById(R.id.delivery_date_field);
        EditText time = view.findViewById(R.id.delivery_time_field);
        contentBuilder.append("Delivery by: " + date.getText() + " " + time.getText() + "\n");

        view.findViewById(R.id.finish_order_btn).setEnabled(false);

        String body = contentBuilder.toString();
        composeEmail("Order", body);
    }

    public void composeEmail(String subject, String body) {
        Intent selectorIntent = new Intent(Intent.ACTION_SENDTO);
        selectorIntent.setData(Uri.parse("mailto:" + RESTAURANT_EMAIL));

        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{RESTAURANT_EMAIL});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);
        emailIntent.setSelector(selectorIntent);

        getActivity().startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    private void addBasketContentTable(View view, Basket basket) {
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