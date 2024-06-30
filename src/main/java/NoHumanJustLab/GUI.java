package NoHumanJustLab;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class GUI {
    private ReactorTypeStorage reactorHolder = new ReactorTypeStorage();

    public static void main(String[] args) {
        showFrame();
    }

    public static void showFrame() {
        GUI mainFrame = new GUI();
        JFrame frame = new JFrame();
        frame.setTitle("Laba 3");
        JLabel label = new JLabel("Select file");
        JButton chooseButton = new JButton("Select file");

        chooseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = null;
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
                        mainFrame.importFile(selectedFile);
                        mainFrame.displayReactorTree();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.add(label);
        label.setFont(new Font("Verdana", Font.BOLD, 14));
        panel.add(chooseButton);
        chooseButton.setFont(new Font("Verdana", Font.BOLD, 14));
        frame.add(panel);
        frame.setSize(500, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void importFile(File file) throws IOException {
        JsonBasicImporter importerChain = new JsonBasicImporter();
        XmlBasicImporter xmlImporter = new XmlBasicImporter();
        YamlBasicImporter yamlImporter = new YamlBasicImporter();
        importerChain.setNext(xmlImporter);
        xmlImporter.setNext(yamlImporter);
        importerChain.importFile(file, reactorHolder);
    }

    private void displayReactorTree() {
        JFrame treeFrame = new JFrame("Reactor Tree");
        treeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Reactor Types");
        for (String type : reactorHolder.getReactorMap().keySet()) {
            DefaultMutableTreeNode reactorNode = new DefaultMutableTreeNode(type);
            root.add(reactorNode);
        }

        JTree tree = new JTree(root);
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                if (selectedNode != null && selectedNode.getUserObject() instanceof String) {
                    String reactorName = (String) selectedNode.getUserObject();
                    ReactorType reactor = reactorHolder.getReactorMap().get(reactorName);
                    if (reactor != null) {
                        showReactorData(reactor);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tree);
        treeFrame.add(scrollPane, BorderLayout.CENTER);

        treeFrame.pack();
        treeFrame.setLocationRelativeTo(null);
        treeFrame.setVisible(true);
    }

    private void showReactorData(ReactorType reactor) {
        JFrame infoFrame = new JFrame("Reactor Data");
        JTextArea infoTextArea = new JTextArea(reactor.toString());
        infoTextArea.setEditable(false);
        infoTextArea.setFont(new Font("Verdana", Font.BOLD, 14));
        infoFrame.add(infoTextArea);
        infoFrame.pack();
        infoFrame.setLocationRelativeTo(null);
        infoFrame.setVisible(true);
    }

}
