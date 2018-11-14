package mathLib.fem.util.container;

import java.util.ArrayList;

public class ObjIndex extends ArrayList<Integer> {
	private static final long serialVersionUID = 6760999388784536061L;

	/**
	 * index starts from 0,1,2,3...
	 * 
	 * @param is
	 */
	public ObjIndex(Integer ...is) {
		for(Integer i : is)
			this.add(i);
	}

}
