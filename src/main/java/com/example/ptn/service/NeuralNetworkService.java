package com.example.ptn.service;

import com.example.ptn.data.LabeledImage;
import com.example.ptn.data.Matrix;
import com.example.ptn.data.NonLabeledImage;
import com.example.ptn.model.MatrixFieldsModel;
import com.example.ptn.model.MatrixModel;
import com.example.ptn.model.MatrixRowsModel;
import com.example.ptn.model.NeuralNetworkModel;
import com.example.ptn.model.NeuralNetworkVersionModel;
import com.example.ptn.nn.NeuralNetwork;
import com.example.ptn.repo.NeuralNetworkRepository;
import com.example.ptn.repo.NeuralNetworkVersionRepository;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import static java.util.stream.Collectors.groupingBy;

/**
 * Service for find and save neuralNetwork.
 */
@Service
public class NeuralNetworkService {
  /**
   * Repository of neuralNetwork to save and get neuralNetwork.
   */
  @Autowired
  private NeuralNetworkRepository neuralNetworkRepository;

  @Autowired
  private NeuralNetworkVersionRepository neuralNetworkVersionRepository;

  @Autowired
  private NeuralNetwork neuralNetwork;

  @Autowired
  private DataSource dataSource;

  /**
   * Logger for service.
   */
  private static final Logger LOGGER =
          LoggerFactory.getLogger(NeuralNetworkService.class);

  /**
   * Bean to create neural network.
   *
   * @param id             from dataBase
   * @param filePath       where neuralNetwork stores
   * @param layerAmount    amount of layers (include input and result)
   * @param layerDimension dimension of weights layers
   * @param targetWidth    of image
   * @param targetHeight   of image
   * @param lRate          coefficient
   * @return neuralNetwork
   */
  @Bean
  public NeuralNetwork neuralNetwork(
          @Value("${layer}") Integer layerAmount,
          @Value("#{'${layerDimension}'}") Integer[] layerDimension,
          @Value("${id:false}") String id,
          @Value("${targetWidth}") Integer targetWidth,
          @Value("${filePath:false}") String filePath,
          @Value("${lRate}") Double lRate,
          @Value("${targetHeight}") Integer targetHeight) {

    if (id.equals("false") && filePath.equals("false")) {
      return new NeuralNetwork(layerAmount, layerDimension, targetWidth, targetHeight, lRate);
    } else if (id.equals("false")) {
      return NeuralNetwork.readFromFile(filePath);
    }
    return findNeuralNetworkById(UUID.fromString(id));
  }


  /**
   * Method to find and made neuralNetwork by id.
   *
   * @param neuralNetworkId uuid
   * @return neuralNetwork
   */
  @Transactional
  public NeuralNetwork findNeuralNetworkById(UUID neuralNetworkId) {
    final long startTime = System.currentTimeMillis();
    NeuralNetworkModel neuralNetworkModel = neuralNetworkRepository
            .findNeuralNetworkModelById(neuralNetworkId);

    NeuralNetworkVersionModel neuralNetworkVersionModel = neuralNetworkVersionRepository
            .findTopByNeuralNetworkModelIdOrderByVersionDesc(neuralNetworkId);
    System.out.println("Version: " + neuralNetworkVersionModel.getVersion());
    NeuralNetwork neuralNetwork = new NeuralNetwork(
            neuralNetworkModel.getLayerAmount().intValue(),
            neuralNetworkModel.getTargetWidth(),
            neuralNetworkModel.getTargetHeight(),
            neuralNetworkModel.getLRate().doubleValue(),
            neuralNetworkId,
            neuralNetworkVersionModel.getVersion(),
            neuralNetworkVersionModel.getCreate_on());

    Map<Integer, List<MatrixModel>> matrixMap = neuralNetworkVersionModel
            .getMatrixModels()
            .stream()
            .collect(groupingBy(MatrixModel::getMatrixType));
    fillLayerListOfNeuralNetwork(neuralNetwork.weights, matrixMap.get(0));
    fillLayerListOfNeuralNetwork(neuralNetwork.bias, matrixMap.get(1));
    long endTime = System.currentTimeMillis();
    LOGGER.info("time of finding: " + (endTime - startTime));

    return neuralNetwork;
  }

