package com.example.ptn.model;

import java.math.BigDecimal;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Neural_network table represents.
 */
@Getter
@Setter
@Entity
@Table(name = "neural_network")
@NoArgsConstructor
@AllArgsConstructor
public class NeuralNetworkModel {
  @Id
  @GeneratedValue
  private UUID id;

  @Column(name = "layer_amount")
  private Long layerAmount;

  @Column(name = "l_rate")
  private BigDecimal lRate;

  @Column(name = "target_width")
  private Integer targetWidth;

  @Column(name = "target_height")
  private Integer targetHeight;


  /**
   * Method for save NeuralNetwork.
   *
   * @param layerAmount  amount of layers (include input and result)
   * @param lRate        coefficient to change weights
   * @param targetWidth  of image
   * @param targetHeight of image
   */

  public void setParameters(
          final Long layerAmount,
          final BigDecimal lRate,
          final Integer targetWidth,
          final Integer targetHeight) {
    this.layerAmount = layerAmount;
    this.lRate = lRate;
    this.targetWidth = targetWidth;
    this.targetHeight = targetHeight;
  }

}
