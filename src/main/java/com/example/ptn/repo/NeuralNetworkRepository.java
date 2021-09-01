package com.example.ptn.repo;

import com.example.ptn.model.NeuralNetworkModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository to NeuralNetworkModel.
 */
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
