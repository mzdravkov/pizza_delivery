package com.example.pizza_f103255;

import android.app.Application;
import android.graphics.Bitmap;

import com.example.pizza_f103255.entities.ProductList;
import com.example.pizza_f103255.entities.SupplementList;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class PizzaApp extends Application {
    public ProductList productList;
    public SupplementList supplementList;
    public ConcurrentMap<Integer, Bitmap> productToImage = new ConcurrentHashMap<>();
}
