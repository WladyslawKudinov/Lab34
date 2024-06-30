package EpicLabFolder;

import EpicLabFolder.OldCodebase.StorageOfReactorsOld;
import EpicLabFolder.AddOn.ReactorHolderNew;
import EpicLabFolder.AddOn.ReadFromDB;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;

public class GUI {
    private ReactorHolderNew reactorHolderNew = new ReactorHolderNew();
    private StorageOfReactorsOld storageOfReactorsOld;

    public GUI(StorageOfReactorsOld storageOfReactorsOld) {
        this.storageOfReactorsOld = storageOfReactorsOld;
    }
    public void ShowFrame() {
        JFrame frame = new JFrame("Nuclear Reactor Data Analysis");
        GUI mainFrame = new GUI(storageOfReactorsOld);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon(getClass().getResource("/nuclear.jpeg"));                Image img = icon.getImage();
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JButton chooseButton = new JButton("Choose YAML, JSON, or XML File");
        chooseButton.setIcon(new ImageIcon("icons/file.png"));
        chooseButton.setToolTipText("Select a YAML, JSON, or XML file to import data.");
        chooseButton.setPreferredSize(new Dimension(300, 40));

        JButton selectFileButton = new JButton("Select Database File");
        selectFileButton.setIcon(new ImageIcon("icons/database.png"));
        selectFileButton.setToolTipText("Select an SQLite database file.");
        selectFileButton.setPreferredSize(new Dimension(200, 40));

        JButton countryButton = new JButton("Aggregate by Country");
        countryButton.setIcon(new ImageIcon("icons/country.png"));
        countryButton.setToolTipText("View consumption aggregated by country.");
        countryButton.setPreferredSize(new Dimension(200, 40));

        JButton operatorButton = new JButton("Aggregate by Operator");
        operatorButton.setIcon(new ImageIcon("icons/operator.png"));
        operatorButton.setToolTipText("View consumption aggregated by operator.");
        operatorButton.setPreferredSize(new Dimension(200, 40));

        JButton regionButton = new JButton("Aggregate by Region");
        regionButton.setIcon(new ImageIcon("icons/region.png"));
        regionButton.setToolTipText("View consumption aggregated by region.");
        regionButton.setPreferredSize(new Dimension(200, 40));

        selectFileButton.setEnabled(false);
        countryButton.setEnabled(false);
        operatorButton.setEnabled(false);
        regionButton.setEnabled(false);
        chooseButton.setOpaque(true);
        selectFileButton.setOpaque(true);
        countryButton.setOpaque(true);
        operatorButton.setOpaque(true);
        regionButton.setOpaque(true);

        chooseButton.addActionListener(e -> {
            JFileChooser fileChooser;
            try {
                fileChooser = new JFileChooser(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile());
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
            FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON, XML, YAML Files", "json", "xml", "yaml");
            fileChooser.setFileFilter(filter);
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    Main.importFile(selectedFile);
                    JOptionPane.showMessageDialog(null, "Data successfully imported!");
                    reactorHolderNew.calculateConsumptionPerYear();
                    selectFileButton.setEnabled(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Warning", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        selectFileButton.addActionListener(e -> {
            JFileChooser fileChooser;
            try {
                fileChooser = new JFileChooser(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile());
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
            fileChooser.setDialogTitle("Select SQLite Database File");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("SQLite Database (*.sqlite, *.db)", "sqlite", "db");
            fileChooser.setFileFilter(filter);
            int returnValue = fileChooser.showOpenDialog(frame);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    ReadFromDB.fetchFromDatabase(selectedFile.getAbsolutePath(), reactorHolderNew, storageOfReactorsOld);
                    JOptionPane.showMessageDialog(null, "Data successfully imported!");
                    reactorHolderNew.calculateConsumptionPerYear();
                    countryButton.setEnabled(true);
                    operatorButton.setEnabled(true);
                    regionButton.setEnabled(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Warning", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        countryButton.addActionListener(e -> showAggregateData("Country", reactorHolderNew.agregatePerCountry()));
        operatorButton.addActionListener(e -> showAggregateData("Operator", reactorHolderNew.agregatePerOperator()));
        regionButton.addActionListener(e -> showAggregateData("Region", reactorHolderNew.agregatePerRegion()));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(chooseButton, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(selectFileButton, gbc);

        gbc.gridx = 1;
        panel.add(countryButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(operatorButton, gbc);

        gbc.gridx = 1;
        panel.add(regionButton, gbc);

        frame.getContentPane().add(panel);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void showAggregateData(String aggregationType, HashMap<String, HashMap<Integer, Double>> aggregateMap) {
        String[] columnNames = {aggregationType, "Year", "Annual Consumption"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (HashMap.Entry<String, HashMap<Integer, Double>> entry : aggregateMap.entrySet()) {
            String key = entry.getKey();
            HashMap<Integer, Double> yearData = entry.getValue();
            for (int year = 2014; year <= 2024; year++) {
                Object[] rowData = new Object[3];
                rowData[0] = key;
                rowData[1] = year;
                rowData[2] = yearData.getOrDefault(year, 0.0);
                model.addRow(rowData);
            }
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        JFrame tableFrame = new JFrame("Aggregate Consumption by " + aggregationType);
        tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tableFrame.setSize(800, 600);
        tableFrame.add(scrollPane);
        tableFrame.setLocationRelativeTo(null);
        tableFrame.setVisible(true);
    }
//    private void importFile(File file) throws IOException {
//        JsonFileImporter importerChain = new JsonFileImporter();
//        XmlFileImporter xmlImporter = new XmlFileImporter();
//        YamlFileImporter yamlImporter = new YamlFileImporter();
//        importerChain.setNext(xmlImporter);
//        xmlImporter.setNext(yamlImporter);
//        importerChain.importFile(file, reactorTypeStorage);
  //   }

}