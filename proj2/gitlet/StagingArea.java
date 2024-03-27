package gitlet;

import java.io.File;

import java.util.HashMap;

import static gitlet.Utils.join;

public class StagingArea {
    private static final File CWD = Repository.CWD;

    public static final File indexFold = Utils.join(Repository.GITLET_DIR, "object");

    public static HashMap<String, String> additionStage = new HashMap<>();
    public static HashMap<String, String> removalStage = new HashMap<>();
    public static File additionStageFile = Utils.join(Repository.GITLET_DIR, "additionStage");
    public static File removalStageFile = Utils.join(Repository.GITLET_DIR, "removalStage");

    public static void init() {
        try {
            if (!additionStageFile.exists()) {
                additionStageFile.createNewFile();
            }
            Utils.writeObject(additionStageFile, additionStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, String> stage = (HashMap<String, String>)Utils.readObject(additionStageFile,HashMap.class);
        //TODO:creat the StagingArea
        if (!indexFold.exists()) {
            indexFold.mkdir();
            System.out.println(".index creat");
        }
    }

    public static void add(String name) {
        //TODO:Serialize the file added and then store in the StagingArea
        //TODO:How to get the file of the name?
        /*
         */
        File addFile = findAddFile(name);
        if (addFile == null) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        Blobs blob = new Blobs(name, addFile);
        saveBlobs(blob);
        additionStage.put(name, blob.getSha1ID());
    }

    public static void rm(String name) {
        //TODO: if add, cancel
        if (additionStage.containsKey(name)) {
            additionStage.remove(name);
        } else if (CommitTree.HEAD.version.containsKey(name)) {
            String rmID = additionStage.get(name);
            removalStage.put(name, rmID);
            File file = Utils.join(CWD, name);
            if (file.exists()) {
                file.delete();
            }
        } else {
            System.out.println("No reason to remove the file");
        }
        //TODO: if commmit and track, remark delete and rm from CWD
    }

    public static void cleanStage() {
        additionStage.clear();
    }

    private static File fromFile(String SHA) {
        String blobSha2 = SHA.substring(0,2);
        String fileName = SHA.substring(2);
        File storeFile = Utils.join(indexFold, blobSha2, fileName);
        return Utils.readObject(storeFile, File.class);
    }

    private static void saveBlobs(Blobs blob) {
        String blobSha = blob.getSha1ID();
        String blobSha2 = blobSha.substring(0,2);
        String fileName = blobSha.substring(2);
        File storeFile = Utils.join(indexFold, blobSha2, fileName);
        File storeFileFold = Utils.join(indexFold, blobSha2);
        try {
            if (!storeFileFold.exists()) {
                storeFileFold.mkdir();
            }
            if (!storeFile.exists()) {
                storeFile.createNewFile();
            }
            Utils.writeObject(storeFile, blob);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static File findAddFile(String name) {
        File[] files = CWD.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().equals(name)) {
                    return file;
                }
            }
        }
        return null;
    }
}


