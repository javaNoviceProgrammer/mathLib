package mathLib.matrix.algebra.solver.external;

import java.util.Map;
import java.util.Map.Entry;

import mathLib.matrix.algebra.CompressedRowMatrix;
import mathLib.matrix.algebra.intf.MatrixEntry;
import mathLib.matrix.algebra.intf.SparseMatrix;
import mathLib.matrix.algebra.intf.SparseVector;
import no.uib.cipr.matrix.sparse.BiCG;
import no.uib.cipr.matrix.sparse.BiCGstab;
import no.uib.cipr.matrix.sparse.CG;
import no.uib.cipr.matrix.sparse.CGS;
import no.uib.cipr.matrix.sparse.GMRES;
import no.uib.cipr.matrix.sparse.IterativeSolverNotConvergedException;
import no.uib.cipr.matrix.sparse.QMR;

/**
 * Matrix-Toolkits-Java(MTJ) interface
 * 
 *
 */
public class SolverMTJ {
	public boolean debug = false;
	
	public static int[][] getColIndex(SparseMatrix A) {
		CompressedRowMatrix m = new CompressedRowMatrix(A,false);
		return m.getColIndex();
	}
	
	/**
	 * CG solver
	 * 
	 */
	public Vector solveCG(SparseMatrix A, SparseVector b, 
			SparseVector x) {
		int dim = b.getDim();
		no.uib.cipr.matrix.sparse.SparseVector template = 
			new no.uib.cipr.matrix.sparse.SparseVector(dim);
		CG sol = new CG(template);
		
		no.uib.cipr.matrix.sparse.CompRowMatrix A2 = 
			new no.uib.cipr.matrix.sparse.CompRowMatrix(
					A.getRowDim(),A.getColDim(),getColIndex(A));
		for(MatrixEntry e : A) {
			A2.set(e.getRow()-1, e.getCol()-1, e.getValue());
		}
		
		no.uib.cipr.matrix.sparse.SparseVector b2 = 
			new no.uib.cipr.matrix.sparse.SparseVector(dim);
		no.uib.cipr.matrix.sparse.SparseVector x2 = 
			new no.uib.cipr.matrix.sparse.SparseVector(dim);
		Map<Integer,Double> bData = b.getAll();
		for(Entry<Integer,Double> ety : bData.entrySet()) {
			b2.set(ety.getKey()-1,ety.getValue());
		}
		for(int i=0;i<dim;i++) {
			x2.set(i, 0.01);
		}
		
		try {
			long begin = System.currentTimeMillis();
			sol.solve(A2, b2, x2);
			long end = System.currentTimeMillis();
			System.out.println(String.format("Iter=%03d Time=%dms",
					sol.getIterationMonitor().iterations(),(end-begin)));

		} catch (IterativeSolverNotConvergedException e) {
			e.printStackTrace();
		}
		
		
		for(int i=1;i<=dim;i++) {
			x.set(i, x2.get(i-1));
		}
		
		return x;
    }
	
	public Vector solveCGS(SparseMatrix A, SparseVector b, 
			SparseVector x) {
		int dim = b.getDim();
		no.uib.cipr.matrix.sparse.SparseVector template = 
			new no.uib.cipr.matrix.sparse.SparseVector(dim);
		CGS sol = new CGS(template);
		
		no.uib.cipr.matrix.sparse.CompRowMatrix A2 = 
			new no.uib.cipr.matrix.sparse.CompRowMatrix(
					A.getRowDim(),A.getColDim(),getColIndex(A));
		
		Map<Integer, Map<Integer, Double>> data = A.getAll();
		for(Entry<Integer,Map<Integer,Double>> row : data.entrySet()) {
			int nRow = row.getKey();
			for(Entry<Integer,Double> col : row.getValue().entrySet()) {
				int nCol = col.getKey();
				A2.set(nRow-1, nCol-1, col.getValue());
			}
			row.getValue().clear();
		}
		data.clear();
		
		no.uib.cipr.matrix.sparse.SparseVector b2 = 
			new no.uib.cipr.matrix.sparse.SparseVector(dim);
		no.uib.cipr.matrix.sparse.SparseVector x2 = 
			new no.uib.cipr.matrix.sparse.SparseVector(dim);
		Map<Integer,Double> bData = b.getAll();
		for(Entry<Integer,Double> ety : bData.entrySet()) {
			b2.set(ety.getKey()-1,ety.getValue());
		}
		for(int i=0;i<dim;i++) {
			x2.set(i, 0.01);
		}
		
		try {
			long begin = System.currentTimeMillis();
			sol.solve(A2, b2, x2);
			long end = System.currentTimeMillis();
			System.out.println(String.format("Iter=%03d Time=%dms",
					sol.getIterationMonitor().iterations(),(end-begin)));

		} catch (IterativeSolverNotConvergedException e) {
			e.printStackTrace();
		}
		
		
		for(int i=1;i<=dim;i++) {
			x.set(i, x2.get(i-1));
		}
		
		return x;
    }
	
