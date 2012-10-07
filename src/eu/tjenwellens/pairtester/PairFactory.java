package eu.tjenwellens.pairtester;

import android.content.Context;

/**
 *
 * @author Tjen
 */
public class PairFactory
{
    public static RatedPairI loadPair(Context context, int id, String key, String value, int corrects, int wrongs, int last_try)
    {
        return new RatedPair(id, key, value, corrects, wrongs, last_try);
    }

    public static RatedPairI createPair(Context context, int id, String key, String value)
    {
        return new RatedPair(id, key, value);
    }

    private static class RatedPair extends Pair implements RatedPairI
    {
        private int corrects, wrongs;
        private int lastTry;

        public RatedPair(int id, String key, String value)
        {
            super(id, key, value);
        }

        public RatedPair(int id, String key, String value, int corrects, int wrongs, int lastTry)
        {
            super(id, key, value);
            this.corrects = corrects;
            this.wrongs = wrongs;
            this.lastTry = lastTry;
        }

        @Override
        public String toString()
        {
            return super.toString() + ";" + corrects + ";" + wrongs + ";" + lastTry;
        }

        public int getCorrects()
        {
            return corrects;
        }

        public int getWrongs()
        {
            return wrongs;
        }

        public int getLastTry()
        {
            return lastTry;
        }

        public void correct()
        {
            corrects++;
            lastTry = 1;
        }

        public void wrong()
        {
            wrongs++;
            lastTry = -1;
        }

        public void clearScore()
        {
            corrects = 0;
            wrongs = 0;
            lastTry = 0;
        }

        public void skip()
        {
            lastTry = 0;
        }
    }

    private static class Pair implements PairI
    {
        private int id;
        private String key, value;

        public Pair(int id, String key, String value)
        {
            this.id = id;
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString()
        {
            return "" + key + "=" + value;
        }

        public int getId()
        {
            return id;
        }

        public String getKey()
        {
            return key;
        }

        public String getValue()
        {
            return value;
        }

        public void setValue(String value)
        {
            this.value = value;
        }
    }
}
