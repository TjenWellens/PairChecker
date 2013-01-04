package eu.tjenwellens.pairtester.pairs;

import eu.tjenwellens.pairtester.groups.Group;

/**
 *
 * @author Tjen
 */
public class GroupPairFactory extends PairFactory
{
    public static GroupPair loadGroupPair(int id, Group group, String key, String value, int corrects, int wrongs, int last_try)
    {
        return new SimpleGroupPair(id, group, key, value, corrects, wrongs, last_try);
    }

    public static GroupPair createGroupPair(int id, Group group, String key, String value)
    {
        return new SimpleGroupPair(id, group, key, value);
    }

    protected static class SimpleGroupPair extends SimpleRatedPair implements GroupPair
    {
        private Group group;

        public SimpleGroupPair(int id, Group group, String key, String value)
        {
            super(id, key, value);
            this.group = group;
        }

        public SimpleGroupPair(int id, Group group, String key, String value, int corrects, int wrongs, int lastTry)
        {
            super(id, key, value, corrects, wrongs, lastTry);
            this.group = group;
        }

        public Group getGroup()
        {
            return group;
        }
    }
}
