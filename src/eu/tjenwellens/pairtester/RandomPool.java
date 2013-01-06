package eu.tjenwellens.pairtester;

import eu.tjenwellens.pairtester.pairs.RatedPair;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Tjen
 */
public abstract class RandomPool implements Pool
{
    private List<Integer> fullPool;
    private List<Integer> pool;
    private Random random = new Random(System.currentTimeMillis());
    private Integer currentIndex;

    public RandomPool(Collection<? extends RatedPair> pairs)
    {
        this.fullPool = initFullPool(pairs);
        this.pool = initPool(pairs);
    }

    public int getNextIndex()
    {
        if (pool == null || pool.isEmpty())
        {
            pool = new LinkedList<Integer>(fullPool);
        }
        if (pool == null || pool.isEmpty())
        {
            return 0;
        }
        currentIndex = pool.get(random.nextInt(pool.size()));
        return currentIndex;
    }

    public abstract void correct();

    public abstract void wrong();

    public abstract void check();

    public abstract void skip();

    public abstract void start();

    protected List<Integer> initFullPool(Collection<? extends RatedPair> pairs)
    {
        List<Integer> ret = new LinkedList<Integer>();
        for (int i = 0; i < pairs.size(); i++)
        {
            ret.add(i);
        }
        return ret;
    }
    protected List<Integer> initPool(Collection<? extends RatedPair> pairs)
    {
        return new LinkedList<Integer>(fullPool);
    }

    protected boolean removeCurrentIndex()
    {
        if (currentIndex != null)
        {
            return pool.remove(currentIndex);
        } else
        {
            return false;
        }
    }

    public int getSize()
    {
        return pool.size();
    }

    public int getOriginalSize()
    {
        return fullPool.size();
    }

    public boolean removeIndex(Integer index)
    {
        boolean result = fullPool.remove(index);
        pool.remove(index);
        return result;
    }
}
