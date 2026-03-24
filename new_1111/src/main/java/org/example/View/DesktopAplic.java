package org.example.View;

import org.example.Presenter.GradinaBotanicaGUI;
import org.example.Presenter.GradinaBotanicaPresenter;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.File;

public class DesktopAplic extends JFrame implements GradinaBotanicaGUI {

    private final JTextField textField_planta_id = new JTextField(4);
    private final JTextField textField_denumire = new JTextField(12);
    private final JTextField textField_tip = new JTextField(10);
    private final JTextField textField_specie = new JTextField(10);
    private final JTextField textField_imagine = new JTextField(20);

    // două controale diferite: unul pentru formular, unul pentru filtru
    private final JCheckBox checkBoxCarnivora = new JCheckBox("Este carnivoră");
    private final JComboBox<String> comboCarnivoraFiltru = new JComboBox<>(new String[]{"", "Da", "Nu"});

    private final JTextField filter_tip = new JTextField(8);
    private final JTextField filter_specie = new JTextField(8);
    private final JComboBox<String> combo_sort = new JComboBox<>();

    private final JTextField textField_exemplar_id = new JTextField(4);
    private final JTextField textField_zona = new JTextField(10);
    private final JTextField textField_imagine_ex = new JTextField(15);
    private final JTextField textField_exemplar_planta_id = new JTextField(4);
    private final JTextField textField_cauta = new JTextField(10);

    private final JTable tablePlante = new JTable();
    private final JTable tableExemplare = new JTable();
    private final JTextArea textArea = new JTextArea();

    private final GradinaBotanicaPresenter presenter;

    public DesktopAplic() {
        setTitle("🌿 Grădina Botanică");
        setSize(1100, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initCombo();
        setContentPane(buildUI());
        presenter = new GradinaBotanicaPresenter(this);
        setVisible(true);
    }

    private void initCombo() {
        combo_sort.addItem("denumire");
        combo_sort.addItem("tip");
        combo_sort.addItem("specie");
    }

    private JPanel buildUI() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Plante", buildPlantePanel());
        tabs.addTab("Exemplare", buildExemplarePanel());
        tabs.addTab("Listă", new JScrollPane(textArea));

        JPanel root = new JPanel(new BorderLayout());
        root.add(tabs, BorderLayout.CENTER);
        return root;
    }

