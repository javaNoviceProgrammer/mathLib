package mathLib.plot.util;

import java.awt.Color;

public class ColorMap {

	double min, max;
	ColorMapName map;

	public enum ColorMapName {
		BlackAndWhite, 
		Rainbow,
		Heat
	}

	public ColorMap(double min, double max, ColorMapName map) {
		this.min = min;
		this.max = max;
		this.map = map;
	}

	public Color getColor(double var) {
		Color color = null;
		switch (map) {
		case BlackAndWhite: {
			int g = (int) (getNormalizedValue(var) * 255);
			color = new Color(g, g, g);
			break;
		}
		case Rainbow: {
			color = getRainbow(var);
			break;
		}
		case Heat:{
			color = getHeat(var) ;
			break ;
		}

		default:
			break;
		}

		return color;

	}

	private double getNormalizedValue(double var) {
//		return (var - min) / (max - min);
		double normalizedVal = (var - min) / (max - min) ;
		if(normalizedVal < 0.0 )
			return 0.0 ;
		if(normalizedVal >= 1.0)
			return 1.0 ;
		return normalizedVal ;
	}

	private Color getRainbow(double var) {
		double f = getNormalizedValue(var);
		double a = (1 - f) / 0.25;
		int r, g, b;
		Color color = null;
		int X = (int) a;
		int Y = (int) (255 * (a - X));
		switch (X) {
		case 0:
			r = 255;
			g = Y;
			b = 0;
			break;
		case 1:
			r = 255 - Y;
			g = 255;
			b = 0;
			break;
		case 2:
			r = 0;
			g = 255;
			b = Y;
			break;
		case 3:
			r = 0;
			g = 255 - Y;
			b = 255;
			break;
		case 4:
			r = 0;
			g = 0;
			b = 255;
			break;
		default:
			r = 0;
			g = 0;
			b = 255;
			break;
		}
		color = new Color(r, g, b);
		return color;
	}
	
	private Color getHeat(double var) {
		double f = getNormalizedValue(var) ;
		double a = 1 - f ;
		int Y = (int) (255*a) ;
		int r = 255 ;
		int g = Y ;
		int b = 0 ;
		return new Color(r, g, b) ;
	}

}
