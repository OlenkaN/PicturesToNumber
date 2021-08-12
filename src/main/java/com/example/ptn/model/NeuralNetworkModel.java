package com.example.ptn.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "neural_network")
@NoArgsConstructor
@AllArgsConstructor
public class NeuralNetworkModel {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "layerAmount")
    private Long layerAmount;

    @Column(name = "l_rate")
    private Double l_rate;

    @Column(name = "targetWidth")
    private Integer targetWidth;

    @Column(name = "targetHeight")
    private Integer targetHeight;

    @OneToMany(mappedBy = "neuralNetworkModel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NeuralNetworkVersionModel> neuralNetworkVersionModels = new ArrayList<>();

}
