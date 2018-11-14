package mathLib.fem.core;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mathLib.fem.core.geometry.GeoEntity;
import mathLib.fem.core.geometry.GeoEntity0D;
import mathLib.fem.core.geometry.GeoEntity1D;
import mathLib.fem.core.geometry.GeoEntity2D;
import mathLib.fem.core.geometry.GeoEntity3D;
import mathLib.fem.core.geometry.topology.HexahedronTp;
import mathLib.fem.core.geometry.topology.RectangleTp;
import mathLib.fem.core.geometry.topology.TetrahedronTp;
import mathLib.fem.core.geometry.topology.Topology3D;
import mathLib.fem.core.geometry.topology.TriangleTp;
import mathLib.fem.util.FutureyeException;
import mathLib.fem.util.container.DOFList;
import mathLib.fem.util.container.ElementList;
import mathLib.fem.util.container.ObjList;
import mathLib.fem.util.container.ObjVector;
import mathLib.fem.util.container.VertexList;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.func.symbolic.intf.VectorShapeFunction;
import mathLib.matrix.algebra.SpaceVector;

/**
 * <blockquote><pre>
 * Element of a triangulation
 * </blockquote></pre>
 *
 */
public class Element {
	/**
	 * Global index of this element
	 * �?�元全局编�?�
	 */
	public int globalIndex = 0;
	
	/**
	 * Node list of this element
	 * �?�元结点列表
	 */
	public NodeList nodes = new NodeList();
	
	/**
	 * Neighbors of this element
	 * 相邻�?�元
	 */
	public ElementList neighbors = new ElementList();
	
	/**
	 * A map between local index of a node,edge,face or volume and their corresponding DOFList
	 */
	protected Map<Integer,DOFList> nodeDOFList;
	protected Map<Integer,DOFList> edgeDOFList;
	protected Map<Integer,DOFList> faceDOFList;
	protected DOFList volumeDOFList;
	
	protected GeoEntity0D geoEntity = null;
	public GeoEntity0D getGeoEntity() {
		return geoEntity;
	}
	@SuppressWarnings("rawtypes")
	public GeoEntity1D getGeoEntity1D() {
		return (GeoEntity1D)geoEntity;
	}
	@SuppressWarnings("rawtypes")
	public GeoEntity2D getGeoEntity2D() {
		return (GeoEntity2D)geoEntity;
	}
	@SuppressWarnings("rawtypes")
	public GeoEntity3D getGeoEntity3D() {
		return (GeoEntity3D)geoEntity;
	}
	public void setGeoEntity(GeoEntity0D geoEntity) {
		this.geoEntity = geoEntity;
		this.nodes.addAll(getNodeList(geoEntity));
		if(geoEntity instanceof GeoEntity2D<?,?>)
			this.eleDim = 2;
		else if(geoEntity instanceof GeoEntity3D<?,?,?>)
			this.eleDim = 3;
		else
			this.eleDim = 1;
	}

	/**
	 * �?�元维度：1D, 2D or 3D
	 */
	protected int eleDim = 0;
	public int dim() {
		return eleDim;
	}

	private CoordinateTransform trans = null;
	protected MathFunc jac = null;

	////////////////////////////////////////////////////////////////////
	public Element() {
		
	}
	
	/**
	 * <blockquote><pre>
	 * 构造一个简�?�Element，�?�数结点默认按照以下编�?�：
	 * 
	 * 一维�?�元：
	 * 
	 * 1---2
	 * 
	 * 1--3--2
	 * 
	 * 1--3--4--2
	 * 
	 * 二维三角形�?�元:
	 *  3
	 *  | \
	 *  |  \
	 *  1---2 
	 *  
	 *  3
	 *  | \
	 *  |  \
	 *  6   5
	 *  |    \
	 *  |     \
	 *  1--4---2 
	 *
	 *  3
	 *  | \
	 *  |  \
	 *  6   8
	 *  |    \
	 *  9     5
	 *  |      \
	 *  |       \
	 *  1--4--7--2 
	 *
	 * 二维四边形�?�元：
	 * 4----3
	 * |    |
	 * |    |
	 * 1----2
	 * 
	 * 4--7--3
	 * |     |
	 * 8     6
	 * |     |
	 * 1--5--2
	 * 
	 * 4--11--7--3
	 * |         |
	 * 8         10
	 * |         |
	 * 12        6
	 * |         |
	 * 1-- 5--9--2
	 * 
	 * 三维四�?�体�?�元：
	 *    4
	 *   /|\
	 *  / | \
	 * /  |  \
	 *1---|---3
	 * \  |  /
	 *  \ | /
	 *   \|/
	 *    2
	 * 
	 * 三维六�?�体�?�元：
	 *   4--------3
	 *  /|       /|
	 * 1--------2 |
	 * | |      | |
	 * | |      | |
	 * | 8------|-7
	 * | /      | /
	 * 5--------6
	 *
	 *</blockquote></pre>
	 * @param nodes an object of NodeList
	 */
	public Element(NodeList nodes) {
		buildElement(nodes);
	}
	
	/**
	 * 构造一个一般Element，需�?�??供�?�元几何信�?�
	 * @param node
	 */
	public Element(GeoEntity0D geoEntity) {
		this.geoEntity = geoEntity;
		this.nodes.addAll(getNodeList(geoEntity));
		if(geoEntity instanceof GeoEntity2D<?,?>)
			this.eleDim = 2;
		else if(geoEntity instanceof GeoEntity3D<?,?,?>)
			this.eleDim = 3;
		else
			this.eleDim = 1;
	}
	
	public static ObjList<NodeLocal> getLocalNodeList1D(
			GeoEntity1D<NodeLocal> edge) {
		ObjList<NodeLocal> localNodes = new ObjList<NodeLocal>();
		ObjList<Vertex> vertices = edge.getVertices();
		for(int i=1;i<=vertices.size();i++)
			localNodes.add(vertices.at(i).localNode());
		ObjList<NodeLocal> edgeNodes = edge.getEdgeNodes();
		if(edgeNodes != null) {
			for(int i=1;i<=edgeNodes.size();i++)
				localNodes.add(edgeNodes.at(i));
		}
		return localNodes;
	}
	
	public static ObjList<NodeLocal> getLocalNodeList2D(
			GeoEntity2D<EdgeLocal,NodeLocal> face) {
		ObjList<NodeLocal> localNodes = new ObjList<NodeLocal>();
		ObjList<Vertex> vertices = face.getVertices();
		for(int i=1;i<=vertices.size();i++)
			localNodes.add(vertices.at(i).localNode());
		localNodes.addAll(getInnerLocalNodeList2D(face));
		return localNodes;
	}	
	public static ObjList<NodeLocal> getInnerLocalNodeList2D(
			GeoEntity2D<EdgeLocal,NodeLocal> face) {
		ObjList<NodeLocal> localNodes = new ObjList<NodeLocal>();
		ObjList<EdgeLocal> edges = face.getEdges();
		for(int i=1;i<=edges.size();i++)  {
			localNodes.addAll(edges.at(i).getEdgeNodes());
		}
		ObjList<NodeLocal> faceNodes = face.getFaceNodes();
		if(faceNodes != null) {
			for(int i=1;i<=faceNodes.size();i++)
				localNodes.add(faceNodes.at(i));
		}
		return localNodes;		
	}
	
