package mathLib.matrix;

public class BlockMatrix {

	Matrix[][] blocks ;
	Matrix compose ;
	int row, column ;
	
	BlockMatrix(int row, int column){
		this.row = row ;
		this.column = column ;
		blocks = new Matrix[row][column] ;
	}
	
	public void add(Matrix A, int i, int j) {
		blocks[i][j] = A ;
	}
	
	public boolean checkRowCompatible() {
		boolean rowCompatible = true ;
		for(int i=0; i<row; i++) {
			for(int j=0; j<column-1; j++)
				if(blocks[i][j].getRowDim() != blocks[i][j+1].getRowDim()) {
					rowCompatible = false ;
				}
		}
		return rowCompatible ;
	}
	
	
	
	
	
	
	
}
