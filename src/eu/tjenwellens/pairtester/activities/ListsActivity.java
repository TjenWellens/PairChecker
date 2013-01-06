package eu.tjenwellens.pairtester.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;
import eu.tjenwellens.pairtester.PairTesterApplication;
import eu.tjenwellens.pairtester.R;
import eu.tjenwellens.pairtester.ReadWrite;
import eu.tjenwellens.pairtester.groups.Group;
import eu.tjenwellens.pairtester.groups.GroupFactory;
import eu.tjenwellens.pairtester.groups.ListPanel;
import eu.tjenwellens.pairtester.model.ListsModel;
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
    private final File DIRECTORY = new File(Environment.getExternalStorageDirectory().getPath() + "//PairChecker//");
    // dialogs
    ProgressDialog progressDialog;
    Dialog fileChooserDialog;
    //my handler
    private Handler mHandler;

    @Override
    protected void onStop()
    {
        super.onStop();
        if (progressDialog != null)
        {
            progressDialog.cancel();
        }
        if (fileChooserDialog != null)
        {
            fileChooserDialog.cancel();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.lists);
        mHandler = new MyHandler();
        setSpinning(false);
        initModel();
        initGroupPanels();
    }

    private void setSpinning(boolean spin)
    {
        setSpinning(spin, "Loading. Please wait...");
    }

    private void setSpinning(boolean spin, String message)
    {
        if (spin)
        {
            progressDialog = ProgressDialog.show(this, "", message, true);
        } else if (progressDialog != null)
        {
            progressDialog.hide();
        }
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        loadFileList();

        switch (id)
        {
            case DIALOG_LOAD_FILE:
                builder.setTitle("Choose your file");
                if (mFileList == null)
                {
                    Log.e("TAG", "Showing file picker before loading the file list");
                    fileChooserDialog = builder.create();
                    return fileChooserDialog;
                }
                builder.setItems(mFileList, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        doImportGroup(mFileList[which]);
                        //you can do stuff with the file here too
                    }
                });
                break;
        }
        fileChooserDialog = builder.show();
        return fileChooserDialog;
    }

    private void addGroupPanel(ListPanel groupPanel)
    {
        groups.add(groupPanel);
        final ViewGroup groupPanelContainer = (ViewGroup) findViewById(R.id.pnlListsGroups);
        groupPanelContainer.addView(groupPanel);
        registerForContextMenu(groupPanel);
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

    private boolean removeGroup(ListPanel groupPanel)
    {
        boolean result = model.removeGroup(groupPanel);
        final ViewGroup groupPanelContainer = (ViewGroup) findViewById(R.id.pnlListsGroups);
        groupPanelContainer.removeView(groupPanel);
        groups.remove(groupPanel);
        return result;
    }

    private void launchStart()
    {
        startActivity(new Intent(this, StartActivity.class));
//        finish();
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

    public void shortClick(ListPanel panel)
    {
        doSwitchGoup(panel.getGroupId());
    }

    private void doSwitchGoup(int groupId)
    {
        setSpinning(true);
        switchGroup(groupId);
//        new Thread(new GroupSwitcher(groupId)).start();
//        new Thread(new GroupSwitcher(model, groupId)).start();
        setSpinning(false);
        switchDone();
    }

    private void switchGroup(int groupId)
    {
        model.setCurrentGroup(groupId);
    }

    private void doImportGroup(String fileName)
    {
        setSpinning(true, "Importing. Please wait...");
        new Thread(new ImportLoader(fileName)).start();
//        Group g = importGroup(fileName);
//        setSpinning(false);
//        importDone(g);
//        new Thread(new ImportLoader(getApplication(), model, fileName, SPLITTER)).start();
    }

//    private Group importGroup(String fileName)
//    {
//        if (fileName == null)
//        {
//            return null;
//        }
//        String groupName = fileName.split("[^a-zA-Z0-9_-]", 2)[0];
//        Group group;
//        if ((group = GroupFactory.createGroup(model, groupName)) != null)
//        {
//            String path = Environment.getExternalStorageDirectory().getPath() + "//PairChecker//" + fileName;
//            ReadWrite.readDatabasePairs(getApplication(), model, group, path, SPLITTER);
////            addGroupPanel(new ListPanel(this, group));
//        }
////        else{
////            Toast.makeText(this, "Importing group failed...", Toast.LENGTH_LONG).show();
////        }
//        return group;
//    }

    /*
     * Handles window rotation
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        // catch window rotation
        super.onConfigurationChanged(newConfig);
    }

    private void switchDone()
    {
        setSpinning(false);
        launchStart();
    }

    private void importDone(Group group)
    {
        setSpinning(false);
        if (group != null)
        {
            addGroupPanel(new ListPanel(this, group));
        } else
        {
            Toast.makeText(this, "Error: Cannot create group...", Toast.LENGTH_LONG).show();
        }
    }
//
//    private class GroupSwitcher implements Runnable
//    {
//        private int groupId;
//
//        public GroupSwitcher(int groupId)
//        {
//            this.groupId = groupId;
//        }
//
//        public void run()
//        {
//            switchGroup();
//            done();
//        }
//
//        private void switchGroup()
//        {
//            model.setCurrentGroup(groupId);
//        }
//
//        private void done()
//        {
//            Message msg = new Message();
//            msg.what = MSG_SWITCH_DONE;
////            msg.obj = g;
//
//            mHandler.sendMessage(msg);
//        }
//    }

    private class ImportLoader implements Runnable
    {
        private String fileName;

        public ImportLoader(String fileName)
        {
            this.fileName = fileName;
        }

        public void run()
        {
            Group g = loadGroup();
            done(g);
        }

        private Group loadGroup()
        {
            if (fileName == null)
            {
                return null;
            }
            String groupName = fileName.split("[^a-zA-Z0-9_-]", 2)[0];
            Group group;
            if ((group = GroupFactory.createGroup(model, groupName)) != null)
            {
                String path = Environment.getExternalStorageDirectory().getPath() + "//PairChecker//" + fileName;
                ReadWrite.readDatabasePairs(getApplication(), model, group, path, SPLITTER);
            }
            return group;
        }

        private void done(Group g)
        {
            Message msg = new Message();
            msg.what = MSG_IMPORT_DONE;
            msg.obj = g;

            mHandler.sendMessage(msg);
        }
    }
    private static final int MSG_SWITCH_DONE = 1;
    private static final int MSG_IMPORT_DONE = 2;

    private class MyHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
//                case MSG_SWITCH_DONE:
//
//                    switchDone();
////                    lblChat.append("Received: " + msg.obj + "\r\n");
//                    break;
                case MSG_IMPORT_DONE:
                    importDone((Group) msg.obj);
                    break;
            }
        }
    }
//    private boolean test = false;
//
//    public void btnTest(View v)
//    {
//        setSpinning(true);
//        try
//        {
//            wait(5000);
//        } catch (InterruptedException e)
//        {
//            System.out.println(e);
//        }
//        setSpinning(false);
//    }
}
