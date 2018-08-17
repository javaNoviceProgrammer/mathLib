package mathLib.fitting.lmse;

public interface LeastSquareFitter {
    
    public void setData(double[][] xvalues, double[] zvalues);
    public void setParameters(double[] parameters);
    public double[] getParameters();
    public double[] getUncertainty();
    public void fitData();
    public double calculateErrors();
}
