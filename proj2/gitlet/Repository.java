package gitlet;

import java.io.File;
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

    public static void commit(String message) {
        Commit commit = new Commit(message);
        CommitTree.readHEAD();
        CommitTree.readCurBranch();
        StagingArea.readRmStage();
        StagingArea.readAddStage();
        CommitTree.addCommit(commit);
    }

    public static void getLog() {
        CommitTree.readHEAD();
        System.out.println(CommitTree.getLog(CommitTree.HEAD));
    }

    public static void getGlobalLog() {
        StringBuilder log = new StringBuilder();
        List<String> commitList = getComitList();
        for (String com : commitList) {
            log.append(CommitTree.fromFile(com).toString());
        }
        System.out.println(log.toString());
    }

    private static List<String> getComitList() {
        List<String> FileList = Utils.plainFilenamesIn(CommitTree.indexFold);
        List<String> commitList = new ArrayList<>();
        if (FileList != null) {
            for (String com : FileList) {
                if (!StagingArea.blobs.contains(com)) {
                    commitList.add(com);
                }
            }
        }
        return commitList;
    }

    public static void find(String arg) {
        StringBuilder findResult = new StringBuilder();
        List<String> commitList = getComitList();
        for (String com : commitList) {
            Commit c = CommitTree.fromFile(com);
            if (c.getMessage().equals(arg)) {
                findResult.append(c.getSha1ID());
                findResult.append('\n');
            }
        }
        System.out.println(findResult.toString());
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
        CommitTree.checkoutBranch(branchName);

    }

    public static void branch(String arg) {
        CommitTree.creatBranch(arg);
    }

    public static void rmBranch(String arg) {
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
    }

    public static void merge(String branch) {
    }
}
