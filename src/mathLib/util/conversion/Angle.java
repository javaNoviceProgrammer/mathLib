package mathLib.util.conversion;

public class Angle {

	public static double toRadian(double degree) {
		return degree * Math.PI / 180;
	}

	public static double toDegree(double radian) {
		return radian * 180 / Math.PI;
	}

}
