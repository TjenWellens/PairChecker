package eu.tjenwellens.pairtester;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity
{
    /* contains all the key-value pairs */
    private List<RatedPairI> pairs = null;
    private boolean checked = true;
    private RatedPairI currentPair;
    //In an Activity
    private String SPLITTER = "=";
    private String[] mFileList;
    private static final int DIALOG_LOAD_FILE = 1000;
    private final String FTYPE = ".txt";
    private final File DIRECTORY = new File(Environment.getExternalStorageDirectory().getPath() + "//PairTester//");

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        loadFileList();
        reset(loadState());
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        saveState(pairs);
    }

    private List<RatedPairI> loadState()
    {
        Toast.makeText(this, "loading", Toast.LENGTH_SHORT).show();
        return DatabaseHandler.getInstance(this).getAllPairs();
    }

    private void saveState(List<RatedPairI> savePairs)
    {
        DatabaseHandler dbh = DatabaseHandler.getInstance(this);
        dbh.clearPairs();
        if (savePairs != null && !savePairs.isEmpty())
        {
            int saves = dbh.addAllPairs(savePairs);
            Toast.makeText(this, "Saved " + saves + " items.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        Dialog dialog;
        AlertDialog.Builder builder = new Builder(this);

        switch (id)
        {
            case DIALOG_LOAD_FILE:
                builder.setTitle("Choose your file");
                if (mFileList == null)
                {
                    Log.e("TAG", "Showing file picker before loading the file list");
                    dialog = builder.create();
                    return dialog;
                }
                builder.setItems(mFileList, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        load(mFileList[which]);
                        //you can do stuff with the file here too
                    }
                });
                break;
        }
        dialog = builder.show();
        return dialog;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    // This method is called once the menu is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            // We have only one menu option
            case R.id.menu_select_file:
                onCreateDialog(DIALOG_LOAD_FILE);
                break;
            case R.id.menu_show_score:
                showScore();
                break;
            case R.id.menu_clear_score:
                clearScore();
                break;
            case R.id.menu_clear_entries:
                clearEntries();
                break;
        }
        return true;
    }

    private void loadFileList()
    {
        try
        {
            DIRECTORY.mkdirs();
        } catch (SecurityException e)
        {
            Log.e("TAG", "unable to write on the sd card " + e.toString());
        }
        if (DIRECTORY.exists())
        {
            FilenameFilter filter = new FilenameFilter()
            {
                public boolean accept(File dir, String filename)
                {
                    File sel = new File(dir, filename);
                    return filename.contains(FTYPE) || sel.isDirectory();
                }
            };
            mFileList = DIRECTORY.list(filter);
        } else
        {
            mFileList = new String[0];
        }
    }

    private void load(String fileName)
    {
        if (fileName == null)
        {
            return;
        }
        List<RatedPairI> newPairs = read(Environment.getExternalStorageDirectory().getPath() + "//PairTester//" + fileName);
        if (newPairs.isEmpty())
        {
            return;
        }
        pairs = newPairs;
        reset(newPairs);
    }

    private void reset(List<RatedPairI> newPairs)
    {
        if (newPairs == null || newPairs.isEmpty())
        {
            newPairs = initPairs();
        }
        pairs = newPairs;
        resetState();
        resetGUI();
        Toast.makeText(this, "" + pairs.size() + " items loaded", Toast.LENGTH_LONG).show();
    }

    private void resetState()
    {
        checked = true;
        currentPair = null;
    }

    private void resetGUI()
    {
        setContentView(R.layout.main);
        final TextView tvKey = (TextView) findViewById(R.id.txtKey);
        final TextView tvValue = (TextView) findViewById(R.id.txtValue);
        final Button btn = (Button) findViewById(R.id.btnCheckNext);
        tvKey.setText(R.string.empty);
        tvValue.setText(R.string.empty);
        btn.setText(R.string.start);
    }

    private List<RatedPairI> read(String path)
    {
        List<RatedPairI> newPairs = new ArrayList<RatedPairI>();
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
                String[] s = line.split(SPLITTER, 2);
                if (s.length == 2)
                {
                    newPairs.add(PairFactory.createPair(this, counter, s[0], s[1]));
                    counter++;
                }
            }
            myReader.close();
            Toast.makeText(getBaseContext(), "New entries read: " + counter, Toast.LENGTH_SHORT).show();
        } catch (IOException e)
        {
            Toast.makeText(getBaseContext(), "Error reading file", Toast.LENGTH_SHORT).show();
        }
        return newPairs;
    }

    public void btnCheckNext(View button)
    {
        if (pool != null && pairs != null && currentPair != null)
        {
            pool.add(pairs.indexOf(currentPair));
        }
        checkNext();
    }

    private void checkNext()
    {
        if (checked)
        {
            next();
        } else
        {
            showValue();
        }
    }

    public void btnCorrect(View button)
    {
        if (checked && currentPair != null)
        {
            currentPair.correct();
            checkNext();
        }
    }

    public void btnWrong(View button)
    {
        if (checked && currentPair != null)
        {
            currentPair.wrong();
            pool.add(pairs.indexOf(currentPair));
            checkNext();
        }
    }

    private void next()
    {
        currentPair = pairs.get(getRandomIndex());
        showKey(currentPair);
        // change button text
        changeButtonText(R.string.check);
        // swap turn
        checked = false;
    }

    private List<Integer> numbers()
    {
        List<Integer> ret = new LinkedList<Integer>();
        for (int i = 0; i < pairs.size(); i++)
        {
            ret.add(i);
        }
        return ret;
    }
    private List<Integer> pool;
    private Random random = new Random(System.currentTimeMillis());

    private int getRandomIndex()
    {
        if (pool == null || pool.isEmpty())
        {
            pool = numbers();
            Toast.makeText(this, "Starting new training course", Toast.LENGTH_SHORT).show();
        }
        Integer ret = pool.get(random.nextInt(pool.size()));
        pool.remove(ret);
        return ret;
    }

    private void showValue()
    {
        if (currentPair != null)
        {
            final TextView tvValue = (TextView) findViewById(R.id.txtValue);
            tvValue.setText(currentPair.getValue());
        }
        // change button text
        changeButtonText(R.string.next);
        // swap turn
        checked = true;
    }

    private void showKey(RatedPairI p)
    {
        final TextView tvKey = (TextView) findViewById(R.id.txtKey);
        final TextView tvValue = (TextView) findViewById(R.id.txtValue);
        tvKey.setText(p.getKey());
        tvValue.setText(R.string.empty);
    }

    private List<RatedPairI> initPairs()
    {
        List<RatedPairI> newPairs = new ArrayList<RatedPairI>();
        newPairs.add(PairFactory.createPair(this, 0, "0", "saw"));
        newPairs.add(PairFactory.createPair(this, 1, "1", "day"));
        newPairs.add(PairFactory.createPair(this, 2, "2", "Noah"));
        newPairs.add(PairFactory.createPair(this, 3, "3", "home"));
        newPairs.add(PairFactory.createPair(this, 4, "04", "ray"));
        newPairs.add(PairFactory.createPair(this, 5, "5", "law"));
        newPairs.add(PairFactory.createPair(this, 6, "6", "siege"));
        newPairs.add(PairFactory.createPair(this, 7, "7", "shoe"));
        newPairs.add(PairFactory.createPair(this, 8, "8", "UFO"));
        newPairs.add(PairFactory.createPair(this, 9, "9", "bee"));
        return newPairs;
    }

    private void changeButtonText(int resid)
    {
        final Button btn = (Button) findViewById(R.id.btnCheckNext);
        btn.setText(resid);
    }

    private void showScore()
    {
        int score, total, corrects = 0, wrongs = 0;
        for (RatedPairI pair : pairs)
        {
            corrects += pair.getCorrects();
            wrongs += pair.getWrongs();
        }
        total = corrects + wrongs;
        if (total == 0)
        {
            Toast.makeText(this, "Your don't have a score...", Toast.LENGTH_LONG).show();
        } else
        {
            score = (int) Math.round(100 * corrects / total);
            Toast.makeText(this, "Your current score is: " + score + "%", Toast.LENGTH_LONG).show();
        }
    }

    private void clearScore()
    {
        for (RatedPairI pair : pairs)
        {
            pair.clearScore();
        }
        Toast.makeText(this, "Score reset.", Toast.LENGTH_LONG).show();
    }

    private void clearEntries()
    {
        reset(null);
    }
}
