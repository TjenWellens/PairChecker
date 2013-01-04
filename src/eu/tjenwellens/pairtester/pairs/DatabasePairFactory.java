package eu.tjenwellens.pairtester.pairs;

import eu.tjenwellens.pairtester.database.GroupDatabase;
import eu.tjenwellens.pairtester.database.PairDatabase;
import eu.tjenwellens.pairtester.groups.Group;

/**
 *
 * @author Tjen
 */
public class DatabasePairFactory extends GroupPairFactory
{
    public static DatabasePair loadDatabasePair(PairDatabase database, int id, Group group, String key, String value, int corrects, int wrongs, int last_try)
    {
        return new SimpleDatabasePair(database, id, group, key, value, corrects, wrongs, last_try);
    }

    public static DatabasePair createDatabasePair(GroupDatabase database, Group group, String key, String value)
    {
        SimpleDatabasePair pair = new SimpleDatabasePair(database, -1, group, key, value);
        if (database.createPair(pair))
        {
            return pair;
        } else
        {
            return null;
        }
    }

    private static class SimpleDatabasePair extends SimpleGroupPair implements DatabasePair
    {
        private PairDatabase database;

        public SimpleDatabasePair(PairDatabase database, int id, Group group, String key, String value)
        {
            super(id, group, key, value);
            this.database = database;
        }

        public SimpleDatabasePair(PairDatabase database, int id, Group group, String key, String value, int corrects, int wrongs, int lastTry)
        {
            super(id, group, key, value, corrects, wrongs, lastTry);
            this.database = database;
        }

        public void setDatabase(PairDatabase database)
        {
            this.database = database;
        }

        @Override
        public boolean correct()
        {
            super.correct();
            update();
            return true;
        }

        @Override
        public boolean wrong()
        {
            super.wrong();
            update();
            return true;
        }

        @Override
        public void resetScore()
        {
            super.resetScore();
            update();
        }

        @Override
        public boolean skip()
        {
            super.skip();
            update();
            return true;
        }

        @Override
        public void setValue(String value)
        {
            super.setValue(value);
            update();
        }

        public void update()
        {
            if (database != null)
            {
                database.updatePair(this);
            }
        }

        @Override
        public void setPairId(int id)
        {
            super.setId(id);
        }

        public boolean remove()
        {
            if (database != null)
            {
                return database.removePair(this);
            }
            return false;
        }
    }
}
