package eu.tjenwellens.pairtester.database;

import eu.tjenwellens.pairtester.groups.Group;
import eu.tjenwellens.pairtester.pairs.DatabasePair;
import java.util.Set;

/**
 *
 * @author Tjen
 */
public interface GroupDatabase extends PairDatabase
{
    /*
     * Returns a list of groups if found, 
     */
    Set<Group> getGroups();
    
    /*
     * Creates a pair entry in the database
     * Arguments: the pair to add tot the database 
     * Returns: true when successful added to the database, false when unsuccessful
     */
    boolean createPair(DatabasePair pair);
    
    /*
     * Creates multiple pairs at once
     * Arguments: the pairs to add tot the database 
     * Returns: true when at least one pair successful added to the database, false when all unsuccessful
     */
    boolean createPairs(Set<DatabasePair> pairs);

    /*
     * Retreives all pairs from a group from the database
     * Arguments: the group to retreive pairs from
     * Returns: a set of pairs when found, null when not found
     */
    Set<DatabasePair> getPairs(Group group);
    
    boolean removeGroup(Group group);

    boolean createGroup(Group group);
}
