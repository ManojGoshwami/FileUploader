package com.sushi.fileUploader.common;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.sushi.fileUploader.MyApplication;
import com.sushi.fileUploader.R;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static java.lang.String.format;

public class Utility {

    private static String TAG = Utility.class.getSimpleName();

    private static final float maxHeight = 680.0f;
    private static final float maxWidth = 680.0f;//1280.0f;

    public static void playBeep(Context context, String beepSoundFile) {
        MediaPlayer m = new MediaPlayer();
        try {
            if (m.isPlaying()) {
                m.stop();
                m.release();
                m = new MediaPlayer();
            }

            AssetFileDescriptor descriptor = context.getAssets().openFd(beepSoundFile != null ? beepSoundFile : "beep.mp3");
            m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();

            m.prepare();
            m.setVolume(1f, 1f);
            m.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager ConnectionManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public static Dialog startProgress(Context context) {
        Dialog dialog = new ProgressDialog(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.setContentView(inflater.inflate(R.layout.layout_dialog_h, null));
        ProgressBar bar = (ProgressBar) dialog.findViewById(R.id.progressBar);
        return dialog;
    }

    public static void snackBar(View view, String msg, Integer duration, int color) {
        Snackbar sb = Snackbar.make(view, msg, duration);
        View sbView = sb.getView();
        TextView tv = (TextView) (sbView).findViewById(R.id.snackbar_text);
        tv.setTextSize(14);
        sbView.setBackgroundColor(color);
        sb.show();
    }

    // method for renaming file
    public static String renameFile(String oldName, String newName) {
        File dir = mkFileDir();
        String path = "";
        if (dir.exists()) {
            File from = new File(dir, oldName);
            File to = new File(dir, newName);
            if (from.exists()) {
                from.renameTo(to);
                path = to.getPath();
            } else {
                Log.v("Exist", "rename file not exist");
            }
        } else {
            Log.v("Else", "rename file");
        }
        Log.v("changed image path", "" + path);
        return path;
    }


    public static File mkFileDir() {
        File dir = new File(MyApplication.getInstance().getApplicationContext().getFilesDir(), "Uk_IMG");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static void stopProgress(Dialog dialog) {

        try {
            dialog.dismiss();
        } catch (Exception e) {
        }
        // return 1;
    }

    public static void setPic(String imagePath, int maxHeight, int maxWidth) {
        // Get the dimensions of the bitmap
        try {
            Bitmap photoBm = Utility.getBitmap(imagePath);
            //get its orginal dimensions
            int bmOriginalWidth = photoBm.getWidth();
            int bmOriginalHeight = photoBm.getHeight();
            double originalWidthToHeightRatio = 1.0 * bmOriginalWidth / bmOriginalHeight;
            double originalHeightToWidthRatio = 1.0 * bmOriginalHeight / bmOriginalWidth;
            //choose a maximum height

            //call the method to get the scaled bitmap
            photoBm = getScaledBitmap(photoBm, bmOriginalWidth, bmOriginalHeight,
                    originalWidthToHeightRatio, originalHeightToWidthRatio,
                    maxHeight, maxWidth);

            /**********THE REST OF THIS IS FROM Prabu's answer*******/
            //create a byte array output stream to hold the photo's bytes
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            //compress the photo's bytes into the byte array output stream
            photoBm.compress(Bitmap.CompressFormat.PNG, 80, bytes);

            FileOutputStream fo = new FileOutputStream(imagePath);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (Exception se) {
        }
    }

    public static String bitmapToBASE64(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } else return "";
    }

    public static File saveImage(final Context context, final String imageData) throws IOException {
        final byte[] imgBytesData = android.util.Base64.decode(imageData,
                android.util.Base64.DEFAULT);

        final File file = File.createTempFile("image", ".jpg", context.getCacheDir());
        final FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                fileOutputStream);
        try {
            bufferedOutputStream.write(imgBytesData);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                bufferedOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    //==============================================================================================

    public static String getExt(String filePath) {
        int strLength = filePath.lastIndexOf(".");
        if (strLength > 0)
            return filePath.substring(strLength + 1).toLowerCase();
        return null;
    }

    public static String getFileName(Uri uri ,Context context) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public static File getDocumentCacheDir(@NonNull Context context) {
       File cacheDir = new File(context.getCacheDir(), "documents");
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir;
    }

    @Nullable
    public static File generateFileName(@Nullable String name, File directory) {
        if (name == null) {
            return null;
        }

        File file = new File(directory, name);

        if (file.exists()) {
            String fileName = name;
            String extension = "";
            int dotIndex = name.lastIndexOf('.');
            if (dotIndex > 0) {
                fileName = name.substring(0, dotIndex);
                extension = name.substring(dotIndex);
            }

            int index = 0;

            while (file.exists()) {
                index++;
                name = fileName + '(' + index + ')' + extension;
                file = new File(directory, name);
            }
        }
        try {
            if (!file.createNewFile()) {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
        return file;
    }

    private static void saveFileFromUri(Context context, Uri uri, String destinationPath) {
        InputStream is = null;
        BufferedOutputStream bos = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            bos = new BufferedOutputStream(new FileOutputStream(destinationPath, false));
            byte[] buf = new byte[1024];
            is.read(buf);
            do {
                bos.write(buf);
            } while (is.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //=================================================================

    public static Bitmap getScaledBitmap(Bitmap bm, int bmOriginalWidth, int bmOriginalHeight, double originalWidthToHeightRatio, double originalHeightToWidthRatio, int maxHeight, int maxWidth) {
        if (bmOriginalWidth > maxWidth || bmOriginalHeight > maxHeight) {
            Log.v(TAG, format("RESIZING bitmap FROM %sx%s ", bmOriginalWidth, bmOriginalHeight));

            if (bmOriginalWidth > bmOriginalHeight) {
                bm = scaleDeminsFromWidth(bm, maxWidth, bmOriginalHeight, originalHeightToWidthRatio);
            } else {
                bm = scaleDeminsFromHeight(bm, maxHeight, bmOriginalHeight, originalWidthToHeightRatio);
            }

            Log.v(TAG, format("RESIZED bitmap TO %sx%s ", bm.getWidth(), bm.getHeight()));
        }
        return bm;
    }

    private static Bitmap scaleDeminsFromHeight(Bitmap bm, int maxHeight, int bmOriginalHeight, double originalWidthToHeightRatio) {
        int newHeight = (int) Math.min(maxHeight, bmOriginalHeight);
        int newWidth = (int) (newHeight * originalWidthToHeightRatio);
        bm = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
        return bm;
    }

    private static Bitmap scaleDeminsFromWidth(Bitmap bm, int maxWidth, int bmOriginalWidth, double originalHeightToWidthRatio) {
        //scale the width
        int newWidth = (int) Math.min(maxWidth, bmOriginalWidth);
        int newHeight = (int) (newWidth * originalHeightToWidthRatio);
        bm = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
        return bm;
    }

    public static Bitmap getBitmapByStringImage(String base64) {

        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public static Bitmap getBitmap(String path) {
        Bitmap bitmp = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmp = BitmapFactory.decodeFile(path, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmp;
    }

    public static int getRotation(String photoPath) {
        ExifInterface ei = null;
        int rotation = 0;
        try {
            ei = new ExifInterface(photoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);

        Bitmap rotatedBitmap = null;
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                //rotatedBitmap = rotateImage(Utility.getBitmap(photoPath), 90);
                rotation = 90;
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                //rotatedBitmap = rotateImage(Utility.getBitmap(photoPath), 180);
                rotation = 180;
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                //rotatedBitmap = rotateImage(Utility.getBitmap(photoPath), 270);
                rotation = 270;
                break;
            /*case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = rotateImage(Utility.getBitmap(photoPath), 90);*/
        }
        return rotation;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

}





