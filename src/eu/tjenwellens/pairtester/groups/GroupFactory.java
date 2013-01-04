package eu.tjenwellens.pairtester.groups;

import eu.tjenwellens.pairtester.database.GroupDatabase;

/**
 *
 * @author Tjen
 */
public class GroupFactory
{
    public static Group createGroup(GroupDatabase database, String groupName)
    {
        Group group = new SimpleGroup(-1, groupName);
        if (database.createGroup(group))
        {
            return group;
        } else
        {
            return null;
        }
    }

    public static Group loadGroup(int groupId, String groupName)
    {
        return new SimpleGroup(groupId, groupName);
    }

    private static class SimpleGroup implements Group
    {
        private int groupId;
        private String name;
        private int size;
        private int corrects;
        private int wrongs;
        private int skips;

        public SimpleGroup(int id, String name)
        {
            this.groupId = id;
            this.name = name;
            this.size = -1;
            this.corrects = -1;
            this.wrongs = -1;
            this.skips = -1;
        }

        public SimpleGroup(int id, String name, int size, int corrects, int wrongs, int skips)
        {
            this.groupId = id;
            this.name = name;
            this.size = size;
            this.corrects = corrects;
            this.wrongs = wrongs;
            this.skips = skips;
        }

        public int getGroupId()
        {
            return groupId;
        }

        public String getName()
        {
            return name;
        }

        public int getSize()
        {
            return size;
        }

        public int getCorrects()
        {
            return corrects;
        }

        public int getWrongs()
        {
            return wrongs;
        }

        public int getSkips()
        {
            return skips;
        }

        public int getScore()
        {
            return corrects - wrongs;
        }

        public int getProgress()
        {
            return -1;
        }

        public void setGroupId(int id)
        {
            this.groupId = id;
        }
    }
}