	public static ObjList<NodeLocal> getLocalNodeList3D(
			GeoEntity3D<FaceLocal,EdgeLocal,NodeLocal> volume) {
		ObjList<NodeLocal> localNodes = new ObjList<NodeLocal>();
		ObjList<Vertex> vertices = volume.getVertices();
		for(int i=1;i<=vertices.size();i++)
			localNodes.add(vertices.at(i).localNode());
		localNodes.addAll(getInnerLocalNodeList3D(volume));
		return localNodes;		
	}	
	public static ObjList<NodeLocal> getInnerLocalNodeList3D(
			GeoEntity3D<FaceLocal,EdgeLocal,NodeLocal> volume) {
		ObjList<NodeLocal> localNodes = new ObjList<NodeLocal>();
		ObjList<FaceLocal> faces = volume.getFaces();
		for(int i=1;i<=faces.size();i++) 
			localNodes.addAll(getInnerLocalNodeList2D(faces.at(i)));
		ObjList<NodeLocal> volumeNodes = volume.getVolumeNodes();
		if(volumeNodes != null) {
			for(int i=1;i<=volumeNodes.size();i++)
				localNodes.add(volumeNodes.at(i));
		}
		return localNodes;		
	}	

	@SuppressWarnings("unchecked")
	public static NodeList getNodeList(GeoEntity geoEntity) {
		ObjList<NodeLocal> localNodes = null;
		if(geoEntity instanceof GeoEntity1D<?>) {
			localNodes = getLocalNodeList1D(
					(GeoEntity1D<NodeLocal>) geoEntity);
		} else if(geoEntity instanceof GeoEntity2D<?,?>) {
			localNodes = getLocalNodeList2D(
					(GeoEntity2D<EdgeLocal, NodeLocal>) geoEntity);
		} else if(geoEntity instanceof GeoEntity3D<?,?,?>) {
			localNodes = getLocalNodeList3D(
					(GeoEntity3D<FaceLocal, EdgeLocal, NodeLocal>) geoEntity);
		} else {
			FutureyeException ex = new FutureyeException("Error: Can not build NodeList, geoEntity="+
					geoEntity.getClass().getName());
			ex.printStackTrace();
			System.exit(0);
		}
//		Object[] a=localNodes.toArray();
//		Arrays.sort(a, (Comparator)new Comparator<NodeLocal>(){
//			@Override
//			public int compare(NodeLocal o1, NodeLocal o2) {
//				if(o1.localIndex > o2.localIndex)
//					return 1;
//				else
//					return -1;
//			}
//		});
		//返回的全局结点按照局部结点的编�?�顺�?返回
		List<NodeLocal> list = localNodes.toList();
		Collections.sort(list, new Comparator<NodeLocal>(){
			@Override
			public int compare(NodeLocal o1, NodeLocal o2) {
				//�?��?
				if(o1.localIndex > o2.localIndex)
					return 1;
				else
					return -1;
			}
		});
		NodeList nodes = new NodeList();
		for(int i=0;i<list.size();i++) {
			nodes.add(list.get(i).globalNode);
		}
		return nodes;
	}
	
	/**
	 * 构建一个有�?�?�元
	 * 注�?：该方法构建的�?�元几何信�?��?包�?�：全局Edge（2D,3D），全局Face（3D），
	 * 全局信�?�需�?调用Mesh类的相关函数计算得到。
	 * 
	 * @param nodes
	 */
	protected void buildElement(NodeList nodes) {
		this.nodes.clear();
		this.nodes.addAll(nodes);
		
		//以结点的维度决定�?�元的维度
		this.eleDim = nodes.at(1).dim();
		int n = nodes.size();
		if(this.eleDim == 1) {
//			GeoEntity1D<NodeLocal> entity = new GeoEntity1D<NodeLocal>();
			Edge entity = new Edge();//2011/11/28
			if(n >= 2) {
				entity.addVertex(new Vertex(1,new NodeLocal(1,nodes.at(1))));
				entity.addVertex(new Vertex(2,new NodeLocal(2,nodes.at(n))));
				if(n > 2) {
					for(int i=2;i<n;i++) //edge nodes number from 3,4,5,...
						entity.addEdgeNode(new NodeLocal(1+i,nodes.at(i)));
				}
			} else {
				FutureyeException ex = new FutureyeException("Error: Number of nodes should be at least 2");
				ex.printStackTrace();
				System.exit(0);
			}
			this.geoEntity = entity;
			
		} else if(this.eleDim == 2) {
//			GeoEntity2D<EdgeLocal,NodeLocal> entity = 
//						new GeoEntity2D<EdgeLocal,NodeLocal>();
			Face entity = new Face();//add 2011/11/28
			int [][]edgesTp = null;
			if(n == 3) {
				//线性三角形�?�元
				TriangleTp triTp = new TriangleTp();
				entity.setTopology(triTp);
				edgesTp = triTp.getEdges();
				for(int i=1;i<=nodes.size();i++)
					entity.addVertex(new Vertex(i,new NodeLocal(i,nodes.at(i))));
				for(int i=0;i<edgesTp.length;i++) {
					EdgeLocal el = new EdgeLocal(i+1,this);
					for(int j=0;j<edgesTp[i].length;j++) {
						el.addVertex(
								new Vertex(edgesTp[i][j],
								new NodeLocal(edgesTp[i][j],nodes.at(edgesTp[i][j])))
								);
					}
					entity.addEdge(el);
				}
			} else if(n == 4) {
				//线性四边形�?�元
				RectangleTp rectTp = new RectangleTp();
				entity.setTopology(rectTp);
				edgesTp = rectTp.getEdges();
				for(int i=1;i<=nodes.size();i++)
					entity.addVertex(new Vertex(i,new NodeLocal(i,nodes.at(i))));
				for(int i=0;i<edgesTp.length;i++) {
					EdgeLocal el = new EdgeLocal(i+1,this);
					for(int j=0;j<edgesTp[i].length;j++) {
						el.addVertex(
								new Vertex(edgesTp[i][j],
								new NodeLocal(edgesTp[i][j],nodes.at(edgesTp[i][j])))
								);
					}
					entity.addEdge(el);
				}
			} else if(n == 6) {
				//二次三角形�?�元
				TriangleTp triTp = new TriangleTp();
				entity.setTopology(triTp);
				edgesTp = triTp.getEdges();
				for(int i=1;i<=3;i++)
					entity.addVertex(new Vertex(i,new NodeLocal(i,nodes.at(i))));
				for(int i=0;i<edgesTp.length;i++) {
					EdgeLocal el = new EdgeLocal(i+1,this);
					for(int j=0;j<edgesTp[i].length;j++) {
						el.addVertex(
								new Vertex(edgesTp[i][j],
								new NodeLocal(edgesTp[i][j],nodes.at(edgesTp[i][j])))
								);
					}
					el.addEdgeNode(new NodeLocal(4+i,nodes.at(4+i)));
					entity.addEdge(el);
				}
			} else if(n == 8) {
				
			} else if(n ==9) {
				
			} else if(n == 12) {
				
			} else {
				FutureyeException ex = new FutureyeException("Error: Not supported element, try use Element(GeoEntity geoEntity)");
				ex.printStackTrace();
				System.exit(0);
			}

			this.geoEntity = entity;
			
		} else if(this.eleDim == 3) {
//			GeoEntity3D<FaceLocal,EdgeLocal,NodeLocal> entity = 
//						new GeoEntity3D<FaceLocal,EdgeLocal,NodeLocal>();
			Volume entity = new Volume();//2011/11/28
			int [][]edgesTp = null;
			int [][]facesTp = null;
			Topology3D topo = null;
			if(n == 4) {
				//线性四�?�体�?�元
				topo = new TetrahedronTp();
				entity.setTopology(topo);
				edgesTp = topo.getEdges();
				facesTp = topo.getFaces();
				for(int i=1;i<=topo.getVertices().length;i++)
					entity.addVertex(new Vertex(i,new NodeLocal(i,nodes.at(i))));
			} else if(n == 8) {
				//线性六�?�体�?�元
				topo = new HexahedronTp();
				entity.setTopology(topo);
				edgesTp = topo.getEdges();
				facesTp = topo.getFaces();
				for(int i=1;i<=topo.getVertices().length;i++)
					entity.addVertex(new Vertex(i,new NodeLocal(i,nodes.at(i))));
			} else {
				FutureyeException ex = new FutureyeException("Error: Not supported element, try use Element(GeoEntity geoEntity)");
				ex.printStackTrace();
				System.exit(0);
			}
			for(int k=0;k<facesTp.length;k++) {
				FaceLocal fl = new FaceLocal(k+1,this);
				for(int i=0;i<facesTp[k].length;i++) {
					fl.addVertex(
							new Vertex(facesTp[k][i],
									new NodeLocal(facesTp[k][i],nodes.at(facesTp[k][i])))
							);
				}
				for(int i=0;i<edgesTp.length;i++) {
					EdgeLocal el = new EdgeLocal(i+1,this);
					for(int j=0;j<edgesTp[i].length;j++) {
						el.addVertex(
								new Vertex(edgesTp[i][j],
										new NodeLocal(edgesTp[i][j],nodes.at(edgesTp[i][j])))
								);
					}
					if(topo.edgeOnface(facesTp[k],edgesTp[i]))
						fl.addEdge(el);
				}
				entity.addFace(fl);
			}
			this.geoEntity = entity;
			
		} else {
			FutureyeException ex = new FutureyeException("Error: Node dim should be 1,2,3 dim="+eleDim);
			ex.printStackTrace();
			System.exit(0);
		}
	}
	
