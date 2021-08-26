package com.example.ptn.repo;

import com.example.ptn.model.NeuralNetworkModel;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface NeuralNetworkRepository
        extends CrudRepository<NeuralNetworkModel, UUID> {
    /**
     * Method to find NeuralNetworkModel by id.
     *
     * @param id model
     * @return NeuralNetworkModel
     */
    NeuralNetworkModel findNeuralNetworkModelById(UUID id);
}
