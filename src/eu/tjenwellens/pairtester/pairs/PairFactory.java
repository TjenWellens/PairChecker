package eu.tjenwellens.pairtester.pairs;

import android.content.Context;

/**
 *
 * @author Tjen
 */
public class PairFactory
{
    public static RatedPair loadPair(Context context, int id, String key, String value, int corrects, int wrongs, int last_try)
    {
        return new SimpleRatedPair(id, key, value, corrects, wrongs, last_try);
    }

    public static RatedPair createPair(Context context, int id, String key, String value)
    {
        return new SimpleRatedPair(id, key, value);
    }

    protected static class SimplePair implements Pair
    {
        private int id;
        private String key, value;

        public SimplePair(int id, String key, String value)
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

        public int getPairId()
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

        protected void setId(int id)
        {
            this.id = id;
        }

        public void setKey(String key)
        {
            this.key = key;
        }
    }

    protected static class SimpleRatedPair extends SimplePair implements RatedPair
    {
        private int corrects, wrongs, skips;

        public SimpleRatedPair(int id, String key, String value)
        {
            super(id, key, value);
        }

        public SimpleRatedPair(int id, String key, String value, int corrects, int wrongs, int skips)
        {
            super(id, key, value);
            this.corrects = corrects;
            this.wrongs = wrongs;
            this.skips = skips;
        }

        @Override
        public String toString()
        {
            return super.toString() + ";" + corrects + ";" + wrongs + ";" + skips;
        }

        public int getCorrects()
        {
            return corrects;
        }

        public int getWrongs()
        {
            return wrongs;
        }

        public boolean correct()
        {
            corrects++;
            return true;
        }

        public boolean wrong()
        {
            wrongs++;
            return true;
        }

        public void resetScore()
        {
            corrects = 0;
            wrongs = 0;
        }

        public boolean skip()
        {
            skips++;
            return true;
        }

        public int getSkips()
        {
            return skips;
        }
    }
}
