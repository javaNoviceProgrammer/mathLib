package mathLib.arrays;


public class NdArray {

	int dim ;
	double[] x ;

	public NdArray(double... x) {
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
	}

	public double[] array() {
		return x ;
	}

	public int dim() {
		return dim ;
	}

	//********operator overloading capability **************

	public static NdArray valueOf(double[] v) { // assignment operator: NdArray d = new double[]{1,2}
		return new NdArray(v) ;
	}

	public NdArray add(double v) {
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = this.x[i] + v ;
		return new NdArray(y) ;
	}

	public NdArray add(double... v) {
		// care about x.lenght == v.length
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = this.x[i] + v[i] ;
		return new NdArray(y) ;
	}

	public NdArray addRev(double v) {
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = v + this.x[i] ;
		return new NdArray(y) ;
	}

	public NdArray addRev(double... v) {
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = v[i] + this.x[i] ;
		return new NdArray(y) ;
	}

	public NdArray add(NdArray v) {
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = this.x[i] + v.x[i] ;
		return new NdArray(y) ;
	}

	public NdArray addRev(NdArray v) {
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = v.x[i] + this.x[i] ;
		return new NdArray(y) ;
	}

	public NdArray subtract(double v) {
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = this.x[i] - v ;
		return new NdArray(y) ;
	}

	public NdArray subtract(double... v) {
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = this.x[i] - v[i] ;
		return new NdArray(y) ;
	}

	public NdArray subtractRev(double v) {
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = v - this.x[i] ;
		return new NdArray(y) ;
	}

	public NdArray subtractRev(double... v) {
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = v[i] - this.x[i] ;
		return new NdArray(y) ;
	}

	public NdArray subtract(NdArray v) { // this - v
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = this.x[i] - v.x[i] ;
		return new NdArray(y) ;
	}

	public NdArray subtractRev(NdArray v) { // v - this
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = v.x[i] - this.x[i] ;
		return new NdArray(y) ;
	}

	public NdArray multiply(double v) { // this * v
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = this.x[i] * v ;
		return new NdArray(y) ;
	}

	public NdArray multiplyRev(double v) { // v * this
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = v * this.x[i] ;
		return new NdArray(y) ;
	}

	public NdArray multiply(NdArray v) { // this * v
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = this.x[i] * v.x[i] ;
		return new NdArray(y) ;
	}

	public NdArray multiplyRev(NdArray v) { // v * this
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = v.x[i] * this.x[i] ;
		return new NdArray(y) ;
	}

	public NdArray divide(double v) { // this/v
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = this.x[i]/v ;
		return new NdArray(y) ;
	}

	public NdArray divide(NdArray v) { // this/v
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = this.x[i]/v.x[i] ;
		return new NdArray(y) ;
	}

	public NdArray divideRev(double v) {
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = v/this.x[i] ;
		return new NdArray(y) ;
	}

	public NdArray divideRev(NdArray v) {
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = v.x[i]/this.x[i] ;
		return new NdArray(y) ;
	}

	public NdArray negate() { // -this
		double[] y = new double[dim] ;
		for(int i=0; i<dim; i++)
			y[i] = -this.x[i] ;
		return new NdArray(y) ;
	}

}