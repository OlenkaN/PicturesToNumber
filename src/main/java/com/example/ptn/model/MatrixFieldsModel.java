package com.example.ptn.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "matrix_fields")
@NoArgsConstructor
@AllArgsConstructor
public class MatrixFieldsModel {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "data")
    private Double data;

    @ManyToOne
    private MatrixRowsModel matrixRowsModel;
}
