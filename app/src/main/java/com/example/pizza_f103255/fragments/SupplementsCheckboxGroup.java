package com.example.pizza_f103255.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.pizza_f103255.PizzaApp;
import com.example.pizza_f103255.R;
import com.example.pizza_f103255.entities.Supplement;

public class SupplementsCheckboxGroup extends Fragment {
    public static final int SUPPLEMENT_ID_TAG = View.generateViewId();

    public SupplementsCheckboxGroup() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            checkBox.setTag(checkBox.getId(), supplement.id);
            listView.addView(checkBox);
        }

        return view;
    }
}
