package mathLib.fourier;

import mathLib.fourier.core.FFT;
import mathLib.fourier.intf.InverseFourierFunction;

public class InverseFourierTransform {

	public InverseFourierFunction func;
	public double fs, ts, df;
	public int M, N;
	public double[] time, freq, funcReal, funcImag, ftReal, ftImag, ftAmp, ftPhase;

	public InverseFourierTransform(InverseFourierFunction func) {
		this.func = func;
	}

	public void setFunction(InverseFourierFunction func) {
		this.func = func;
	}

	// time resolution and frequency resolution
	public void setTimeResAndFreqRes(double ts, double df) {
		this.ts = ts;
		this.fs = 1.0 / ts;
		double a = Math.floor(fs / df);
		M = (int) (Math.log(a) / Math.log(2) + 1);
		N = (int) (Math.pow(2, M));
		this.df = fs / N;
	}

	// time duration and number of samples
	public void setTimeDurationAndNumSamples(double T, int N) {
		M = (int) (Math.log(N) / Math.log(2) + 1);
		this.N = (int) (Math.pow(2, M));
		this.ts = (double) (T / N);
		this.fs = 1.0 / ts;
		this.df = fs / this.N;
	}

	// Total frequency bandwidth and freq resolution
	public void setFreqResAndBandwidth(double fs, double df) {
		this.fs = fs;
		double a = Math.floor(fs / df);
		M = (int) (Math.log(a) / Math.log(2) + 1);
		N = (int) (Math.pow(2, M));
		this.df = fs / N;
		this.ts = 1.0 / fs;
	}

	// total frequency bandwidth and number of samples
	public void setFreqBandwidthAndSamples(double fs, double N) {
		this.fs = fs;
		M = (int) (Math.log(N) / Math.log(2) + 1);
		this.N = (int) (Math.pow(2, M));
		this.df = fs / this.N;
		this.ts = 1.0 / fs;
	}

	public void performIFT() {
		double[] realPart = new double[N];
		double[] imagPart = new double[N];
		time = new double[N / 2];
		funcReal = new double[N / 2];
		funcImag = new double[N / 2];
		freq = new double[N];
		ftReal = new double[N];
		ftImag = new double[N];
		ftAmp = new double[N];
		ftPhase = new double[N];
		for (int i = 0; i < N; i++) {
			realPart[i] = func.imagPart(i * df);
			imagPart[i] = func.realPart(i * df);
			freq[i] = i * df;
			ftReal[i] = func.realPart(i * df);
			ftImag[i] = func.imagPart(i * df);
			ftAmp[i] = Math.sqrt(ftReal[i] * ftReal[i] + ftImag[i] * ftImag[i]);
			ftPhase[i] = Math.atan(ftImag[i] / ftReal[i]);

		}
		FFT transform = new FFT(N);
		transform.fft(realPart, imagPart);
		for (int i = 0; i < N / 2; i++) {
			time[i] = i * ts;
			funcReal[i] = df * (2 * imagPart[i] - ftReal[0]);
			funcImag[i] = 0;
		}
	}

}
