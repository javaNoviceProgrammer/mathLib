package mathLib.sequence;

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

	public int getStart() {
		return this.start ;
	}

	public int getEnd() {
		return this.end ;
	}

	public double evaluate(int start, int end) {
		this.start = start ;
		this.end = end ;

		if(start == end)
			return func.value(start) ;
		double result = 0.0 ;
		for(int i=start; i<= end; i++){
			result += func.value(i) ;
		}
		return result ;
	}

	// for test
	public static void main(String[] args) {
		Summation sum = new Summation(k -> (k/2.0)*(k/2.0)) ;
		System.out.println(sum.evaluate(1, 100));
	}

}
