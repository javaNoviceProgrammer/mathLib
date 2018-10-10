package mathLib.util;

import java.util.ArrayList;

import flanagan.io.FileInput;
import mathLib.plot.MatlabChart;

public class WavelengthTriggerConversion {
	
	double[] time, light, trigger, lambdaNm, lightConverted ;
	ArrayList<Double> timeList, lightList, triggerList ;
	
	int startIndex, endIndex ;
	double startLambdaNm, endLambdaNm, sweepStepNm, sweepRateNmPerSec ;
	
	String triggerFile = "C:\\Users\\ilirm\\Desktop\\trigger.txt" ;
	String lightFile = "C:\\Users\\ilirm\\Desktop\\photodiode.txt" ;
	
	public WavelengthTriggerConversion() {
		timeList = new ArrayList<>() ;
		lightList = new ArrayList<>() ;
		triggerList = new ArrayList<>() ;
	}
	
	public void setTriggerFilePath(String triggerFile) {
		this.triggerFile = triggerFile ;
	}
	
	public void setLightFilePath(String lightFile) {
		this.lightFile = lightFile ;
	}
	
	public void setStartSweepNm(double startLambdaNm) {
		this.startLambdaNm = startLambdaNm ;
	}
	
	public void setEndSweepNm(double endLambdaNm) {
		this.endLambdaNm = endLambdaNm ;
	}
	
	public void setSweepStepNm(double sweepStepNm) {
		this.sweepStepNm = sweepStepNm ;
	}
	
	public void setSweepSpeed(double sweepRateNmPerSec) {
		this.sweepRateNmPerSec = sweepRateNmPerSec ;
	}
	
	public void readData() {
		FileInput fi = new FileInput(triggerFile) ;
		int k = fi.numberOfLines() ;
		fi.readLine() ;
		for(int i=1; i<k; i++) {
			timeList.add(fi.readDouble()) ;
			triggerList.add(fi.readDouble()) ;
		}
		
		fi = new FileInput(lightFile) ;
		k = fi.numberOfLines() ;
		fi.readLine() ;
		for(int i=1; i<k; i++) {
			fi.readDouble() ;
			lightList.add(fi.readDouble()) ;
		}
		
		fi.close();
		System.gc();
		
		time = new double[timeList.size()] ;
		light = new double[lightList.size()] ;
		trigger = new double[triggerList.size()] ;
		
		for(int i=0; i<time.length; i++) {
			time[i] = timeList.get(i) ;
			light[i] = lightList.get(i) ;
			trigger[i] = triggerList.get(i) ;
		}
	}
	
	public void findTriggerInterval() {
		int m = time.length ;
		for(int k=0; k<m; k++) {
			if(trigger[k] > 2.5) {
				startIndex = k ;
				break ;
			}
		}
		
		for(int k=m-1; k>=0; k--) {
			if(trigger[k] > 2.5) {
				endIndex = k ;
				break ;
			}
		}
		
		System.out.println("start = " + time[startIndex]);
		System.out.println("end = " + time[endIndex]);
		
		ArrayList<Double> lambda = new ArrayList<>() ;
		for(int i=startIndex; i<endIndex+1; i++) {
			double a = startLambdaNm + (time[i] - time[startIndex]) * sweepRateNmPerSec ;
			lambda.add(a) ;
		}
		
		lightConverted = new double[endIndex-startIndex+1] ;
		lambdaNm = new double[lambda.size()] ;
		
		for(int i=0; i<lambda.size(); i++) {
			lambdaNm[i] = lambda.get(i) ;
			lightConverted[i] = light[i+startIndex] ;
		}
	
	}
	
	public void plot() {
		MatlabChart fig = new MatlabChart() ;
		fig.plot(lambdaNm, lightConverted);
		fig.RenderPlot();
		fig.run(true);
	}
	
	public static void main(String[] args) {
		WavelengthTriggerConversion wt = new WavelengthTriggerConversion() ;
		
		wt.setStartSweepNm(1519);
		wt.setEndSweepNm(1521);
//		wt.setSweepStepNm(1e-3);
		wt.setSweepSpeed(1);
		
		wt.readData();
		wt.findTriggerInterval();
		wt.plot();
	}

}
