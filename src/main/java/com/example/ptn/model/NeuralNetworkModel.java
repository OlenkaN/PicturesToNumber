package com.example.ptn.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @OneToMany(
            mappedBy = "neuralNetworkModel",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    private List<NeuralNetworkVersionModel> neuralNetworkVersionModels = new ArrayList<>();

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

    /**
     * Method to add element to neuralNetworkVersionModels list
     * and also set the field of neuralNetworkModel in element.
     *
     * @param neuralNetworkVersionModel element to be added
     */
    public void addNeuralNetworkVersion(
            final NeuralNetworkVersionModel neuralNetworkVersionModel) {
        neuralNetworkVersionModels.add(neuralNetworkVersionModel);
        neuralNetworkVersionModel.setNeuralNetworkModel(this);
    }
}
