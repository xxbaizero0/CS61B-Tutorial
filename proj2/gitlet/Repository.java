package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = Utils.join(CWD, ".gitlet");

    // the HashMap of the version of File


    /* TODO: fill in the rest of this class. */
    public static void initCommand() {
        checkIfSystemExists();
        GITLET_DIR.mkdir();
        StagingArea.indexFold.mkdir();
        CommitTree.refs.mkdir();
        CommitTree.heads.mkdir();
        try {
            CommitTree.master.createNewFile();
            CommitTree.head.createNewFile();
            CommitTree.CURBranch.createNewFile();
            CommitTree.commitList.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        init();
        StagingArea.init();
        CommitTree.init();
    }

    public static void checkIfSystemExists() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);;
        }
    }

    public static void checkIfInitialized() {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }



    private static void init() {
        Commit initialCommit = new Commit();
        CommitTree.addCommit(initialCommit);
//        File initialCommitFile = Utils.join(GITLET_DIR, "initialCommit");
//        Utils.writeObject(initialCommitFile, initialCommit);
    }

    public static void storeInObjectFile(String cSha, Serializable stored) {
        String commitSha2 = cSha.substring(0,2);
        String fileName = cSha.substring(2);

        File storeFile = Utils.join(StagingArea.indexFold, commitSha2, fileName);
        File storeFileFold = Utils.join(StagingArea.indexFold, commitSha2);
        try {
            if (!storeFileFold.exists()) {
                storeFileFold.mkdir();
            }
            if (!storeFile.exists()) {
                storeFile.createNewFile();
            }
            Utils.writeObject(storeFile, stored);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void commit(String message) {
        Commit commit = new Commit(message);
        CommitTree.readHEAD();
        CommitTree.readCurBranch();
        StagingArea.readRmStage();
        StagingArea.readAddStage();
        CommitTree.readCList();
        CommitTree.addCommit(commit);
    }

    public static void getLog() {
        CommitTree.readHEAD();
        System.out.println(CommitTree.getLog(CommitTree.HEAD));
    }

    public static void getGlobalLog() {
        StringBuilder log = new StringBuilder();
        List<Commit> commitList = getComitList();
        for (Commit com : commitList) {
            log.insert(0, com.toString());
        }
        log.delete(log.length() - 2, log.length());
        System.out.println(log);
    }

    public static List<Commit> getComitList() {
        CommitTree.readCList();
        List<Commit> commitList = new ArrayList<>();
        for (String name : CommitTree.cList) {
            commitList.add(CommitTree.fromFile(name));
        }
        return commitList;
    }

    public static void find(String arg) {
        StringBuilder findResult = new StringBuilder();
        List<Commit> commitList = getComitList();
        for (Commit c : commitList) {
            if (c.getMessage().equals(arg)) {
                findResult.append(c.getShaName());
                findResult.append('\n');
            }
        }
        if (findResult.toString().isEmpty()) {
            System.out.println("Found no commit with that message.");
            System.exit(0);
        }
        findResult.delete(findResult.length() - 1, findResult.length());
        System.out.println(findResult);

        //Found no commit with that message.
    }

    public static void status() {
        StagingArea.readRmStage();
        StagingArea.readAddStage();
        printBranches();
        printStagedFile();
        printRemovedFiles();
        printModifiedNotStagedFile();
        printUntrackedFiles();
    }

    public static void checkout(String fileName) {
        CommitTree.checkout(fileName);
    }

    public static void checkout(String ID, String fileName) {
        CommitTree.checkout(ID, fileName);
    }

    public static void checkoutBranch(String branchName) {
        CommitTree.readHEAD();
        CommitTree.checkoutBranch(branchName);

    }

    public static void branch(String arg) {
        CommitTree.creatBranch(arg);
    }

    public static void rmBranch(String arg) {
        CommitTree.readHEAD();
        CommitTree.rmBranch(arg);
    }

    public static void rm(String arg) {
        CommitTree.readHEAD();
        StagingArea.readRmStage();
        StagingArea.readAddStage();
        StagingArea.rm(arg);
    }

    private static void printBranches() {
        List<String> branchList = Utils.plainFilenamesIn(CommitTree.heads);
        String curBranch = CommitTree.readCurBranchAsString();
        System.out.println("=== Branches ===");
        System.out.println("*" + curBranch);
        if (branchList.size() > 1) {
            for (String branch : branchList) {
                if (!branch.equals(curBranch)) {
                    System.out.println(branch);
                }
            }
        }
        System.out.println();
    }

    private static void printStagedFile() {
        System.out.println("=== Staged Files ===");
        Set<String> addStage = StagingArea.additionStage.keySet();
        for (String b : addStage) {
            System.out.println(b);
        }
        System.out.println();
    }

    private static void printRemovedFiles() {
        System.out.println("=== Removed Files ===");
        Set<String> removeStage = StagingArea.removalStage.keySet();
        for (String b : removeStage) {
            System.out.println(b);
        }
        System.out.println();
    }

    private static void printModifiedNotStagedFile() {
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
    }

    private static void printUntrackedFiles() {
        System.out.println("=== Untracked Files ===");
        System.out.println();
    }

    public static void reset(String commitId) {
        CommitTree.readHEAD();
        StagingArea.readRmStage();
        StagingArea.readAddStage();
        CommitTree.readCurBranch();
        CommitTree.reset(commitId);
    }

    public static void merge(String branch) {
        CommitTree.readHEAD();
        StagingArea.readRmStage();
        StagingArea.readAddStage();
        CommitTree.merge(branch);
    }
}
