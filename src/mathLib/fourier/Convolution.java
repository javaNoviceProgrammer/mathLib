package mathLib.fourier;

import mathLib.fourier.core.FFT;
import mathLib.fourier.intf.ConvolutionFunctionPair;
import mathLib.utils.MathUtils;

public class Convolution {

	/**
	 * This class is for convolution of two REAL functions:  z(t) = x(t)*y(t)
	 */

	public ConvolutionFunctionPair funcs ;
	public double fs, ts, df ;
	public int M, N ;
	public double[] time, func1, func2, convResult ;

	public Convolution(
			ConvolutionFunctionPair funcs,
			double tPeriod,
			double ts
			){
		this.funcs = funcs ;
		this.ts = ts ;
		M = (int) (Math.log(tPeriod/ts)/Math.log(2) + 1) ;
		N = (int) (Math.pow(2, M)) ;
		this.fs = 1.0/ts ;
		this.df = fs/N ;
	}

	public void performConvolution(){
		double[] realFunc1 = MathUtils.Arrays.setZero(2*N) ; // N points zero padding at the end
		double[] imagFunc1 = MathUtils.Arrays.setZero(2*N) ; // N points zero padding at the end
		double[] realFunc2 = MathUtils.Arrays.setZero(2*N) ; // N points zero padding at the end
		double[] imagFunc2 = MathUtils.Arrays.setZero(2*N) ; // N points zero padding at the end
		time = new double[N] ;
		func1 = new double[N] ;
		func2 = new double[N] ;
		convResult = new double[N] ;
		for(int i=0; i<N; i++){
			realFunc1[i] = funcs.func1(i*ts) ;
			imagFunc1[i] = 0 ; // no imaginary part
			realFunc2[i] = funcs.func2(i*ts) ;
			imagFunc2[i] = 0 ; // no imaginary part
			time[i] = i*ts ;
			func1[i] = funcs.func1(i*ts) ;
			func2[i] = funcs.func2(i*ts) ;
		}
		FFT fft = new FFT(2*N) ;
		fft.fft(realFunc1, imagFunc1);
		fft.fft(realFunc2, imagFunc2);
		double[] realConv = new double[2*N] ;
		double[] imagConv = new double[2*N] ;
		for(int i=0; i<2*N; i++){
			realConv[i] = realFunc1[i]*realFunc2[i]-imagFunc1[i]*imagFunc2[i] ;
			imagConv[i] = realFunc1[i]*imagFunc2[i]+imagFunc1[i]*realFunc2[i] ;
		}
		fft.fft(imagConv, realConv);
		for(int i=0; i<N; i++){
			convResult[i] = realConv[i]/(2*N*fs) ; // don't forget to divide by 2*N*fs
		}
	}
}
