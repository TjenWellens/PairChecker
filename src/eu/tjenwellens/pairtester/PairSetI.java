package eu.tjenwellens.pairtester;

import java.util.List;

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
    
    List<RatedPairI> getPairs();
    
    String getCurrentKey();
    
    String getCurrentValue();
}
