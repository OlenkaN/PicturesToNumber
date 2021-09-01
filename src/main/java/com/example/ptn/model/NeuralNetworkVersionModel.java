package com.example.ptn.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;


/**
 * Neural_network_version table represents.
 */
@Getter
@Setter
@Entity
@Table(name = "neural_network_version")
@NoArgsConstructor
@AllArgsConstructor
public class NeuralNetworkVersionModel {
  @Id
  @GeneratedValue
  private UUID id;

  @Column(name = "version", columnDefinition = "serial")
  @Generated(GenerationTime.INSERT)
  private Long version;

  @Column(name = "create_on")
  @Generated(GenerationTime.INSERT)
  private Timestamp create_on;

  @ManyToOne
  private NeuralNetworkModel neuralNetworkModel;

  @OneToMany(mappedBy = "neuralNetworkVersionModel",
          fetch = FetchType.EAGER,
          cascade = CascadeType.ALL,
          orphanRemoval = true)
  private List<MatrixModel> matrixModels = new ArrayList<>();

  /**
   * Method to add element to matrixModels list
   * and also set the field of neuralNetworkVersionModel in element.
   *
   * @param matrixModel element to be added
   */
  public void addMatrixModels(final MatrixModel matrixModel) {
    matrixModels.add(matrixModel);
    matrixModel.setNeuralNetworkVersionModel(this);
  }


}
