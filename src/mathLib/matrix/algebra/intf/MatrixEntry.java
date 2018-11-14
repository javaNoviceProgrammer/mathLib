package mathLib.matrix.algebra.intf;

/**
 * An entry of a sparse matrix. Returned by the iterators over a sparse matrix
 * 
 * 
 */
public interface MatrixEntry {
    /**
     * Return the current number of row (row index)
     */
    int getRow();

    /**
     * Return the current number of column (column index)
     */
    int getCol();

    /**
     * Return the current value
     */
    double getValue();

    /**
     * Set the current value
     */
    void setValue(double value);
}