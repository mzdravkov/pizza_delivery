package com.example.pizza_f103255.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.pizza_f103255.R;
import com.example.pizza_f103255.adapters.ProductItemAdapter;
import com.example.pizza_f103255.entities.ProductItem;
import com.example.pizza_f103255.entities.ProductList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LoadProducts extends AsyncTask<String, Void, ProductList> {
    private ProgressDialog progressDialog;
    private Context context;
    private ListView listView;

    public LoadProducts(Context context, ListView listView) {
        this.context = context;
        this.listView = listView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context, "Please wait","Loading products...", true);
    }

    @Override
    protected ProductList doInBackground(String... urls) {
        try {
            System.out.println("@!# !@# !@#");
            InputStream responseStream = getRequest(urls[0]);
            System.out.println("@!# !@#");
            System.out.println(responseStream);
            return parseResponse(responseStream);
        } catch (IOException e) {
            // TODO handle exception
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO handle exception
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute (ProductList result){
        super.onPostExecute(result);
        progressDialog.dismiss();
        ArrayAdapter<ProductItem> adapter = new ProductItemAdapter(context, R.layout.fragment_item_list, result.products);
        listView.setAdapter(adapter);
    }

    private InputStream getRequest(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("accept", "application/json");
        return connection.getInputStream();
    }

    private ProductList parseResponse(InputStream inputStream) throws IOException, JSONException {
        String response;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }

            response = sb.toString();
        }

        System.out.println("!@#");
        System.out.println(response);
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