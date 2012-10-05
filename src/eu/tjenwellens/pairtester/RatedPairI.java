package eu.tjenwellens.pairtester;

/**
 *
 * @author Tjen
 */
public interface RatedPairI extends PairI
{
    int getCorrects();

    int getWrongs();

    boolean getLastTryCorrect();

    void correct();

    void wrong();

    public void clearScore();
}
