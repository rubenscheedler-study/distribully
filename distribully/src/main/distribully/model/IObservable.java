package distribully.model;

import java.util.Observer;

public interface IObservable {
	public void addObserver(Observer observer);
	public void removeObserver(Observer observer);
	public void notifyObservers();
}