	/**
	 * If you do some changes in this.geoEntity, call this method to 
	 * update the information in Element
	 * 
	 */
	public void applyChange() {
		NodeList oldNodes = new NodeList();
		for(int i=1;i<=this.nodes.size();i++) {
			oldNodes.add(this.nodes.at(i));
		}
		this.nodes.clear();
		
		//有问题，边界没办法调整
		//this.nodes.addAll(getNodeList(this.geoEntity));
		this.buildElement(getNodeList(this.geoEntity));
		
		//关�?�的自由度怎么办？
		if(this.nodeDOFList!=null) {
			Map<Integer,DOFList> newNodeDOFList = new LinkedHashMap<Integer,DOFList>();
			for(int i=1;i<=oldNodes.size();i++) {
				Node node = oldNodes.at(i);
				DOFList DOFs = this.nodeDOFList.get(i);
				//新结点中查找
				for(int j=1;j<=this.nodes.size();j++) {
					if(node.globalIndex == this.nodes.at(j).globalIndex) {
						newNodeDOFList.put(j, DOFs);
						break;
					}
				}
			}
			this.nodeDOFList = newNodeDOFList;
		}		
	}
	
	/**
	 * 获�?��?�元的局部结点列表 2011-11-4
	 * 
	 * @see getNodeList() 这两个函数内部类似，何以考虑简化实现
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ObjList<NodeLocal> localNodes() {
		ObjList<NodeLocal> localNodes = null;
		if(geoEntity instanceof GeoEntity1D<?>) {
			localNodes = getLocalNodeList1D(
					(GeoEntity1D<NodeLocal>) geoEntity);
		} else if(geoEntity instanceof GeoEntity2D<?,?>) {
			localNodes = getLocalNodeList2D(
					(GeoEntity2D<EdgeLocal, NodeLocal>) geoEntity);
		} else if(geoEntity instanceof GeoEntity3D<?,?,?>) {
			localNodes = getLocalNodeList3D(
					(GeoEntity3D<FaceLocal, EdgeLocal, NodeLocal>) geoEntity);
		} else {
			FutureyeException ex = new FutureyeException("Error: Can not build NodeList, geoEntity="+
					geoEntity.getClass().getName());
			ex.printStackTrace();
			System.exit(0);
		}
//		Object[] a=localNodes.toArray();
//		Arrays.sort(a, (Comparator)new Comparator<NodeLocal>(){
//			@Override
//			public int compare(NodeLocal o1, NodeLocal o2) {
//				if(o1.localIndex > o2.localIndex)
//					return 1;
//				else
//					return -1;
//			}
//		});
		//按照局部结点的编�?�顺�?返回
		List<NodeLocal> list = localNodes.toList();
		Collections.sort(list, new Comparator<NodeLocal>(){
			@Override
			public int compare(NodeLocal o1, NodeLocal o2) {
				//�?��?
				if(o1.localIndex > o2.localIndex)
					return 1;
				else
					return -1;
			}
		});
		return localNodes;
	}
	
	/**
	 * 获�?��?�元的几何顶点
	 * 应用：
	 * 1.用于�??标�?��?�
	 * 2.用于�?�积计算
	 * 3.等等
	 * @return
	 */
	public VertexList vertices() {
		return this.geoEntity.getVertices();
	}
	
	/**
	 * 获�?��?�元的局部边列表
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ObjList<EdgeLocal> edges() {
		if(this.eleDim == 2) {
			return getGeoEntity2D().getEdges();
		} else if(this.eleDim == 3){
			ObjList<FaceLocal> faces = this.getGeoEntity3D().getFaces();
			ObjList<EdgeLocal> edges = new ObjList<EdgeLocal>();
			for(FaceLocal face : faces) {
				edges.addAll(face.getEdges());
			}
			return edges;
		} else {
			throw new FutureyeException("Error: "+this.geoEntity.getClass().getName());
		}
	}
	
	/**
	 * 获�?��?�元的�?�列表
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ObjList<FaceLocal> faces() {
		if(this.eleDim == 3) {
			return getGeoEntity3D().getFaces();
		} else {
			throw new FutureyeException("Error: "+this.geoEntity.getClass().getName());
		}
	}
	
	
	///////////////////Add and Get DOF(s)//////////////////////////
	/**
	 * 添加一个结点相关自由度（1D/2D/3D）
	 * @param localNodeIndex
	 * @param dof
	 */
	public void addNodeDOF(int localNodeIndex,DOF dof) {
		if(nodeDOFList == null)
			nodeDOFList = new LinkedHashMap<Integer,DOFList>();
		DOFList dofList = nodeDOFList.get(localNodeIndex);
		if(dofList == null) {
			dofList = new DOFList();
			nodeDOFList.put(localNodeIndex, dofList);
		}

		//2010-10-11 DOF�??�?�索引Node
		dof.setOwner(this.nodes.at(localNodeIndex));
		//TODO 2011-11-4考虑是�?�改为：
		//dof.setOwner(this.localNodes().at(localNodeIndex));
		dofList.add(dof);
	}
	
