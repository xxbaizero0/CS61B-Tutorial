package gitlet;

import java.io.File;

import java.util.HashMap;


public class StagingArea {
    private static final File CWD = Repository.CWD;

    public static File indexFold = Utils.join(Repository.GITLET_DIR, "object");//folder

    public static HashMap<String, String> additionStage = new HashMap<>();
    public static HashMap<String, String> removalStage = new HashMap<>();
    public static File additionStageFile = Utils.join(Repository.GITLET_DIR, "additionStage");//file
    public static File removalStageFile = Utils.join(Repository.GITLET_DIR, "removalStage"); //file



    public static void init() {
        try {
            additionStageFile.createNewFile();
            removalStageFile.createNewFile();
        //  TODO:creat the StagingArea.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static void readAddStage() {
        if (checkAddFileExistFile()) {
            return;
        }
        additionStage = (HashMap<String, String>) Utils.readObject(additionStageFile, HashMap.class);
    }

    @SuppressWarnings("unchecked")
    public static void readRmStage() {
        if (checkRemFileExistFile()) {
            return;
        }
        removalStage = (HashMap<String, String>) Utils.readObject(removalStageFile, HashMap.class);
    }
    public static void add(String name) {
        //  TODO:Serialize the file added and then store in the StagingArea.
        //  TODO:How to get the file of the name?.
        readAddStage();
        readRmStage();
        CommitTree.readHEAD();
        File addFile = findAddFile(name);
        if (addFile == null) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        Blobs blob = new Blobs(name, addFile);
        saveBlobs(blob);
        if (removalStage.containsKey(name)) {
            cleanRemStage();
            return;
        }
        if (blob.getSha1ID().equals(CommitTree.HEAD.getFlieVersion(name))) {
            // the situation of had add in Head Commit;
            return;
        }
        additionStage.put(name, blob.getSha1ID());
        saveAddStage();
    }

    public static void rm(String name) {
        File file = Utils.join(CWD, name);
        //  TODO: if add, cancel.
        if (additionStage.containsKey(name)) {
            additionStage.remove(name);
            saveAddStage();
        } else if (CommitTree.HEAD.ifVersionContain(name)) {
            String rmID = CommitTree.HEAD.getFlieVersion(name);
            removalStage.put(name, rmID);
            saveRmStage();
            if (file.exists()) {
                file.delete();
            }
        } else {
            System.out.println("No reason to remove the file");
            System.exit(0);
        }
        //  TODO: if commmit and track, remark delete and rm from CWD.
    }

    public static void cleanStage() {
        cleanAddStage();
        cleanRemStage();
    }

    public static void cleanRemStage() {
        removalStage.clear();
        saveRmStage();
    }

    public static void cleanAddStage() {
        additionStage.clear();
        saveAddStage();
    }

    @SuppressWarnings("unchecked")
    public static boolean checkAddEmpty() {
        additionStage = (HashMap<String, String>) Utils.readObject(additionStageFile, HashMap.class);
        return additionStage.isEmpty();
    }

    @SuppressWarnings("unchecked")
    public static boolean checkRemEmpty() {
        removalStage = (HashMap<String, String>) Utils.readObject(removalStageFile, HashMap.class);
        return removalStage.isEmpty();
    }

    public static Blobs fromFile(String SHA) {
        String blobSha2 = SHA.substring(0, 2);
        String fileName = SHA.substring(2);
        File storeFile = Utils.join(indexFold, blobSha2, fileName);
        return Utils.readObject(storeFile, Blobs.class);
    }

    public static File getFile(String SHA) {
        String blobSha2 = SHA.substring(0, 2);
        String fileName = SHA.substring(2);
        return Utils.join(indexFold, blobSha2, fileName);
    }

    private static void saveBlobs(Blobs blob) {
        String blobSha = blob.getSha1ID();
        Repository.storeInObjectFile(blobSha, blob);
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

    public static void saveAddStage() {
        Utils.writeObject(additionStageFile, additionStage);
    }

    public static void saveRmStage() {
        Utils.writeObject(removalStageFile, removalStage);
    }

    public static String getAddStage(String name) {
        readAddStage();
        return additionStage.get(name);
    }

    public static String getRevStage(String name) {
        readRmStage();
        return removalStage.get(name);
    }

    public static boolean checkAddFileExistFile() {
        return Utils.readContentsAsString(additionStageFile).isEmpty();
    }

    public static boolean checkRemFileExistFile() {
        return Utils.readContentsAsString(removalStageFile).isEmpty();
    }
}


