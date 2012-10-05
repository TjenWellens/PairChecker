package eu.tjenwellens.pairtester;

import android.content.Context;

/**
 *
 * @author Tjen
 */
public class PairFactory
{
    public static RatedPairI loadPair(Context context, int db_id, String db_key, String db_value, int db_corrects, int db_wrongs, boolean db_last_try_correct)
    {
        return new RatedPair(db_id, db_key, db_value, db_corrects, db_wrongs, db_last_try_correct);
    }

    public static RatedPairI createPair(Context context, int db_id, String db_key, String db_value)
    {
        return new RatedPair(db_id, db_key, db_value);
    }

    private static class RatedPair extends Pair implements RatedPairI
    {
        private int corrects, wrongs;
        private boolean lastTryCorrect;

        public RatedPair(int id, String key, String value)
        {
            super(id, key, value);
        }

        public RatedPair(int id, String key, String value, int corrects, int wrongs, boolean lastTryCorrect)
        {
            super(id, key, value);
            this.corrects = corrects;
            this.wrongs = wrongs;
            this.lastTryCorrect = lastTryCorrect;
        }

        public int getCorrects()
        {
            return corrects;
        }

        public int getWrongs()
        {
            return wrongs;
        }

        public boolean getLastTryCorrect()
        {
            return lastTryCorrect;
        }

        public void correct()
        {
            corrects++;
            lastTryCorrect = true;
        }

        public void wrong()
        {
            wrongs++;
            lastTryCorrect = false;
        }

        public void clearScore()
        {
            corrects = 0;
            wrongs = 0;
            lastTryCorrect = false;
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
