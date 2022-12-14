package com.sushi.fileUploader.dbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.sushi.fileUploader.common.Utility;
import com.sushi.fileUploader.models.ImageFileModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.sushi.fileUploader.dbHelper.DatabaseHelper.ID;
import static com.sushi.fileUploader.dbHelper.DatabaseHelper.IMAGE_FILE_MST;
import static com.sushi.fileUploader.dbHelper.DatabaseHelper.IMAGE_NAME;
import static com.sushi.fileUploader.dbHelper.DatabaseHelper.IMAGE_NAME_PATH;
import static com.sushi.fileUploader.dbHelper.DatabaseHelper.UNIQUE_ID;

public class ImageFile_Helper {

    private static ImageFile_Helper a = null;

    public ImageFile_Helper() {
    }

    public static synchronized ImageFile_Helper getInstance() {
        if (a == null)
            a = new ImageFile_Helper();
        return a;
    }

    public static boolean saveImageFile(ImageFileModel imageFileModel, Context context) {
        boolean a = false;
        synchronized (DatabaseHelper.Lock) {
            ContentValues values = new ContentValues();

            values.put(UNIQUE_ID, imageFileModel.getUNIQUE_ID());
            values.put(IMAGE_NAME, imageFileModel.getIMAGE_NAME());
            values.put(IMAGE_NAME_PATH, imageFileModel.getIMAGE_NAME_PATH());

            SQLiteDatabase db = DatabaseHelper.getInstance(context).openDatabase(true);
            long l = db.insertWithOnConflict(IMAGE_FILE_MST, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            db.close();
            if (l > 0)
                a = true;
        }
        return a;
    }

    private ImageFileModel getCursor(Cursor res) {
        ImageFileModel imageFileModel = null;
        try {
            imageFileModel = new ImageFileModel();
            imageFileModel.setID(res.getString(res.getColumnIndex(ID)));
            imageFileModel.setUNIQUE_ID(res.getString(res.getColumnIndex(UNIQUE_ID)));
            imageFileModel.setIMAGE_NAME(res.getString(res.getColumnIndex(IMAGE_NAME)));
            imageFileModel.setIMAGE_NAME_PATH(res.getString(res.getColumnIndex(IMAGE_NAME_PATH)));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return imageFileModel;
    }

    public ImageFileModel getImageFileByUniqueId(Context context, String UniqueId) {
        synchronized (DatabaseHelper.Lock) {
            Cursor res = null;
            ImageFileModel imageFileModel = null;
            try (SQLiteDatabase db = DatabaseHelper.getInstance(context).openDatabase(false)) {
                res = db.rawQuery("SELECT * FROM " + IMAGE_FILE_MST + " where " + UNIQUE_ID + "='" + UniqueId + "'", null);
                if (res.getCount() > 0) {
                    res.moveToFirst();
                    imageFileModel = getCursor(res);
                }
                res.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return imageFileModel;
        }
    }

    public ArrayList<Integer> getAll(Context context) {
        synchronized (DatabaseHelper.Lock) {
            ArrayList<Integer> array_list = new ArrayList<>();
            try (SQLiteDatabase db = DatabaseHelper.getInstance(context).openDatabase(false)) {
                Cursor res = db.rawQuery("select * from " + IMAGE_FILE_MST, null);
                if (res.getCount() > 0 && res.moveToFirst()) {
                    do {
                        array_list.add(res.getInt(res.getColumnIndex(ID)));
                    } while (res.moveToNext());
                }
                res.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return array_list;
        }
    }
//    public ArrayList<File> getAllFile(Context context ) {
//        synchronized (DatabaseHelper.Lock) {
//            File array_list = new File();
//            try (SQLiteDatabase db = DatabaseHelper.getInstance(context).openDatabase(false)) {
//                Cursor res = db.rawQuery("select * from " + IMAGE_FILE_MST, null);
//                if (res.getCount() > 0 && res.moveToFirst()) {
//                    do {
//                        array_list.add(res.getInt(res.getColumnIndex(ID)));
//                    } while (res.moveToNext());
//                }
//                res.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            return array_list;
//        }
//    }

    public ArrayList<ImageFileModel> getAllImage(Context context) {
        synchronized (DatabaseHelper.Lock) {
            ImageFileModel consumerListModel = null;
            ArrayList<ImageFileModel> array_list = new ArrayList<>();
            try (SQLiteDatabase db = DatabaseHelper.getInstance(context).openDatabase(false)) {
                Cursor res = db.rawQuery("select * from " + IMAGE_FILE_MST, null);
                if (res.getCount() > 0 && res.moveToFirst()) {
                    do {
                        consumerListModel = getCursor(res);
                        array_list.add(consumerListModel);
                    } while (res.moveToNext());
                }
                res.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return array_list;
        }
    }

    public ImageFileModel getById(int id, Context context) {
        synchronized (DatabaseHelper.Lock) {
            Cursor res = null;
            ImageFileModel recordModel = null;
            try (SQLiteDatabase db = DatabaseHelper.getInstance(context).openDatabase(false)) {
                res = db.rawQuery("SELECT * FROM " + IMAGE_FILE_MST + " where " + ID + "='" + id + "'", null);
                if (res.getCount() > 0) {
                    res.moveToFirst();
                    recordModel = getCursor(res);
                }
                res.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return recordModel;
        }
    }

    public File getFileById(int id, Context context) throws IOException {
        synchronized (DatabaseHelper.Lock) {
            Cursor res = null;
            File file = null;
            try (SQLiteDatabase db = DatabaseHelper.getInstance(context).openDatabase(false)) {
                res = db.rawQuery("SELECT * FROM " + IMAGE_FILE_MST + " where " + ID + "='" + id + "'", null);
                if (res.getCount() > 0) {
                    res.moveToFirst();
                    file = new File(res.getString(res.getColumnIndex(IMAGE_NAME_PATH)));
                }
                res.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return file;
        }
    }

    public boolean DeleteById(int id, Context context) { // If we don't press submit to send in take action; this option deletes the data. Also if there we cancel the request to send data this deletes image from table
        boolean a = false;
        synchronized (DatabaseHelper.Lock) {
            try (SQLiteDatabase db = DatabaseHelper.getInstance(context).openDatabase(true)) {

                db.execSQL("DELETE FROM " + IMAGE_FILE_MST + " where " + ID + "=?", new Integer[]{id});
                a = true;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return a;
    }

    public boolean DeleteByUniqueId(int UniqueId, Context context) { // If we don't press submit to send in take action; this option deletes the data. Also if there we cancel the request to send data this deletes image from table
        boolean a = false;
        synchronized (DatabaseHelper.Lock) {
            try (SQLiteDatabase db = DatabaseHelper.getInstance(context).openDatabase(true)) {

                db.execSQL("DELETE FROM " + IMAGE_FILE_MST + " where " + UNIQUE_ID + "=?", new Integer[]{UniqueId});
                a = true;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return a;
    }

    public boolean deleteAll(Context context) {
        boolean a = false;
        synchronized (DatabaseHelper.Lock) {

            try (SQLiteDatabase db = DatabaseHelper.getInstance(context).openDatabase(true)) {
                db.execSQL("DELETE FROM " + IMAGE_FILE_MST);
                a = true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return a;
    }
}
