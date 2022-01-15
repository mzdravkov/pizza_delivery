package com.example.pizza_f103255.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pizza_f103255.PizzaApp;
import com.example.pizza_f103255.R;
import com.example.pizza_f103255.fragments.BasketFragment;
import com.example.pizza_f103255.fragments.FavouritesFragment;
import com.example.pizza_f103255.fragments.ProductListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PizzaApp app = (PizzaApp) getApplication();

        // If the products or supplements data is lost we start
        // the LoadingActivity to pull the data from the server.
        if (app.productList == null || app.supplementList == null) {
            Intent intent = new Intent(this, LoadingActivity.class);
            startActivity(intent);
            finish();
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, ProductListFragment.class, null)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favourites_main_menu_btn:
                FavouritesFragment favouritesFragment = new FavouritesFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container_view, favouritesFragment, "favourites")
                        .addToBackStack(null)
                        .commit();
                return true;
            case R.id.basket_main_menu_btn:
                BasketFragment basketFragment = new BasketFragment();
                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_view, basketFragment, "basket")
                    .addToBackStack(null)
                    .commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}