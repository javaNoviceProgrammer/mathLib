package mathLib.numbers.discrete;

public class Summation {

	SumFunction func ;
	int start, end ;

	public Summation(SumFunction func) {
		this.func = func ;
	}

	public void setStart(int start) {
		this.start = start ;
	}

	public void setEnd(int end) {
		this.end = end ;
	}

	public double evaluate(int start, int end) {
		this.start = start ;
		this.end = end ;
		double result = 0.0 ;
		for(int i=start; i<= end; i++){
			result += func.value(i) ;
		}
		return result ;
	}

	// for test
	public static void main(String[] args) {
		Summation sum = new Summation(k -> k*k*k) ;
		System.out.println(sum.evaluate(1, 10));
	}

}
