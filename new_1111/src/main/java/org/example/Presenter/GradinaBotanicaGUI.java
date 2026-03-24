package org.example.Presenter;

import java.awt.event.ActionListener;

public interface GradinaBotanicaGUI {
    // Metode pentru plante
    String getPlantaDenumire();
    String getPlantaTip();
    String getPlantaSpecie();
    boolean isPlantaCarnivora();
    String getPlantaId();
    String getPlantaImagePath();

    // Metode pentru exemplare
    String getExemplarId();
    String getExemplarZona();
    String getExemplarImagine();
    String getExemplarPlantaId();
    // În interfața GradinaBotanicaGUI, adaugă:
    void setTextAreaContent(String content);
    // Metode pentru afișare date
    void setTableData(String[][] data, String[] columnNames);
    void setExemplarTableData(String[][] data, String[] columnNames);

    // Metode pentru filtrare și sortare
    String getSortCriteria();
    String getFilterTip();
    String getFilterSpecie();
    Boolean getFilterCarnivora();

    // Metode pentru export
    String getExportFilePath();

    // Mesaje
    void showMessage(String title, String message);

    // Listeners pentru plante
    void addAddPlantsListener(ActionListener listener);
    void addEditPlantsListener(ActionListener listener);
    void addDeletePlantsListener(ActionListener listener);
    void addFilterPlantsListener(ActionListener listener);
    void addSortPlantsListener(ActionListener listener);
    void addViewPlantsListener(ActionListener listener);

    // Listeners pentru exemplare
    void addAddExemplarListener(ActionListener listener);
    void addEditExemplarListener(ActionListener listener);
    void addDeleteExemplarListener(ActionListener listener);
    void addViewExemplarsListener(ActionListener listener);
    void addSearchExemplarsListener(ActionListener listener);
}
