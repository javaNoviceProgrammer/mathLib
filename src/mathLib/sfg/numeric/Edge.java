package mathLib.sfg.numeric;

import mathLib.numbers.Complex;

public class Edge {

	private int to;
	private Complex gain;

	public Edge(int to, Complex gain){
		this.to = to;
		this.gain = gain;
	}

	public int getTo() {
		return to;
	}

	public Complex getGain() {
		return gain;
	}

	public void setGain(Complex gain) {
		this.gain = gain;
	}

	public void setTo(int to) {
		this.to = to;
	}
}
