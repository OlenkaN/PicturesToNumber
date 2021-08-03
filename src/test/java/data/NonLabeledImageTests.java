package data;

import com.example.ptn.data.NonLabeledImage;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NonLabeledImageTests {


    @Test
    public void meanNormalizeArray() {
        double[] input = new double[]{2, 3, 42, 8, 23, 12, 19, 9, 5, 37};
        //min =2 max=42 mean=16
        double[] expected = new double[]{-0.35, -0.325, 0.65, -0.2, 0.175, -0.1, 0.075, -0.175, -0.275, 0.525};
        NonLabeledImage test = new NonLabeledImage(input);
        assertTrue(Arrays.equals(expected, test.getMeanNormalizedPixel()));
    }

    @Test
    public void imageArrayDimension() {
        int expectedDimension = 784;
        NonLabeledImage test = new NonLabeledImage("src/main/resources/6.1.png", 28, 28);
        assertEquals(test.getMeanNormalizedPixel().length, expectedDimension);


    }

}
