package eu.tjenwellens.pairtester.model;

/**
 *
 * @author Tjen
 */
public interface MainModel
{
    boolean isListSelected();

    String getKey();

    String getValue();

    int getProgress();

    boolean correct();

    boolean wrong();

    boolean skip();

    boolean check();

    void resetScore();

    boolean start();

    int getScore();
}
