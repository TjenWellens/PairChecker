package eu.tjenwellens.pairtester.model;

import eu.tjenwellens.pairtester.database.GroupDatabase;
import eu.tjenwellens.pairtester.groups.DatabasePairSet;
import eu.tjenwellens.pairtester.groups.Group;
import eu.tjenwellens.pairtester.groups.GroupSet;
import eu.tjenwellens.pairtester.pairs.DatabasePair;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author Tjen
 */
public class PairTesterModel
        //        extends Model
        implements GroupDatabase, StartModel, ListsModel, MainModel, OverviewModel
{
    private Set<Group> groups;
    private GroupSet currentGroup;
    private GroupDatabase database;

    public PairTesterModel(GroupDatabase database)
    {
        this.database = database;
    }

    private void loadGroups()
    {
        this.groups = database.getGroups();
    }

    public boolean check()
    {
        return currentGroup.check();
    }

    public boolean start()
    {
        return currentGroup.start();
    }

    public boolean correct()
    {
        return currentGroup.correct();
    }

    public boolean wrong()
    {
        return currentGroup.wrong();
    }

    public boolean skip()
    {
        return currentGroup.skip();
    }

    public boolean isListSelected()
    {
        return currentGroup != null;
    }

    public boolean setCurrentGroup(int groupId)
    {
        if (groups == null)
        {
            loadGroups();
        }
        for (Group group : groups)
        {
            if (group.getGroupId() == groupId)
            {
                currentGroup = new DatabasePairSet(group, this);
                return true;
            }
        }
        return false;
    }

    public Set<Group> getGroups()
    {
        if (groups == null)
        {
            loadGroups();
        }
        return new LinkedHashSet<Group>(groups);
    }

    public boolean createPair(DatabasePair pair)
    {
        return database.createPair(pair);
    }

    public boolean createPairs(Set<DatabasePair> pairs)
    {
        return database.createPairs(pairs);
    }

    public Set<DatabasePair> getPairs(Group group)
    {
        return database.getPairs(group);
    }

    public boolean removeGroup(Group group)
    {
        if (groups == null)
        {
            loadGroups();
        }
        if (database.removeGroup(group))
        {
            groups.remove(group);
            return true;
        } else
        {
            return false;
        }
    }

    public boolean updatePair(DatabasePair pair)
    {
        return database.updatePair(pair);
    }

    public boolean removePair(DatabasePair pair)
    {
        return database.removePair(pair);
    }

    public boolean createGroup(Group group)
    {
        if (groups == null)
        {
            loadGroups();
        }
        if (database.createGroup(group))
        {
            groups.add(group);
            return true;
        } else
        {
            return false;
        }
    }

    public String getKey()
    {
        return currentGroup.getKey();
    }

    public String getValue()
    {
        return currentGroup.getValue();
    }

    public int getProgress()
    {
        return currentGroup.getProgress();
    }

    public void resetScore()
    {
        currentGroup.resetScore();
    }
}
