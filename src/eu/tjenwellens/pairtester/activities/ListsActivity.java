package eu.tjenwellens.pairtester.activities;

import eu.tjenwellens.pairtester.groups.ListPanel;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import eu.tjenwellens.pairtester.PairTesterApplication;
import eu.tjenwellens.pairtester.R;
import eu.tjenwellens.pairtester.groups.Group;
import eu.tjenwellens.pairtester.groups.GroupFactory;
import eu.tjenwellens.pairtester.model.ListsModel;
import eu.tjenwellens.pairtester.ReadWrite;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tjen
 */
public class ListsActivity extends Activity
{
    private List<ListPanel> groups = new ArrayList<ListPanel>();
    private ListsModel model;
    // file selection
    private String SPLITTER = "=";
    private String[] mFileList;
    private static final int DIALOG_LOAD_FILE = 1000;
    private final String FTYPE = ".txt";
    private final File DIRECTORY = new File(Environment.getExternalStorageDirectory().getPath() + "//PairTester//");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lists);
        initModel();
        initGroupPanels();
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

    @Override
    protected Dialog onCreateDialog(int id)
    {
        Dialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        loadFileList();

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
                        loadGroup(mFileList[which]);
                        //you can do stuff with the file here too
                    }
                });
                break;
        }
        dialog = builder.show();
        return dialog;
    }

    private void loadGroup(String fileName)
    {
        if (fileName == null)
        {
            return;
        }
        String groupName = fileName.split("[^a-zA-Z0-9_-]", 2)[0];
        Group group;
        if ((group = GroupFactory.createGroup(model, groupName)) != null)
        {
            String path = Environment.getExternalStorageDirectory().getPath() + "//PairTester//" + fileName;
            ReadWrite.readDatabasePairs(getApplication(), model, group, path, SPLITTER);
            addGroupPanel(new ListPanel(this, group));
        } else
        {
            Toast.makeText(this, "Error: Cannot create group: " + groupName + "...", Toast.LENGTH_LONG).show();
        }
    }

    private void initModel()
    {
        model = ((PairTesterApplication) getApplication()).getModel();
    }

    private void initGroupPanels()
    {
        for (Group group : model.getGroups())
        {
            addGroupPanel(new ListPanel(this, group));
        }
    }

    private void addGroupPanel(ListPanel groupPanel)
    {
        groups.add(groupPanel);
        final ViewGroup groupPanelContainer = (ViewGroup) findViewById(R.id.pnlListsGroups);
        groupPanelContainer.addView(groupPanel);
        registerForContextMenu(groupPanel);
    }

    private boolean removeGroup(ListPanel groupPanel)
    {
        if (model.removeGroup(groupPanel))
        {
            final ViewGroup groupPanelContainer = (ViewGroup) findViewById(R.id.pnlListsGroups);
            groupPanelContainer.removeView(groupPanel);
            groups.remove(groupPanel);
            return true;
        } else
        {
            return false;
        }
    }

    public void shortClick(ListPanel panel)
    {
        model.setCurrentGroup(panel.getGroupId());
        launchStart();
    }

    private void launchStart()
    {
        startActivity(new Intent(this, StartActivity.class));
        finish();
    }

    public void longClick(ListPanel panel)
    {
        launchContextMenu(panel);
//        Toast.makeText(this, "Long Click: " + panel.getName(), Toast.LENGTH_LONG).show();
    }
    private ListPanel selectedGroupPanel;

    private void launchContextMenu(ListPanel p)
    {
        selectedGroupPanel = p;
        // start context menu
        openContextMenu(p);
    }

    private void importList()
    {
        onCreateDialog(DIALOG_LOAD_FILE);
    }

    private void createList()
    {
        Group g = GroupFactory.createGroup(model, "NewList" + groups.size());
        if (g != null)
        {
            addGroupPanel(new ListPanel(this, g));
        } else
        {
            Toast.makeText(this, "Error: Cannot create a new group...", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.lists_menu, menu);
        return true;
    }

    // This method is called once the menu is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_add:
                createList();
                break;
            case R.id.menu_import:
                importList();
                break;
        }
        return true;
    }

    /*
     * Creates context menu
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.lists_context_menu, menu);
    }

    /*
     * Handles context-menu
     */
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        if (selectedGroupPanel == null)
        {
            return super.onOptionsItemSelected(item);
        }
        switch (item.getItemId())
        {
            // Edit
            case R.id.menu_edit:
                launchEdit(selectedGroupPanel);
                return true;

            // Delete
            case R.id.menu_delete:
                removeGroup(selectedGroupPanel);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void launchEdit(ListPanel groupPanel)
    {
        Toast.makeText(this, "Not yet implemented...", Toast.LENGTH_SHORT).show();
    }
}
