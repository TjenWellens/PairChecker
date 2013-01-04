package observer;

import eu.tjenwellens.observer.Observable;
import eu.tjenwellens.observer.Observer;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;

/**
 *
 * @author Tjen
 */
public class Model implements Observable
{
    private final Collection<Observer> observers = new LinkedHashSet<Observer>();

    public boolean register(Observer observer)
    {
        synchronized (this.observers)
        {
            return observers.add(observer);
        }
    }

    public boolean remove(Observer observer)
    {
        synchronized (this.observers)
        {
            return observers.remove(observer);
        }
    }

    protected final void updateAll()
    {
        Collection<Observer> observersCopy;
        synchronized (this.observers)
        {
            observersCopy = new LinkedList<Observer>(this.observers);
        }
        for (Observer observer : observersCopy)
        {
            observer.update();
        }
    }
}
