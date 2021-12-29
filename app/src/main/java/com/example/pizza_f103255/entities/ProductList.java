package com.example.pizza_f103255.entities;

import java.io.Serializable;
import java.util.List;

public class ProductList implements Serializable {
    public final List<ProductItem> products;

    public ProductList(List<ProductItem> products) {
        this.products = products;
    }
}
