package ch.zhaw.wonders.dto;

public class ClassificationResult {

    private String label;
    private double probability;

    public ClassificationResult(String label, double probability) {
        this.label = label;
        this.probability = probability;
    }

    public String getLabel() {
        return label;
    }

    public double getProbability() {
        return probability;
    }
}