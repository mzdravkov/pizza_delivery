package com.example.pizza_f103255.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.pizza_f103255.PizzaApp;
import com.example.pizza_f103255.R;
import com.example.pizza_f103255.entities.SerializableSet;
import com.example.pizza_f103255.entities.Supplement;

public class SupplementsCheckboxGroupFragment extends Fragment {
    public static final String SELECTED_SUPPLEMENTS_KEY = "SELECTED_SUPPLEMENTS";

    SerializableSet<Integer> selectedSupplements = new SerializableSet<>();

    public SupplementsCheckboxGroupFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            selectedSupplements =
                    (SerializableSet<Integer>) savedInstanceState
                            .getSerializable(SELECTED_SUPPLEMENTS_KEY);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(SELECTED_SUPPLEMENTS_KEY, selectedSupplements);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.supplements_checkbox_group, container, false);
        Context context = view.getContext();

        LinearLayout listView = view.findViewById(R.id.supplements);

        PizzaApp app = (PizzaApp) getActivity().getApplication();

        for (Supplement supplement : app.supplementList.supplements) {
            CheckBox checkBox = new CheckBox(context);
            checkBox.setText(supplement.toString());
            checkBox.setTag(R.id.supplement_id, supplement.id);
            checkBox.setChecked(selectedSupplements.contains(supplement.id));
            checkBox.setOnCheckedChangeListener((checkboxView, isChecked) -> {
                Integer supplementId = (Integer) checkboxView.getTag(R.id.supplement_id);
                if (isChecked) {
                    selectedSupplements.add(supplementId);
                } else {
                    selectedSupplements.remove(supplementId);
                }
            });
            listView.addView(checkBox);
        }

        return view;
    }
}
