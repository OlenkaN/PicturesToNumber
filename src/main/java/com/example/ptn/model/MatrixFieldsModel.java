package com.example.ptn.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "matrix_fields")
@NoArgsConstructor
@AllArgsConstructor
public class MatrixFieldsModel {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "data")
    private BigDecimal data;

    @ManyToOne
    private MatrixRowsModel matrixRowsModel;
}
