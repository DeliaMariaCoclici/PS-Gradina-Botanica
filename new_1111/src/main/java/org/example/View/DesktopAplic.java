package org.example.View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.example.Presenter.GradinaBotanicaGUI;
import org.example.Presenter.GradinaBotanicaPresenter;
import java.awt.*;
import java.awt.event.ActionListener;

public class DesktopAplic extends JFrame implements GradinaBotanicaGUI {

    // ── Câmpuri plante ────────────────────────────────────────────────────────
    private JTextField textField_planta_id;
    private JTextField textField_denumire_planta;
    private JTextField textField_tip;
    private JTextField textField_specie;
    private JTextField textField_imagine;
    private JCheckBox  ePlantaCarnivoraCheckBox;

    // ── Butoane plante ────────────────────────────────────────────────────────
    private JButton adaugaPlantaButton;
    private JButton button_editeaza_planta;
    private JButton stergerePlantaButton;
    private JButton vizualizareListaButton;
    private JButton filtrareListaPlanteButton;
    private JButton salvareListaPlanteButton;
    private JComboBox<String> ComboBox_sort;
    private JTable table1;

    // ── Câmpuri exemplare ─────────────────────────────────────────────────────
    private JTextField textField_id_exemplar;
    private JTextField textField_zona;
    private JTextField textField_imagine_exemplar;
    private JTextField textField_cauta;

    // ── Butoane exemplare ─────────────────────────────────────────────────────
    private JButton adaugaExemplarButton;
    private JButton button_editeaza_exemplar;
    private JButton stergeExemplarButton;
    private JButton cautaButton;
    private JTable table2;

    // ── TextArea ──────────────────────────────────────────────────────────────
    private JTextArea textArea_afisare_lista;

    private GradinaBotanicaPresenter presenter;

