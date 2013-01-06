package eu.tjenwellens.pairtester.groups;

import eu.tjenwellens.pairtester.Pool;
import eu.tjenwellens.pairtester.RandomPoolCorrectOnlyLoadState;
import eu.tjenwellens.pairtester.database.GroupDatabase;
import eu.tjenwellens.pairtester.database.PairDatabase;
import eu.tjenwellens.pairtester.pairs.DatabasePair;
import eu.tjenwellens.pairtester.pairs.RatedPair;
import eu.tjenwellens.pairtester.state.State;
import eu.tjenwellens.pairtester.state.StateType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tjen
 */
public class DatabasePairSet implements GroupSet
{
    // group
    private Group group;
    // database
    private GroupDatabase database;
    // 
    private State state;
    private List<DatabasePair> pairs;
    private Pool pool;
    private DatabasePair currentPair;

    public DatabasePairSet(Group group, GroupDatabase database)
    {
        this.group = group;
        this.database = database;
        loadPairs();
        this.state = StateType.NEW;
        pool = new RandomPoolCorrectOnlyLoadState(pairs);
    }

    private void loadPairs()
    {
        pairs = new ArrayList<DatabasePair>(database.getPairs(group));
    }

    public int getGroupId()
    {
        return group.getGroupId();
    }

    public String getName()
    {
        return group.getName();
    }

    public boolean correct()
    {
        if (state.correct())
        {
            if (currentPair != null)
            {
                currentPair.correct();
            }
            pool.correct();
            next();
            return true;
        }
        return false;
    }

    public boolean wrong()
    {
        if (state.wrong())
        {
            if (currentPair != null)
            {
                currentPair.wrong();
            }
            pool.wrong();
            next();
            return true;
        }
        return false;
    }

    public boolean skip()
    {
        if (state.skip())
        {
            if (currentPair != null)
            {
                currentPair.skip();
            }
            pool.skip();
            next();
            return true;
        }
        return false;
    }

    public boolean start()
    {
        if (state.start())
        {
            pool.start();
            next();
            return true;
        }
        return false;
    }

    public boolean check()
    {
        if (state.check())
        {
            state = StateType.CHECKED;
            return true;
        }
        return false;
    }

    private void next()
    {
        next(getNextPair());
    }

    private void next(DatabasePair nextPair)
    {
        if (nextPair != null)
        {
            currentPair = nextPair;
        }
        state = StateType.UNCHECKED;
    }

    private DatabasePair getNextPair()
    {
        if (pairs == null || pairs.isEmpty())
        {
            return null;
        }
        return pairs.get(pool.getNextIndex());
    }

    public int getScore()
    {
        int score, total, corrects = 0, wrongs = 0;
        for (RatedPair pair : pairs)
        {
            corrects += pair.getCorrects();
            wrongs += pair.getWrongs();
        }
        total = corrects + wrongs;
        if (total == 0)
        {
            score = -1;
        } else
        {
            score = (int) Math.round(100 * corrects / total);
        }
        return score;
    }

    public void resetScore()
    {
        for (RatedPair pair : pairs)
        {
            pair.resetScore();
        }
    }

    public int getCurrentSize()
    {
        return pool.getSize();
    }

    public int getSize()
    {
        return pool.getOriginalSize();
    }

    public boolean addPair(DatabasePair pair)
    {
        if (pairs.add(pair))
        {
            next(pair);
            return true;
        }
        return false;
    }

    public boolean deletePair(DatabasePair pair)
    {
        boolean result = pairs.remove(pair);
        if (result)
        {
            pair.remove();
            pool.removeIndex(Integer.SIZE);
            if (currentPair != null && currentPair.equals(pair))
            {
                next();
            }
        }
        return result;
    }

    public void deleteCurrentPair()
    {
        if (currentPair != null)
        {
            deletePair(currentPair);
        }
    }

    public void setDatabase(PairDatabase database)
    {
        for (DatabasePair databasePair : pairs)
        {
            databasePair.setDatabase(database);
        }
    }

    public void update()
    {
        if (currentPair != null)
        {
            currentPair.update();
        }
    }

    public void setPairId(int id)
    {
        if (currentPair != null)
        {
            currentPair.setPairId(id);
        }
    }

    public boolean remove()
    {
        return database.removeGroup(this.group);
    }

    public int getCorrects()
    {
        int corrects = 0;
        for (DatabasePair databasePair : pairs)
        {
            corrects += databasePair.getCorrects();
        }
        return corrects;
    }

    public int getWrongs()
    {
        int wrongs = 0;
        for (DatabasePair databasePair : pairs)
        {
            wrongs += databasePair.getWrongs();
        }
        return wrongs;
    }

    public int getSkips()
    {
        int skips = 0;
        for (DatabasePair databasePair : pairs)
        {
            skips += databasePair.getSkips();
        }
        return skips;
    }

    public String getKey()
    {
        if (currentPair == null)
        {
            return "";
        } else
        {
            return currentPair.getKey();
        }
    }

    public String getValue()
    {
        if (currentPair == null)
        {
            return "";
        } else
        {
            return currentPair.getValue();
        }
    }

    public void setKey(String key)
    {
        if (currentPair != null)
        {
            currentPair.setKey(key);
        }
    }

    public void setValue(String value)
    {
        if (currentPair != null)
        {
            currentPair.setValue(value);
        }
    }

    public int getProgress()
    {
        if (pool.getOriginalSize() == 0)
        {
            return -1;
        }
//        Log.d("\n\n\nPROGRESS: ", "size:" + pool.getSize() + " - orig:" + pool.getOriginalSize() + "%");
        return 100 - (pool.getSize() * 100 / pool.getOriginalSize());
    }

    public int getPairId()
    {
        if (currentPair == null)
        {
            return -1;
        } else
        {
            return currentPair.getPairId();
        }
    }

    public Group getGroup()
    {
        return group;
    }

    public void setGroupId(int id)
    {
        group.setGroupId(id);
    }
}
