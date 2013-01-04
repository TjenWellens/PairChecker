package eu.tjenwellens.pairtester.database;

import eu.tjenwellens.pairtester.pairs.PairFactory;
import eu.tjenwellens.pairtester.pairs.RatedPair;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper
{
    // singleton
    private static DatabaseHandler dbh;
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "pairtester";
    // Contacts table name
    private static final String TABLE_PAIRS = "pair";
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_KEY = "key";
    private static final String KEY_VALUE = "value";
    private static final String KEY_CORRECTS = "corrects";
    private static final String KEY_WRONGS = "wrongs";
    private static final String KEY_SKIPS = "last_try_correct";
    // Create table
    private static final String CREATE_PAIRS_TABLE = "CREATE TABLE " + TABLE_PAIRS + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_KEY + " TEXT,"
            + KEY_VALUE + " TEXT,"
            + KEY_CORRECTS + " INTEGER,"
            + KEY_WRONGS + " INTEGER,"
            // mind the , before the )
            + KEY_SKIPS + " TEXT"
            + ")";
    private Context context;

    private DatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static DatabaseHandler getInstance(Context context)
    {
        if (dbh == null || dbh.context != context)
        {
            dbh = new DatabaseHandler(context);
        }
        return dbh;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_PAIRS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAIRS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
    // Adding new contact
    public void addPair(RatedPair pair)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        addPairWithoutClosing(db, pair);
        db.close(); // Closing database connection
    }

    private void addPairWithoutClosing(SQLiteDatabase db, RatedPair pair)
    {
        ContentValues values = new ContentValues();
        values.put(KEY_ID, pair.getPairId());        // id
        values.put(KEY_KEY, pair.getKey()); // kalender
        values.put(KEY_VALUE, pair.getValue());        // title
        values.put(KEY_CORRECTS, pair.getCorrects()); // begin
        values.put(KEY_WRONGS, pair.getWrongs()); // end
        values.put(KEY_SKIPS, pair.getSkips()); // description

        // Inserting Row
        db.insert(TABLE_PAIRS, null, values);
    }

    public int addAllPairs(List<RatedPair> pairs)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int counter = 0;
        for (RatedPair pair : pairs)
        {
            addPairWithoutClosing(db, pair);
            counter++;
        }
        db.close();
        return counter;
    }

    // Getting single contact
    public RatedPair getPair(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = TABLE_PAIRS;
        String[] columns = new String[]
        {
            KEY_ID, KEY_KEY, KEY_VALUE, KEY_CORRECTS, KEY_WRONGS, KEY_SKIPS
        };
        String selection = KEY_ID + "=?";
        String[] selectionArgs = new String[]
        {
            String.valueOf(id)
        };
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = null;
        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        if (cursor != null)
        {
            cursor.moveToFirst();
        }

        int db_id = Integer.parseInt(cursor.getString(0));
        String db_key = cursor.getString(1);
        String db_value = cursor.getString(2);
        int db_corrects = Integer.parseInt(cursor.getString(3));
        int db_wrongs = Integer.parseInt(cursor.getString(4));
        int db_last_try = Integer.parseInt(cursor.getString(4));
        cursor.close();
        db.close();

        RatedPair pair = PairFactory.loadPair(context, db_id, db_key, db_value, db_corrects, db_wrongs, db_last_try);
        // return contact
        return pair;
    }

    // Getting All Contacts
    public List<RatedPair> getAllPairs()
    {
        List<RatedPair> contactList = new ArrayList<RatedPair>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PAIRS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                int db_id = Integer.parseInt(cursor.getString(0));
                String db_key = cursor.getString(1);
                String db_value = cursor.getString(2);
                int db_corrects = Integer.parseInt(cursor.getString(3));
                int db_wrongs = Integer.parseInt(cursor.getString(4));
                int db_last_try = Integer.parseInt(cursor.getString(4));

                RatedPair pair = PairFactory.loadPair(context, db_id, db_key, db_value, db_corrects, db_wrongs, db_last_try);
                // Adding contact to list
                contactList.add(pair);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return contact list
        return contactList;
    }

    // Updating single contact
    public int updatePair(RatedPair pair)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, pair.getPairId());        // id
        values.put(KEY_KEY, pair.getKey()); // kalender
        values.put(KEY_VALUE, pair.getValue());        // title
        values.put(KEY_CORRECTS, pair.getCorrects()); // begin
        values.put(KEY_WRONGS, pair.getWrongs()); // end
        values.put(KEY_SKIPS, pair.getSkips()); // description
        int result = db.update(TABLE_PAIRS, values, KEY_ID + " = ?",
                new String[]
                {
                    String.valueOf(pair.getPairId())
                });
        db.close();
        // updating row
        return result;
    }

    // Deleting single contact
    public void deletePair(RatedPair pair)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PAIRS, KEY_ID + " = ?",
                new String[]
                {
                    String.valueOf(pair.getPairId())
                });
        db.close();
    }

    // Getting contacts Count
    public int getPairsCount()
    {
        String countQuery = "SELECT  * FROM " + TABLE_PAIRS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        // return count
        return count;
    }

    public void clearPairs()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PAIRS, null, null);
        db.close();
    }
}