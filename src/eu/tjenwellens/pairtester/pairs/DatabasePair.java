package eu.tjenwellens.pairtester.pairs;

import eu.tjenwellens.pairtester.database.PairDatabase;

/**
 *
 * @author Tjen
 */
public interface DatabasePair extends GroupPair
{
    void setDatabase(PairDatabase database);

    void update();

    void setPairId(int id);

    boolean remove();
}
