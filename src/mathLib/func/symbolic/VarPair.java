package mathLib.func.symbolic;

/**
 * Used for construct objects of type <tt>Variable</tt>
 *
 */
public class VarPair {
	public VN vn = null;
	public String name = null;
	public double value = 0.0;

	public VarPair(String name, double value) {
		this.name = name;
		this.value = value;
	}
	
	public VarPair(VN name, double value) {
		vn = name;
		this.value = value;
	}
	
	public int getVarID() {
		if(vn != null) return vn.getID();
		else return VN.getID(name);
	}
}
