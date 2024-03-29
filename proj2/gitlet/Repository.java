package gitlet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
        StagingArea.init();
        init();
    }

    private static void init() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            return;
        }
        GITLET_DIR.mkdir();
        Commit initialCommit = new Commit();
        CommitTree.addCommit(initialCommit);
        File initialCommitFile = Utils.join(GITLET_DIR, "initialCommit");
        Utils.writeObject(initialCommitFile, initialCommit);
    }

    public static void commit(String message) {
        Commit commit = new Commit(message);
        CommitTree.addCommit(commit);
    }

    public static void getLog() {
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
        printBranches();
        printStagedFile();
        printRemovedFiles();
        printModifiedNotStagedFile();
        printUntrackedFiles();
    }

    private static void printBranches() {
        List<String> branchList = Utils.plainFilenamesIn(CommitTree.head);
//        currBranch = readCurrBranch();
//        System.out.println("=== Branches ===");
//        System.out.println("*" + currBranch);
//        if (branchList.size() > 1) {
//            for (String branch : branchList) {
//                if (!branch.equals(currBranch)) {
//                    System.out.println(branch);
//                }
//            }
//        }
        System.out.println();
    }

    private static void printStagedFile() {
        System.out.println("=== Staged Files ===");
//        addStage = StagingArea.additionStage;
//        for (Blobs b : addStage) {
//            System.out.println(b.getFileName());
//        }
        System.out.println();
    }

    private static void printRemovedFiles() {
        System.out.println("=== Removed Files ===");
//        removeStage = readRemoveStage();
//        for (Blob b : removeStage.getBlobList()) {
//            System.out.println(b.getFileName());
//        }
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
}
