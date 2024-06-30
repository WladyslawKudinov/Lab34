package NoHumanJustLab;

import java.io.File;
import java.io.IOException;

public abstract class BasicImporter {
    protected BasicImporter next;

    public void setNext(BasicImporter next) {
        this.next = next;
    }

    public abstract void importFile(File file, ReactorTypeStorage reactorMap) throws IOException;
}

