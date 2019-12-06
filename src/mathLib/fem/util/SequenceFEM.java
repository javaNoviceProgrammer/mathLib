package mathLib.fem.util;

/**
 * Generate a unique integer sequence starting from 0 in the entire life cycle
 * 
 *
 */
public class SequenceFEM {
	private long index = -1;
	private static SequenceFEM ins = new SequenceFEM();
	
	private SequenceFEM() {
	}
	
	public static SequenceFEM getInstance() {
		return ins;
	}
	
	public long nextSeq() {
		synchronized(ins) {
			return ++index;
		}
	}
}
