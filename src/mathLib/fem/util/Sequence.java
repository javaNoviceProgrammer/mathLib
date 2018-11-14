package mathLib.fem.util;

/**
 * Generate a unique integer sequence starting from 0 in the entire life cycle
 * 
 *
 */
public class Sequence {
	private long index = -1;
	private static Sequence ins = new Sequence();
	
	private Sequence() {
	}
	
	public static Sequence getInstance() {
		return ins;
	}
	
	public long nextSeq() {
		synchronized(ins) {
			return ++index;
		}
	}
}
