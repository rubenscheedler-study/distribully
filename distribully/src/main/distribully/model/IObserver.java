package distribully.model;

public interface IObserver {
	public void update(IObservable observable, Object changedObject);
}
