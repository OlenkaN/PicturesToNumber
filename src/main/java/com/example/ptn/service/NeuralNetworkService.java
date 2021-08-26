package com.example.ptn.service;

import com.example.ptn.data.Matrix;
import com.example.ptn.model.*;
import com.example.ptn.nn.NeuralNetwork;
import com.example.ptn.repo.NeuralNetworkRepository;
import com.example.ptn.сontrollers.PictureController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

@Service
public class NeuralNetworkService {
    /**
     * Repository of neuralNetwork to save and get neuralNetwork.
     */
    @Autowired
    private NeuralNetworkRepository neuralNetworkRepository;


    @Bean
    public NeuralNetwork neuralNetwork(@Value("${layer}") Integer layerAmount,
                                       @Value("#{'${layerDimension}'}") Integer[] layerDimension,
                                       @Value("${id:false}") String id,
                                       @Value("${filePath:false}") String filePath,
                                       @Value("${targetWidth}") Integer targetWidth,
                                       @Value("${targetHeight}") Integer targetHeight,
                                       @Value("${lRate}") Double lRate) {

        if (id.equals("false") && filePath.equals("false")) {
            return new NeuralNetwork(layerAmount, layerDimension, targetWidth, targetHeight, lRate);
        } else if (id.equals("false")) {
            return NeuralNetwork.readFromFile(filePath);
        }
        System.out.println("yep");
        return findNeuralNetworkById(UUID.fromString(id));
    }


    /**
     * Method to find and made neuralNetwork by id.
     *
     * @param neuralNetworkId uuid
     * @return neuralNetwork
     */
    public NeuralNetwork findNeuralNetworkById(UUID neuralNetworkId) {

        NeuralNetworkModel neuralNetworkModel = neuralNetworkRepository
                .findNeuralNetworkModelById(neuralNetworkId);


        NeuralNetworkVersionModel neuralNetworkVersionModel = Collections
                .max(neuralNetworkModel.getNeuralNetworkVersionModels(),
                        Comparator.comparing(c -> c.getVersion()));
        NeuralNetwork neuralNetwork = new NeuralNetwork(
                neuralNetworkModel.getLayerAmount().intValue(),
                neuralNetworkModel.getTargetWidth(),
                neuralNetworkModel.getTargetHeight(),
                neuralNetworkModel.getLRate().doubleValue(),
                neuralNetworkId,
                neuralNetworkVersionModel.getVersion());

        Map<Integer, List<MatrixModel>> matrixMap = neuralNetworkVersionModel
                .getMatrixModels()
                .stream()
                .collect(groupingBy(MatrixModel::getMatrixType));
        fillLayerListOfNeuralNetwork(neuralNetwork.weights, matrixMap.get(0));
        fillLayerListOfNeuralNetwork(neuralNetwork.bias, matrixMap.get(1));
        return neuralNetwork;
    }

    public void saveNeuralNetwork(NeuralNetwork neuralNetwork) {
        NeuralNetworkModel neuralNetworkModel = new NeuralNetworkModel();
        if (neuralNetwork.getId() != null) {
            neuralNetworkModel = neuralNetworkRepository
                    .findNeuralNetworkModelById(UUID
                            .fromString(neuralNetwork.getId()));
        }
        neuralNetworkModel.setParameters((long) neuralNetwork.getLayersAmount(),
                BigDecimal.valueOf(neuralNetwork.getlRate()),
                neuralNetwork.getTargetWidth(),
                neuralNetwork.getTargetHeight());
        NeuralNetworkVersionModel neuralNetworkVersionModel = new NeuralNetworkVersionModel();
        addMatrixModel(neuralNetworkVersionModel, neuralNetwork.weights, 0);
        addMatrixModel(neuralNetworkVersionModel, neuralNetwork.bias, 1);
        neuralNetworkModel.addNeuralNetworkVersion(neuralNetworkVersionModel);
        neuralNetworkRepository.save(neuralNetworkModel);
    }

    private void addMatrixModel(NeuralNetworkVersionModel neuralNetworkVersionModel,
                                ArrayList<Matrix> layerList, int matrixType) {
        for (Matrix matrix : layerList) {
            MatrixModel matrixModel = new MatrixModel(matrixType);
            for (int i = 0; i < matrix.getRows(); ++i) {
                MatrixRowsModel matrixRowsModel = new MatrixRowsModel();
                for (int j = 0; j < matrix.getCols(); ++j) {
                    MatrixFieldsModel matrixFieldsModel = new MatrixFieldsModel(BigDecimal.valueOf(matrix.data[i][j]));
                    matrixRowsModel.addMatrixFieldModels(matrixFieldsModel);
                }
                matrixModel.addMatrixModels(matrixRowsModel);
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
        for (int i = 0; i < matrix.getRows(); ++i) {
            MatrixRowsModel matrixRowsModel = matrixModel
                    .getMatrixRowsModels().get(i);
            for (int j = 0; j < matrix.getCols(); ++j) {
                matrix.data[i][j] = matrixRowsModel
                        .getMatrixFieldsModels().get(j)
                        .getData().doubleValue();
            }
        }
        return matrix;
    }
}
