package gpt;

import java.io.File;
import java.io.Serializable;

public class FileMessage implements Serializable {
    private File file;

    public FileMessage(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }
}
