package com.example.ptn.dto;

public class RecognitionResultDto {
    int result;
    double probability;

    public RecognitionResultDto(int result, double probability) {
        this.result = result;
        this.probability = probability;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }
}
