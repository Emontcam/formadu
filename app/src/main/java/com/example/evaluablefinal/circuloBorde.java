package com.example.evaluablefinal;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

public class circuloBorde extends BitmapTransformation {
    private static final String ID = "com.example.evaluablefinal.CircleWithBorderTransformation";
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

    private int borderWidth;
    private int borderColor;

    public circuloBorde(int borderWidth, int borderColor) {
        this.borderWidth = borderWidth;
        this.borderColor = borderColor;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        int diameter = Math.min(toTransform.getWidth(), toTransform.getHeight());
        Bitmap result = pool.get(diameter, diameter, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888);
        }


        return result;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof circuloBorde;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }
}
