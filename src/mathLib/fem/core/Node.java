package mathLib.fem.core;

import mathLib.fem.util.FutureyeException;
import mathLib.fem.util.container.ElementList;
import mathLib.fem.util.container.ObjVector;

/**
 * Global finite element node class
 *
 */
public class Node implements Point {

	public int globalIndex = 0;	
	public ElementList belongToElements = null;
	public NodeList neighbors = null;
	protected int dim = 0;
	protected double[] coords = new double[3];
	protected ObjVector<NodeType> nodeTypes = null;
	protected int refineLevel = 1;

	public Node() {
	}
	
	public Node(int dim) {
		this.dim = dim;
	}
	
	public Node(int globalIndex, double x, double ...coords) {
		this.globalIndex = globalIndex;
		this.coords[0] = x;
		if(coords!=null && coords.length > 0) {
			this.dim = 1+coords.length;
			for(int i=0;i<coords.length;i++)
				this.coords[1+i] = coords[i];
		} else {
			this.dim = 1;
		}
	}
	
	/**
	 * Set node globalIndex and coordinates
	 * 
	 * @param globalIndex
	 * @param coords
	 * @return
	 */
	public Node set(int globalIndex, double ...coords) {
		this.globalIndex = globalIndex;
		if(coords!=null && coords.length > 0) {
			this.dim = coords.length;
			for(int i=0;i<dim;i++)
				this.coords[i] = coords[i];
		}
		return this;
	}
	
	@Override
	public int getIndex() {
		return this.globalIndex;
	}
	
	@Override
	public int dim() {
		return dim;
	}
	
	@Override
	public double coord(int index) {
		return coords[index-1];
	}
	
	@Override
	public double[] coords() {
		double[] rlt;
		if(this.dim < 3) {
			rlt = new double[dim];
			for(int i=0;i<dim;i++)
				rlt[i] = this.coords[i];
		} else
			rlt = this.coords;
		return rlt;
	}
	
	public void setCoord(int index,double val) {
		coords[index-1] = val;
	}
	
	@Override
	public boolean coordEquals(Point p) {
		if(this.dim != p.dim())
			return false;
		for(int i=1;i<=this.dim;i++) {
			if(Math.abs(this.coord(i)-p.coord(i)) > Constant.meshEps)
				return false;
		}
		return true;
	}
	
	/**
	 * <P>Return true if the node is a inner node</P>
	 * 
	 * <B>Note:</B>
	 * Function <code>Mesh.computeNodeBelongsToElements()</code> or 
	 * <code>Mesh.markBorderNode()</code> must be called before calling
	 * this function
	 * 
	 * @return
	 */
	public boolean isInnerNode() {
		if(this.getNodeType() == null) {
			
			if(belongToElements==null || belongToElements.size()==0)
				throw new FutureyeException("Call Mesh.computeNodeBelongsToElements() first!");
			double sum = 0.0;
			double coef = 0;
			if(dim==1) { //1D 
				if(belongToElements.size()==2)
					return true;
				else
					return false;
			} else if(dim==2) {//2D
				coef = 2;
				for(int j=1;j<=belongToElements.size();j++) {
					sum += belongToElements.at(j).getAngleInElement2D(this);
				}
			} else if(dim==3) {//3D 
				coef = 4;
				for(int j=1;j<=belongToElements.size();j++) {
					sum += belongToElements.at(j).getUnitSphereTriangleArea(this);
				}
			}
			if(Math.abs(sum-coef*Math.PI) <= Constant.meshEps)//2D=2*PI，3D=4*PI
				return true;
			else
				return false;
		} else {//Use the result of Mesh.markBorderNode() 
			
			for(int i=1;i<=this.nodeTypes.size();i++)
				if(this.nodeTypes.at(i) == null ||
						this.nodeTypes.at(i) != NodeType.Inner)
					return false;
			return true;
		}
	}
	
	/**
	 * Get node type for scalar valued function problems
	 * 
	 * @return
	 */
	public NodeType getNodeType() {
		if(nodeTypes == null) 
			return null;
		return nodeTypes.at(1);
	}
	
