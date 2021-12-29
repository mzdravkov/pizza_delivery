package com.example.pizza_f103255.entities;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductItem implements Serializable {
    public final int id;
    public final String name;
    public final String description;
    public final String imageUrl;
    public final String size;
    public final BigDecimal price;

    public ProductItem(int id, String name, String description, String imageUrl, String size, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.size = size;
        this.price = price;
    }
}
