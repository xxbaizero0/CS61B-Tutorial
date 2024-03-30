package gitlet;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class CommitTree {
    public static Commit HEAD;
    public static Commit Master;

    public static String curBranchName;

    public static Commit curBranch;
    static File indexFold = StagingArea.indexFold;

    static File head = Utils.join(Repository.GITLET_DIR, "HEAD"); //file
    static File refs = Utils.join(Repository.GITLET_DIR,"refs"); //file
    static File heads = Utils.join(refs, "heads"); //folder
    static File master = Utils.join(heads, "master"); // folder

    static String log;

    static StringBuilder logSB = new StringBuilder();

    public static void init() {
        try {
            master.createNewFile();
            head.createNewFile();
            HEAD = Utils.readObject(head, Commit.class);
            Master = Utils.readObject(master, Commit.class);
            curBranchName = "Master";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getLog(Commit commit) {
        if (commit.getParent() == null) {
            logSB.append(commit);
            String log1 = logSB.toString();
            logSB = new StringBuilder();
            return log1;
        }
        logSB.append(commit);
        return getLog(fromFile(commit.getParent()));
    }

    public static void addCommit(Commit c) {
        if (HEAD != null) {
            c.setParent(HEAD.getShaName());
            HEAD.copyMapTo(c);
        }
        if (HEAD == null) {
            curBranchName = "Master";
        }
        HEAD = c;
        curBranch = c;
        Utils.writeObject(head, HEAD);
        Utils.writeObject(Utils.join(heads, curBranchName), curBranch);
        c.updateVersion();
        c.checkLegal();
        StagingArea.cleanStage();
        //log += c.toString();
        saveCommit(c);
    }

    public static Commit fromFile(String SHA) {
        String commitSha2 = SHA.substring(0,2);
        String fileName = SHA.substring(2);
        File storeFile = Utils.join(indexFold, commitSha2, fileName);
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

    public static void setBranch(String name) {
        curBranchName = name;
    }

    public static String readCurBranch() {
        return Objects.requireNonNull(Utils.plainFilenamesIn(Utils.join(heads, curBranchName))).get(0);
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
}
