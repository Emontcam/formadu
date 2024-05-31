package com.example.evaluablefinal.transformations;
import android.content.Context;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BitmapUtils {

    // Método para convertir un Bitmap a Uri
    public static Uri bitmapToUri(Context context, Bitmap bitmap) {
        Uri uri = null;
        try {
            // Guardar el Bitmap en un archivo temporal
            File tempFile = createTempImageFile(context);
            saveBitmapToFile(bitmap, tempFile);

            // Obtener la Uri del archivo temporal
            uri = Uri.fromFile(tempFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
    }

    // Método para crear un archivo temporal para guardar el Bitmap
    private static File createTempImageFile(Context context) throws IOException {
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    // Método para guardar un Bitmap en un archivo
    private static void saveBitmapToFile(Bitmap bitmap, File file) throws IOException {
        OutputStream outputStream = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        outputStream.flush();
        outputStream.close();
    }
}
