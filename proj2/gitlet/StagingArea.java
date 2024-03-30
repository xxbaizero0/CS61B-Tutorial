package gitlet;

import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static gitlet.Utils.join;

public class StagingArea {
    private static final File CWD = Repository.CWD;

    public static File indexFold = Utils.join(Repository.GITLET_DIR, "object");//folder

    public static List<String> blobs = new ArrayList<>();

    public static HashMap<String, String> additionStage = new HashMap<>();
    public static HashMap<String, String> removalStage = new HashMap<>();
    public static File additionStageFile = Utils.join(Repository.GITLET_DIR, "additionStage");//file
    public static File removalStageFile = Utils.join(Repository.GITLET_DIR, "removalStage"); //file


    public static void init() {
        try {
            additionStageFile.createNewFile();
            removalStageFile.createNewFile();
        //TODO:creat the StagingArea
            System.out.println(".index creat");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private static void readAddStage() {
        HashMap additionalStageObj = Utils.readObject(additionStageFile, HashMap.class);
        if (additionalStageObj != null) {
            additionStage = (HashMap<String, String>) additionalStageObj;
        }
    }

    @SuppressWarnings("unchecked")
    private static void readRmStage() {
        HashMap removalStageObj = Utils.readObject(removalStageFile, HashMap.class);
        if (removalStageObj != null) {
            removalStage = (HashMap<String, String>)removalStageObj;
        }
    }
    public static void add(String name) {
        //TODO:Serialize the file added and then store in the StagingArea
        //TODO:How to get the file of the name?
        /*
         */
        if (!Utils.readContentsAsString(additionStageFile).isEmpty()) {
            readAddStage();
        }
        File addFile = findAddFile(name);
        if (addFile == null) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        Blobs blob = new Blobs(name, addFile);
        blobs.add(blob.getFileName());
        saveAddStage();
        saveBlobs(blob);
        additionStage.put(name, blob.getSha1ID());
    }

    public static void rm(String name) {
        if (!Utils.readContentsAsString(removalStageFile).isEmpty()) {
            readRmStage();
        }
        //TODO: if add, cancel
        if (additionStage.containsKey(name)) {
            additionStage.remove(name);
        } else if (CommitTree.HEAD.version.containsKey(name)) {
            String rmID = additionStage.get(name);
            removalStage.put(name, rmID);
            saveRmStage();
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
        removalStage.clear();
    }

    private static Blobs fromFile(String SHA) {
        String blobSha2 = SHA.substring(0,2);
        String fileName = SHA.substring(2);
        File storeFile = Utils.join(indexFold, blobSha2, fileName);
        return Utils.readObject(storeFile, Blobs.class);
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

    private static void saveAddStage() {
        Utils.writeObject(additionStageFile, additionStage);
    }

    private static void saveRmStage() {
        Utils.writeObject(removalStageFile, removalStage);
    }
}


