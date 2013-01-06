package eu.tjenwellens.pairtester;

import eu.tjenwellens.pairtester.pairs.RatedPair;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Tjen
 */
public class RandomPoolCorrectOnlyLoadState extends RandomPool
{
    public RandomPoolCorrectOnlyLoadState(Collection<? extends RatedPair> pairs)
    {
        super(pairs);
    }

    @Override
    protected List<Integer> initPool(Collection<? extends RatedPair> pairs)
    {
        List<Integer> ret = new LinkedList<Integer>();
        int minimum = -1;
        int counter = 0;
        for (RatedPair ratedPair : pairs)
        {
            int total = ratedPair.getCorrects();
            if (minimum < 0)// 1st loop
            {
                // set start minimum
                minimum = total;
                // add
                ret.add(counter);
            } else if (total < minimum) // ! new minimum
            {
                // clear previously added
                ret.clear();
                minimum = total;
                ret.add(counter);
            } else if (total == minimum)
            {
                // add
                ret.add(counter);
            } else // total > max
            {
                // ignore
            }
            counter++;
        }
//        for (int i = 0; i < pairs.size(); i++)
//        {
//            ret.add(i);
//        }
        return ret;
    }

    public void correct()
    {
        removeCurrentIndex();
    }

    public void wrong()
    {
        // Do nothing
    }

    public void check()
    {
        // Do nothing
    }

    public void skip()
    {
        // Do nothing
    }

    public void start()
    {
        // Do nothing
    }
}
