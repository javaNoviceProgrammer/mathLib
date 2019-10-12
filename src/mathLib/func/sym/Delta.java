package mathLib.func.sym;

/**
 * f(x) = delta(x) : Dirac delta function
 *  + operator overloading supported
 *
 * @author meisam
 *
 */

public class Delta extends Function {


	public Delta() {
		compositeFunc = null ;
	}

	public Delta(Function f) {
		compositeFunc = f ;
	}

	@Override
	public double getValue(double x) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Function diff() {
		return null;
	}

}
