package com.example.pizza_f103255.entities;

import java.math.BigDecimal;

public class Supplement {
    public final int id;
    public final String name;
    public final String size;
    public final BigDecimal price;

    public Supplement(int id, String name, String size, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %s", name, size, price);
    }
}
