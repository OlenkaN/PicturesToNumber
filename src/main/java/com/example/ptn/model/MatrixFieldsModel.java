package com.example.ptn.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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

    @Column(name = "index")
    private Integer index;

    @Column(name = "data")
    private BigDecimal data;

    @ManyToOne
    private MatrixRowsModel matrixRowsModel;

    /**
     * Constructor to save from database.
     *
     * @param index of field
     * @param data  in field
     */
    public MatrixFieldsModel(final Integer index, final BigDecimal data) {
        this.index = index;
        this.data = data;
    }
}
