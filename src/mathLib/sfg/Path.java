package mathLib.sfg;

import edu.uta.futureye.function.intf.MathFunc;

public class Path {

	private MathFunc gain;
	private String path;

	public Path(MathFunc gain, String path){
		this.gain = gain;
		this.path = path;
	}

	public MathFunc getGain() {
		return gain;
	}

	public String getPath() {
		return path;
	}

	public void setGain(MathFunc gain) {
		this.gain = gain;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
