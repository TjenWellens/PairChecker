package eu.tjenwellens.observer;

/**
 *
 * @author Tjen
 */
public interface Observable
{
    boolean register(Observer observer);

    boolean remove(Observer observer);
}
