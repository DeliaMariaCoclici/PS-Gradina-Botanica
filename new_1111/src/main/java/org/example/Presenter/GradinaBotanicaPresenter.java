package org.example.Presenter;

import org.example.Model.Exemplar;
import org.example.Model.Planta;
import org.example.Model.Repository.ExemplarRepo;
import org.example.Model.Repository.PlantaRepo;

import java.util.List;

public class GradinaBotanicaPresenter {

    private final GradinaBotanicaGUI gui;
    private final PlantaRepo plantaRepo;
    private final ExemplarRepo exemplarRepo;

    private List<Planta> plante;
    private List<Exemplar> exemplare;

    public GradinaBotanicaPresenter(GradinaBotanicaGUI gui) {
        this.gui = gui;
        this.plantaRepo = new PlantaRepo();
        this.exemplarRepo = new ExemplarRepo();

        afiseazaToatePlantele();
        afiseazaToateExemplarele();
    }

    // ══════════════════════════════════════════════════════════════════════════
    // PLANTE
    // ══════════════════════════════════════════════════════════════════════════

    public void adaugaPlanta() {
        try {
            Planta p = obtineDatePlanta();
            if (p == null) return;
            if (plantaRepo.addPlant(p)) {
                gui.showMessage("Succes", "Planta a fost adăugată!");
                afiseazaToatePlantele();
            } else gui.showMessage("Eroare", "Adăugarea a eșuat!");
        } catch (Exception e) {
            gui.showMessage("Eroare", e.getMessage());
        }
    }

    public void actualizeazaPlanta() {
        try {
            Planta p = obtineDatePlanta();
            if (p == null || p.getId() == 0) {
                gui.showMessage("Atenție", "Selectează o plantă pentru editare!");
                return;
            }
            if (plantaRepo.updatePlant(p)) {
                gui.showMessage("Succes", "Planta actualizată!");
                afiseazaToatePlantele();
            } else gui.showMessage("Eroare", "Actualizarea a eșuat!");
        } catch (Exception e) {
            gui.showMessage("Eroare", e.getMessage());
        }
    }

    public void stergePlanta() {
        try {
            String idStr = gui.getPlantaId();
            if (idStr == null || idStr.isEmpty()) {
                gui.showMessage("Atenție", "Selectează o plantă!");
                return;
            }
            int id = Integer.parseInt(idStr);
            if (plantaRepo.deletePlant(id)) {
                gui.showMessage("Succes", "Planta a fost ștearsă!");
                afiseazaToatePlantele();
            } else gui.showMessage("Eroare", "Ștergerea a eșuat!");
        } catch (Exception e) {
            gui.showMessage("Eroare", e.getMessage());
        }
    }

    public void afiseazaToatePlantele() {
        try {
            plante = plantaRepo.getAllPlants();
            afiseazaPlante();
        } catch (Exception e) {
            gui.showMessage("Eroare", e.getMessage());
        }
    }

    public void filtreazaPlante() {
        try {
            String tip = gui.getFilterTip();
            String specie = gui.getFilterSpecie();
            Boolean carn = gui.getFilterCarnivora();
            plante = plantaRepo.filterPlants(tip, specie, carn);
            afiseazaPlante();
        } catch (Exception e) {
            gui.showMessage("Eroare", e.getMessage());
        }
    }

    public void sorteazaPlante() {
        try {
            String crit = gui.getSortCriteria();
            plante = plantaRepo.getPlantsSortedBy(crit);
            afiseazaPlante();
        } catch (Exception e) {
            gui.showMessage("Eroare", e.getMessage());
        }
    }

    private void afiseazaPlante() {
        String[] col = {"ID", "Denumire", "Tip", "Specie", "Carnivoră", "Imagine"};
        if (plante == null || plante.isEmpty()) {
            gui.setTableData(new String[0][col.length], col);
            return;
        }
        String[][] data = new String[plante.size()][col.length];
        for (int i = 0; i < plante.size(); i++) {
            Planta p = plante.get(i);
            data[i][0] = String.valueOf(p.getId());
            data[i][1] = p.getDenumire();
            data[i][2] = p.getTip();
            data[i][3] = p.getSpecie();
            data[i][4] = p.isPlantaCarnivora() ? "Da" : "Nu";
            data[i][5] = p.getImagePath() != null ? p.getImagePath() : "";
        }
        gui.setTableData(data, col);
        actualizeazaListaText();
    }

    // ══════════════════════════════════════════════════════════════════════════
    // EXEMPLARE
    // ══════════════════════════════════════════════════════════════════════════

    public void adaugaExemplar() {
        try {
            Exemplar e = obtineDateExemplar();
            if (e == null) return;
            if (exemplarRepo.addExemplar(e)) {
                gui.showMessage("Succes", "Exemplarul a fost adăugat!");
                afiseazaToateExemplarele();
            } else gui.showMessage("Eroare", "Inserarea a eșuat!");
        } catch (Exception ex) {
            gui.showMessage("Eroare", ex.getMessage());
        }
    }

