package eu.tjenwellens.pairtester;

import android.app.Application;
import android.content.ContextWrapper;
import android.widget.Toast;
import eu.tjenwellens.pairtester.database.GroupDatabase;
import eu.tjenwellens.pairtester.groups.Group;
import eu.tjenwellens.pairtester.pairs.DatabasePair;
import eu.tjenwellens.pairtester.pairs.DatabasePairFactory;
import eu.tjenwellens.pairtester.pairs.PairFactory;
import eu.tjenwellens.pairtester.pairs.RatedPair;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Tjen
 */
public class ReadWrite
{
    public static List<RatedPair> readRatedPairs(ContextWrapper context, String path, String splitter)
    {
        List<RatedPair> newPairs = new ArrayList<RatedPair>();
        try
        {
            File myFile = new File(path);
            FileInputStream fIn;
            fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
            String line;
            int counter = 0;
            while ((line = myReader.readLine()) != null)
            {
                if (line.startsWith("#"))
                {
                    continue;
                }
                String[] key_value = line.split(splitter, 2);
                if (key_value.length == 2)
                {
                    String key = key_value[0];
                    String value = key_value[1];
                    String[] value_corrects_wrongs_lastTry = value.split(";");
                    if (value_corrects_wrongs_lastTry.length == 4)
                    {
                        value = value_corrects_wrongs_lastTry[0];
                        int corrects = Integer.parseInt(value_corrects_wrongs_lastTry[1]);
                        int wrongs = Integer.parseInt(value_corrects_wrongs_lastTry[2]);
                        int lastTry = Integer.parseInt(value_corrects_wrongs_lastTry[3]);
                        newPairs.add(PairFactory.loadPair(context, counter, key, value, corrects, wrongs, lastTry));
                    } else
                    {
                        newPairs.add(PairFactory.createPair(context, counter, key_value[0], key_value[1]));
                    }
                    counter++;
                }
            }
            myReader.close();
            Toast.makeText(context.getBaseContext(), "New entries read: " + counter, Toast.LENGTH_SHORT).show();
        } catch (IOException e)
        {
            Toast.makeText(context.getBaseContext(), "Error reading file", Toast.LENGTH_SHORT).show();
        }
        return newPairs;
    }

    public static Collection<DatabasePair> readDatabasePairs(Application context, GroupDatabase database, Group group, String path, String splitter)
    {
        List<DatabasePair> newPairs = new LinkedList<DatabasePair>();
        try
        {
            File myFile = new File(path);
            FileInputStream fIn;
            fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
            String line;
            int counter = 0;
            while ((line = myReader.readLine()) != null)
            {
                if (line.startsWith("#"))
                {
                    continue;
                }
                String[] key_value = line.split(splitter, 2);
                if (key_value.length == 2)
                {
                    String key = key_value[0];
                    String value = key_value[1];
//                    String[] value_corrects_wrongs_lastTry = value.split(";");
//                    if (value_corrects_wrongs_lastTry.length == 4)
//                    {
//                        value = value_corrects_wrongs_lastTry[0];
//                        int corrects = Integer.parseInt(value_corrects_wrongs_lastTry[1]);
//                        int wrongs = Integer.parseInt(value_corrects_wrongs_lastTry[2]);
//                        int lastTry = Integer.parseInt(value_corrects_wrongs_lastTry[3]);
//                        newPairs.add(DatabasePairFactory.createDatabasePair(database, groupId, key, value));
//                        newPairs.add(PairFactory.loadPair(context, counter, key, value, corrects, wrongs, lastTry));
//                    } else
                    {
                        newPairs.add(DatabasePairFactory.createDatabasePair(database, group, key, value));
//                        newPairs.add(PairFactory.createPair(context, counter, key_value[0], key_value[1]));
                    }
                    counter++;
                }
            }
            myReader.close();
            Toast.makeText(context.getBaseContext(), "New entries read: " + counter, Toast.LENGTH_SHORT).show();
        } catch (IOException e)
        {
            Toast.makeText(context.getBaseContext(), "Error reading file", Toast.LENGTH_SHORT).show();
        }
        return newPairs;
    }
}
