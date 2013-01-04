package eu.tjenwellens.pairtester.database;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import eu.tjenwellens.pairtester.groups.Group;
import eu.tjenwellens.pairtester.groups.GroupFactory;
import eu.tjenwellens.pairtester.pairs.DatabasePair;
import eu.tjenwellens.pairtester.pairs.DatabasePairFactory;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author Tjen
 */
public class GroupDatabaseHandler extends SQLiteOpenHelper implements GroupDatabase
{
    // Database Version
    private static final int DATABASE_VERSION = 3;
    // Database Name
    private static final String DATABASE_NAME = "pairtester";
    // Tables
    private static final String TABLE_PAIRS = "pairs";
    private static final String TABLE_GROUPS = "groups";
    // Table Creation
    private static final String CREATE_PAIRS_TABLE = "CREATE TABLE " + TABLE_PAIRS + "("
            + DatabasePair.KEY_ID + " INTEGER PRIMARY KEY,"
            + DatabasePair.KEY_GROUP_ID + " INTEGER,"
            + DatabasePair.KEY_KEY + " TEXT,"
            + DatabasePair.KEY_VALUE + " TEXT,"
            + DatabasePair.KEY_CORRECTS + " INTEGER,"
            + DatabasePair.KEY_WRONGS + " INTEGER,"
            // mind the , before the )
            + DatabasePair.KEY_SKIPS + " TEXT"
            + ")";
    private static final String CREATE_GROUPS_TABLE = "CREATE TABLE " + TABLE_GROUPS + "("
            + Group.KEY_ID + " INTEGER PRIMARY KEY,"
            + Group.KEY_NAME + " TEXT"
            + ")";
    // todo: remove
    private Context context;

