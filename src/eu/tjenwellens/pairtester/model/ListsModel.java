package eu.tjenwellens.pairtester.model;

import eu.tjenwellens.pairtester.database.GroupDatabase;

/**
 *
 * @author Tjen
 */
public interface ListsModel extends GroupDatabase
{
    boolean setCurrentGroup(int groupId);
}
