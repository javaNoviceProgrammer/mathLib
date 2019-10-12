package mathLib.optimize.pso;

// immutable
public class VectorND {

	int dim ;
	double[] x ;

	// vararg constructor
	VectorND(double... x) {
		this.x = x ;
		this.dim = x.length ;
	}

	public double at(int i) {
		return x[i] ;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("(") ;
		for(int i=0; i<dim-1; i++){
			sb.append(x[i]).append(",") ;
		}
		sb.append(x[dim-1]).append(")") ;
		return sb.toString() ;

		// or simply use
//		return Arrays.toString(x) ;
	}

	//********operator overloading capability **************

	public VectorND add(double v) {
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = this.x[i] + v ;
		return new VectorND(y) ;
	}

	public VectorND add(double... v) {
		// care about x.lenght == v.length
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = this.x[i] + v[i] ;
		return new VectorND(y) ;
	}

	public VectorND addRev(double v) {
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = v + this.x[i] ;
		return new VectorND(y) ;
	}

	public VectorND addRev(double... v) {
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = v[i] + this.x[i] ;
		return new VectorND(y) ;
	}

	public VectorND add(VectorND v) {
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = this.x[i] + v.x[i] ;
		return new VectorND(y) ;
	}

	public VectorND addRev(VectorND v) {
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = v.x[i] + this.x[i] ;
		return new VectorND(y) ;
	}

	public VectorND subtract(double v) {
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = this.x[i] - v ;
		return new VectorND(y) ;
	}

	public VectorND subtract(double... v) {
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = this.x[i] - v[i] ;
		return new VectorND(y) ;
	}

	public VectorND subtractRev(double v) {
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = v - this.x[i] ;
		return new VectorND(y) ;
	}

	public VectorND subtractRev(double... v) {
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = v[i] - this.x[i] ;
		return new VectorND(y) ;
	}

	public VectorND subtract(VectorND v) { // this - v
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = this.x[i] - v.x[i] ;
		return new VectorND(y) ;
	}

	public VectorND subtractRev(VectorND v) { // v - this
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = v.x[i] - this.x[i] ;
		return new VectorND(y) ;
	}

	public VectorND multiply(double v) { // this * v
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = this.x[i] * v ;
		return new VectorND(y) ;
	}

	public VectorND multiplyRev(double v) { // v * this
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = v * this.x[i] ;
		return new VectorND(y) ;
	}

	public VectorND divide(double v) { // this/v
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = this.x[i]/v ;
		return new VectorND(y) ;
	}

	public VectorND negate() { // -this
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = -this.x[i] ;
		return new VectorND(y) ;
	}

}