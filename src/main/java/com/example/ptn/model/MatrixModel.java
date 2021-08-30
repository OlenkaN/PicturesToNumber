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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@Entity
@Table(name = "matrix")
@NoArgsConstructor
@AllArgsConstructor
public class MatrixModel {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "matrix_type")
    private Integer matrixType;

    @ManyToOne
    private NeuralNetworkVersionModel neuralNetworkVersionModel;


    @OneToMany(mappedBy = "matrixModel",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    private List<MatrixRowsModel> matrixRowsModels = new ArrayList<>();

    /**
     * Method to add element to matrixRowsModels list
     * and also set the field of matrixModel in element.
     *
     * @param matrixRowsModel element to be added
     */
    public void addMatrixRowsModels(MatrixRowsModel matrixRowsModel) {
        matrixRowsModels.add(matrixRowsModel);
        matrixRowsModel.setMatrixModel(this);
    }

    public MatrixModel(Integer matrixType) {
        this.matrixType = matrixType;
    }
}
