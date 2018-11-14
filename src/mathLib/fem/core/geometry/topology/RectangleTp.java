/**
 * Copyright (c) 2010, nkliuyueming@gmail.com. All rights reserved.
 * 
 * 
 */
package mathLib.fem.core.geometry.topology;

/**
 * 四边形拓扑结构
 * @author liuyueming
 *
 */
public class RectangleTp implements Topology2D {
	public static int[] vertices = {1,2,3,4};
	public static int[][] edges = {{1,2},{2,3},{3,4},{4,1}};
	
	@Override
	public int[] getVertices() {
		return vertices;
	}
	
	@Override
	public int[][] getEdges() {
		return edges;
	}
}
