package com.example.pizza_f103255.tasks;

import com.example.pizza_f103255.entities.Supplement;
import com.example.pizza_f103255.entities.SupplementList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class LoadSupplements extends LoadData<SupplementList> {
    public LoadSupplements(Function<SupplementList, Void> onCompleteCallback) {
        this.onCompleteCallback = onCompleteCallback;
    }

    @Override
    protected SupplementList parseJSON(String response) throws JSONException {
        JSONArray array = new JSONArray(response);

        List<Supplement> supplements = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            Supplement supplement = new Supplement(
                    obj.getInt("id"),
                    obj.getString("name"),
                    obj.getString("size"),
                    BigDecimal.valueOf(obj.getDouble("price"))
            );
            supplements.add(supplement);
        }
        return new SupplementList(supplements);
    }
}