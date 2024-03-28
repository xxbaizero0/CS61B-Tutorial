package gitlet;

import java.io.File;

public class CommitTree {
    public static Commit HEAD;
    public static Commit Master;
    static File indexFold = StagingArea.indexFold;

    static File head = Utils.join(Repository.GITLET_DIR, "HEAD");
    static File refs = Utils.join(Repository.GITLET_DIR,"refs");
    static File heads = Utils.join(refs, "heads");
    static File master = Utils.join(refs, "master");

    static String log;

    static StringBuilder logSB;

    public static void init() {
        try {
            if (!refs.exists()) {
                refs.mkdir();
            }
            if (!heads.exists()) {
                heads.mkdir();
            }
            if (!master.exists()) {
                master.createNewFile();

            }
            if (!head.exists()) {
                head.createNewFile();
            }
            HEAD = Utils.readObject(head, Commit.class);
            Master = Utils.readObject(master, Commit.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getLog(Commit commit) {
        if (commit.getParent() == null) {
            String log1 = logSB.toString();
            logSB = new StringBuilder();
            return log1;
        }
        logSB.append(commit.toString());
        return getLog(fromFile(commit.getParent()));
    }

    public static void addCommit(Commit c) {
        c.setParent(HEAD.getShaName());
        HEAD.copyMapTo(c);
        HEAD = c;
        Master = c;
        Utils.writeObject(head, HEAD);
        Utils.writeObject(master, Master);
        c.updateVersion();
        c.checkLegal();
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
}