	/**
	 * 添加一个边相关自由度（2D/3D）
	 * @param localEdgeIndex
	 * @param dof
	 */
	public void addEdgeDOF(int localEdgeIndex,DOF dof) {
		if(edgeDOFList == null)
			edgeDOFList = new LinkedHashMap<Integer,DOFList>();
		DOFList dofList = edgeDOFList.get(localEdgeIndex);
		if(dofList == null) {
			dofList = new DOFList();
			edgeDOFList.put(localEdgeIndex, dofList);
		}
		//DOF�??�?�索引Edge
		dof.setOwner(this.edges().at(localEdgeIndex));
		dofList.add(dof);
	}
	
	/**
	 * 添加一个�?�相关自由度（3D）
	 * @param localFaceIndex
	 * @param dof
	 */
	public void addFaceDOF(int localFaceIndex,DOF dof) {
		if(faceDOFList == null)
			faceDOFList = new LinkedHashMap<Integer,DOFList>();
		DOFList dofList = faceDOFList.get(localFaceIndex);
		if(dofList == null) {
			dofList = new DOFList();
			faceDOFList.put(localFaceIndex, dofList);
		}
		//DOF�??�?�索引Face
		dof.setOwner(this.faces().at(localFaceIndex));
		dofList.add(dof);
	}
	
	/**
	 * 添加一个�?�元相关自由度（1D/2D/3D），
	 * 对于1D，为�?�元线上的自由度
	 * 对于2D，为�?�元�?�上的自由度
	 * 对于3D，为�?�元体上的自由度
	 * @param dof
	 */
	public void addVolumeDOF(DOF dof) {
		if(volumeDOFList == null)
			volumeDOFList = new DOFList();
		//DOF�??�?�索引Element
		//TODO???体自由度的owner是this.geoEntity???
		dof.setOwner(this.geoEntity);
		volumeDOFList.add(dof);
	}
	
	/**
	 * 获�?��?�元结点localNodeIndex对应的自由度列表
	 * @param localNodeIndex
	 * @return
	 */
	public DOFList getNodeDOFList(int localNodeIndex) {
		if(nodeDOFList == null) return null;
		DOFList dofList = nodeDOFList.get(localNodeIndex);
		return dofList;
	}
	
	/**
	 * 获�?��?�元边localEdgeIndex对应的自由度列表
	 * @param localEdgeIndex
	 * @return
	 */
	public DOFList getEdgeDOFList(int localEdgeIndex) {
		if(edgeDOFList == null) return null;
		DOFList dofList = edgeDOFList.get(localEdgeIndex);
		return dofList;
	}	
	/**
	 * 获�?��?�元�?�localFaceIndex对应的自由度列表
	 * @param localFaceIndex
	 * @return
	 */
	public DOFList getFaceDOFList(int localFaceIndex) {
		if(faceDOFList == null) return null;
		DOFList dofList = faceDOFList.get(localFaceIndex);
		return dofList;
	}
	/**
	 * 获�?��?�元体对应的自由度列表
	 * @return
	 */
	public DOFList getVolumeDOFList() {
		return volumeDOFList;
	}
	
	public DOFList getAllNodeDOFList() {
		DOFList rlt = new DOFList();
		if(nodeDOFList != null) {
			for(Entry<Integer,DOFList> entry : nodeDOFList.entrySet()) {
				rlt.addAll(entry.getValue());
			}
		}
		return rlt;
	}
	public DOFList getAllEdgeDOFList() {
		DOFList rlt = new DOFList();
		if(edgeDOFList != null) {
			for(Entry<Integer,DOFList> entry : edgeDOFList.entrySet()) {
				rlt.addAll(entry.getValue());
			}
		}
		return rlt;
	}
	public DOFList getAllFaceDOFList() {
		DOFList rlt = new DOFList();
		if(faceDOFList != null) {
			for(Entry<Integer,DOFList> entry : faceDOFList.entrySet()) {
				rlt.addAll(entry.getValue());
			}
		}
		return rlt;
	}
	public DOFList getAllVolumeDOFList() {
		DOFList rlt = new DOFList();
		if(volumeDOFList != null) {
			rlt.addAll(volumeDOFList);
		}
		return rlt;
	}
	
	/**
	 * 获�?��?�元上所有的自由度，自由度的排列顺�?由order指定
	 * 注�?：该顺�?与按照自由度局部编�?�从�?到大排列的顺�?�?一定相�?�
	 * 
	 * @param order 自由度排�?方�?：NEFV-点�?边�?�?��?体； VFEN-体�?�?��?边�?点
	 * @return
	 */
	public DOFList getAllDOFList(DOFOrder order) {
		DOFList rlt = new DOFList();
		switch (order) {
		case NEFV: 
			if(nodeDOFList != null) {
				for(Entry<Integer,DOFList> entry : nodeDOFList.entrySet()) {
					rlt.addAll(entry.getValue());
				}
			}
			if(edgeDOFList != null) {
				for(Entry<Integer,DOFList> entry : edgeDOFList.entrySet()) {
					rlt.addAll(entry.getValue());
				}
			}
			if(faceDOFList != null) {
				for(Entry<Integer,DOFList> entry : faceDOFList.entrySet()) {
					rlt.addAll(entry.getValue());
				}
			}
			if(volumeDOFList != null) {
				rlt.addAll(volumeDOFList);
			}
			break;
		case VFEN:
			if(volumeDOFList != null) {
				rlt.addAll(volumeDOFList);
			}			
			if(faceDOFList != null) {
				for(Entry<Integer,DOFList> entry : faceDOFList.entrySet()) {
					rlt.addAll(entry.getValue());
				}
			}
			if(edgeDOFList != null) {
				for(Entry<Integer,DOFList> entry : edgeDOFList.entrySet()) {
					rlt.addAll(entry.getValue());
				}
			}
			if(nodeDOFList != null) {
				for(Entry<Integer,DOFList> entry : nodeDOFList.entrySet()) {
					rlt.addAll(entry.getValue());
				}
			}
			break;
		default:
			throw new FutureyeException();	
		}

		return rlt;
	}
	
	/**
	 * Get all DOF in a DOFList, sorted by localIndex of DOF object ascending.
	 * 
	 * @return
	 */
	public DOFList getAllDOFListSortedByLocalIndex() {
		DOFList list = this.getAllDOFList(DOFOrder.NEFV);
		Collections.sort(list.toList(), new Comparator<DOF>() {

			@Override
			public int compare(DOF o1, DOF o2) {
				if(o1.getLocalIndex()>o2.getLocalIndex())
					return 1;
				else 
					return -1;
			}
			
		});
		return list;
	}

