package com.example.pizza_f103255.entities;

import java.util.List;

public class Basket {
    public List<ProductInBasket> products;

    public static class ProductInBasket {
        public final ProductItem product;
        public final int number;
        public final List<Supplement> productSupplements;

        public ProductInBasket(ProductItem product, int number, List<Supplement> productSupplements) {
            this.product = product;
            this.number = number;
            this.productSupplements = productSupplements;
        }
    }
}