    private JPanel buildPlantePanel() {
        // ─── Butoane ───────────────────────────────────────────────
        JButton add = new JButton("Adaugă");
        JButton edit = new JButton("Editează");
        JButton del = new JButton("Șterge");
        JButton view = new JButton("Afișează toate");
        JButton filter = new JButton("Filtrează");
        JButton sort = new JButton("Sortează");

        add.addActionListener(e -> presenter.adaugaPlanta());
        edit.addActionListener(e -> presenter.actualizeazaPlanta());
        del.addActionListener(e -> presenter.stergePlanta());
        view.addActionListener(e -> presenter.afiseazaToatePlantele());
        filter.addActionListener(e -> presenter.filtreazaPlante());
        sort.addActionListener(e -> presenter.sorteazaPlante());

        // ─── Formular detalii plante (3 rânduri) ───────────────────
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Detalii plantă"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 6, 5, 6);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;

        // rând 1
        g.gridx = 0; g.gridy = 0; form.add(new JLabel("ID:"), g);
        g.gridx = 1; form.add(textField_planta_id, g);
        g.gridx = 2; form.add(new JLabel("Denumire:"), g);
        g.gridx = 3; form.add(textField_denumire, g);

        // rând 2
        g.gridy = 1;
        g.gridx = 0; form.add(new JLabel("Tip:"), g);
        g.gridx = 1; form.add(textField_tip, g);
        g.gridx = 2; form.add(new JLabel("Specie:"), g);
        g.gridx = 3; form.add(textField_specie, g);

        // rând 3
        g.gridy = 2;
        g.gridx = 0; form.add(new JLabel("Carnivoră:"), g);
        g.gridx = 1; form.add(checkBoxCarnivora, g);
        g.gridx = 2; form.add(new JLabel("Imagine:"), g);
        g.gridx = 3; form.add(textField_imagine, g);

        // rând 4 – butoane CRUD
        g.gridy = 3; g.gridx = 0; g.gridwidth = 4;
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        btns.add(add); btns.add(edit); btns.add(del); btns.add(view);
        form.add(btns, g);

        // ─── Filtrare / Sortare ────────────────────────────────────
        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        filters.setBorder(BorderFactory.createTitledBorder("Filtrare & Sortare"));
        filters.add(new JLabel("Tip:"));
        filters.add(filter_tip);
        filters.add(new JLabel("Specie:"));
        filters.add(filter_specie);
        filters.add(new JLabel("Carnivoră:"));
        filters.add(comboCarnivoraFiltru);
        filters.add(filter);
        filters.add(new JLabel(" Sortează după:"));
        filters.add(combo_sort);
        filters.add(sort);

        // ─── Tabel ─────────────────────────────────────────────────
        tablePlante.setDefaultRenderer(Object.class, new ImageTableCellRenderer());
        tablePlante.setRowHeight(100);
        JScrollPane scroll = new JScrollPane(tablePlante);
        scroll.setBorder(BorderFactory.createTitledBorder("Lista plantelor"));

        // ─── Asamblare finală ─────────────────────────────────────
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.add(form, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(filters, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildExemplarePanel() {
        JButton add = new JButton("Adaugă");
        JButton edit = new JButton("Editează");
        JButton del = new JButton("Șterge");
        JButton search = new JButton("Caută");

        add.addActionListener(e -> presenter.adaugaExemplar());
        edit.addActionListener(e -> presenter.actualizeazaExemplar());
        del.addActionListener(e -> presenter.stergeExemplar());
        search.addActionListener(e -> presenter.cautaExemplare());

        // --- rând 1: detalii exemplar ---
        JPanel formTop = new JPanel(new GridLayout(2, 4, 8, 6));
        formTop.setBorder(BorderFactory.createTitledBorder("Detalii exemplar"));
        formTop.add(new JLabel("ID:"));
        formTop.add(textField_exemplar_id);
        formTop.add(new JLabel("Zonă:"));
        formTop.add(textField_zona);

        formTop.add(new JLabel("Imagine (cale sau fișier):"));
        formTop.add(textField_imagine_ex);
        formTop.add(new JLabel("ID Plantă asociată:"));
        formTop.add(textField_exemplar_planta_id);

        // --- rând 2: butoane ---
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        btns.add(add);
        btns.add(edit);
        btns.add(del);

        // --- tabel ---
        tableExemplare.setDefaultRenderer(Object.class, new ImageTableCellRenderer());
        tableExemplare.setRowHeight(100);
        JScrollPane scroll = new JScrollPane(tableExemplare);
        scroll.setBorder(BorderFactory.createTitledBorder("Lista exemplarelor"));

        // --- căutare ---
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Căutare după specie"));
        searchPanel.add(new JLabel("Specie:"));
        searchPanel.add(textField_cauta);
        searchPanel.add(search);

        // --- asamblare finală ---
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        JPanel top = new JPanel(new BorderLayout());
        top.add(formTop, BorderLayout.CENTER);
        top.add(btns, BorderLayout.SOUTH);

        panel.add(top, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(searchPanel, BorderLayout.SOUTH);
        return panel;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // GETTERS INTERFAȚĂ
    // ══════════════════════════════════════════════════════════════════════════
    public String getPlantaDenumire() { return textField_denumire.getText(); }
    public String getPlantaTip() { return textField_tip.getText(); }
    public String getPlantaSpecie() { return textField_specie.getText(); }

    @Override
    public boolean isPlantaCarnivora() {
        return checkBoxCarnivora.isSelected();
    }

    public String getPlantaId() { return textField_planta_id.getText(); }
    public String getPlantaImagePath() { return textField_imagine.getText(); }

    public String getExemplarId() { return textField_exemplar_id.getText(); }
    public String getExemplarZona() { return textField_zona.getText(); }
    public String getExemplarImagine() { return textField_imagine_ex.getText(); }
    public String getExemplarPlantaId() { return textField_exemplar_planta_id.getText(); }

    public String getSortCriteria() { return (String) combo_sort.getSelectedItem(); }
    public String getFilterTip() { return filter_tip.getText(); }
    public String getFilterSpecie() { return filter_specie.getText().isEmpty() ? textField_cauta.getText() : filter_specie.getText(); }

    @Override
    public Boolean getFilterCarnivora() {
        String val = (String) comboCarnivoraFiltru.getSelectedItem();
        if ("Da".equalsIgnoreCase(val)) return true;
        if ("Nu".equalsIgnoreCase(val)) return false;
        return null;
    }

    public void setTextAreaContent(String text) { textArea.setText(text); }

    public void setTableData(String[][] data, String[] col) {
        tablePlante.setModel(new DefaultTableModel(data, col));
    }

    public void setExemplarTableData(String[][] data, String[] col) {
        tableExemplare.setModel(new DefaultTableModel(data, col));
    }

    public void showMessage(String title, String msg) {
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // RENDERER IMAGINI
    // ══════════════════════════════════════════════════════════════════════════
    static class ImageTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object val,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int col) {
            Component c = super.getTableCellRendererComponent(table, val, isSelected, hasFocus, row, col);
            if (val != null && (val.toString().toLowerCase().endsWith(".jpg") || val.toString().toLowerCase().endsWith(".png"))) {
                String path = val.toString();
                if (new File(path).exists()) {
                    ImageIcon icon = new ImageIcon(path);
                    Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    JLabel lbl = new JLabel(new ImageIcon(img));
                    lbl.setHorizontalAlignment(CENTER);
                    return lbl;
                }
            }
            return c;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DesktopAplic::new);
    }
}
