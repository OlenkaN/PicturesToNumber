package com.example.ptn.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    public void addMatrixModels(MatrixModel matrixModel) {
        matrixModels.add(matrixModel);
        matrixModel.setNeuralNetworkVersionModel(this);
    }


}
