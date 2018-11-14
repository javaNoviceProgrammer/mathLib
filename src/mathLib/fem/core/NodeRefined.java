package mathLib.fem.core;

public class NodeRefined extends Node {
	public NodeList constrainNodes = new NodeList();
	
	public NodeRefined(int dim) {
		super(dim);
		this.refineLevel = 2;
	}
	
	public void addConstrainNode(Node node) {
		for(int i=1;i<=this.constrainNodes.size();i++)
			if(node.equals(this.constrainNodes.at(i)))
				return;
		constrainNodes.add(node);
	}
	
	public void clearConstrainNodes() {
		this.constrainNodes.clear();
	}	
	
	public boolean isHangingNode() {
		return this.constrainNodes.size()>0;
	}
	

}
