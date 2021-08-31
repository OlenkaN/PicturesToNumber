package com.example.ptn.model;

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



/**
 * Matrix_rows table represents.
 */
@Getter
@Setter
@Entity
@Table(name = "matrix_rows")
@NoArgsConstructor
@AllArgsConstructor
public class MatrixRowsModel {
  @Id
  @GeneratedValue
  private UUID id;

  @Column(name = "index")
  private Integer index;

  @ManyToOne
  private MatrixModel matrixModel;

  @OneToMany(mappedBy = "matrixRowsModel",
          fetch = FetchType.EAGER,
          cascade = CascadeType.ALL,
          orphanRemoval = true)
  private List<MatrixFieldsModel> matrixFieldsModels = new ArrayList<>();

  /**
   * Method to add element to matrixFieldsModel list
   * and also set the field of matrixRowsModel in element.
   *
   * @param matrixFieldsModel element to be added
   */
  public void addMatrixFieldModels(MatrixFieldsModel matrixFieldsModel) {
    matrixFieldsModels.add(matrixFieldsModel);
    matrixFieldsModel.setMatrixRowsModel(this);
  }

  /**
   * Constructor to save to db.
   *
   * @param index of row.
   */
  public MatrixRowsModel(Integer index) {
    this.index = index;
  }
}
