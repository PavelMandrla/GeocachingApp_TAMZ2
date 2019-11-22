package tamz.geocaching;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PointsTableHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "Points";
    private static final int VERSION = 1;

    private static final String COL1 = "latitude";
    private static final String COL2 = "longitude";
    private static final String COL3 = "state";
    private static final String COL4 = "name";
    private static final String COL5 = "descriprion";
    private static final String COL6 = "marker_color";
    private static final String COL7 = "photo";

    private static PointsTableHelper instance;
    public static PointsTableHelper getInstance() {
        if (PointsTableHelper.instance == null) {
            PointsTableHelper.instance = new PointsTableHelper();
        }
        return PointsTableHelper.instance;
    }

    private PointsTableHelper() {
        super(App.getContext(), TABLE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable =
                "CREATE TABLE " + TABLE_NAME + "( "+
                COL1 + " REAL, " +
                COL2 + " REAL, " +
                COL3 + " INTEGER, " +
                COL4 + " TEXT, " +
                COL5 + " TEXT, " +
                COL6 + " TEXT, " +
                COL7 + " TEXT, " +
                "PRIMARY KEY (" + COL1 + ", " + COL2 +"));";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

    }

    public boolean insertPoint(Point point) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL1, point.getPosition().getLatitude());
        values.put(COL2, point.getPosition().getLongitude());
        values.put(COL3, point.isVisited());
        values.put(COL4, point.getName());
        values.put(COL5, point.getDescription());
        values.put(COL6, point.getMarkerColor());
        values.put(COL7, point.getPhotoURL());

        db.insert(TABLE_NAME, null, values);
        db.close();
        return true;
    }

    public boolean updatePoint(Point point) {
        //TODO -> test
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL3, point.isVisited());
        values.put(COL4, point.getName());
        values.put(COL5, point.getDescription());
        values.put(COL6, point.getMarkerColor());
        values.put(COL7, point.getPhotoURL());

        db.update(TABLE_NAME, values, COL1 + " = ? AND " + COL2 + " = ?",
                new String[] {
                    Double.toString(point.getPosition().getLatitude()),
                    Double.toString(point.getPosition().getLongitude())
                }
        );
        db.close();
        return false;
    }

    public void insertPoints(List<Point> points) {
        for (Point p : points) {
            insertPoint(p);                         //TODO -> point already in DB
        }
    }

    public List<Point> getAllPoints() {
        double lat, lon;
        int visited;
        ArrayList<Point> result = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (c.moveToFirst()) {
            do {
                lat = c.getDouble(0);
                lon = c.getDouble(1);
                visited = c.getInt(2);

                String name = c.getString(3);
                String desc = c.getString(4);
                String color = c.getString(5);
                String photo = c.getString(6);

                //double latitude, double longitude, String name, String description, String markerColor, int state, String photoURL
                result.add(new Point(lat, lon, name, desc, color, visited, photo));
                //result.add()
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return result;
    }

    public Point getPointByCoords(double lat, double lon) {
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d("COORDS", lat + " " + lon);
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL1 + " = ? AND " + COL2 + " = ?", new String[] {
                Double.toString(lat),
                Double.toString(lon)
                });

        if (!c.moveToFirst()) {
            Log.d("FML", "FML, FML, FML");
            return null;
        }
        double latitude = c.getDouble(0);
        double longitude = c.getDouble(1);
        int visited = c.getInt(2);
        String name = c.getString(3);
        String desc = c.getString(4);
        String color = c.getString(5);
        String photo = c.getString(6);
        Log.d("POINT_INFO", latitude + " " + longitude + " " + name +" ");

        return new Point(latitude, longitude, name, desc, color, visited, photo);
    }
}
