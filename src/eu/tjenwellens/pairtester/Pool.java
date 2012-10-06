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
        Integer ret = pool.get(random.nextInt(pool.size()));
        return ret;
    }

    public boolean correct(Integer index)
    {
        return pool.remove(index);
    }

    public boolean wrong(Integer index)
    {
        return true;
    }

    public boolean check(Integer index)
    {
        return true;
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
