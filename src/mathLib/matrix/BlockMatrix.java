package mathLib.matrix;

public class BlockMatrix {

	Matrix[] blocks ;
	Matrix compose ;
	int row, column ;
	
	BlockMatrix(int row, int column, Matrix... args){
		this.row = row ;
		this.column = column ;
		this.blocks = args ;
		if(row*column != args.length)
			throw new IllegalArgumentException("Dimensions don't agree") ;
		buildBlockMatrix() ;
	}
	
	private void buildBlockMatrix() {
		
	}
	
	
}
