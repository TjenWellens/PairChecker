package eu.tjenwellens.pairtester;

import eu.tjenwellens.pairtester.state.State;
import eu.tjenwellens.pairtester.state.StateType;
import java.util.List;

/**
 *
 * @author Tjen
 */
public class PairSet implements PairSetI
{
    /* contains all the key-value pairs */
    private List<RatedPairI> pairs;
    private RatedPairI currentPair;
    // randomize
    private Pool pool;
    //
    private State state;
    private RatedPairI previousPair;

    public PairSet(List<RatedPairI> pairs)
    {
        this.pairs = pairs;
        this.pool = new Pool(pairs);
        this.state = StateType.NEW;
    }

    public boolean correct()
    {
        if (state.correct(currentPair))
        {
            pool.correct();
            next();
            return true;
        }
        return false;
    }

    public boolean wrong()
    {
        if (state.wrong(currentPair))
        {
            pool.wrong();
            next();
            return true;
        }
        return false;
    }

    public boolean skip()
    {
        if (state.skip(currentPair))
        {
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

    private RatedPairI getNextPair()
    {
        return pairs.get(pool.getRandomIndex());
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
        for (RatedPairI pair : pairs)
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
        for (RatedPairI pair : pairs)
        {
            pair.clearScore();
        }
    }

    public List<RatedPairI> getPairs()
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

    public RatedPairI getPreviousPair()
    {
        return previousPair;
    }

    public long getStartTime()
    {
        return pool.getStartTime();
    }
}
