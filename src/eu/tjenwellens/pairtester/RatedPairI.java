package eu.tjenwellens.pairtester;

/**
 *
 * @author Tjen
 */
public interface RatedPairI extends PairI
{
    int getCorrects();

    int getWrongs();

    int getLastTry();

    void correct();

    void wrong();

    public void clearScore();

    public void skip();
}
