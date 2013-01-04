package eu.tjenwellens.pairtester.pairs;

/**
 *
 * @author Tjen
 */
public interface Pair
{
    int getPairId();

    String getKey();

    String getValue();
    
    void setKey(String key);
    
    void setValue(String value);
}
