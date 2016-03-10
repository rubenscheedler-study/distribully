package distribully.rmi;

import java.io.Serializable;

public class TaskInvite implements Task<Boolean>, Serializable {

	private static final long serialVersionUID = 3201747976549469225L;

	@Override
	public Boolean execute() {
		System.out.println("invite task!");
		return true;
	}

}
