package com.example.pizza_f103255.tasks;

import com.example.pizza_f103255.entities.ProductItem;
import com.example.pizza_f103255.entities.ProductList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class LoadProducts extends LoadData<ProductList> {
    public LoadProducts(Function<ProductList, Void> onCompleteCallback) {
        this.onCompleteCallback = onCompleteCallback;
    }

    @Override
    protected ProductList parseJSON(String response) throws JSONException {
        JSONArray array = new JSONArray(response);

        List<ProductItem> productItems = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            ProductItem productItem = new ProductItem(
                    obj.getInt("id"),
                    obj.getString("name"),
                    obj.getString("description"),
                    obj.getString("image"),
                    obj.getString("size"),
                    BigDecimal.valueOf(obj.getDouble("price"))
            );
            productItems.add(productItem);
        }
        return new ProductList(productItems);
    }
}