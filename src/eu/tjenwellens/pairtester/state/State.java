package eu.tjenwellens.pairtester.state;

import eu.tjenwellens.pairtester.RatedPairI;

/**
 *
 * @author Tjen
 */
public interface State
{
    boolean correct(RatedPairI pair);

    boolean wrong(RatedPairI pair);

    boolean skip(RatedPairI pair);

    boolean check();

    boolean start();
}
