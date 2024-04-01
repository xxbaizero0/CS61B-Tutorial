package gitlet;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

public class CommitTree {
    public static Commit HEAD;
    public static Commit Master;

    public static String curBranchName;

    public static Commit curBranch;
    static File indexFold = StagingArea.indexFold;


    static File refs = Utils.join(Repository.GITLET_DIR,"refs"); //folder
    static File heads = Utils.join(refs, "heads"); //folder
    static File master = Utils.join(heads, "Master"); // file
    static File head = Utils.join(Repository.GITLET_DIR, "HEAD"); //file

    static File CURBranch = Utils.join(Repository.GITLET_DIR, "curBranch");

    static String log;

    static StringBuilder logSB = new StringBuilder();

    public static void init() {
        try {
            master.createNewFile();
            head.createNewFile();
            CURBranch.createNewFile();
            readHEAD();
            Master = Utils.readObject(master, Commit.class);
            curBranchName = "Master";
            Utils.writeObject(CURBranch, curBranchName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void readHEAD() {
        HEAD = Utils.readObject(head, Commit.class);
    }

    public static Commit getHEAD() {
        return Utils.readObject(head, Commit.class);
    }

    public static String getLog(Commit commit) {
        if (commit.getParent() == null) {
            logSB.append(commit);
            logSB.delete(logSB.length() - 2, logSB.length());
            String log1 = logSB.toString();

            logSB = new StringBuilder();
            return log1;
        }
        logSB.append(commit);
        return getLog(fromFile(commit.getParent()));
    }

    public static void addCommit(Commit c) {
        // During the init time, the CT and SA have not yet init;
        if (HEAD == null) {
            curBranchName = "Master";
            Utils.writeObject(head, c);
            Utils.writeObject(Utils.join(heads, curBranchName), c);
            saveCommit(c);
            return;
        }
        c.setParent(HEAD.getShaName());
        HEAD.copyMapTo(c);
        c.updateVersion();
        c.checkLegal();
        StagingArea.cleanStage();
        Utils.writeObject(head, c);
        Utils.writeObject(Utils.join(heads, curBranchName), c);
        //log += c.toString();
        saveCommit(c);
    }

    public static Commit fromFile(String SHA) {
        String commitSha2 = SHA.substring(0,2);
        String fileName = SHA.substring(2);
        File storeFile = Utils.join(indexFold, commitSha2, fileName);
        if (!storeFile.exists()) {
            return null;
        }

        return Utils.readObject(storeFile, Commit.class);
    }

    private static void saveCommit(Commit commit) {
        String cSha = commit.getSha1ID();
        String commitSha2 = cSha.substring(0,2);
        String fileName = cSha.substring(2);

        File storeFile = Utils.join(indexFold, commitSha2, fileName);
        File storeFileFold = Utils.join(indexFold, commitSha2);
        try {
            if (!storeFileFold.exists()) {
                storeFileFold.mkdir();
            }
            if (!storeFile.exists()) {
                storeFile.createNewFile();
            }
            Utils.writeObject(storeFile, commit);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setCurBranch(String name) {
        curBranchName = name;
        Utils.writeObject(CURBranch, curBranchName);
    }

    public static void readCurBranch() {
        curBranchName = Utils.readObject(CURBranch, String.class);
    }

    public static String readCurBranchAsString() {
        return Utils.readObject(CURBranch, String.class);
//        List<String> bL = Utils.plainFilenamesIn(heads);
//        if (bL != null) {
//            for (String b : bL) {
//                Commit c = Utils.readObject(Utils.join(heads, b), Commit.class);
//                if (c.getShaName().equals(Utils.readObject(head, Commit.class).getShaName())) {
//                    return b;
//                }
//            }
//        }
//        return null;
    }

    public static void creatBranch(String name) {
        File b = Utils.join(heads, name);
        if (b.exists()) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        try {
            b.createNewFile();
            Utils.writeObject(b, head);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void rmBranch(String name) {
        List<String> headList = Utils.plainFilenamesIn(heads);
        for (String h : headList) {
            if (h.equals(name)) {
                File b = Utils.join(heads, h);
                if (Utils.readObject(b, Commit.class).equals(HEAD)) {
                    System.out.println("Cannot remove the current branch.");
                    System.exit(0);
                }
                b.delete();
            }
        }
        System.out.println("A branch with that name does not exist.");
        System.exit(0);
    }

    public static void checkout(String name) {
        // provide a filename and put i in the CWD
        String storedFile = getHEAD().getFlieVersion(name);
        checkoutHelper(name, storedFile);
    }

    public static void checkout(String ID, String name) {
        // provide a filename and ID and put i in the CWD
        Commit c = fromFile(ID);
        if (c == null) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        String storedFile = c.getFlieVersion(name);
        checkoutHelper(name, storedFile);
    }

    private static void checkoutHelper(String name, String storedFile) {
        if (storedFile == null) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        Blobs fileBlob = StagingArea.fromFile(storedFile);
        List<String> fileList = Utils.plainFilenamesIn(Repository.CWD);
        if (fileList != null) {
            for (String f : fileList) {
                if (fileBlob.getFileName().equals(f)) {
                    Utils.join(Repository.CWD, f).delete();
                }
            }
        }
        try {
            File newFile = Utils.join(Repository.CWD, name);
            newFile.createNewFile();
            byte[] bytes = fileBlob.getaByte();
            Utils.writeContents(newFile, bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void checkoutBranch(String Branch) {
        if (Branch.equals(readCurBranchAsString())) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }
        Commit branch = readBranch(Branch);
        Set<String> branchVersion = branch.getVersion().keySet();
        Set<String> headVersion = branch.getVersion().keySet();
        for (String file : branchVersion) {
            if (headVersion.contains(file)) {
                Utils.join(Repository.CWD, file).delete();
                checkoutBranchHelper(file);
            } else {
                checkoutBranchHelper(file);
            }
        }
        setCurBranch(Branch);
    }

    private static void checkoutBranchHelper(String file) {
        try {
            File newFile = Utils.join(Repository.CWD, file);
            newFile.createNewFile();
            Blobs newBlobs = StagingArea.fromFile(file);
            Utils.writeObject(newFile, newBlobs.file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Commit readBranch(String Branch) {
        File branch = Utils.join(heads, Branch);
        if (!branch.exists()) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        return Utils.readObject(branch, Commit.class);
    }
}
