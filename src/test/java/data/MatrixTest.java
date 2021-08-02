package data;

import com.example.ptn.data.Matrix;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MatrixTest {
    Matrix testMatrix;

    @Before
    public void setUp() {
        testMatrix = new Matrix(2, 2, new double[][]{{1, 2}, {3, 4}});
    }

    @Test
    public void add2() {
        Matrix expected = new Matrix(2, 2, new double[][]{{3, 4}, {5, 6}});
        testMatrix.add(2);
        assertTrue(Matrix.equals(expected, testMatrix));
    }

    @Test
    public void addTheSameMatrix() {
        Matrix expected = new Matrix(2, 2, new double[][]{{2, 4}, {6, 8}});
        testMatrix.add(new Matrix(2, 2, new double[][]{{1, 2}, {3, 4}}));
        assertTrue(Matrix.equals(expected, testMatrix));
    }

    @Test
    public void addWrongDimension() {
        Matrix result = testMatrix.add(new Matrix(1, 2, new double[][]{{1, 2}}));
        assertTrue(null == result);
    }

    @Test
    public void subtractTheSame() {
        Matrix expected = new Matrix(2, 2, new double[][]{{0, 0}, {0, 0}});
        Matrix result = Matrix.subtract(testMatrix, new Matrix(2, 2, new double[][]{{1, 2}, {3, 4}}));
        assertTrue(Matrix.equals(expected, result));
    }

    @Test
    public void multiplyByRule() {
        Matrix toMultiply = new Matrix(2, 3, new double[][]{{0, 9, 8}, {7, 6, 5}});
        Matrix expected = new Matrix(2, 3, new double[][]{{14, 21, 18}, {28, 51, 44}});
        Matrix result = Matrix.multiply(testMatrix, toMultiply);
        assertTrue(Matrix.equals(expected, result));

    }

    @Test
    public void multiplyOneByOne() {
        Matrix expected = new Matrix(2, 2, new double[][]{{1, 4}, {9, 16}});
        testMatrix.multiply(new Matrix(2, 2, new double[][]{{1, 2}, {3, 4}}));
        assertTrue(Matrix.equals(expected, testMatrix));

    }

    @Test
    public void sigmoid() {
        Matrix expected = new Matrix(2, 2, new double[][]{{0.7310585786300049, 0.8807970779778823}, {0.9525741268224334, 0.9820137900379085}});
        testMatrix.sigmoid();
        assertTrue(Matrix.equals(expected, testMatrix));
    }

    @Test
    public void dSigmoid() {
        Matrix expected = new Matrix(2, 2, new double[][]{{0.0, -2.0}, {-6.0, -12.0}});
        Matrix result = testMatrix.dsigmoid();
        assertTrue(Matrix.equals(result, expected));
    }

    @Test
    public void equalDifferentDimension() {
        Matrix toCompare = new Matrix(2, 3, new double[][]{{0, 9, 8}, {7, 6, 5}});
        assertFalse(Matrix.equals(testMatrix, toCompare));
    }

    @Test
    public void equalDifferentMatrix() {
        Matrix toCompare = new Matrix(2, 2, new double[][]{{1, 9}, {7, 6}});
        assertFalse(Matrix.equals(testMatrix, toCompare));
    }

    @Test
    public void equalMatrix() {
        Matrix toCompare = new Matrix(2, 2, new double[][]{{1, 2}, {3, 4}});
        assertTrue(Matrix.equals(testMatrix, toCompare));
    }

    @Test
    public void cloneTest() {
        Matrix clone = testMatrix.clone();
        assertTrue(clone.getCols() == clone.getCols());
        assertTrue(clone.getRows() == clone.getRows());
        assertFalse(clone == testMatrix);
        assertFalse(clone.data == testMatrix.data);
        for (int i = 0; i < clone.getRows(); ++i) {
            assertFalse(clone.data[i] == testMatrix.data[i]);
        }
    }


}
