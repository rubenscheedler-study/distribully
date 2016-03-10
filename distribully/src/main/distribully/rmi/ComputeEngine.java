package distribully.rmi;

public class ComputeEngine {
	public <T> T executeTask(Task<T> t) {
		return t.execute();
	}
}
