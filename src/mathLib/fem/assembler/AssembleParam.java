package mathLib.fem.assembler;

import mathLib.fem.core.Element;
import mathLib.fem.core.Node;
import mathLib.fem.core.intf.VecFiniteElement;

/**
 * Parameters passed into functions in the process of assembly
 *
 */
public class AssembleParam {
	public VecFiniteElement fe;
	public Element element; //current element which is being assembled
	public int trialDOFIdx; //local trial function index
	public int testDOFIdx;  //local test function index

	public Node node; //In some cases, node is used instead of array of coordinates

	public AssembleParam(Element e, int i, int j) {
		this.element = e;
		this.trialDOFIdx = i;
		this.testDOFIdx = j;
	}

	public AssembleParam(Element e, int i, int j, Node n) {
		this.element = e;
		this.trialDOFIdx = i;
		this.testDOFIdx = j;
		this.node = n;
	}

}
