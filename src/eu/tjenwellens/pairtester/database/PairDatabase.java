package eu.tjenwellens.pairtester.database;

import eu.tjenwellens.pairtester.pairs.DatabasePair;

/**
 *
 * @author Tjen
 */
public interface PairDatabase
{
    /*
     * Updates a pair entry in the database
     * Arguments: the pair to update
     * Returns: true when successful update, false when unsuccessful
     */
    boolean updatePair(DatabasePair pair);

    boolean removePair(DatabasePair pair);
}
