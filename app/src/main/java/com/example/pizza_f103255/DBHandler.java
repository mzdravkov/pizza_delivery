package com.example.pizza_f103255;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pizza_f103255.entities.ProductItem;
import com.example.pizza_f103255.entities.Supplement;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public void addProductToBasket(int times, ProductItem product, List<Supplement> supplements) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        ContentValues productValues = new ContentValues();

        productValues.put(PRODUCT_COL, product.id);
        productValues.put(NUMBER_COL, times);

        db.insert(BASKET_PRODUCTS, null, productValues);

        for (Supplement supplement : supplements) {
            ContentValues supplementValues = new ContentValues();

            supplementValues.put(SUPPLEMENT_COL, supplement.id);
            supplementValues.put(BASKET_PRODUCTS_ID_COL, product.id);

            db.insert(BASKET_SUPPLEMENTS, null, supplementValues);
        }

        db.endTransaction();

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
