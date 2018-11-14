package mathLib.fem;

import mathLib.geometry.algebra.Point;
import mathLib.plot.util.MeshGrid;
import mathLib.util.MathUtils;

public class Mesh2DRect {
	
	int nx, ny ;
	Mesh mesh ;
	Point pLL, pLR, pUR, pUL ;
	
	public Mesh2DRect(Point pLL, Point pUR, int nx, int ny) {
		this.pLL = pLL ;
		this.pUR = pUR ;
		this.pLR = Point.getInstance(pUR.getX(), pLL.getY()) ;
		this.pUL = Point.getInstance(pLL.getX(), pUR.getY()) ;
		this.nx = nx ;
		this.ny = ny ;
		createMesh();
	}
	
	private void createMesh() {
		this.mesh = new Mesh();
		double x0 = pLL.getX() ;
		double y0 = pLL.getY() ;
		double x1 = pUR.getX() ;
		double y1 = pUR.getY() ;
		double stepx = (x1 - x0) / (nx-1);
		double stepy = (y1 - y0) / (ny-1);
		
		int n1;
		for (int i = 0; i < ny; ++i) {
			double y = y0 + i * stepy;

			for (n1 = 0; n1 < nx; ++n1) {
				double x = x0 + n1 * stepx;
				int index = i * ny + n1 + 1;
				Node node = new Node(index, x, new double[]{y});
				mesh.addNode(node);
			}
		}

		NodeList nodes = mesh.getNodeList();

		for (int i = 0; i < nx - 1; ++i) {
			for (int j = 0; j < ny - 1; ++j) {
				n1 = i * nx + j;
				int n2 = n1 + 1;
				int n3 = (i + 1) * nx + j;
				NodeList list = new NodeList();
				list.add((Node) nodes.at(n1 + 1));
				list.add((Node) nodes.at(n2 + 1));
				list.add((Node) nodes.at(n3 + 1));
				Element e = new Element(list);
				mesh.addElement(e);
				new Element();
				n1 = i * nx + j + 1;
				n2 = (i + 1) * nx + j + 1;
				n3 = n2 - 1;
				list = new NodeList();
				list.add((Node) nodes.at(n1 + 1));
				list.add((Node) nodes.at(n2 + 1));
				list.add((Node) nodes.at(n3 + 1));
				e = new Element(list);
				mesh.addElement(e);
			}
		}
	}
	
	public Mesh getMesh() {
		return mesh ;
	}
	
	public int getNumberOfNodes() {
		return mesh.getNodeList().size() ;
	}
	
	public int getNumberOfElements() {
		return mesh.getElementList().size() ;
	}
	
	public void refine() {
		nx = 2*nx ;
		ny = 2*ny ;
		createMesh();
	}
	
	public Mesh2DRect getRefined() {
		// double the density of nodes
		return new Mesh2DRect(pLL, pUR, 2*nx, 2*ny) ;
	}
	
	public MeshGrid getGrid() {
		double[] x = MathUtils.linspace(pLL.getX(), pUR.getX(), nx) ;
		double[] y = MathUtils.linspace(pLL.getY(), pUR.getY(), ny) ;
		return new MeshGrid(x, y) ;
	}
	
	// for test
	public static void main(String[] args) {
		Mesh2DRect mesh2d = new Mesh2DRect(Point.getInstance(0, 0), Point.getInstance(1, 1), 10, 10) ;
//		mesh2d.getMesh().computeNodeBelongsToElements();
		System.out.println(mesh2d.getMesh().getElementList());
		System.out.println(mesh2d.getMesh().getNodeList().size());
		System.out.println(mesh2d.getMesh().getElementList().size());
		mesh2d.refine();
		System.out.println(mesh2d.getMesh().getNodeList().size());
		System.out.println(mesh2d.getMesh().getElementList().size());
	}

}
