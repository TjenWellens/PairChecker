package eu.tjenwellens.pairtester.old;

import eu.tjenwellens.pairtester.pairs.RatedPair;
import java.util.Collection;

/**
 *
 * @author Tjen
 */
public interface PairSetI
{
    boolean correct();

    boolean wrong();

    boolean skip();

    boolean check();

    boolean start();

//    void load();
    int getScore();

    void resetScore();

//    void clearEntries();
    Collection<? extends RatedPair> getPairs();

    String getCurrentKey();

    String getCurrentValue();

    int getCurrentSize();

    int getOriginalSize();
}
