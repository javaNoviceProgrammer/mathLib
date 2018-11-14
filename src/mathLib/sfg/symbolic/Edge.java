package mathLib.sfg.symbolic;

import mathLib.func.symbolic.intf.MathFunc;

public class Edge {

	private int to;
	private MathFunc gain;

	public Edge(int to, MathFunc gain){
		this.to = to;
		this.gain = gain;
	}

	public int getTo() {
		return to;
	}

	public MathFunc getGain() {
		return gain;
	}

	public void setGain(MathFunc gain) {
		this.gain = gain;
	}

	public void setTo(int to) {
		this.to = to;
	}
}
