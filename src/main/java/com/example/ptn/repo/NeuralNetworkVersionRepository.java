package com.example.ptn.repo;

import com.example.ptn.model.NeuralNetworkVersionModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository to NeuralNetworkVersionModel.
 */
@Repository
public interface NeuralNetworkVersionRepository
        extends JpaRepository<NeuralNetworkVersionModel, UUID> {
  NeuralNetworkVersionModel findTopByNeuralNetworkModelIdOrderByVersionDesc(UUID uuid);
}