	/**
	 * 对于�?��?值问题，在�?个几何实体上的自由度也是一个�?��?，
	 * 例如：二维Stokes问题的速度�?压强�?��?：(u,v,p)，在结点上
	 * 对应的自由度为list： [DOFu DOFv DOFp]
	 * 
	 * @param order
	 * @param vectorDim: 自由度list index  
	 * @return
	 */
	public DOFList getAllDOFListByVVFComponent(DOFOrder order,int nVVFComponent) {
		DOFList rlt = new DOFList();
		if(nodeDOFList != null) {
			for(Entry<Integer,DOFList> entry : nodeDOFList.entrySet()) {
				if(entry.getValue().size()>=nVVFComponent)
					rlt.add(entry.getValue().at(nVVFComponent));
			}
		}
		if(edgeDOFList != null) {
			for(Entry<Integer,DOFList> entry : edgeDOFList.entrySet()) {
				if(entry.getValue().size()>=nVVFComponent)
					rlt.add(entry.getValue().at(nVVFComponent));
			}
		}
		if(faceDOFList != null) {
			for(Entry<Integer,DOFList> entry : faceDOFList.entrySet()) {
				if(entry.getValue().size()>=nVVFComponent)
					rlt.add(entry.getValue().at(nVVFComponent));
			}
		}
		if(volumeDOFList != null) {
			if(volumeDOFList.size()>=nVVFComponent)
				rlt.add(volumeDOFList.at(nVVFComponent));
		}
		return rlt;
	}
	
	//////////////////////DOF Number///////////////////////////
	public int getNodeDOFNumber() {
		if(nodeDOFList == null) return 0;
		int nTotal = 0;
		for(Entry<Integer,DOFList> entry : nodeDOFList.entrySet()) {
			nTotal += entry.getValue().size();
		}
		return nTotal;
	}
	public int getEdgeDOFNumber() {
		if(edgeDOFList == null) return 0;
		int nTotal = 0;
		for(Entry<Integer,DOFList> entry : edgeDOFList.entrySet()) {
			nTotal += entry.getValue().size();
		}
		return nTotal;
	}
	public int getFaceDOFNumber() {
		if(faceDOFList == null) return 0;
		int nTotal = 0;
		for(Entry<Integer,DOFList> entry : faceDOFList.entrySet()) {
			nTotal += entry.getValue().size();
		}
		return nTotal;
	}
	/**
	 * 获�?��?�元相关自由度总数（1D/2D/3D），
	 * 对于1D，为�?�元线上的自由度总数
	 * 对于2D，为�?�元�?�上的自由度总数
	 * 对于3D，为�?�元体上的自由度总数
	 * @return
	 */
	public int getVolumeDOFNumber() {
		if(volumeDOFList == null) return 0;
		return volumeDOFList.size();
	}

	/**
	 * 获�?�所有结点�?边�?�?�和体上的自由度总数
	 * @return
	 */
	public int getAllDOFNumber() {
		int nTotal = 0;
		nTotal += this.getNodeDOFNumber();
		nTotal += this.getEdgeDOFNumber();
		nTotal += this.getFaceDOFNumber();
		nTotal += this.getVolumeDOFNumber();
		return nTotal;
	}
	///////////////////////////////////////////////
	
	/**
	 * 局部自由度编�?�与全局自由度编�?�转�?�
	 * 默认规则：
	 * 按照点�?边�?�?��?体的顺�?将局部自由度编�?�从�?到大排列
	 * @param local
	 * @return
	 */
	public int local2GlobalDOFIndex(int local) {
		int base = 0;
		if(nodeDOFList != null) {
			for(int j=1;j<=nodes.size();j++) {
				DOFList list = nodeDOFList.get(j);
				if(list != null) {
			 		for(int i=1;i<=list.size();i++) {
			 			DOF dof = list.at(i);
						if(dof.getLocalIndex() == local-base)
							return dof.getGlobalIndex();
					}
		 		}
			}
			base += this.getNodeDOFNumber();
		}
		if(edgeDOFList != null) {
			ObjList<EdgeLocal> edges = this.edges();
			for(int j=1;j<=edges.size();j++) {
				DOFList list = edgeDOFList.get(j);
				if(list != null) {
			 		for(int i=1;i<=list.size();i++) {
			 			DOF dof = list.at(i);
						if(dof.getLocalIndex() == local-base)
							return dof.getGlobalIndex();
					}
		 		}
			}
			base += this.getEdgeDOFNumber();
		}
		if(faceDOFList != null) {
			ObjList<FaceLocal> faces = this.faces();
			for(int j=1;j<=faces.size();j++) {
				DOFList list = faceDOFList.get(j);
				if(list != null) {
			 		for(int i=1;i<=list.size();i++) {
			 			DOF dof = list.at(i);
						if(dof.getLocalIndex() == local-base)
							return dof.getGlobalIndex();
					}
		 		}
			}
			base += this.getFaceDOFNumber();
		}
		if(volumeDOFList != null) {
	 		for(int i=1;i<=volumeDOFList.size();i++) {
	 			DOF dof = volumeDOFList.at(i);
				if(dof.getLocalIndex() == local-base)
					return dof.getGlobalIndex();
			}
		}
		return 0;
	}
	
	public void clearAllDOF() {
		if(nodeDOFList != null)
			nodeDOFList.clear();
	}
	
	////////////////////////////////////////////////////////////////////
	
	/**
	 * For 1D element
	 * @return
	 */
	public double getElementLength() {
		return Utils.computeLength(nodes.at(1), 
				nodes.at(nodes.size()));
	}
	/**
	 * For 2D element
	 * @return
	 */
	public double getElementArea() {
		double area = 0.0;
		if(this.eleDim == 2) {
			//�?使用Node，而是Vertex，而且�?�?�?定点逆时针
			VertexList vertices = this.vertices();
			if(vertices.size() == 3) {
				area = Utils.getTriangleArea(vertices);
			} else if(vertices.size() == 4) {
				area = Utils.getRectangleArea(vertices);
			} else {
				area = Utils.getPolygonArea(vertices);
			}
		}
		return area;
	}
	
	/**
	 * For 3D element
	 * @return
	 */
	public double getElementVolume() {
		VertexList vertices = this.vertices();
		if(this.eleDim == 3 && vertices.size() == 4)
			return Utils.getTetrahedronVolume(vertices);
		else
			//TODO
		return 0.0;
	}
	
	/**
	 * 
	 * TODO 获�?��?�元直径 2011-11-7
	 * @return
	 */
	public double getElementDiameter() {
		double dia = Utils.computeLength(nodes.at(1), nodes.at(2));
		for(int i=3;i<=nodes.size();i++) {
			double tmp = Utils.computeLength(nodes.at(i-1), nodes.at(i));
			if(tmp>dia) dia = tmp;
		}
		return dia;
	}
	
