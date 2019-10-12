package mathLib.func.sym;

/**
 * Constant function: FC(x) = c0
 * assignment to primitive supported: FC fc = 2.3
 *
 * @author meisam
 *
 */

public class FC extends Function {

	private double xval ;

	public FC(double x) {
		this.xval = x ;
	}

	@Override
	public double getValue(double x) {
		return xval ;
	}

	public static FC valueOf(double v) {
		return new FC(v) ;
	}

	@Override
	public String toString() {
		return xval+"" ;
	}

	@Override
	public Function diff() {
		return new FC(0.0);
	}

}