    public void actualizeazaExemplar() {
        try {
            Exemplar e = obtineDateExemplar();
            if (e == null || e.getId() == 0) {
                gui.showMessage("Atenție", "Selectează un exemplar pentru editare!");
                return;
            }
            if (exemplarRepo.updateExemplar(e)) {
                gui.showMessage("Succes", "Exemplarul actualizat!");
                afiseazaToateExemplarele();
            } else gui.showMessage("Eroare", "Actualizarea a eșuat!");
        } catch (Exception ex) {
            gui.showMessage("Eroare", ex.getMessage());
        }
    }

    public void stergeExemplar() {
        try {
            String idStr = gui.getExemplarId();
            if (idStr == null || idStr.isEmpty()) {
                gui.showMessage("Atenție", "Selectează un exemplar!");
                return;
            }
            int id = Integer.parseInt(idStr);
            if (exemplarRepo.deleteExemplar(id)) {
                gui.showMessage("Succes", "Exemplarul a fost șters!");
                afiseazaToateExemplarele();
            } else gui.showMessage("Eroare", "Ștergerea a eșuat!");
        } catch (Exception e) {
            gui.showMessage("Eroare", e.getMessage());
        }
    }

    public void afiseazaToateExemplarele() {
        try {
            exemplare = exemplarRepo.getAllExemplare();
            afiseazaExemplare();
        } catch (Exception e) {
            gui.showMessage("Eroare", e.getMessage());
        }
    }

    public void cautaExemplare() {
        try {
            String specie = gui.getFilterSpecie();
            exemplare = exemplarRepo.findBySpecie(specie);
            afiseazaExemplare();
        } catch (Exception e) {
            gui.showMessage("Eroare", e.getMessage());
        }
    }

    private void afiseazaExemplare() {
        String[] col = {"ID", "Zonă", "Imagine", "ID Plantă", "Denumire Plantă"};
        if (exemplare == null || exemplare.isEmpty()) {
            gui.setExemplarTableData(new String[0][col.length], col);
            return;
        }
        String[][] data = new String[exemplare.size()][col.length];
        for (int i = 0; i < exemplare.size(); i++) {
            Exemplar e = exemplare.get(i);
            data[i][0] = String.valueOf(e.getId());
            data[i][1] = e.getZona();
            data[i][2] = e.getImagine() != null ? e.getImagine() : "";
            data[i][3] = String.valueOf(e.getPlantaId());
            data[i][4] = e.getDenumirePlanta() != null ? e.getDenumirePlanta() : "";
        }
        gui.setExemplarTableData(data, col);
        actualizeazaListaText();
    }

    // ══════════════════════════════════════════════════════════════════════════
    // METODE AUXILIARE
    // ══════════════════════════════════════════════════════════════════════════

    private Planta obtineDatePlanta() {
        try {
            int id = 0;
            try { id = Integer.parseInt(gui.getPlantaId()); } catch (Exception ignored) {}
            String den = gui.getPlantaDenumire();
            if (den == null || den.isBlank()) return null;

            String tip = gui.getPlantaTip();
            String sp = gui.getPlantaSpecie();
            if (tip == null || tip.isBlank() || sp == null || sp.isBlank()) return null;

            return new Planta(id, den, tip, sp, gui.isPlantaCarnivora(), gui.getPlantaImagePath());
        } catch (Exception e) {
            gui.showMessage("Eroare", "Date invalid plantă");
            return null;
        }
    }

    private Exemplar obtineDateExemplar() {
        try {
            int id = 0;
            try { id = Integer.parseInt(gui.getExemplarId()); } catch (Exception ignored) {}
            String zona = gui.getExemplarZona();
            if (zona == null || zona.isBlank()) return null;
            int pid = Integer.parseInt(gui.getExemplarPlantaId());
            return new Exemplar(id, zona, gui.getExemplarImagine(), pid);
        } catch (Exception e) {
            gui.showMessage("Eroare", "Date invalid exemplar");
            return null;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // RAPORT TEXTUAL (TABEL "LISTĂ")
    // ══════════════════════════════════════════════════════════════════════════

    private void actualizeazaListaText() {
        StringBuilder sb = new StringBuilder();

        sb.append("🌿 LISTA PLANTELOR\n");
        sb.append("========================\n");
        if (plante != null && !plante.isEmpty()) {
            for (Planta p : plante) {
                sb.append(String.format("%d. %s (%s) | Tip: %s | Carnivoră: %s | Img: %s\n",
                        p.getId(), p.getDenumire(), p.getSpecie(), p.getTip(),
                        p.isPlantaCarnivora() ? "Da" : "Nu",
                        p.getImagePath() != null ? p.getImagePath() : "-"));
            }
        } else sb.append(" - Nicio plantă înregistrată - \n");

        sb.append("\n🔍 LISTA EXEMPLARELOR\n");
        sb.append("========================\n");
        if (exemplare != null && !exemplare.isEmpty()) {
            for (Exemplar e : exemplare) {
                sb.append(String.format("Zona %s | Planta #%d %s | Img: %s\n",
                        e.getZona(), e.getPlantaId(),
                        e.getDenumirePlanta() != null ? "(" + e.getDenumirePlanta() + ")" : "",
                        e.getImagine() != null ? e.getImagine() : "-"));
            }
        } else sb.append(" - Niciun exemplar înregistrat - \n");

        gui.setTextAreaContent(sb.toString());
    }
}