	public Vector solveGMRES(SparseMatrix A, SparseVector b, 
			SparseVector x) {
		int dim = b.getDim();
		no.uib.cipr.matrix.sparse.SparseVector template = 
			new no.uib.cipr.matrix.sparse.SparseVector(dim);
		GMRES sol = new GMRES(template);
		
		no.uib.cipr.matrix.sparse.CompRowMatrix A2 = 
			new no.uib.cipr.matrix.sparse.CompRowMatrix(
					A.getRowDim(),A.getColDim(),getColIndex(A));
		
		Map<Integer, Map<Integer, Double>> data = A.getAll();
		for(Entry<Integer,Map<Integer,Double>> row : data.entrySet()) {
			int nRow = row.getKey();
			for(Entry<Integer,Double> col : row.getValue().entrySet()) {
				int nCol = col.getKey();
				A2.set(nRow-1, nCol-1, col.getValue());
			}
			row.getValue().clear();
		}
		data.clear();
		
		no.uib.cipr.matrix.sparse.SparseVector b2 = 
			new no.uib.cipr.matrix.sparse.SparseVector(dim);
		no.uib.cipr.matrix.sparse.SparseVector x2 = 
			new no.uib.cipr.matrix.sparse.SparseVector(dim);
		Map<Integer,Double> bData = b.getAll();
		for(Entry<Integer,Double> ety : bData.entrySet()) {
			b2.set(ety.getKey()-1,ety.getValue());
		}
		for(int i=0;i<dim;i++) {
			x2.set(i, 0.01);
		}
		
		try {
			long begin = System.currentTimeMillis();
			sol.solve(A2, b2, x2);
			long end = System.currentTimeMillis();
			if(debug) {
				System.out.println(String.format("Iter=%03d Time=%dms",
						sol.getIterationMonitor().iterations(),(end-begin)));
			}
		} catch (IterativeSolverNotConvergedException e) {
			e.printStackTrace();
		}
		
		
		for(int i=1;i<=dim;i++) {
			x.set(i, x2.get(i-1));
		}
		
		return x;
    }

	
	public Vector solveBiCG(SparseMatrix A, SparseVector b, 
			SparseVector x) {
		int dim = b.getDim();
		no.uib.cipr.matrix.sparse.SparseVector template = 
			new no.uib.cipr.matrix.sparse.SparseVector(dim);
		BiCG sol = new BiCG(template);
		
		no.uib.cipr.matrix.sparse.CompRowMatrix A2 = 
			new no.uib.cipr.matrix.sparse.CompRowMatrix(
					A.getRowDim(),A.getColDim(),getColIndex(A));
		
		Map<Integer, Map<Integer, Double>> data = A.getAll();
		for(Entry<Integer,Map<Integer,Double>> row : data.entrySet()) {
			int nRow = row.getKey();
			for(Entry<Integer,Double> col : row.getValue().entrySet()) {
				int nCol = col.getKey();
				A2.set(nRow-1, nCol-1, col.getValue());
			}
			row.getValue().clear();
		}
		data.clear();
		
		no.uib.cipr.matrix.sparse.SparseVector b2 = 
			new no.uib.cipr.matrix.sparse.SparseVector(dim);
		no.uib.cipr.matrix.sparse.SparseVector x2 = 
			new no.uib.cipr.matrix.sparse.SparseVector(dim);
		Map<Integer,Double> bData = b.getAll();
		for(Entry<Integer,Double> ety : bData.entrySet()) {
			b2.set(ety.getKey()-1,ety.getValue());
		}
		for(int i=0;i<dim;i++) {
			x2.set(i, 0.01);
		}
		
		try {
			long begin = System.currentTimeMillis();
			sol.solve(A2, b2, x2);
			long end = System.currentTimeMillis();
			if(debug) {
				System.out.println(String.format("Iter=%03d Time=%dms",
						sol.getIterationMonitor().iterations(),(end-begin)));
			}
		} catch (IterativeSolverNotConvergedException e) {
			e.printStackTrace();
		}
		
		
		for(int i=1;i<=dim;i++) {
			x.set(i, x2.get(i-1));
		}
		
		return x;
    }
	
