package mathLib.integral.intf;

public interface IntegralDomain3D {
	double getVar1Min() ;
	double getVar1Max() ;
	double getVar2Min(double var1) ;
	double getVar2Max(double var1) ;
	double getVar3Min(double var1, double var2) ;
	double getVar3Max(double var1, double var2) ;
}