	/**
	 * Return true if exists an edge of this element that is on the border of the domain
	 * 判断是�?�边界�?�元，�?�至少存在�?�元的一边�?于区域边界上
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean isBorderElement() {
		if(this.eleDim == 2) {
			GeoEntity2D<EdgeLocal,NodeLocal> entity = 
				(GeoEntity2D<EdgeLocal,NodeLocal>)this.geoEntity;
			ObjList<EdgeLocal> edges = entity.getEdges();
			for(int i=1;i<=edges.size();i++) {
				if(edges.at(i).isBorderEdge())
					return true;
			}
		} else if(this.eleDim == 3) {
			GeoEntity3D<FaceLocal,EdgeLocal,NodeLocal> entity = 
				(GeoEntity3D<FaceLocal,EdgeLocal,NodeLocal>)this.geoEntity;
			ObjList<FaceLocal> faces = entity.getFaces();
			for(int i=1;i<=faces.size();i++) {
				if(faces.at(i).isBorderFace())
					return true;
			}
		}
		return false;
 	}
	
	public NodeList getNodesByType(NodeType nodeType) {
		NodeList l = new NodeList();
		for(int i=1;i<=nodes.size();i++) {
			if(nodes.at(i).getNodeType() == nodeType) {
				l.add(nodes.at(i));
			}
		}
		return l;
	}
	
	/**
	 * 在2D�?�元中，计算以node为顶点，其相邻两结点与之形�?的夹角角度
	 * 
	 * @param node: 必须为�?�元上的一个结点
	 * @return
	 */
	public double getAngleInElement2D(Node node) {
		int li = getLocalIndex(node);
		if(li == 0)
			throw new FutureyeException("Node("+node+") is NOT belongs to Element("+this+")!");
		int vn = this.geoEntity.getVertices().size();
		if(li <= vn) { //node在�?�元顶点上
			Node l = nodes.at(li-1<1?vn:li-1);
			Node r = nodes.at(li+1>vn?1:li+1);
			return Utils.computeAngle2D(l, node, r, node);
		} else if(this.nodes.size()/vn == 2){ //node在�?�元边上
			Node l = nodes.at(li - vn);
			//TODO 错误的：nodes.at(li - vn + 1)
			Node r = nodes.at( (li - vn + 1)>vn?1:(li - vn + 1));
			return Utils.computeAngle2D(l, node, r, node);
		} else {
			throw new FutureyeException("Can NOT compute angle: Node("+node+") Element("+this+")!");
		}
		
	}
	
	/**
	 * 获�?�以node为顶点，其相邻两结点与之形�?的两个�?��?的和�?��?
	 * @param node
	 * @return
	 */
	public Vector getDiagVectorInElement2D(Node node) {
		int li = getLocalIndex(node);
		SpaceVector v = new SpaceVector(2);
		v.set(1, 0.0);
		v.set(2, 0.0);
		if(li == 0)
			throw new FutureyeException("Node("+node+") is NOT belongs to Element("+this+")!");
		int vn = this.geoEntity.getVertices().size();
		if(li <= vn) { //node在�?�元顶点上
			Node l = nodes.at(li-1<1?vn:li-1);
			Node r = nodes.at(li+1>vn?1:li+1);
			v.set(1,l.coord(1)+r.coord(1)-2*node.coord(1));
			v.set(2,l.coord(2)+r.coord(2)-2*node.coord(2));
			return v;
		} else if(this.nodes.size()/vn == 2){ //node在�?�元边上
			//Node l = nodes.at(li - vn);
			//Node r = nodes.at( (li - vn + 1)>vn?1:(li - vn + 1));
			//TODO 结点在边上，返回边的法方�?�的�??方�?��?��?？
		} else {
			//throw new FutureyeException("Can NOT compute angle: Node("+node+") Element("+this+")!");
		}
		//如果无法处�?�返回0�?��?
		return v;
	}
	
	/**
	 * 适用于四�?�体�?�元
	 * 
	 * 计算以node为顶点，其�?�元内相邻三结点与之形�?的�?��?�?��?�三角形的�?�积，
	 * �?�用于判断是�?�内点，�?�当�?结点相邻所有�?�元上的结点与之形�?的�?��?�?��?�三角形的�?�积之和等于4*PI时为内点
	 * （�?��?�体：4*PI*r^2，�?�体积：(4/3)*PI*r^3）
	 * �?�数�?求：node为�?�元上的一个结点
	 * 
	 * @param node
	 * @return
	 */	
	public double getUnitSphereTriangleArea(Node node) {
		final int [][] ary = {{0,0,0},{2,3,4},{3,4,1},{4,1,2},{1,2,3}};
		int li = getLocalIndex(node);
		int vn = this.geoEntity.getVertices().size();
		if(li <= vn) {
			if(this.getGeoEntity3D().getTopology() instanceof TetrahedronTp) {
				return Utils.getSphereTriangleArea(1, node, 
						nodes.at(ary[li][0]), 
						nodes.at(ary[li][1]), 
						nodes.at(ary[li][2]));
			} else if(this.getGeoEntity3D().getTopology() instanceof HexahedronTp) {
				ObjList<Edge> edges = this.getGlobalEdges();
				Node[] findNode = new Node[3];
				int k=0;
				for(int i=1;i<=edges.size();i++) {
					Edge edge = edges.at(i);
					if(edge.beginNode().coordEquals(node))
						findNode[k++] = edge.endNode();
					else if(edge.endNode().coordEquals(node))
						findNode[k++] = edge.beginNode();
				}
				return Utils.getSphereTriangleArea(1, node, 
						findNode[0], 
						findNode[1], 
						findNode[2]);
			}
		}
		throw new FutureyeException("");
	}
	
	public ObjList<Edge> getGlobalEdges() {
		ObjList<Edge> rlt = new ObjList<Edge>();
		@SuppressWarnings("unchecked")
		GeoEntity3D<FaceLocal,EdgeLocal,NodeLocal> g = this.getGeoEntity3D();
		ObjList<FaceLocal> faces = g.getFaces();
		for(int i=1;i<=faces.size();i++) {
			ObjList<EdgeLocal> edges = faces.at(i).getEdges();
			for(int j=1;j<=edges.size();j++) {
				if(!rlt.contains(edges.at(j).globalEdge))
					rlt.add(edges.at(j).globalEdge);
			}
		}
		return rlt;
	}
	
	/**
	 * 获�?��??标�?��?�对象，用�?�得到Jacobian matrix和Jacobian determinant
	 * 
	 * @return
	 */
	public CoordinateTransform getCoordTrans() {
		return trans;
	}
	
	/**
	 * 
	 * 二维问题的一�?边界：[x,y]->[r]
	 * 
	 * 一维问题：[x]->[r] //TODO 现在对于一�?问题还是[x,y]两个�?��?，�?��?多y对应的是0，所有�?影�?计算结果
	 */
	public void updateJacobinLinear1D() {
		String[] fromVars = {"x","y"};
		String[] toVars = {"r"};
		
		//Coordinate transform and Jacbian on this border element
		//CoordinateTransform transBorder = new CoordinateTransform(fromVars,toVars);
		//transBorder.transformLinear1D(this);
		//transBorder.computeJacobianMatrix();
		//transBorder.computeJacobian1D();
		//jac = transBorder.getJacobian();
		//bugfix  transBorder->trans
		trans = new CoordinateTransform(fromVars,toVars);
		trans.transformLinear1D(this);
		trans.computeJacobianMatrix();
		trans.computeJacobian1D();		
		jac = trans.getJacobian();
		
		//TODO �?�?用这个，因为Jacobian有时会�?�??标值�?�?�而�?�?�
		//但是当Jacobian是常数时�?�以�??高计算速度
		//jac = FC.c(transBorder.getJacobian().value(null));
	}

