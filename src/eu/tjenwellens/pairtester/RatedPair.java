/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.tjenwellens.pairtester;

/**
 *
 * @author Tjen
 */
public class RatedPair extends Pair
{
    private int corrects, wrongs;
    private boolean lastTryCorrect;

    public RatedPair(String key, String value)
    {
        super(key, value);
    }

    public int getCorrects()
    {
        return corrects;
    }

    public int getWrongs()
    {
        return wrongs;
    }

    public boolean getLastTryCorrect()
    {
        return lastTryCorrect;
    }

    public void correct()
    {
        corrects++;
        lastTryCorrect = true;
    }

    public void wrong()
    {
        wrongs++;
        lastTryCorrect = false;
    }
}