    private GroupDatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // todo: remove
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_GROUPS_TABLE);
        db.execSQL(CREATE_PAIRS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUPS);
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAIRS);
        // Create tables again
        onCreate(db);
    }

    public static GroupDatabaseHandler getInstance(Application application)
    {
        return new GroupDatabaseHandler(application);
    }

    public Set<Group> getGroups()
    {
        Set<Group> groups = new LinkedHashSet<Group>();
        String selectQuery = "SELECT  * FROM " + TABLE_GROUPS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
        {
            do
            {
                groups.add(getGroupFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return groups;
    }

    public boolean createPair(DatabasePair pair)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean result = createPair(db, pair);
        db.close(); // Closing database connection
        return result;
    }

    public boolean createGroup(Group group)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean result = createGroup(db, group);
        db.close(); // Closing database connection
        return result;
    }

    public boolean createPairs(Set<DatabasePair> pairs)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean result = false;
        for (DatabasePair pair : pairs)
        {
            result = createPair(db, pair) || result;
        }
        db.close(); // Closing database connection
        return result;
    }

    public boolean updatePair(DatabasePair pair)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean result = updatePair(db, pair);
        db.close();
        return result;
    }

    public boolean updatePairs(Set<DatabasePair> pairs)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean result = false;
        for (DatabasePair pair : pairs)
        {
            result = updatePair(db, pair) || result;
        }
        db.close();
        return result;
    }

    public Set<DatabasePair> getPairs(Group group)
    {
        Set<DatabasePair> pairs = new LinkedHashSet<DatabasePair>();
        SQLiteDatabase db = this.getReadableDatabase();

        String table = TABLE_PAIRS;
        String[] columns = new String[]
        {
            DatabasePair.KEY_ID, DatabasePair.KEY_GROUP_ID, DatabasePair.KEY_KEY, DatabasePair.KEY_VALUE, DatabasePair.KEY_CORRECTS, DatabasePair.KEY_WRONGS, DatabasePair.KEY_SKIPS
        };
        String selection = DatabasePair.KEY_GROUP_ID + "=?";
        String[] selectionArgs = new String[]
        {
            String.valueOf(group.getGroupId())
        };
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = null;
        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                pairs.add(getPairFromCursor(cursor, group));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return pairs;
    }

    private boolean createPair(SQLiteDatabase db, DatabasePair pair)
    {
        ContentValues values = new ContentValues();
        prepareValuesForPair(values, pair, false);
        // Insert Row
        long id = db.insert(TABLE_PAIRS, null, values);
        if (id >= 0)
        {
            pair.setPairId((int) id);
            return true;
        } else
        {
            return false;
        }
    }

    private boolean createGroup(SQLiteDatabase db, Group group)
    {
        ContentValues values = new ContentValues();
        prepareValuesForGroup(values, group, false);
        // Insert Row
        long id = db.insert(TABLE_GROUPS, null, values);
        if (id >= 0)
        {
            group.setGroupId((int) id);
            return true;
        } else
        {
            return false;
        }
    }

    private void prepareValuesForGroup(ContentValues values, Group group, boolean addKey)
    {
        if (addKey)
        {
            values.put(Group.KEY_ID, group.getGroupId());        // id
        }
        values.put(Group.KEY_NAME, group.getName());        // groupid
        //TODO:
//        values.put(Group.KEY_SIZE, group.getSize()); // kalender
//        values.put(Group.KEY_CORRECTS, group.getCorrects()); // begin
//        values.put(Group.KEY_WRONGS, group.getWrongs()); // end
//        values.put(Group.KEY_SKIPS, group.getSkips()); // description
    }

    private boolean updatePair(SQLiteDatabase db, DatabasePair pair)
    {
        ContentValues values = new ContentValues();
        prepareValuesForPair(values, pair, true);
        String[] args = new String[1];
        args[0] = String.valueOf(pair.getPairId());
        // Update row
        return db.update(TABLE_PAIRS, values, DatabasePair.KEY_ID + " = ?", args) > 0;
    }

    private void prepareValuesForPair(ContentValues values, DatabasePair pair, boolean addKey)
    {
        if (addKey)
        {
            values.put(DatabasePair.KEY_ID, pair.getPairId());        // id
        }
        values.put(DatabasePair.KEY_GROUP_ID, pair.getGroup().getGroupId());        // groupid
        values.put(DatabasePair.KEY_KEY, pair.getKey()); // kalender
        values.put(DatabasePair.KEY_VALUE, pair.getValue());        // title
        values.put(DatabasePair.KEY_CORRECTS, pair.getCorrects()); // begin
        values.put(DatabasePair.KEY_WRONGS, pair.getWrongs()); // end
        values.put(DatabasePair.KEY_SKIPS, pair.getSkips()); // description
    }

    private DatabasePair getPairFromCursor(Cursor cursor, Group group)
    {
        int db_id = Integer.parseInt(cursor.getString(0));
//        int db_group_id = Integer.parseInt(cursor.getString(1));
        String db_key = cursor.getString(2);
        String db_value = cursor.getString(3);
        int db_corrects = Integer.parseInt(cursor.getString(4));
        int db_wrongs = Integer.parseInt(cursor.getString(5));
        int db_last_try = Integer.parseInt(cursor.getString(6));
        return DatabasePairFactory.loadDatabasePair(this, db_id, group, db_key, db_value, db_corrects, db_wrongs, db_last_try);
    }

    private Group getGroupFromCursor(Cursor cursor)
    {
        int db_id = Integer.parseInt(cursor.getString(0));
        String name = cursor.getString(1);
        return GroupFactory.loadGroup(db_id, name);
    }

    public boolean removePair(DatabasePair pair)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_PAIRS, DatabasePair.KEY_ID + " = ?",
                new String[]
                {
                    String.valueOf(pair.getPairId())
                });
        db.close();
        return result > 0;
    }

    public boolean removeGroup(Group group)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        // remove group
        boolean result = db.delete(TABLE_GROUPS, Group.KEY_ID + " = ?",
                new String[]
                {
                    String.valueOf(group.getGroupId())
                }) > 0;

        Log.d(">>>>>>>>>>>>>>>>>>>>>>>>>>>", "group deleted: " + result);
        if (result)
        {
            result = db.delete(TABLE_PAIRS, DatabasePair.KEY_GROUP_ID + " = ?",
                    new String[]
                    {
                        String.valueOf(group.getGroupId())
                    }) > 0;
        }
        db.close();
        Log.d(">>>>>>>>>>>>>>>>>>>>>>>>>>>", "pairs deleted: " + result);
        return result;
    }
}
