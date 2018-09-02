package mathLib.util;

public class Timer {

	long startTime, endTime, elapseTime ;

	public void start() {
		startTime = System.currentTimeMillis() ;
	}

	public void stop() {
		endTime = System.currentTimeMillis() ;
		elapseTime = endTime - startTime ;
	}

	public void reset() {
		startTime = 0 ;
		endTime = 0 ;
		elapseTime = 0 ;
	}

	public void show() {
		System.out.println("Timer duration = " + elapseTime + " ms");
	}


	@Override
	public String toString() {
		return "Timer duration = " + elapseTime + " ms";
	}

	// for test
	public static void main(String[] args) {
		Timer timer = new Timer() ;
		timer.start();
		long N = 1000000 ;
		// summing 1 + 2 + ... + N
		double x = 0 ;
		for(long i=1; i<=N; i++) {
			x += i ;
		}
		System.out.println(x);
		timer.stop();
		timer.show();

		// using closed-form solution
		timer.reset();
		timer.start();
		double y = N*(N+1)/2.0 ;
		System.out.println(y);
		timer.stop();
		timer.show();
	}

}