	public Vector solveBiCGstab(SparseMatrix A, SparseVector b, 
			SparseVector x) {
		int dim = b.getDim();
		no.uib.cipr.matrix.sparse.SparseVector template = 
			new no.uib.cipr.matrix.sparse.SparseVector(dim);
		BiCGstab sol = new BiCGstab(template);
		
		no.uib.cipr.matrix.sparse.CompRowMatrix A2 = 
			new no.uib.cipr.matrix.sparse.CompRowMatrix(
					A.getRowDim(),A.getColDim(),getColIndex(A));
		
		Map<Integer, Map<Integer, Double>> data = A.getAll();
		for(Entry<Integer,Map<Integer,Double>> row : data.entrySet()) {
			int nRow = row.getKey();
			for(Entry<Integer,Double> col : row.getValue().entrySet()) {
				int nCol = col.getKey();
				A2.set(nRow-1, nCol-1, col.getValue());
			}
			row.getValue().clear();
		}
		data.clear();
		
		no.uib.cipr.matrix.sparse.SparseVector b2 = 
			new no.uib.cipr.matrix.sparse.SparseVector(dim);
		no.uib.cipr.matrix.sparse.SparseVector x2 = 
			new no.uib.cipr.matrix.sparse.SparseVector(dim);
		Map<Integer,Double> bData = b.getAll();
		for(Entry<Integer,Double> ety : bData.entrySet()) {
			b2.set(ety.getKey()-1,ety.getValue());
		}
		for(int i=0;i<dim;i++) {
			x2.set(i, 0.01);
		}
		
		try {
			long begin = System.currentTimeMillis();
			sol.solve(A2, b2, x2);
			long end = System.currentTimeMillis();
			if(debug) {
				System.out.println(String.format("Iter=%03d Time=%dms",
						sol.getIterationMonitor().iterations(),(end-begin)));
			}
		} catch (IterativeSolverNotConvergedException e) {
			e.printStackTrace();
		}
		
		
		for(int i=1;i<=dim;i++) {
			x.set(i, x2.get(i-1));
		}
		
		return x;
    }	
	
	public Vector solveQMR(SparseMatrix A, SparseVector b, 
			SparseVector x) {
		int dim = b.getDim();
		no.uib.cipr.matrix.sparse.SparseVector template = 
			new no.uib.cipr.matrix.sparse.SparseVector(dim);
		QMR sol = new QMR(template);
		
		no.uib.cipr.matrix.sparse.CompRowMatrix A2 = 
			new no.uib.cipr.matrix.sparse.CompRowMatrix(
					A.getRowDim(),A.getColDim(),getColIndex(A));
		
		Map<Integer, Map<Integer, Double>> data = A.getAll();
		for(Entry<Integer,Map<Integer,Double>> row : data.entrySet()) {
			int nRow = row.getKey();
			for(Entry<Integer,Double> col : row.getValue().entrySet()) {
				int nCol = col.getKey();
				A2.set(nRow-1, nCol-1, col.getValue());
			}
			row.getValue().clear();
		}
		data.clear();
		
		no.uib.cipr.matrix.sparse.SparseVector b2 = 
			new no.uib.cipr.matrix.sparse.SparseVector(dim);
		no.uib.cipr.matrix.sparse.SparseVector x2 = 
			new no.uib.cipr.matrix.sparse.SparseVector(dim);
		Map<Integer,Double> bData = b.getAll();
		for(Entry<Integer,Double> ety : bData.entrySet()) {
			b2.set(ety.getKey()-1,ety.getValue());
		}
		for(int i=0;i<dim;i++) {
			x2.set(i, 0.01);
		}
		
		try {
			long begin = System.currentTimeMillis();
			sol.solve(A2, b2, x2);
			long end = System.currentTimeMillis();
			if(debug) {
				System.out.println(String.format("Iter=%03d Time=%dms",
						sol.getIterationMonitor().iterations(),(end-begin)));
			}
		} catch (IterativeSolverNotConvergedException e) {
			e.printStackTrace();
		}
		
		
		for(int i=1;i<=dim;i++) {
			x.set(i, x2.get(i-1));
		}
		
		return x;
    }	
}
