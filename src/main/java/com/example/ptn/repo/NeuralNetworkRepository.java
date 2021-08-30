package com.example.ptn.repo;

import com.example.ptn.model.NeuralNetworkModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NeuralNetworkRepository
        extends JpaRepository<NeuralNetworkModel, UUID> {
    /**
     * Method to find NeuralNetworkModel by id.
     *
     * @param id model
     * @return NeuralNetworkModel
     */
    NeuralNetworkModel findNeuralNetworkModelById(UUID id);
}
