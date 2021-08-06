package com.example.ptn.config;

import com.example.ptn.nn.NeuralNetwork;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class AppConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        DataSourceBuilder dsb = DataSourceBuilder.create();
        if (dsb == null) {
            return null;
        }
        return dsb.build();
    }

    @Bean
    public NeuralNetwork neuralNetwork(@Value("${layer}") Integer layerAmount,
                                       @Value("#{'${layerDimension}'}") Integer[] layerDimension,
                                       @Value("${filePath:false}") String filePath) {

        if (filePath.equals("false")) {
            return new NeuralNetwork(layerAmount, layerDimension);
        } else
            return NeuralNetwork.readFromFile(filePath);
    }


}
