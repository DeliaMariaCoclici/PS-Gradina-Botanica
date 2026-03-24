package org.example.Model;

public class Exemplar {
    private int id;
    private String zona;
    private String imagine;
    private int plantaId;
    private String denumirePlanta;

    public Exemplar(int id, String zona, String imagine, int plantaId) {
        this.id = id;
        this.zona = zona;
        this.imagine = imagine;
        this.plantaId = plantaId;
    }

    public Exemplar(int id, String zona, String imagine, int plantaId, String denumirePlanta) {
        this(id, zona, imagine, plantaId);
        this.denumirePlanta = denumirePlanta;
    }

    public int getId() { return id; }
    public String getZona() { return zona; }
    public String getImagine() { return imagine; }
    public int getPlantaId() { return plantaId; }
    public String getDenumirePlanta() { return denumirePlanta; }

    public void setId(int id) { this.id = id; }
    public void setZona(String zona) { this.zona = zona; }
    public void setImagine(String imagine) { this.imagine = imagine; }
    public void setPlantaId(int plantaId) { this.plantaId = plantaId; }
    public void setDenumirePlanta(String denumirePlanta) { this.denumirePlanta = denumirePlanta; }

    @Override
    public String toString() {
        return "Zona: " + zona + " | Planta: " + (denumirePlanta != null ? denumirePlanta : plantaId);
    }
}