	public void updateJacobinLinear2D() {
		//Coordinate transform and Jacbian on this element
		if(this.dim() == 2 && this.nodes.at(1).dim() == 2)
			trans = new CoordinateTransform(2);
		else if(this.dim() == 2 && this.nodes.at(1).dim() == 3)
			trans = new CoordinateTransform(3,2); //�?��?�?�元的�?��?�元是二维的，但是其上的结点是三维的
		else
			throw new FutureyeException("Element dim or node dim error!");
		
		trans.transformLinear2D(this);
		trans.computeJacobianMatrix();
		trans.computeJacobian2D();
		jac = trans.getJacobian();

//TODO adaptive的时候�?适用		
//		List<FunctionDerivable> funs = trans.getTransformFunction(
//				trans.getTransformShapeFunctionByElement(this)
//					);
//		trans.setTransformFunction(funs);
//		jac = trans.getJacobian2D();
	}
	

	public void updateJacobinLinear3D() {
		if(trans == null) trans = new CoordinateTransform(3);
		
		//Coordinate transform and Jacbian on this element
		if(getGeoEntity3D().getTopology() instanceof TetrahedronTp) {
			//trans.computeJacobianMatrix();
			trans.computeJacobian3DTetrahedron(this);
			jac = trans.getJacobian();
		}
		else {
			
			trans.transformLinear3D(this); //coordinate transform of this element
			trans.computeJacobianMatrix();
			trans.computeJacobian3D();
			jac = (MathFunc) trans.getJacobian();
		}
	}
	
	/**
	 * 判断�?�元维度，更新积分�??标�?��?�的Jacobin矩阵
	 */
	public void updateJacobin() {
		if(this.eleDim == 2)
			this.updateJacobinLinear2D();
		else if(this.eleDim ==3 )
			this.updateJacobinLinear3D();
		else if(this.eleDim == 1)
			this.updateJacobinLinear1D();
		else {
			FutureyeException ex = new FutureyeException("updateJacobin: dim="+this.eleDim);
			ex.printStackTrace();
			System.exit(-1);
		}
	}
	
	/**
	 * Call updateJacobin() if null returned
	 * 
	 * @return
	 */
	public MathFunc getJacobin() {
		if(jac == null) throw new FutureyeException("");
		return jac;
	}
	
	/**
	 * 获�?�边界�?�元，用于自然边界的边界积分
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ElementList getBorderElements() {
		ElementList el = new ElementList();
		if(this.eleDim == 2) {
			GeoEntity2D<EdgeLocal,NodeLocal> entity = 
				(GeoEntity2D<EdgeLocal,NodeLocal>)this.geoEntity;
			ObjList<EdgeLocal> edges = entity.getEdges();
			for(int i=1;i<=edges.size();i++) {
				if(edges.at(i).isBorderEdge())
					el.add(edges.at(i).changeToElement(this));
			}
		} else if(this.eleDim == 3) {
			GeoEntity3D<FaceLocal,EdgeLocal,NodeLocal> entity = 
				(GeoEntity3D<FaceLocal,EdgeLocal,NodeLocal>)this.geoEntity;
			ObjList<FaceLocal> faces = entity.getFaces();
			for(int i=1;i<=faces.size();i++) {
				if(faces.at(i).isBorderFace())
					el.add(faces.at(i).changeToElement());
			}
		}
		return el;
	}
	
	/**
	 * 获�?�边界结点类型
	 * @return
	 */
	public NodeType getBorderNodeType() {
		return getBorderNodeType(1);
	}
	
	/**
	 * For vector valued problems, return boundary type of component <tt>nVVFComponent</tt>
	 * <p>
	 * 对于�?��?值问题，�?个分�?在�?�一边界上的类型�?一定相�?�，
	 * 该函数返回分�?<tt>nVVFComponent</tt>对应的边界类型
	 * 
	 * @param nVVFComponent
	 * @return
	 */
	public NodeType getBorderNodeType(int nVVFComponent) {
		if(this.eleDim == 2) {
			//从一个三维�?�元的�?�构造而�?�的Element对象的几何实体
			//是一个边全局�?�象，获�?�该�?�对象的边界类型
			Face face = (Face)this.geoEntity;
			return face.getBorderType(nVVFComponent);
		} else if(this.eleDim == 1) {
			//从一个二维�?�元的边构造而�?�的Element对象的几何实体
			//是一个边全局对象，获�?�该边对象的边界类型
			Edge edge = (Edge)this.geoEntity;
			return edge.getBorderType(nVVFComponent);
		} else {
			throw new FutureyeException("this.eleDim="+this.eleDim);
		}
	}
	
	/**
	 * 根�?�全局结点node返回该结点在�?�元的局部索引（编�?�），
	 * 如果该结点�?在�?�元中，返回0
	 * 
	 * 注�?：如果改�?�了�?�元的geoEntity，必须调用applyChange()，
	 * �?�则该函数�?�能返回错误的结果
	 * 
	 * @param node
	 * @return
	 */
	public int getLocalIndex(Node node) {
		for(int i=1;i<=nodes.size();i++) {
			if(node.coordEquals(nodes.at(i)))
				return i;
		}
		return 0;
	}
	
	public Node getNode(Point p) {
		for(int i=1;i<=nodes.size();i++) {
			if(p.coordEquals(nodes.at(i)))
				return nodes.at(i);
		}
		return null;		
	}
	
	/**
	 * 判断结点node是�?�属于该�?�元
	 * 
	 * @param node
	 * @return
	 */
	public boolean isBelongToElement(Node node) {
		return this.getLocalIndex(node)>0;
	}
	
	/**
	 * 判断一个�??标点是�?�在�?�元内部（包括�?�元边界和�?�元结点），适用于任�?维度
	 * 
	 * @param coord
	 * @return
	 */
	public boolean isCoordInElement(double[] coord) {
		Vertex v = new Vertex().set(0, coord);
		for(int i=1;i<=nodes.size();i++) {
			if(nodes.at(i).coordEquals(v))
				return true;
		}
		if(this.eleDim == 1) {
			return Utils.isPointOnLine(
						this.vertices().at(1), 
						this.vertices().at(2), 
						new Vertex().set(0,coord)
					);
		} else if(this.eleDim == 2) {
			for(int i=2;i<=nodes.size();i++) {
				boolean b = Utils.isPointOnLineSegmentNoEndingPoint(nodes.at(i-1), nodes.at(i), v);
				if(b) 
					return true;
			}
			//计算以 coord 为顶点，分别以�?�元顶点为方�?�的夹角，如果总和为360度，则是内点。
			double angle = 0.0;
			ObjList<EdgeLocal> edges = this.edges();
			for(int i=1;i<=edges.size();i++) {
				EdgeLocal edge = edges.at(i);
				angle += Utils.computeAngle2D(
									edge.beginNode(), v, 
									edge.endNode(),v
									);
			}
			if(Math.abs(angle-Math.PI*2) < Constant.angleEps)
				return true;
		} else if(this.eleDim == 3) {
			ObjList<FaceLocal> faces = this.faces();
			double angle = 0.0;
			for(int i=1;i<=faces.size();i++) {
				FaceLocal face = faces.at(i);
				VertexList vs = face.getVertices();
				//多�?�体的�?��?�积分解为三角形�?��?�积的和
				//e.g. 五边形SphereArea(1,2,3,4,5) = SA(1,2,3)+SA(1,3,4)+SA(1,4,5)
				for(int j=3;j<=vs.size();j++) {
					angle += Utils.getSphereTriangleArea(1, v, 
							vs.at(1), vs.at(j-1), vs.at(j));
				}
			}
			if(Math.abs(angle-4*Math.PI) <= Constant.angleEps)
				return true;
		} else {
			FutureyeException ex = new FutureyeException("Error: isCoordInElement");
			ex.printStackTrace();
			System.exit(0);
		}
		return false;
	}
	
