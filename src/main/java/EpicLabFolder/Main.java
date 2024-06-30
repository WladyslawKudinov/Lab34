package EpicLabFolder;
import EpicLabFolder.OldCodebase.*;

import java.io.File;
import java.io.IOException;

public class Main {
    private static StorageOfReactorsOld storageOfReactorsOld = new StorageOfReactorsOld();

    public static void main(String[] args) {
        GUI gui = new GUI(storageOfReactorsOld);
        try {
            gui.ShowFrame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void importFile(File file) throws IOException {
        JsonImporter importerChain = new JsonImporter();
        XmlImporter xmlImporter = new XmlImporter();
        YamlImporter yamlImporter = new YamlImporter();
        importerChain.setNext(xmlImporter);
        xmlImporter.setNext(yamlImporter);
        importerChain.importFile(file, storageOfReactorsOld);
    }
}