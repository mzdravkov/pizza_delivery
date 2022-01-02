package com.example.pizza_f103255;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.pizza_f103255.fragments.ProductListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, ProductListFragment.class, null)
                    .commit();
        }
    }
}