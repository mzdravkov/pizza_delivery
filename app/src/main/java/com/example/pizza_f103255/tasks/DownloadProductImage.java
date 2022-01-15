package com.example.pizza_f103255.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.pizza_f103255.R;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ConcurrentMap;

public class DownloadProductImage extends AsyncTask<String, Void, Bitmap> {
    private View productView;
    private Integer productId;
    private ConcurrentMap<Integer, Bitmap> bitmaps;

    public DownloadProductImage(
            View listItemView,
            Integer productId,
            ConcurrentMap<Integer, Bitmap> bitmaps) {
        this.productId = productId;
        this.productView = listItemView;
        this.bitmaps = bitmaps;
    }

    protected Bitmap doInBackground(String... urls) {
        // get image from cache if we've already downloaded it
        if (bitmaps.containsKey(productId)) {
            return bitmaps.get(productId);
        }

        String url = urls[0];
        Bitmap bitmap = null;
        try {
            InputStream in = new URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

        // add image to cache
        bitmaps.put(productId, bitmap);

        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
        // Make sure that we're setting the bitmap only if the view is for
        // the same product.
        // This is necessary because Android would reuse row views in lists.
        if (productView.getTag(R.id.product_id).equals(productId)) {
            ImageView imageView = productView.findViewById(R.id.product_image);
            imageView.setImageBitmap(result);
        }
    }
}