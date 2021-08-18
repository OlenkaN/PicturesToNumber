package com.example.ptn.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @ManyToOne
    private MatrixModel matrixModel;

    @OneToMany(mappedBy = "matrixRowsModel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatrixFieldsModel> matrixFieldsModels = new ArrayList<>();
}
