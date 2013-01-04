package eu.tjenwellens.pairtester.old;

import eu.tjenwellens.pairtester.pairs.RatedPair;
import eu.tjenwellens.pairtester.Pool;
import eu.tjenwellens.pairtester.RandomPoolCorrectOnly;
import eu.tjenwellens.pairtester.state.State;
import eu.tjenwellens.pairtester.state.StateType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Tjen
 */
public class PairSet implements PairSetI
{
    /* contains all the key-value pairs */
    private List<RatedPair> pairs;
    private RatedPair currentPair;
    // randomize
    private Pool pool;
    //
    private State state;
    private RatedPair previousPair;

    public PairSet(Collection<RatedPair> pairs)
    {
        this.pairs = new ArrayList<RatedPair>(pairs);
        this.pool = new RandomPoolCorrectOnly(pairs);
        this.state = StateType.NEW;
    }

    public boolean correct()
    {
        if (state.correct())
        {
            currentPair.correct();
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
            currentPair.wrong();
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
            currentPair.skip();
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

    private void next()
    {
        previousPair = currentPair;
        currentPair = getNextPair();
        state = StateType.UNCHECKED;
    }

    private RatedPair getNextPair()
    {
        return pairs.get(pool.getNextIndex());
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

    public List<RatedPair> getPairs()
    {
        return pairs;
    }

    public String getCurrentKey()
    {
        return currentPair.getKey();
    }

    public String getCurrentValue()
    {
        return currentPair.getValue();
    }

    public RatedPair getPreviousPair()
    {
        return previousPair;
    }

    public int getCurrentSize()
    {
        return pool.getSize();
    }

    public int getOriginalSize()
    {
        return pool.getOriginalSize();
    }
}
