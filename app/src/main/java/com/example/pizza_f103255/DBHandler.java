package com.example.pizza_f103255;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pizza_f103255.entities.ProductItem;
import com.example.pizza_f103255.entities.Supplement;

import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    private static final String DB_NAME = "pizza_f103255";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "basket";
    private static final String ID_COL = "id";
    private static final String PRODUCT_COL = "product_id";
    private static final String NUMBER_COL = "number";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PRODUCT_COL + " INTEGER,"
                + NUMBER_COL + " INTEGER";

        db.execSQL(query);
    }

    public void addProduct(int times, ProductItem product, List<Supplement> supplements) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(PRODUCT_COL, product.id);
        values.put(NUMBER_COL, times);

        db.insert(TABLE_NAME, null, values);

        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
