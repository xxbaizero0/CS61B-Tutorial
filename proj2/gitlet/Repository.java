package gitlet;

import java.io.File;
import java.util.HashMap;

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

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
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

    public static String getLog() {
        return CommitTree.log;
    }
}
