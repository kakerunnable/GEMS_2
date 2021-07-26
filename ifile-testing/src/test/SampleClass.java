package test;

public class SampleClass {

	public void onCreate1() {
		System.out.println("tset10");
		System.out.println("tset20");
		System.out.println("tset300");
		System.out.println("tset4000");
	}

	public void onCreate2(String n, Integer x) {
		System.out.println("tset20");
		System.out.println(n);
		System.out.println(x);
		System.out.println("tset4000");
	}

	public int exit() {
		return other(0);
	}

	public int other(int n) {
		int i = n + 1;
		System.out.println("tset20");
		System.out.println("tset300");
		System.out.println("tset4000");
		return n;
	}
}
