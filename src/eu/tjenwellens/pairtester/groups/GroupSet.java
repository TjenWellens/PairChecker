package eu.tjenwellens.pairtester.groups;

import eu.tjenwellens.pairtester.pairs.DatabasePair;

/**
 *
 * @author Tjen
 */
public interface GroupSet extends Group, DatabasePair
{
    // actions
    boolean check();

    boolean start();

    // edit
    boolean addPair(DatabasePair pair);

    boolean deletePair(DatabasePair pair);

    void deleteCurrentPair();

    // data
    int getScore();

    void resetScore();

    int getCurrentSize();

    int getSize();
}
