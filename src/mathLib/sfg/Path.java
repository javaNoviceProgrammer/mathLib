package mathLib.sfg;

import mathLib.numbers.Complex;

public class Path {

	private Complex gain;
	private String path;

	public Path(Complex gain, String path){
		this.gain = gain;
		this.path = path;
	}

	public Complex getGain() {
		return gain;
	}

	public String getPath() {
		return path;
	}

	public void setGain(Complex gain) {
		this.gain = gain;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
