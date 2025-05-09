package ch.zhaw.wonders.dto;

public class Model {
    private String name;
    private Double accuracy;
    private Integer version;

    public Model(String name, Double accuracy, Integer version) {
        this.name = name;
        this.accuracy = accuracy;
        this.version = version;
    }

    public String getName() { return name; }
    public Double getAccuracy() { return accuracy; }
    public Integer getVersion() { return version; }
}