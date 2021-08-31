package serviceTest;

import com.example.ptn.PicturesToNumberApplication;
import com.example.ptn.data.Matrix;
import com.example.ptn.nn.NeuralNetwork;
import com.example.ptn.service.NeuralNetworkService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.OrderBy;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = PicturesToNumberApplication.class)
@TestPropertySource(
        locations = "classpath:application.properties")
class NeuralNetworkServiceTest {

  @Autowired
  private NeuralNetworkService neuralNetworkService;

  private NeuralNetwork testNN;

  public void setUp() {
    testNN = new NeuralNetwork(3, new Integer[]{2, 2, 2}, 2, 1, 0.5);
    ArrayList<Matrix> weights = new ArrayList<>();
    weights.add(0, new Matrix(2, 2, new double[][]{{0.15, 0.20}, {0.25, 0.30}}));
    weights.add(1, new Matrix(2, 2, new double[][]{{0.40, 0.45}, {0.50, 0.55}}));
    testNN.setWeights(weights);
    ArrayList<Matrix> bias = new ArrayList<>();
    bias.add(0, new Matrix(2, 1, new double[][]{{0.35}, {0.35}}));
    bias.add(1, new Matrix(2, 1, new double[][]{{0.60}, {0.60}}));
    testNN.setBias(bias);
  }



  @Test
  @Order(1)
  public void saveNeuralNetwork() {
    setUp();
    String result = neuralNetworkService.saveNeuralNetwork(testNN);
    assertEquals(result, "success");

  }


  @Test
  @Order(2)
  public void findNeuralNetwork() {
    setUp();
    neuralNetworkService.saveNeuralNetwork(testNN);
    NeuralNetwork result =
            neuralNetworkService
                    .findNeuralNetworkById(testNN.getId());
    assertTrue(testNN.equal(result));
  }
}
