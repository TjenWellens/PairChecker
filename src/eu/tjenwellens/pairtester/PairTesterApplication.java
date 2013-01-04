package eu.tjenwellens.pairtester;

import android.app.Application;
import eu.tjenwellens.pairtester.database.GroupDatabaseHandler;
import eu.tjenwellens.pairtester.model.PairTesterModel;

/**
 *
 * @author Tjen
 */
public class PairTesterApplication extends Application
{
    PairTesterModel model;

    public PairTesterApplication()
    {
        model = new PairTesterModel(GroupDatabaseHandler.getInstance(this));
    }

    public PairTesterModel getModel()
    {
        return model;
    }
}
