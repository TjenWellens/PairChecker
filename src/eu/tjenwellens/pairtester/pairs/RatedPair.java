package eu.tjenwellens.pairtester.pairs;

/**
 *
 * @author Tjen
 */
public interface RatedPair extends Pair
{
    int getCorrects();

    int getWrongs();

    int getSkips();

    boolean correct();

    boolean wrong();

    boolean skip();

    void resetScore();
}
