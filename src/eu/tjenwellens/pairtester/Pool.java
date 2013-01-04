package eu.tjenwellens.pairtester;

/**
 *
 * @author Tjen
 */
public interface Pool
{
    int getNextIndex();

    boolean removeIndex(Integer index);

    void correct();

    void wrong();

    void check();

    void skip();

    void start();

    int getSize();

    int getOriginalSize();
}
