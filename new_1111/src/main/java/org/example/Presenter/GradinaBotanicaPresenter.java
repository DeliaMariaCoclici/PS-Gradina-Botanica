package org.example.Presenter;

import org.example.Model.Exemplar;
import org.example.Model.Planta;
import org.example.Model.Repository.ExemplarRepo;
import org.example.Model.Repository.PlantaRepo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class GradinaBotanicaPresenter {

    private final GradinaBotanicaGUI gui;
    private final PlantaRepo plantaRepo;
    private final ExemplarRepo exemplarRepo;
    private List<Planta> plante;
    private List<Exemplar> exemplare;

    public GradinaBotanicaPresenter(GradinaBotanicaGUI gui) {
        this.gui = gui;
        this.plantaRepo  = new PlantaRepo();
        this.exemplarRepo = new ExemplarRepo();

        // ── Listeners plante ───────────────────────────────────────────────────
        gui.addAddPlantsListener(e    -> adaugaPlanta());
        gui.addEditPlantsListener(e   -> actualizeazaPlanta());
        gui.addDeletePlantsListener(e -> stergePlanta());
        gui.addFilterPlantsListener(e -> filtreazaPlante());
        gui.addSortPlantsListener(e   -> sorteazaPlante());
        gui.addViewPlantsListener(e   -> afiseazaToatePlantele());

        // ── Listeners exemplare ────────────────────────────────────────────────
        gui.addAddExemplarListener(e    -> adaugaExemplar());
        gui.addEditExemplarListener(e   -> actualizeazaExemplar());
        gui.addDeleteExemplarListener(e -> stergeExemplar());
        gui.addViewExemplarsListener(e  -> afiseazaToateExemplarele());
        gui.addSearchExemplarsListener(e -> cautaExemplare());

        // Încărcare inițială
        afiseazaToatePlantele();
    }

    // ══════════════════════════════════════════════════════════════════════════
    // PLANTE
    // ══════════════════════════════════════════════════════════════════════════

    public void adaugaPlanta() {
        try {
            Planta planta = obtineDatePlanta();
            if (planta == null) return;

            if (plantaRepo.addPlant(planta)) {
                gui.showMessage("Succes", "Planta a fost adăugată cu succes!");
                afiseazaToatePlantele();
            } else {
                gui.showMessage("Eroare", "Adăugarea plantei a eșuat!");
            }
        } catch (Exception ex) {
            gui.showMessage("Eroare", "Eroare la adăugarea plantei: " + ex.getMessage());
        }
    }

    public void actualizeazaPlanta() {
        try {
            String id = gui.getPlantaId();
            if (id == null || id.isEmpty()) {
                gui.showMessage("Atenție", "Nu a fost selectată nicio plantă pentru actualizare!");
                return;
            }
            Planta planta = obtineDatePlanta();
            if (planta == null) return;

            if (plantaRepo.updatePlant(planta)) {
                gui.showMessage("Succes", "Planta a fost actualizată cu succes!");
                afiseazaToatePlantele();
            } else {
                gui.showMessage("Eroare", "Actualizarea plantei a eșuat!");
            }
        } catch (Exception ex) {
            gui.showMessage("Eroare", "Eroare la actualizarea plantei: " + ex.getMessage());
        }
    }

    public void stergePlanta() {
        try {
            String id = gui.getPlantaId();
            if (id == null || id.isEmpty()) {
                gui.showMessage("Atenție", "Nu a fost selectată nicio plantă pentru ștergere!");
                return;
            }
            if (plantaRepo.deletePlant(Integer.parseInt(id))) {
                gui.showMessage("Succes", "Planta a fost ștearsă cu succes!");
                afiseazaToatePlantele();
            } else {
                gui.showMessage("Eroare", "Ștergerea plantei a eșuat!");
            }
        } catch (Exception ex) {
            gui.showMessage("Eroare", "Eroare la ștergerea plantei: " + ex.getMessage());
        }
    }

    public void afiseazaToatePlantele() {
        try {
            plante = plantaRepo.getAllPlants();
            afiseazaPlante();
        } catch (Exception ex) {
            gui.showMessage("Eroare", "Eroare la afișarea plantelor: " + ex.getMessage());
        }
    }

    public void filtreazaPlante() {
        try {
            String tip      = gui.getFilterTip();
            String specie   = gui.getFilterSpecie();
            Boolean carniv  = gui.getFilterCarnivora();

            plante = plantaRepo.filterPlants(tip, specie, carniv);
            afiseazaPlante();

            // Afișare și în textArea
            StringBuilder sb = new StringBuilder("LISTA PLANTELOR FILTRATE:\n\n");
            if (plante != null && !plante.isEmpty()) {
                for (Planta p : plante) {
                    sb.append("ID: ").append(p.getId()).append("\n")
                            .append("Denumire: ").append(p.getDenumire()).append("\n")
                            .append("Tip: ").append(p.getTip()).append("\n")
                            .append("Specie: ").append(p.getSpecie()).append("\n")
                            .append("Carnivoră: ").append(p.isPlantaCarnivora() ? "Da" : "Nu").append("\n")
                            .append("Imagine: ").append(p.getImagePath()).append("\n")
                            .append("------------------------------\n");
                }
            } else {
                sb.append("Nu există plante care să corespundă criteriilor de filtrare.");
            }
            gui.setTextAreaContent(sb.toString());

        } catch (Exception ex) {
            gui.showMessage("Eroare", "Eroare la filtrarea plantelor: " + ex.getMessage());
        }
    }

    public void sorteazaPlante() {
        try {
            String criteriu = gui.getSortCriteria();
            if (criteriu == null || criteriu.isEmpty()) {
                gui.showMessage("Atenție", "Nu a fost selectat niciun criteriu de sortare!");
                return;
            }
            plante = plantaRepo.getPlantsSortedBy(criteriu);
            afiseazaPlante();

            // Afișare și în textArea
            StringBuilder sb = new StringBuilder("PLANTE SORTATE DUPĂ " + criteriu.toUpperCase() + ":\n\n");
            if (plante != null && !plante.isEmpty()) {
                for (Planta p : plante) {
                    sb.append("ID: ").append(p.getId())
                            .append(" | ").append(p.getDenumire())
                            .append(" | ").append(p.getTip())
                            .append(" | ").append(p.getSpecie())
                            .append(" | Carnivoră: ").append(p.isPlantaCarnivora() ? "Da" : "Nu")
                            .append("\n");
                }
            } else {
                sb.append("Nu există plante în baza de date.");
            }
            gui.setTextAreaContent(sb.toString());

        } catch (Exception ex) {
            gui.showMessage("Eroare", "Eroare la sortarea plantelor: " + ex.getMessage());
        }
    }

    // Afișare internă în tabel
    private void afiseazaPlante() {
        String[] coloane = {"ID", "Denumire", "Tip", "Specie", "Carnivoră", "Imagine"};
        if (plante == null || plante.isEmpty()) {
            gui.setTableData(new String[0][6], coloane);
            return;
        }
        String[][] data = new String[plante.size()][6];
        for (int i = 0; i < plante.size(); i++) {
            Planta p = plante.get(i);
            data[i][0] = String.valueOf(p.getId());
            data[i][1] = p.getDenumire();
            data[i][2] = p.getTip();
            data[i][3] = p.getSpecie();
            data[i][4] = p.isPlantaCarnivora() ? "Da" : "Nu";
            data[i][5] = p.getImagePath() != null ? p.getImagePath() : "";
        }
        gui.setTableData(data, coloane);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // EXEMPLARE
    // ══════════════════════════════════════════════════════════════════════════

    public void adaugaExemplar() {
        try {
            Exemplar exemplar = obtineDateExemplar();
            if (exemplar == null) return;

            if (exemplarRepo.addExemplar(exemplar)) {
                gui.showMessage("Succes", "Exemplarul a fost adăugat cu succes!");
                afiseazaToateExemplarele();
            } else {
                gui.showMessage("Eroare", "Adăugarea exemplarului a eșuat!");
            }
        } catch (Exception ex) {
            gui.showMessage("Eroare", "Eroare la adăugarea exemplarului: " + ex.getMessage());
        }
    }

    public void actualizeazaExemplar() {
        try {
            String id = gui.getExemplarId();
            if (id == null || id.isEmpty()) {
                gui.showMessage("Atenție", "Nu a fost selectat niciun exemplar pentru actualizare!");
                return;
            }
            Exemplar exemplar = obtineDateExemplar();
            if (exemplar == null) return;

            if (exemplarRepo.updateExemplar(exemplar)) {
                gui.showMessage("Succes", "Exemplarul a fost actualizat cu succes!");
                afiseazaToateExemplarele();
            } else {
                gui.showMessage("Eroare", "Actualizarea exemplarului a eșuat!");
            }
        } catch (Exception ex) {
            gui.showMessage("Eroare", "Eroare la actualizarea exemplarului: " + ex.getMessage());
        }
    }

    public void stergeExemplar() {
        try {
            String id = gui.getExemplarId();
            if (id == null || id.isEmpty()) {
                gui.showMessage("Atenție", "Nu a fost selectat niciun exemplar pentru ștergere!");
                return;
            }
            if (exemplarRepo.deleteExemplar(Integer.parseInt(id))) {
                gui.showMessage("Succes", "Exemplarul a fost șters cu succes!");
                afiseazaToateExemplarele();
            } else {
                gui.showMessage("Eroare", "Ștergerea exemplarului a eșuat!");
            }
        } catch (Exception ex) {
            gui.showMessage("Eroare", "Eroare la ștergerea exemplarului: " + ex.getMessage());
        }
    }

    public void afiseazaToateExemplarele() {
        try {
            exemplare = exemplarRepo.getAllExemplare();
            afiseazaExemplare();
        } catch (Exception ex) {
            gui.showMessage("Eroare", "Eroare la afișarea exemplarelor: " + ex.getMessage());
        }
    }

    public void cautaExemplare() {
        try {
            String specie = gui.getFilterSpecie();
            if (specie == null || specie.isEmpty()) {
                gui.showMessage("Atenție", "Specificați o specie pentru căutare!");
                return;
            }
            exemplare = exemplarRepo.findBySpecie(specie);
            afiseazaExemplare();

            if (exemplare == null || exemplare.isEmpty()) {
                gui.showMessage("Informare", "Nu există exemplare pentru specia: " + specie);
            }
        } catch (Exception ex) {
            gui.showMessage("Eroare", "Eroare la căutarea exemplarelor: " + ex.getMessage());
        }
    }

    // Afișare internă în tabel
    public void afiseazaExemplare() {
        String[] coloane = {"ID", "Zonă", "Imagine", "ID Plantă", "Denumire Plantă"};
        if (exemplare == null || exemplare.isEmpty()) {
            gui.setExemplarTableData(new String[0][5], coloane);
            return;
        }
        String[][] data = new String[exemplare.size()][5];
        for (int i = 0; i < exemplare.size(); i++) {
            Exemplar e = exemplare.get(i);
            data[i][0] = String.valueOf(e.getId());
            data[i][1] = e.getZona();
            data[i][2] = e.getImagine() != null ? e.getImagine() : "";
            data[i][3] = String.valueOf(e.getPlantaId());
            data[i][4] = e.getDenumirePlanta() != null ? e.getDenumirePlanta() : "";
        }
        gui.setExemplarTableData(data, coloane);
    }


    // ══════════════════════════════════════════════════════════════════════════
    // METODE AUXILIARE
    // ══════════════════════════════════════════════════════════════════════════

    private Planta obtineDatePlanta() {
        try {
            String idStr = gui.getPlantaId();
            int id = 0;
            if (idStr != null && !idStr.isEmpty()) {
                try { id = Integer.parseInt(idStr); } catch (NumberFormatException ignored) {}
            }

            String denumire = gui.getPlantaDenumire();
            if (denumire == null || denumire.trim().isEmpty()) {
                gui.showMessage("Atenție", "Denumirea plantei nu poate fi goală!");
                return null;
            }

            String tip    = gui.getPlantaTip();
            String specie = gui.getPlantaSpecie();
            if (tip == null || tip.trim().isEmpty()) {
                gui.showMessage("Atenție", "Tipul plantei nu poate fi gol!");
                return null;
            }
            if (specie == null || specie.trim().isEmpty()) {
                gui.showMessage("Atenție", "Specia plantei nu poate fi goală!");
                return null;
            }

            return new Planta(id, denumire.trim(), tip.trim(), specie.trim(),
                    gui.isPlantaCarnivora(), gui.getPlantaImagePath());

        } catch (Exception ex) {
            gui.showMessage("Eroare", "Eroare la obținerea datelor plantei: " + ex.getMessage());
            return null;
        }
    }

    private Exemplar obtineDateExemplar() {
        try {
            String idStr = gui.getExemplarId();
            int id = 0;
            if (idStr != null && !idStr.isEmpty()) {
                try { id = Integer.parseInt(idStr); } catch (NumberFormatException ignored) {}
            }

            String zona = gui.getExemplarZona();
            if (zona == null || zona.trim().isEmpty()) {
                gui.showMessage("Atenție", "Zona exemplarului nu poate fi goală!");
                return null;
            }

            String plantaIdStr = gui.getExemplarPlantaId();
            int plantaId = 0;
            if (plantaIdStr != null && !plantaIdStr.isEmpty()) {
                try {
                    plantaId = Integer.parseInt(plantaIdStr);
                } catch (NumberFormatException e) {
                    gui.showMessage("Atenție", "ID-ul plantei trebuie să fie un număr valid!");
                    return null;
                }
            }
            if (plantaId <= 0) {
                gui.showMessage("Atenție", "ID-ul plantei asociate trebuie să fie un număr pozitiv!");
                return null;
            }

            return new Exemplar(id, zona.trim(), gui.getExemplarImagine(), plantaId);

        } catch (Exception ex) {
            gui.showMessage("Eroare", "Eroare la obținerea datelor exemplarului: " + ex.getMessage());
            return null;
        }
    }

    // Metode publice stub (pot fi implementate ulterior)
    public void salvareListaPlante() {}
    public void cautaPlanta() {}
}