    public DesktopAplic() {
        setTitle("Grădina Botanică");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        setContentPane(buildUI());

        ComboBox_sort.addItem("denumire");
        ComboBox_sort.addItem("tip");
        ComboBox_sort.addItem("specie");
        ComboBox_sort.setSelectedIndex(0);

        presenter = new GradinaBotanicaPresenter(this);

        // ── Listeners plante ──────────────────────────────────────────────────
        adaugaPlantaButton.addActionListener(e -> presenter.adaugaPlanta());
        button_editeaza_planta.addActionListener(e -> presenter.actualizeazaPlanta());
        stergerePlantaButton.addActionListener(e -> presenter.stergePlanta());
        vizualizareListaButton.addActionListener(e -> presenter.afiseazaToatePlantele());
        filtrareListaPlanteButton.addActionListener(e -> presenter.filtreazaPlante());
        //salvareListaPlanteButton.addActionListener(e -> presenter.salveazaDate());
        ComboBox_sort.addActionListener(e -> presenter.sorteazaPlante());

        // ── Listeners exemplare ───────────────────────────────────────────────
        adaugaExemplarButton.addActionListener(e -> {
            textField_id_exemplar.setText("");
            presenter.adaugaExemplar();
        });
        button_editeaza_exemplar.addActionListener(e -> presenter.actualizeazaExemplar());
        stergeExemplarButton.addActionListener(e -> {
            String id = getExemplarId();
            if (id != null && !id.isEmpty()) {
                presenter.stergeExemplar();
            } else {
                showMessage("Eroare", "Selectați un exemplar din tabel pentru a-l șterge!");
            }
        });
        cautaButton.addActionListener(e -> presenter.cautaExemplare());

        // ── Selecție tabel plante ─────────────────────────────────────────────
        table1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table1.getSelectedRow() != -1)
                populateFormWithSelectedRow();
        });

        // ── Selecție tabel exemplare ──────────────────────────────────────────
        table2.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table2.getSelectedRow() != -1)
                populateExemplarFormWithSelectedRow();
        });

        presenter.afiseazaToatePlantele();
        presenter.afiseazaToateExemplarele();

        setVisible(true);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // INIȚIALIZARE COMPONENTE
    // ══════════════════════════════════════════════════════════════════════════

    private void initComponents() {
        textField_planta_id        = new JTextField(5);
        textField_denumire_planta  = new JTextField(15);
        textField_tip              = new JTextField(12);
        textField_specie           = new JTextField(12);
        textField_imagine          = new JTextField(15);
        ePlantaCarnivoraCheckBox   = new JCheckBox("Carnivoră");

        adaugaPlantaButton         = new JButton("Adaugă plantă");
        button_editeaza_planta     = new JButton("Editează plantă");
        stergerePlantaButton       = new JButton("Șterge plantă");
        vizualizareListaButton     = new JButton("Vizualizează");
        filtrareListaPlanteButton  = new JButton("Filtrează");
        salvareListaPlanteButton   = new JButton("Salvează CSV");
        ComboBox_sort              = new JComboBox<>();

        table1 = new JTable(new DefaultTableModel(
                new String[]{"ID", "Denumire", "Tip", "Specie", "Carnivoră", "Imagine"}, 0));
        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        textField_id_exemplar      = new JTextField(5);
        textField_zona             = new JTextField(12);
        textField_imagine_exemplar = new JTextField(15);
        textField_cauta            = new JTextField(15);

        adaugaExemplarButton       = new JButton("Adaugă exemplar");
        button_editeaza_exemplar   = new JButton("Editează exemplar");
        stergeExemplarButton       = new JButton("Șterge exemplar");
        cautaButton                = new JButton("Caută");

        table2 = new JTable(new DefaultTableModel(
                new String[]{"ID", "Zonă", "Imagine", "ID Plantă", "Denumire Plantă"}, 0));
        table2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table2.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        textArea_afisare_lista = new JTextArea();
        textArea_afisare_lista.setEditable(false);
        textArea_afisare_lista.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
    }

    // ══════════════════════════════════════════════════════════════════════════
    // CONSTRUCȚIE UI
    // ══════════════════════════════════════════════════════════════════════════

    private JPanel buildUI() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("🌿 Plante",    buildPlanteTab());
        tabs.addTab("🔍 Exemplare", buildExemplareTab());
        tabs.addTab("📋 Listă",     new JScrollPane(textArea_afisare_lista));

        JPanel root = new JPanel(new BorderLayout());
        root.add(tabs, BorderLayout.CENTER);
        return root;
    }

    private JPanel buildPlanteTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(buildPlanteForm(), BorderLayout.NORTH);
        panel.add(new JScrollPane(table1), BorderLayout.CENTER);
        panel.add(buildPlanteSortFilter(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildPlanteForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Detalii plantă"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 6, 4, 6);
        g.anchor = GridBagConstraints.WEST;

        g.gridy = 0;
        g.gridx = 0; form.add(new JLabel("ID:"), g);
        g.gridx = 1; form.add(textField_planta_id, g);
        g.gridx = 2; form.add(new JLabel("Denumire:"), g);
        g.gridx = 3; form.add(textField_denumire_planta, g);
        g.gridx = 4; form.add(new JLabel("Tip:"), g);
        g.gridx = 5; form.add(textField_tip, g);

        g.gridy = 1;
        g.gridx = 0; form.add(new JLabel("Specie:"), g);
        g.gridx = 1; form.add(textField_specie, g);
        g.gridx = 2; form.add(new JLabel("Imagine:"), g);
        g.gridx = 3; form.add(textField_imagine, g);
        g.gridx = 4; form.add(ePlantaCarnivoraCheckBox, g);

        g.gridy = 2; g.gridx = 0; g.gridwidth = 6;
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        btns.add(adaugaPlantaButton);
        btns.add(button_editeaza_planta);
        btns.add(stergerePlantaButton);
        btns.add(vizualizareListaButton);
        btns.add(salvareListaPlanteButton);
        form.add(btns, g);
        return form;
    }

    private JPanel buildPlanteSortFilter() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        panel.setBorder(BorderFactory.createTitledBorder("Filtrare & Sortare"));
        panel.add(new JLabel("Tip:"));    panel.add(textField_tip);
        panel.add(new JLabel("Specie:")); panel.add(textField_specie);
        panel.add(ePlantaCarnivoraCheckBox);
        panel.add(filtrareListaPlanteButton);
        panel.add(new JLabel("  Sortare:"));
        panel.add(ComboBox_sort);
        return panel;
    }

    private JPanel buildExemplareTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(buildExemplareForm(), BorderLayout.NORTH);
        panel.add(new JScrollPane(table2), BorderLayout.CENTER);
        panel.add(buildExemplareCautare(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildExemplareForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Detalii exemplar"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 6, 4, 6);
        g.anchor = GridBagConstraints.WEST;

        g.gridy = 0;
        g.gridx = 0; form.add(new JLabel("ID:"), g);
        g.gridx = 1; form.add(textField_id_exemplar, g);
        g.gridx = 2; form.add(new JLabel("Zona:"), g);
        g.gridx = 3; form.add(textField_zona, g);
        g.gridx = 4; form.add(new JLabel("Imagine:"), g);
        g.gridx = 5; form.add(textField_imagine_exemplar, g);

        g.gridy = 1; g.gridx = 0; g.gridwidth = 6;
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        btns.add(adaugaExemplarButton);
        btns.add(button_editeaza_exemplar);
        btns.add(stergeExemplarButton);
        form.add(btns, g);
        return form;
    }

    private JPanel buildExemplareCautare() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        panel.setBorder(BorderFactory.createTitledBorder("Căutare după specie"));
        panel.add(new JLabel("Specie:"));
        panel.add(textField_cauta);
        panel.add(cautaButton);
        return panel;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // POPULARE FORMULARE DIN TABEL
    // ══════════════════════════════════════════════════════════════════════════

    private void populateFormWithSelectedRow() {
        int row = table1.getSelectedRow();
        if (row == -1) return;
        textField_planta_id.setText(table1.getValueAt(row, 0).toString());
        textField_denumire_planta.setText(table1.getValueAt(row, 1).toString());
        textField_tip.setText(table1.getValueAt(row, 2).toString());
        textField_specie.setText(table1.getValueAt(row, 3).toString());
        ePlantaCarnivoraCheckBox.setSelected("Da".equals(table1.getValueAt(row, 4).toString()));
        textField_imagine.setText(table1.getValueAt(row, 5).toString());
    }

    private void populateExemplarFormWithSelectedRow() {
        int row = table2.getSelectedRow();
        if (row == -1) return;
        textField_id_exemplar.setText(table2.getValueAt(row, 0).toString());
        textField_zona.setText(table2.getValueAt(row, 1).toString());
        textField_imagine_exemplar.setText(table2.getValueAt(row, 2).toString());
        textField_planta_id.setText(table2.getValueAt(row, 3).toString());
    }

    // ══════════════════════════════════════════════════════════════════════════
    // IMPLEMENTARE GradinaBotanicaGUI
    // ══════════════════════════════════════════════════════════════════════════

    @Override public String getPlantaDenumire()  { return textField_denumire_planta.getText(); }
    @Override public String getPlantaTip()        { return textField_tip.getText(); }
    @Override public String getPlantaSpecie()     { return textField_specie.getText(); }
    @Override public boolean isPlantaCarnivora()  { return ePlantaCarnivoraCheckBox.isSelected(); }
    @Override public String getPlantaId()         { return textField_planta_id.getText(); }
    @Override public String getPlantaImagePath()  { return textField_imagine.getText(); }

    @Override public String getExemplarId()       { return textField_id_exemplar.getText(); }
    @Override public String getExemplarZona()     { return textField_zona.getText(); }
    @Override public String getExemplarImagine()  { return textField_imagine_exemplar.getText(); }
    @Override public String getExemplarPlantaId() { return textField_planta_id.getText(); }

    @Override public String getSortCriteria()     { return (String) ComboBox_sort.getSelectedItem(); }
    @Override public String getFilterTip()        { return textField_tip.getText(); }
    @Override public String getFilterSpecie()     { return textField_cauta.getText(); }

    @Override
    public Boolean getFilterCarnivora() {
        return ePlantaCarnivoraCheckBox.isSelected() ? true : null;
    }

    @Override
    public void setTextAreaContent(String content) {
        textArea_afisare_lista.setText(content);
    }

    @Override
    public String getExportFilePath() {
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
            return fc.getSelectedFile().getAbsolutePath();
        return null;
    }

    @Override
    public void setTableData(String[][] data, String[] columnNames) {
        table1.setModel(new DefaultTableModel(data, columnNames));
    }

    @Override
    public void setExemplarTableData(String[][] data, String[] columnNames) {
        table2.setModel(new DefaultTableModel(data, columnNames));
        table2.revalidate();
        table2.repaint();
    }

    @Override
    public void showMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    @Override public void addAddPlantsListener(ActionListener l)      {}
    @Override public void addEditPlantsListener(ActionListener l)     {}
    @Override public void addDeletePlantsListener(ActionListener l)   {}
    @Override public void addFilterPlantsListener(ActionListener l)   {}
    @Override public void addSortPlantsListener(ActionListener l)     {}
    @Override public void addViewPlantsListener(ActionListener l)     {}
    @Override public void addAddExemplarListener(ActionListener l)    {}
    @Override public void addEditExemplarListener(ActionListener l)   {}
    @Override public void addDeleteExemplarListener(ActionListener l) {}
    @Override public void addViewExemplarsListener(ActionListener l)  {}
    @Override public void addSearchExemplarsListener(ActionListener l){}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DesktopAplic::new);
    }
}