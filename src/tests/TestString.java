package tests;

public class TestString {
	
	public static void main(String[] args) {
		String delta = "1-L1-L2-L3+L2*L3+L1*L3" ;
		System.out.println(delta);
		String d1 = delta.replaceAll("L1", "0") ;
		System.out.println(d1);
	}

}
