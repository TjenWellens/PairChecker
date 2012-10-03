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
    //In an Activity
    private String splitter="=";
    private String[] mFileList;
    private File mPath = new File(Environment.getExternalStorageDirectory().getPath() + "//PairTester//");
    private String mChosenFile;
    private static final String FTYPE = ".txt";
    private static final int DIALOG_LOAD_FILE = 1000;

    private void loadFileList()
    {
        try
        {
            mPath.mkdirs();
        } catch (SecurityException e)
        {
            Log.e("TAG", "unable to write on the sd card " + e.toString());
        }
        if (mPath.exists())
        {
            FilenameFilter filter = new FilenameFilter()
            {
                public boolean accept(File dir, String filename)
                {
                    File sel = new File(dir, filename);
                    return filename.contains(FTYPE) || sel.isDirectory();
                }
            };
            mFileList = mPath.list(filter);
        } else
        {
            mFileList = new String[0];
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
                        mChosenFile = mFileList[which];
                        load(mChosenFile);
                        //you can do stuff with the file here too
                    }
                });
                break;
        }
        dialog = builder.show();
        return dialog;
    }
    /* contains all the key-value pairs */
    private List<Pair> pairs = null;
    private boolean checked = true;
    private Pair currentPair;

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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private void load(String fileName)
    {
        List<Pair> newPairs = read(Environment.getExternalStorageDirectory().getPath() + "//PairTester//" + fileName);
        if (newPairs.isEmpty())
        {
            return;
        }
        pairs = newPairs;
        reset(newPairs);
    }

    private void reset(List<Pair> newPairs)
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

    private List<Pair> read(String path)
    {
        List<Pair> newPairs = new ArrayList<Pair>();
        try
        {
            File myFile = new File(path);
            FileInputStream fIn;
            fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
            String line;
            while ((line = myReader.readLine()) != null)
            {
                if (line.startsWith("#"))
                {
                    continue;
                }
                String[] s = line.split(splitter, 2);
                if (s.length == 2)
                {
                    newPairs.add(new Pair(s[0], s[1]));
                }
            }
            myReader.close();
            Toast.makeText(getBaseContext(), "Done reading SD 'mysdfile.txt'", Toast.LENGTH_SHORT).show();
        } catch (IOException e)
        {
        }
        return newPairs;
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
        }
        return true;
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

    private void showKey(Pair p)
    {
        final TextView tvKey = (TextView) findViewById(R.id.txtKey);
        final TextView tvValue = (TextView) findViewById(R.id.txtValue);
        tvKey.setText(p.getKey());
        tvValue.setText(R.string.empty);
    }

    private static List<Pair> initPairs()
    {
        List<Pair> newPairs = new ArrayList<Pair>();
        newPairs.add(new Pair("00", "SOS"));
        newPairs.add(new Pair("01", "sad"));
        newPairs.add(new Pair("02", "son"));
        newPairs.add(new Pair("03", "SAME"));
        newPairs.add(new Pair("04", "sour"));
        newPairs.add(new Pair("05", "soul"));
        newPairs.add(new Pair("06", "siege"));
        return newPairs;
    }

    private void changeButtonText(int resid)
    {
        final Button btn = (Button) findViewById(R.id.btnCheckNext);
        btn.setText(resid);
    }
}