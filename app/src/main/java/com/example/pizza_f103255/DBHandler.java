package com.example.pizza_f103255;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.pizza_f103255.entities.Basket;
import com.example.pizza_f103255.entities.ProductItem;
import com.example.pizza_f103255.entities.ProductList;
import com.example.pizza_f103255.entities.Supplement;
import com.example.pizza_f103255.entities.SupplementList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "pizza_f103255";
    private static final int DB_VERSION = 1;

    // table names
    private static final String BASKET_PRODUCTS = "basket_products";
    private static final String BASKET_SUPPLEMENTS = "basket_supplements";
    private static final String FAVOURITES = "favourites";

    // column names
    private static final String ID_COL = "id";
    private static final String PRODUCT_COL = "product_id";
    private static final String SUPPLEMENT_COL = "supplement_id";
    private static final String NUMBER_COL = "number";
    private static final String BASKET_PRODUCTS_ID_COL = "basket_products_id";

    // queries
    private static final String GET_BASKET = "SELECT id, product_id, number, supplement_id " +
            "FROM basket_products p " +
            "LEFT JOIN basket_supplements s ON p.id = s.basket_products_id";
    private static final String DELETE_BASKET_SUPPLEMENTS = "DELETE FROM basket_supplements";
    private static final String DELETE_BASKET_PRODUCTS = "DELETE FROM basket_products";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createBasketProducts = "CREATE TABLE " + BASKET_PRODUCTS + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PRODUCT_COL + " INTEGER,"
                + NUMBER_COL + " INTEGER" + ")";

        db.execSQL(createBasketProducts);

        String createBasketSupplements = "CREATE TABLE " + BASKET_SUPPLEMENTS + " ("
                + SUPPLEMENT_COL + " INTEGER, "
                + BASKET_PRODUCTS_ID_COL + " INTEGER, "
                + "PRIMARY KEY (" + SUPPLEMENT_COL + ", " + BASKET_PRODUCTS_ID_COL + "), "
                + "FOREIGN KEY (basket_products_id) REFERENCES basket_products (id)"
                + ")";

        db.execSQL(createBasketSupplements);

        String createFavourites = "CREATE TABLE " + FAVOURITES + " ("
                + PRODUCT_COL + " INTEGER PRIMARY KEY)";

        db.execSQL(createFavourites);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Basket getBasket(ProductList productList, SupplementList supplementList) {
        Map<Integer, ProductItem> products = productList.products
                .stream()
                .collect(Collectors.toMap(
                        productItem -> productItem.id,
                        productItem -> productItem
                ));

        Map<Integer, Supplement> supplements = supplementList.supplements
                .stream()
                .collect(Collectors.toMap(
                        supplement -> supplement.id,
                        supplement -> supplement
                ));

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(GET_BASKET, null);

        Map<Integer, Basket.ProductInBasket> productsInBasket = new HashMap<>();
        while (cursor.moveToNext()) {
            Integer id = cursor.getInt(cursor.getColumnIndexOrThrow(ID_COL));
            Integer productId = cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_COL));
            Integer number = cursor.getInt(cursor.getColumnIndexOrThrow(NUMBER_COL));
            Integer supplementId = cursor.getInt(cursor.getColumnIndexOrThrow(SUPPLEMENT_COL));

            if (productsInBasket.containsKey(id)) {
                Basket.ProductInBasket productInBasket = productsInBasket.get(id);
                if (supplements.containsKey(supplementId)) {
                    productInBasket.productSupplements.add(supplements.get(supplementId));
                }
            } else {
                ProductItem product = products.get(productId);
                Basket.ProductInBasket productInBasket = new Basket.ProductInBasket(
                        product,
                        number,
                        Optional.ofNullable(supplements.get(supplementId))
                                .map(s -> new ArrayList<>(Collections.singletonList(s)))
                                .orElse(new ArrayList<>())
                );
                productsInBasket.put(id, productInBasket);
            }
        }

        Basket basket = new Basket();
        basket.products = new ArrayList<>(productsInBasket.values());
        return basket;
    }

    public void addProductToBasket(int times, ProductItem product, List<Supplement> supplements) {
        SQLiteDatabase db = this.getWritableDatabase();

//        db.beginTransaction();

        ContentValues productValues = new ContentValues();

        productValues.put(PRODUCT_COL, product.id);
        productValues.put(NUMBER_COL, times);

        long productInBasketID = db.insert(BASKET_PRODUCTS, null, productValues);

        for (Supplement supplement : supplements) {
            ContentValues supplementValues = new ContentValues();

            supplementValues.put(SUPPLEMENT_COL, supplement.id);
            supplementValues.put(BASKET_PRODUCTS_ID_COL, productInBasketID);

            db.insert(BASKET_SUPPLEMENTS, null, supplementValues);
        }

//        db.endTransaction();

        db.close();
    }

    public void clearBasket() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(DELETE_BASKET_SUPPLEMENTS);
        db.execSQL(DELETE_BASKET_PRODUCTS);

        db.close();
    }

    public Set<Integer> getFavourites() {
       SQLiteDatabase db = this.getReadableDatabase();
       Cursor cursor = db.rawQuery("SELECT " + PRODUCT_COL + " FROM " + FAVOURITES, null);

       Set<Integer> products = new HashSet<>();
       while (cursor.moveToNext()) {
           products.add(cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_COL)));
       }

       return products;
    }

    public void addProductToFavourites(ProductItem product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues productValues = new ContentValues();
        productValues.put(PRODUCT_COL, product.id);

        db.insert(FAVOURITES, null, productValues);

        db.close();
    }

    public void removeProductFromFavourites(ProductItem product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues productValues = new ContentValues();
        productValues.put(PRODUCT_COL, product.id);

        db.delete(FAVOURITES, String.format("%s=%s", PRODUCT_COL, product.id), null);

        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BASKET_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + BASKET_SUPPLEMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + FAVOURITES);
        onCreate(db);
    }
}
