package com.example.ptn.dto;

public class RecognitionResultDto {
    private int result;
    private double probability;

    /**
     * Constructor.
     *
     * @param result      of neural network
     * @param probability of result
     */
    public RecognitionResultDto(final int result, final double probability) {
        this.result = result;
        this.probability = probability;
    }

    /**
     * Getter for result.
     *
     * @return result
     */
    public int getResult() {
        return result;
    }

    /**
     * Setter for result.
     *
     * @param result to set
     */
    public void setResult(final int result) {
        this.result = result;
    }

    /**
     * Getter for probability.
     *
     * @return probability
     */
    public double getProbability() {
        return probability;
    }

    /**
     * Setter for probability.
     *
     * @param probability to set
     */
    public void setProbability(final double probability) {
        this.probability = probability;
    }
}
