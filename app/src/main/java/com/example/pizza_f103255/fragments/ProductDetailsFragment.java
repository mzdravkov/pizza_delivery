package com.example.pizza_f103255.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.pizza_f103255.DBHandler;
import com.example.pizza_f103255.PizzaApp;
import com.example.pizza_f103255.R;
import com.example.pizza_f103255.entities.ProductItem;
import com.example.pizza_f103255.entities.Supplement;
import com.example.pizza_f103255.entities.SupplementList;
import com.example.pizza_f103255.widgets.HorizontalNumberPicker;

import java.util.ArrayList;
import java.util.List;

/**
 * Shows the details of a single product.
 * Allows adding it to the basket or favourites.
 */
public class ProductDetailsFragment extends Fragment {
    private ProductItem product;

    public ProductDetailsFragment(ProductItem product) {
        this.product = product;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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
        PizzaApp app = (PizzaApp) getActivity().getApplication();
        SupplementList supplementList = app.supplementList;

        Button addToBasket = view.findViewById(R.id.finish_order_btn);
        addToBasket.setOnClickListener(v -> {
            int number = numberPicker.getValue();
            List<Supplement> supplementForProduct = new ArrayList<>();

            LinearLayout supplementsCheckboxGroup = view.findViewById(R.id.supplements);
            final int childrenCount = supplementsCheckboxGroup.getChildCount();
            for (int i = 0; i < childrenCount; i++) {
                CheckBox checkBox = (CheckBox) supplementsCheckboxGroup.getChildAt(i);
                if (checkBox.isChecked()) {
                    int supplementId = (Integer) checkBox.getTag(checkBox.getId());
                    Supplement supplement = supplementList.supplements
                            .stream()
                            .filter(s -> s.id == supplementId)
                            .findFirst()
                            .get();

                    supplementForProduct.add(supplement);
                }
            }
            db.addProductToBasket(number, product, supplementForProduct);

            CharSequence text = "Added to basket!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(getContext(), text, duration);
            toast.show();
        });

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