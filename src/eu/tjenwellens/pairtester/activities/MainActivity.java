package eu.tjenwellens.pairtester.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import eu.tjenwellens.pairtester.PairTesterApplication;
import eu.tjenwellens.pairtester.R;
import eu.tjenwellens.pairtester.model.MainModel;

public class MainActivity extends Activity
{
    // <editor-fold defaultstate="collapsed" desc="Folding">
    // </editor-fold>
    private MainModel model;
    public static final int START_LISTS = 8888;
    public static final int START_OVERVIEW = 7777;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initModel();
        start();
    }

    /*
     * Handles window rotation
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        // catch window rotation
        super.onConfigurationChanged(newConfig);
    }

    private void start()
    {

        if (model.start())
        {
            next();
        } else
        {
            Toast.makeText(this, "You can't do that", Toast.LENGTH_SHORT).show();
        }
    }

    private void initModel()
    {
        model = ((PairTesterApplication) getApplication()).getModel();
        if (!model.isListSelected())
        {
            Toast.makeText(this, "No list selected...", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Button and state changing">
    public void btnCorrect(View button)
    {
        if (model.correct())
        {
            next();
        } else
        {
            Toast.makeText(this, "You can't do that", Toast.LENGTH_SHORT).show();
        }
    }

    public void btnWrong(View button)
    {
        if (model.wrong())
        {
            next();
        } else
        {
            Toast.makeText(this, "You can't do that", Toast.LENGTH_SHORT).show();
        }
    }

    public void btnSkip(View button)
    {
        if (model.skip())
        {
            next();
        } else
        {
            Toast.makeText(this, "You can't do that", Toast.LENGTH_SHORT).show();
        }
    }

    public void btnCheck(View button)
    {
        if (model.check())
        {
            check();
        } else
        {
            Toast.makeText(this, "You can't do that", Toast.LENGTH_SHORT).show();
        }
    }

    private void check()
    {
        showValue();
        checkedState();
    }

    private void next()
    {
        showKey();
        uncheckedState();
    }

    private void updateProgress()
    {
        final ProgressBar pbProgress = (ProgressBar) findViewById(R.id.pbProgress);
        pbProgress.setProgress(model.getProgress());
    }

    private void updateScore()
    {
        final TextView tvScore = (TextView) findViewById(R.id.txtScore);
        int score = model.getScore();
        String text;
        if (score < 0)
        {
            text = "No score yet";
        } else
        {
            text = "" + score + "%";
        }
        tvScore.setText(text);
    }
//    private static final int PROGRESS = 0x1;
//    private ProgressBar mProgress;
//    private int mProgressStatus = 0;
//    private Handler mHandler = new Handler();
//
//    private void test()
//    {
//        mProgress = (ProgressBar) findViewById(R.id.progress_bar);
//
//        // Start lengthy operation in a background thread
//        new Thread(new Runnable()
//        {
//            public void run()
//            {
//                while (mProgressStatus < 100)
//                {
//                    mProgressStatus = doWork();
//
//                    // Update the progress bar
//                    mHandler.post(new Runnable()
//                    {
//                        public void run()
//                        {
//                            mProgress.setProgress(mProgressStatus);
//                        }
//                    });
//                }
//            }
//        }).start();
//    }

    private void showValue()
    {
        final TextView tvValue = (TextView) findViewById(R.id.txtValue);
        tvValue.setText(model.getValue());
    }

    private void showKey()
    {
        final TextView tvKey = (TextView) findViewById(R.id.txtKey);
        final TextView tvValue = (TextView) findViewById(R.id.txtValue);
        tvKey.setText(model.getKey());
        tvValue.setText(R.string.empty);
        updateProgress();
        updateScore();
    }

    private void uncheckedState()
    {
//        final Button btnCheck = (Button) findViewById(R.id.btnCheck);
//        final Button btnWrong = (Button) findViewById(R.id.btnWrong);
//        final Button btnCorrect = (Button) findViewById(R.id.btnCorrect);
//        final Button btnSkip = (Button) findViewById(R.id.btnSkip);
//        btnCheck.setEnabled(true);
//        btnWrong.setEnabled(false);
//        btnCorrect.setEnabled(false);
//        btnSkip.setEnabled(false);
        final LinearLayout pnlCheck = (LinearLayout) findViewById(R.id.pnlButtonsCheck);
        pnlCheck.setVisibility(View.VISIBLE);
        final LinearLayout pnlCWS = (LinearLayout) findViewById(R.id.pnlButtonsCWS);
        pnlCWS.setVisibility(View.GONE);
    }

    private void checkedState()
    {
//        final Button btnCheck = (Button) findViewById(R.id.btnCheck);
//        final Button btnWrong = (Button) findViewById(R.id.btnWrong);
//        final Button btnCorrect = (Button) findViewById(R.id.btnCorrect);
//        final Button btnSkip = (Button) findViewById(R.id.btnSkip);
//        btnCheck.setEnabled(false);
//        btnWrong.setEnabled(true);
//        btnCorrect.setEnabled(true);
//        btnSkip.setEnabled(true);
        final LinearLayout pnlCheck = (LinearLayout) findViewById(R.id.pnlButtonsCheck);
        pnlCheck.setVisibility(View.GONE);
        final LinearLayout pnlCWS = (LinearLayout) findViewById(R.id.pnlButtonsCWS);
        pnlCWS.setVisibility(View.VISIBLE);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Menu and activity switching">
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // This method is called once the menu is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_lists:
                launchLists();
                break;
            case R.id.menu_clear_score:
                clearScore();
                break;
            case R.id.menu_overview:
                launchOverview();
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == START_LISTS)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                // load new group
                launchStart();
            }
        } else if (requestCode == START_OVERVIEW)
        {
            launchStart();
        }
    }

    private void launchLists()
    {
//        startActivity(new Intent(this, ListsActivity.class));
        finish();
    }

    private void launchOverview()
    {
        startActivityForResult(new Intent(this, OverviewActivity.class), START_OVERVIEW);
    }

    private void launchStart()
    {
        startActivity(new Intent(this, StartActivity.class));
        finish();
    }

    private void clearScore()
    {
        model.resetScore();
        updateScore();
    }
    // </editor-fold>
}
