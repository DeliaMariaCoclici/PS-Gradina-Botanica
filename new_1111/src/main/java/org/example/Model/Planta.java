package org.example.Model;

public class Planta {
    private int id;
    private String denumire;
    private String tip;
    private String specie;
    private boolean plantaCarnivora;
    private String imagePath;

    public Planta(int id, String denumire, String tip, String specie, boolean plantaCarnivora, String imagePath) {
        this.id = id;
        this.denumire = denumire;
        this.tip = tip;
        this.specie = specie;
        this.plantaCarnivora = plantaCarnivora;
        this.imagePath = imagePath;
    }

    public Planta(Planta other) {
        this.id = other.id;
        this.denumire = other.denumire;
        this.tip = other.tip;
        this.specie = other.specie;
        this.plantaCarnivora = other.plantaCarnivora;
        this.imagePath = other.imagePath;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDenumire() { return denumire; }
    public void setDenumire(String denumire) { this.denumire = denumire; }

    public String getTip() { return tip; }
    public void setTip(String tip) { this.tip = tip; }

    public String getSpecie() { return specie; }
    public void setSpecie(String specie) { this.specie = specie; }

    public boolean isPlantaCarnivora() { return plantaCarnivora; }
    public void setPlantaCarnivora(boolean plantaCarnivora) { this.plantaCarnivora = plantaCarnivora; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    @Override
    public String toString() {
        return denumire + " (" + specie + ")";
    }
}