package eu.tjenwellens.pairtester.state;

/**
 *
 * @author Tjen
 */
public interface State
{
    boolean correct();

    boolean wrong();

    boolean skip();

    boolean check();

    boolean start();
}
