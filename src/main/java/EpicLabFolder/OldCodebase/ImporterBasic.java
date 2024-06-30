package EpicLabFolder.OldCodebase;

import java.io.File;
import java.io.IOException;

public abstract class ImporterBasic {
    protected ImporterBasic next;

    public void setNext(ImporterBasic next) {
        this.next = next;
    }

    public abstract void importFile(File file, StorageOfReactorsOld reactorMap) throws IOException;
}

