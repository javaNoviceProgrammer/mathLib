package mathLib.util;

public class Conversions {
	
	// convert angle
	
	public static double angle(double val, Units from, Units to) {
		double var1 = getAngleScale(from) ;
		double var2 = getAngleScale(to) ;
		return (var1/var2)*val ;
	}
	
	private static double getAngleScale(Units unit) {
		double scale = 1.0 ;
		switch (unit) {
			case degree:
				scale = 1.0 ;
				break;
			case radian:
				scale = 180.0/Math.PI ;
				break ;
			default:
				throw new IllegalArgumentException("Must enter a unit of Angle!") ;
		}
		return scale ;
	}

	// convert length
	
	public static double length(double val, Units from, Units to) {
		double var1 = getLengthScale(from) ;
		double var2 = getLengthScale(to) ;
		return (var1/var2)*val ;
	}
	
	private static double getLengthScale(Units unit) {
		double scale = 1.0 ;
		switch (unit) {
			case nm:
				scale = 1e-9 ;
				break;
			case um:
				scale = 1e-6 ;
				break ;
			case mm:
				scale = 1e-3 ;
				break ;
			case cm:
				scale = 1e-2 ;
				break ;
			case meter:
				scale = 1.0 ;
				break ;
			case foot:
				scale = 0.3048 ;
				break ;
			case inch:
				scale = 0.0254 ;
				break ;
			case mile:
				scale = 1609.34 ;
				break ;
			case yard:
				scale = 0.9144 ;
				break ;
			default:
				throw new IllegalArgumentException("Must enter a unit of Length!") ;
		}
		return scale ;
	}

	// convert weight
	
	public static double weight(double val, Units from, Units to) {
		double var1 = getWeightScale(from) ;
		double var2 = getWeightScale(to) ;
		return (var1/var2)*val ;
	}
	
	private static double getWeightScale(Units unit) {
		double scale = 1.0 ;
		switch (unit) {
		case gram:
			scale = 1.0 ;
			break;
		case kg:
			scale = 1e3 ;
			break ;
		case tone:
			scale = 1e6 ;
			break ;
		case lb:
			scale = 453.592 ;
			break ;
		case ounce:
			scale = 28.3495 ;
			break ;
		default:
			throw new IllegalArgumentException("Must enter a unit of Weight") ;
		}
		return scale ;
	}
	
	// convert optical power
	
	public static double opticalPower(double val, Units from, Units to) {
		double var1 = to_mW(val, from) ;
		if(to == Units.mW)
			return var1 ;
		else if(to == Units.dBm)
			return 10.0*Math.log10(var1) ;
		else if(to == Units.watt)
			return var1*1e-3 ;
		else {
			throw new IllegalArgumentException("Must enter a unit of Optical Power") ;
		}
	}
	
	private static double to_mW(double val, Units unit) {
		double var1 = val ;
		switch (unit) {
		case mW:
			if(val<0) throw new IllegalArgumentException("Must enter a positive number") ; 
			var1 = val ;
			break;
		case dBm:
			var1 = Math.pow(10.0, val/10.0) ;
			break ;
		case watt:
			var1 = val*1e3 ;
			break ;
		default:
			throw new IllegalArgumentException("Must enter a unit of Optical Power") ;
		}
		return var1 ;
	}
	
	// convert time
	
	public static double time(double val, Units from, Units to) {
		double var1 = getTimeScale(from) ;
		double var2 = getTimeScale(to) ;
		return (var1/var2)*val ;
	}
	
	private static double getTimeScale(Units unit) {
		double scale = 1.0 ;
		switch (unit) {
		case fsec:
			scale = 1e-15 ;
			break;
		case psec:
			scale = 1e-12 ;
			break ;
		case nsec:
			scale = 1e-9 ;
			break ;
		case usec:
			scale = 1e-6 ;
			break ;
		case msec:
			scale = 1e-3 ;
			break ;
		case sec:
			scale = 1.0 ;
			break;
		case minute:
			scale = 60.0 ;
			break ;
		case hour:
			scale = 60.0*60.0 ;
			break ;
		case day:
			scale = 24.0*60.0*60.0 ;
			break ;
		case year:
			scale = 365.0*24.0*60.0*60.0 ;
		default:
			throw new IllegalArgumentException("Must enter a unit of Time") ;
		}
		return scale ;
	}
	
	// convert frequency
	
	public static double freq(double val, Units from, Units to) {
		double var1 = getFreqScale(from) ;
		double var2 = getFreqScale(to) ;
		return (var1/var2)*val ;
	}
	
	private static double getFreqScale(Units unit) {
		double scale = 1.0 ;
		switch (unit) {
		case THz:
			scale = 1e12 ;
			break;
		case GHz:
			scale = 1e9 ;
			break ;
		case MHz:
			scale = 1e6 ;
			break ;
		case KHz:
			scale = 1e3 ;
			break ;
		case Hz:
			scale = 1.0 ;
			break ;
		default:
			throw new IllegalArgumentException("Must enter a unit of Frequency") ;
		}
		return scale ;
	}
	
	// convert time
	
	public static double wgLoss(double val, Units from, Units to) {
		double var1 = getTimeScale(from) ;
		double var2 = getTimeScale(to) ;
		return (var1/var2)*val ;
	}
	
	private static double getWgLossScale(Units unit) {
		double scale = 1.0 ;
		switch (unit) {
		case perMeter:
			scale = 1 ;
			break;
		case perCm:
			scale = 1 ;
			break ;
		case dBperCm:
			scale = 1 ;
			break ;
		case dBperMeter:
			scale = 1 ;
			break ;
		default:
			throw new IllegalArgumentException("Must enter a unit of Frequency") ;
		}
		return scale ;
	}

}
