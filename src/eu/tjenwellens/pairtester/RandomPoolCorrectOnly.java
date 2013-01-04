package eu.tjenwellens.pairtester;

import eu.tjenwellens.pairtester.pairs.RatedPair;
import java.util.Collection;

/**
 *
 * @author Tjen
 */
public class RandomPoolCorrectOnly extends RandomPool
{
    public RandomPoolCorrectOnly(Collection<? extends RatedPair> pairs)
    {
        super(pairs);
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
