package gr.dit.hua.lab.geofence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBName = "geofencedb.db";
    public static final int DBVersion = 1;

    public static final String SessionTable = "Sessions";

    public static final String FIELD_1 = "latitude";

    public static final String FIELD_2 = "longitude";

    public DBHelper(@Nullable Context context) {
        super(context, DBName, null, DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+  SessionTable + "("+FIELD_1+" REAL, "+FIELD_2+" REAL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
