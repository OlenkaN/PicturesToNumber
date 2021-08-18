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
@Table(name = "neural_network_version")
@NoArgsConstructor
@AllArgsConstructor
public class NeuralNetworkVersionModel {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "version")
    private Long version;

    @ManyToOne
    private NeuralNetworkModel neuralNetworkModel;

    @OneToMany(mappedBy = "neuralNetworkVersionModel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatrixModel> matrixModels = new ArrayList<>();


}
