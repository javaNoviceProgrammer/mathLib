package mathLib.fem.core.geometry;

public interface Point extends GeoEntity {
	
	int dim();
	double coord(int index);
	double[] coords();
	void setCoord(int index,double value);
	boolean coordEquals(Point p);
	int getIndex();
}