  /**
   * Method to save neuralNetwork.
   */
  @Transactional
  public String saveNeuralNetwork() {
    final long  startTime = System.currentTimeMillis();
    LOGGER.info("Saving new version of neuralNetwork with id: " + neuralNetwork.getId());
    NeuralNetworkModel neuralNetworkModel = new NeuralNetworkModel();
    if (neuralNetwork.getId() != null) {
      neuralNetworkModel = neuralNetworkRepository
              .findNeuralNetworkModelById((neuralNetwork.getId()));
    }
    neuralNetworkModel.setParameters((long) neuralNetwork.getLayersAmount(),
            BigDecimal.valueOf(neuralNetwork.getlRate()),
            neuralNetwork.getTargetWidth(),
            neuralNetwork.getTargetHeight());
    NeuralNetworkVersionModel neuralNetworkVersionModel =
            new NeuralNetworkVersionModel();
    addMatrixModel(neuralNetworkVersionModel, neuralNetwork.weights, 0);
    addMatrixModel(neuralNetworkVersionModel, neuralNetwork.bias, 1);
    neuralNetworkVersionModel.setNeuralNetworkModel(neuralNetworkModel);

    neuralNetworkRepository.save(neuralNetworkModel);
    neuralNetworkVersionRepository.save(neuralNetworkVersionModel);
    neuralNetworkRepository.flush();
    neuralNetworkVersionRepository.flush();
    neuralNetwork.setCreateOn(new Timestamp(System.currentTimeMillis()));
    neuralNetwork.setId(neuralNetworkModel.getId());
    long endTime = System.currentTimeMillis();
    LOGGER.info("time of saving: " + (endTime - startTime));
    return "success";
  }

  private void addMatrixModel(NeuralNetworkVersionModel neuralNetworkVersionModel,
                              ArrayList<Matrix> layerList, int matrixType) {
    for (Matrix matrix : layerList) {
      MatrixModel matrixModel = new MatrixModel(matrixType);
      for (int i = 0; i < matrix.getRows(); ++i) {
        MatrixRowsModel matrixRowsModel = new MatrixRowsModel(i);
        for (int j = 0; j < matrix.getCols(); ++j) {
          MatrixFieldsModel matrixFieldsModel =
                  new MatrixFieldsModel(
                          j, BigDecimal.valueOf(matrix.data[i][j]));
          matrixRowsModel.addMatrixFieldModels(matrixFieldsModel);
        }
        matrixModel.addMatrixRowsModels(matrixRowsModel);
      }
      neuralNetworkVersionModel.addMatrixModels(matrixModel);
    }
  }

  private void fillLayerListOfNeuralNetwork(List<Matrix> layerList,
                                            List<MatrixModel> matrixModels) {
    for (int i = 0; i < matrixModels.size(); ++i) {
      layerList.add(i, madeMatrix(matrixModels.get(i)));

    }
  }

  private Matrix madeMatrix(MatrixModel matrixModel) {
    Matrix matrix = new Matrix();
    matrix.setRows(matrixModel.getMatrixRowsModels().size());
    matrix.setCols(matrixModel
            .getMatrixRowsModels()
            .get(0)
            .getMatrixFieldsModels()
            .size());
    matrix.data = new double[matrix.getRows()][matrix.getCols()];
    matrixModel.getMatrixRowsModels()
            .sort(Comparator.comparing(MatrixRowsModel::getIndex));
    for (int i = 0; i < matrix.getRows(); ++i) {
      MatrixRowsModel matrixRowsModel = matrixModel
              .getMatrixRowsModels().get(i);
      matrixRowsModel.getMatrixFieldsModels()
              .sort(Comparator.comparing(MatrixFieldsModel::getIndex));
      for (int j = 0; j < matrix.getCols(); ++j) {
        matrix.data[i][j] = matrixRowsModel
                .getMatrixFieldsModels().get(j)
                .getData().doubleValue();
      }
    }

    return matrix;
  }

  /**
   * Predict from file by neuralNetwork.
   *
   * @param multiFile image with number
   * @return double array of result
   * @throws Exception if image is not correct.
   */
  public double[] filePredict(MultipartFile multiFile) throws Exception {
    return neuralNetwork.predict(
            new NonLabeledImage(multiFile,
                    neuralNetwork.getTargetWidth(),
                    neuralNetwork.getTargetWidth()));
  }

  /**
   * Train neuralNetwork by file.
   *
   * @param multiFile image with number
   * @param label     number
   * @throws Exception if f
   */
  public void fileTrain(MultipartFile multiFile, String label) throws Exception {
    neuralNetwork.train(
            new LabeledImage(
                    new NonLabeledImage(multiFile,
                            neuralNetwork.getTargetWidth(),
                            neuralNetwork.getTargetWidth()),
                    Integer.parseInt(label)));
  }

  /**
   * Method to test connection.
   *
   * @return resultSet with current time
   * @throws SQLException if cannot connect
   */
  public ResultSet testConnection() throws SQLException {
    Connection conn = dataSource.getConnection();
    return conn.createStatement()
            .executeQuery("SELECT NOW()");

  }
}
