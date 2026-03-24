package org.example.Presenter;

import java.awt.event.ActionListener;

public interface GradinaBotanicaGUI {
    // plante
    String getPlantaDenumire();
    String getPlantaTip();
    String getPlantaSpecie();
    boolean isPlantaCarnivora();
    String getPlantaId();
    String getPlantaImagePath();

    // exemplare
    String getExemplarId();
    String getExemplarZona();
    String getExemplarImagine();
    String getExemplarPlantaId();

    // afișare
    void setTextAreaContent(String content);
    void setTableData(String[][] data, String[] columnNames);
    void setExemplarTableData(String[][] data, String[] columnNames);

    // filtre/sortare
    String getSortCriteria();
    String getFilterTip();
    String getFilterSpecie();
    Boolean getFilterCarnivora();

    // mesaj
    void showMessage(String title, String message);
}
