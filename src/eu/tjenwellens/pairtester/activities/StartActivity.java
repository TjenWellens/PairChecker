package eu.tjenwellens.pairtester.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import eu.tjenwellens.pairtester.PairTesterApplication;
import eu.tjenwellens.pairtester.R;
import eu.tjenwellens.pairtester.model.StartModel;

/**
 *
 * @author Tjen
 */
public class StartActivity extends Activity
{
    private StartModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        model = ((PairTesterApplication) getApplication()).getModel();
    }

    public void btnStart(View button)
    {
        if (model.isListSelected())
        {
            launchMain();
        } else
        {
            launchLists();
        }
    }

    private void launchMain()
    {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void launchLists()
    {
        startActivity(new Intent(this, ListsActivity.class));
        finish();
    }
}
