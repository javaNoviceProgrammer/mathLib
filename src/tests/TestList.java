package tests;

import java.util.ArrayList;
import java.util.List;

import mathLib.util.ArrayUtils;

public class TestList {
	public static void main(String[] args) {
		List<Double> list1 = new ArrayList<>() ;
		list1.add(1.2) ;
		list1.add(1.2) ;
		list1.add(2.2) ;
		list1.add(1.2) ;
		list1.add(2.2) ;
		System.out.println(list1);

		// commons of two lists
		List<Double> list2 = new ArrayList<>() ;
		list2.add(2.2) ;
		list2.add(-1.2) ;
		list2.add(1.2) ;
		list2.add(2.2) ;
		list2.add(1.2) ;
		list2.add(2.2) ;
		list2.add(1.2) ;
		System.out.println(list2);

//		/* find commons in two lists */
//		// step1
//		List<Double> list3 = new ArrayList<>(list1) ;
//		list3.retainAll(list2) ;
//		System.out.println(list3);
//
//		List<Double> list4 = new ArrayList<>(list2) ;
//		list4.retainAll(list3) ;
//		System.out.println(list4);
//
////		Set<Double> list5 = new HashSet<Double>(list3) ;
////		System.out.println(list5);
////		for(double x : list5)
////			list3.remove(x) ;
////		System.out.println(list3);
////
////		Set<Double> list6 = new HashSet<Double>(list4) ;
////		System.out.println(list6);
////		for(double x : list6)
////			list4.remove(x) ;
////		System.out.println(list4);
//
//		List<Double> list7 = new ArrayList<>(list3) ;
//
//		List<Double> commons = new ArrayList<>() ;
//		for(double x : list7) {
//			if(list3.remove(x) && list4.remove(x))
//				commons.add(x) ;
//		}
//
//		System.out.println(commons);

		System.out.println(ArrayUtils.getCommonElements(list1, list2));
	}

}
