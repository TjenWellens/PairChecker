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
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity
{
    /* contains all the key-value pairs */
    private List<RatedPair> pairs = null;
    private boolean checked = true;
    private RatedPair currentPair;
    //In an Activity
    private String SPLITTER = "=";
    private String[] mFileList;
    private final File DIRECTORY = new File(Environment.getExternalStorageDirectory().getPath() + "//PairTester//");
    private static final String FTYPE = ".txt";
    private static final int DIALOG_LOAD_FILE = 1000;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        loadFileList();
        reset(pairs);
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
                schowScore();
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
        List<RatedPair> newPairs = read(Environment.getExternalStorageDirectory().getPath() + "//PairTester//" + fileName);
        if (newPairs.isEmpty())
        {
            return;
        }
        pairs = newPairs;
        reset(newPairs);
    }

    private void reset(List<RatedPair> newPairs)
    {
        if (newPairs == null || newPairs.isEmpty())
        {
            newPairs = initPairs();
        }
        pairs = newPairs;
        resetState();
        resetGUI();
    }

    private void resetState()
    {
        checked = true;
        currentPair = null;
    }

    private void resetGUI()
    {
        final TextView tvKey = (TextView) findViewById(R.id.txtKey);
        final TextView tvValue = (TextView) findViewById(R.id.txtValue);
        final Button btn = (Button) findViewById(R.id.btnCheckNext);
        tvKey.setText(R.string.empty);
        tvValue.setText(R.string.empty);
        btn.setText(R.string.start);
    }

    private List<RatedPair> read(String path)
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
                String[] s = line.split(SPLITTER, 2);
                if (s.length == 2)
                {
                    newPairs.add(new RatedPair(s[0], s[1]));
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
        if (currentPair != null)
        {
            currentPair.correct();
        }
    }

    public void btnWrong(View button)
    {
        if (currentPair != null)
        {
            currentPair.wrong();
        }
    }

    private void next()
    {
        Random random = new Random(System.currentTimeMillis());
        currentPair = pairs.get(random.nextInt(pairs.size()));
        showKey(currentPair);
        // change button text
        changeButtonText(R.string.check);
        // swap turn
        checked = false;
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

    private void showKey(RatedPair p)
    {
        final TextView tvKey = (TextView) findViewById(R.id.txtKey);
        final TextView tvValue = (TextView) findViewById(R.id.txtValue);
        tvKey.setText(p.getKey());
        tvValue.setText(R.string.empty);
    }

    private static List<RatedPair> initPairs()
    {
        List<RatedPair> newPairs = new ArrayList<RatedPair>();
        newPairs.add(new RatedPair("00", "SOS"));
        newPairs.add(new RatedPair("01", "sad"));
        newPairs.add(new RatedPair("02", "son"));
        newPairs.add(new RatedPair("03", "SAME"));
        newPairs.add(new RatedPair("04", "sour"));
        newPairs.add(new RatedPair("05", "soul"));
        newPairs.add(new RatedPair("06", "siege"));
        newPairs.add(new RatedPair("07", "sock"));
        newPairs.add(new RatedPair("08", "safe"));
        newPairs.add(new RatedPair("09", "zap"));
        return newPairs;
    }

    private void changeButtonText(int resid)
    {
        final Button btn = (Button) findViewById(R.id.btnCheckNext);
        btn.setText(resid);
    }

    private void schowScore()
    {
        int score = 0;
        int corrects = 0;
        int wrongs = 0;
        int total;
        for (RatedPair ratedPair : pairs)
        {
            corrects += ratedPair.getCorrects();
            wrongs += ratedPair.getWrongs();
        }
        total = corrects + wrongs;
        score = (int) Math.round(100 * corrects / total);
        Toast.makeText(this, "Your current score is: " + score + "%", Toast.LENGTH_LONG).show();
    }
}
