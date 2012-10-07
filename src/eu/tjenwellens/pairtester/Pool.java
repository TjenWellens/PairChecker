package eu.tjenwellens.pairtester;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Tjen
 */
public class Pool
{
    private List<Integer> fullPool;
    private List<Integer> pool;
    private Random random = new Random(System.currentTimeMillis());
    private Integer currentIndex;

    public Pool(List<RatedPairI> pairs)
    {
        this.fullPool = initPool(pairs);
    }

    public int getRandomIndex()
    {
        if (pool == null || pool.isEmpty())
        {
            pool = new LinkedList<Integer>(fullPool);
        }
        currentIndex = pool.get(random.nextInt(pool.size()));
        return currentIndex;
    }

    public void correct()
    {
        pool.remove(currentIndex);
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

    private List<Integer> initPool(List<RatedPairI> pairs)
    {
        List<Integer> ret = new LinkedList<Integer>();
        for (int i = 0; i < pairs.size(); i++)
        {
            ret.add(i);
        }
        return ret;
    }
}
