package gitlet;

import java.io.File;
import java.io.Serializable;

public class Blobs implements Serializable {
    //TODO: a Blobs should contain the file, fileName, sha1ID
        /*
        TODO: should the Blobs contain a file?
        think about the commit's stuff
         */
    String fileName;
    String sha1ID;
    File file;

    byte[] aByte;

    public Blobs(String name, File file) {
        this.fileName = name;
        this.file = file;
        this.sha1ID = makeID(name, file);
        this.aByte = readFile();
        // `this` take the place of Utils.readContentsAsString(file)
    }

    public String makeID(String name, File file) {
        return Utils.sha1(Utils.readContentsAsString(file), name);
    }

    public File getFile() {
        return file;
    }

    public String getFileName() {
        return fileName;
    }

    public String getSha1ID() {
        return sha1ID;
    }

    private byte[] readFile() {
        return Utils.readContents(file);
    }

    public byte[] getaByte() {
        return aByte;
    }
}