package mathLib.fem.core.geometry;

import mathLib.fem.util.container.VertexList;

/**
 * 0D(Vertices) Geometry Entity
 *
 */
public class GeoEntity0D implements GeoEntity {
	protected VertexList vertices = new VertexList();
	
	public void addVertex(Vertex vertex) {
		this.vertices.add(vertex);
	}

	public void addAllVertices(VertexList vertices) {
		this.vertices.clear();
		this.vertices.addAll(vertices);
	}

	public VertexList getVertices() {
		return vertices;
	}
	
	public void clearVertices() {
		this.vertices.clear();
	}
	
	public String toString() {
		return "GeoEntity0D:"+this.vertices.toString();
	}
}
