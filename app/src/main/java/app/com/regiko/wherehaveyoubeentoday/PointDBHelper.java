package app.com.regiko.wherehaveyoubeentoday;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * Created by Ковтун on 14.02.2018.
 */

public class PointDBHelper {
    PointDatabase openHelper;
    private SQLiteDatabase db;
    Date date, current;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",  java.util.Locale.getDefault());
    private String [] columnItemName = {PointContract.COLUMN_NAME_DATE,
            PointContract.COLUMN_NAME_LONGTTITUDE, PointContract.COLUMN_NAME_LATTITUDE};
    public PointDBHelper(Context context) {
        openHelper = new PointDatabase(context);
        db = openHelper.getWritableDatabase();
    }

    public void savePointItem(String date, float lat, float longt){
            ContentValues cv = new ContentValues();
            cv.put(PointContract.COLUMN_NAME_DATE, date);
            cv.put(PointContract.COLUMN_NAME_LATTITUDE, lat);
            cv.put(PointContract.COLUMN_NAME_LONGTTITUDE, longt);
            db.insert(PointContract.TABLE_NAME, null, cv);
        }

    public void removeSamePointItemBydate(String currenytime, float lat, float longt) {
        db = openHelper.getWritableDatabase();
        String last_date, last_long, last_lat;
        Cursor cursor = db.query(PointContract.TABLE_NAME, columnItemName, null, null, null, null, null);
        cursor.moveToLast();
        last_date = cursor.getString(0);
        last_long = cursor.getString(1);
        last_lat = cursor.getString(2);

        try {
            date = sdf.parse(last_date);
            current = sdf.parse(currenytime);
            Log.i("my3", date.getTime() + " " + last_long + " " + last_lat);
            Log.i("my2", current.getTime() + " " + lat + " " + longt);

        } catch (ParseException e) {
        }
        Log.i("my4", current.getTime()-date.getTime() + " " + last_long + " " + last_lat);
        if ((current.getTime()-date.getTime())>300000)
            savePointItem(sdf.format(current), lat, longt);
        cursor.close();
    }


    public void getPointerList(final CallBack mCallback) {

        ArrayList<PointItem> items = new ArrayList<PointItem>();
        db = openHelper.getWritableDatabase();
        Cursor cursor = db.query(PointContract.TABLE_NAME,
                columnItemName, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {

                PointItem item = new PointItem();
                item.setDateTime(cursor.getString(0));
                item.setLat(cursor.getFloat(1));
                item.setLogt(cursor.getFloat(2));
                items.add(item);
            } while (cursor.moveToNext());

            }
            mCallback.onSuccess(items);
            cursor.close();
        }

    public void deleteRowsByDate() {
        String sevendaysBeforeCurrentDate;
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -15);

        sevendaysBeforeCurrentDate = sdf.format(cal.getTime());
        Log.i("my7", sevendaysBeforeCurrentDate);
        db.delete(PointContract.TABLE_NAME, PointContract.COLUMN_NAME_DATE + " < " +"Datetime('"+ sevendaysBeforeCurrentDate+"')", null);
    }

    static class PointDatabase extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "poiter.db";

        private static final String TYPE_TEXT = " TEXT";
        private static final String TYPE_INTEGER = " INTEGER";
        private static final String TYPE_REAL = " REAL";
        private static final String TYPE_BLOB = " BLOB NOT NULL";
        private static final String COMMA_SEP = ",";

        private static final String SQL_CREATE_POINTS =
                "CREATE TABLE " + PointContract.TABLE_NAME + " (" +
                        PointContract._ID + " INTEGER PRIMARY KEY," +
                        PointContract.COLUMN_NAME_DATE    + TYPE_TEXT + COMMA_SEP +
                        PointContract.COLUMN_NAME_LATTITUDE + TYPE_TEXT + COMMA_SEP +
                        PointContract.COLUMN_NAME_LONGTTITUDE + TYPE_TEXT  +
                        ")";

        private static final String SQL_DELETE_POINTS =
                "DROP TABLE IF EXISTS " + PointContract.TABLE_NAME;

        public PointDatabase(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_POINTS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_POINTS);
            onCreate(db);
        }
    }
    public interface CallBack {
        void onSuccess(ArrayList<PointItem> list);

        void onFail(String msg);
    }

}