	/**
	 * Get node type at component <tt>nVVFComponent</tt> for vector valued function problems 
	 * 
	 * @param nVVFComponent: Component of vector valued functions
	 * @return
	 */
	public NodeType getNodeType(int nVVFComponent) {
		if(nodeTypes == null) 
			return null;
		return nodeTypes.at(nVVFComponent);
	}
	
	/**
	 * Set node type for scalar valued function problems
	 * 
	 * @param nodeType: node type
	 */
	public void setNodeType(NodeType nodeType) {
		if(nodeTypes == null) 
			nodeTypes = new ObjVector<NodeType>();
		nodeTypes.setSize(1);
		nodeTypes.set(1,nodeType);
	}
	
	/**
	 * Set node type at component <tt>nVVFComponent<tt> for vector valued function problems
	 * 
	 * @param nVVFComponent: Component of vector valued function
	 * @param nodeType: Node type
	 */
	public void setNodeType(int nVVFComponent, NodeType nodeType) {
		if(nodeTypes == null) 
			nodeTypes = new ObjVector<NodeType>();
		if(nVVFComponent<=0) {
			throw new FutureyeException("nVVFComponent should be greater than 1!");
		}
		if(nodeTypes.size()<nVVFComponent)
			nodeTypes.setSize(nVVFComponent);
		nodeTypes.set(nVVFComponent,nodeType);
	}
	
	/**
	 * Add a element <code>e</code> which contains this node to 
	 * <code>this.belongToElements</code> list. 
	 * Duplicate element will NOT be added.
	 * 
	 * @param e
	 */
	public void addBelongToElements(Element e) {
		if(belongToElements == null)
			belongToElements = new ElementList();
		for(int i=1;i<=belongToElements.size();i++) {
			Element es = belongToElements.at(i);
			if(e.equals(es))//对象直接比较
				return;
			else if(e.globalIndex != 0 && e.globalIndex == es.globalIndex)//全局索引比较
				return;
		}
		belongToElements.add(e);
	}
	
	/**
	 * Add a <code>node</code> which is the neighbor of this node to 
	 * <code>this.neighbors</code> list.
	 * Duplicate node will NOT be added.
	 * 
	 * @param node
	 */
	public void addNeighbors(Node node) {
		if(neighbors == null)
			neighbors = new NodeList();
		for(int i=1;i<=neighbors.size();i++) {
			Node ni = neighbors.at(i);
			if(ni.equals(node))
				return;
			else if(ni.globalIndex != 0 && ni.globalIndex == node.globalIndex)
				return;
		}
		neighbors.add(node);		
	}
	
	public void clearBelongToElements() {
		if(belongToElements != null)
			belongToElements.clear();
	}
	
	public void clearNeighbors() {
		if(neighbors != null)
			neighbors.clear();
	}
	
	/**
	 * Get node level number from refined mesh
	 * 
	 * @return
	 */
	public int getRefineLevel() {
		return this.refineLevel;
	}
	
	/**
	 * Set node level number when doing mesh refinement
	 * 
	 * @param level
	 */
	public void setRefineLevel(int level) {
		this.refineLevel = level;
	}
	
	/**
	 * Return node information. Prefix "GN" means "Global Node"
	 * 
	 */
	public String toString()  {
		String s = "GN"+globalIndex+"( ";
		for(int i=0;i<dim;i++)
			s += String.valueOf(coords[i])+" ";
		return s+")";
	}
	
	@Override
    public boolean equals(Object obj) {
        if(super.equals(obj)) {
        	return true;
        } else {
        	if(obj instanceof Node) {
        		Node n2 = (Node)obj;
        		//if(globalIndex != 0 && globalIndex == n2.globalIndex && dim == n2.dim) {
        		if(globalIndex == n2.globalIndex && dim == n2.dim) {
        			for(int i=0;i<dim;i++) {
        				if(Math.abs(this.coords[i] - n2.coords[i]) > Constant.meshEps) {
        					throw new FutureyeException(
        							this+" and "+n2+" should be equal, please check!");
        				}
        			}
        			return true;
        		}
        	}
        }
        return false;
    }
    
    @Override
    public int hashCode() {
    	if(globalIndex != 0) 
    		return globalIndex;
    	else 
    		return super.hashCode();
    }
}
 