	public String toString() {
		String s = "GE";
		if(globalIndex > 0)
			s += globalIndex;
		s += "( ";
		for(int i=1;i<=nodes.size();i++) {
			String st = "";
			ObjVector<NodeType> nodeTypes = nodes.at(i).nodeTypes;
			if(nodeTypes==null || nodeTypes.size()==0)
				st="U"; //Undefined
			else {
				for(int j=1;j<=nodeTypes.size();j++) {
					NodeType nodeType = nodeTypes.at(j);
					if(nodeType == NodeType.Inner)
						st += "I";
					else if(nodeType == NodeType.Dirichlet)
						st += "D";
					else if(nodeType == NodeType.Neumann)
						st += "N";
					else if(nodeType == NodeType.Robin)
						st += "R";
					else
						st += "U"; //Undefined
				}
			}
			s += nodes.at(i).globalIndex + st + " ";
		}
		return s+")";
	}
	
	/**
	 *将结点编�?�调整为逆时针顺�?
	 */
	//2011-02-19
//	public void adjustVerticeToCounterClockwise() {
//		VertexList list = this.vertices();
//		int dim = list.at(1).dim();
//		if(dim == 2) {
//			if(list.size() == 3 || list.size() == 4) {
//				Vertex v1 = list.at(1);
//				Vertex v2 = list.at(2);
//				Vertex v3 = list.at(3);
//				SpaceVector v12 = null, v13 = null, cp = null;
//				if(v1.dim == 2) {
//					v12 = new SpaceVector(
//							v2.coord(1)-v1.coord(1), v2.coord(2)-v1.coord(2), 0.0);
//					v13 = new SpaceVector(
//							v3.coord(1)-v1.coord(1), v3.coord(2)-v1.coord(2), 0.0);
//		
//				} else if(v1.dim == 3) {
//					v12 = new SpaceVector(
//							v2.coord(1)-v1.coord(1), v2.coord(2)-v1.coord(2), v2.coord(3)-v1.coord(3)
//							);
//					v13 = new SpaceVector(
//							v3.coord(1)-v1.coord(1), v3.coord(2)-v1.coord(2), v3.coord(3)-v1.coord(3)
//							);			
//				}
//				cp = v12.crossProduct(v13);
//				//�?�乘�?于0，编�?�是顺时针，需�?改为逆时针
//				if(cp.get(3) < 0.0) {
//					VertexList tmp = new VertexList();
//					int n = list.size();
//					for(int i=n;i>=1;i--) {
//						list.at(i).localIndex = n-i+1;
//						tmp.add(list.at(i));
//					}
//					this.geoEntity.addAllVertices(tmp);
//					//TODO?
//					applyChange();
//				}
//			}
//		} else if(dim == 3) {
//			//TODO
//		}
//	}
	public boolean adjustVerticeToCounterClockwise() {
		VertexList vertices = this.vertices();
		int dim = vertices.at(1).dim();
		if(dim == 2) {
			double area = this.getElementArea();
			if(area < 0) {
				VertexList tmp = new VertexList();
				int n = vertices.size();
				for(int i=n;i>=1;i--) {
					vertices.at(i).setAllLocalIndex(n-i+1);
					tmp.add(vertices.at(i));
				}
				this.geoEntity.addAllVertices(tmp);
				this.applyChange();
				return true;
			}
		} else if(dim == 3) {
			double volume = this.getElementVolume();
			if(volume < 0) {
				VertexList tmp = new VertexList();
				int n = vertices.size();
				for(int i=n;i>=1;i--) {
					vertices.at(i).setAllLocalIndex(n-i+1);
					tmp.add(vertices.at(i));
				}
				this.geoEntity.addAllVertices(tmp);
				this.applyChange();
				
				volume = this.getElementVolume();
				if(volume < 0)
					throw new FutureyeException("Failed to adjustVerticeToCounterClockwise");
				return true;
			}
		}
		return false;
	}	
	
	public void addNeighborElement(Element nb) {
		for(int i=1;i<=this.neighbors.size();i++) {
			//TODO ??? nb.globalIndex ???
			if(nb.equals(this.neighbors.at(i)))
				return;
		}
		this.neighbors.add(nb);
	}	
	
	@SuppressWarnings("unchecked")
	public boolean containsEdge(Point p1, Point p2) {
		if(eleDim == 2) {
			if(getGeoEntity2D().containsEdge(p1, p2))
				return true;
		} else if(eleDim == 3) {
			if(getGeoEntity3D().containsEdge(p1, p2))
				return true;
		}
		return false;
	}
	
	////////////////////////////////////////////////////////////////////
	/**
	 * 自适应网格加密用�?��?存从该�?�元加密出�?�的�?网格�?�元
	 */
	public ElementList childs = null;
	public Element parent = null; //this field is also used to store the parent element of a border element
	//加密层次
	protected int level = 1;
	
	
	/**
	 * 判断�?�元是�?�加密
	 * @return
	 */
	public boolean isRefined() {
		return this.childs != null;
	}
	
	public int getLevel() {
		return this.level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public NodeList getHangingNode() {
		NodeList rlt = new NodeList();
		for(int i=1;i<=nodes.size();i++) {
			Node node = nodes.at(i);
			if(node instanceof NodeRefined) { 
				NodeRefined nf = (NodeRefined)node;
				if(nf.isHangingNode())
					rlt.add(nf);
			}
		}
		return rlt;
	}
	////////////////////////////////////////////////////////////////////
	
	
	public void printDOFInfo() {
		DOFList DOFs = getAllDOFList(DOFOrder.NEFV);
		int nDOFs = DOFs.size();
		for(int i=1;i<=nDOFs;i++) {
			DOF dofI = DOFs.at(i);
			VectorShapeFunction sfI = dofI.getVSF();
			int nLocalRow = dofI.getLocalIndex();
			int nGlobalRow = dofI.getGlobalIndex();
			System.out.print(String.format("E%d DOFIdx:L=%02d, G=%d SF=", 
					this.globalIndex, nLocalRow, nGlobalRow));
			System.out.println(sfI);
		}
	}

	/**
	 * Return the coordinates of all nodes
	 * in way like [x1,x2,x3,y1,y2,y3]
	 */
	public double[] getNodeCoords() {
		int dim = nodes.at(1).dim;
		double[] rlt = new double[nodes.size()*dim];
		int index = 0;
		for(int j=0; j<dim; j++) {
			for(Node n : nodes) {
				rlt[index++] = n.coords[j];
			}
		}
		return rlt;
	}
}
