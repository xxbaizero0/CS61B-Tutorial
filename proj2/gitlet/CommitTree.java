package gitlet;
import java.io.File;
import java.io.Serializable;
import java.util.List;

public class CommitTree {
    public static Commit HEAD;
    public static Branch Master;

    public static Branch curBranch;
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
            Master = Utils.readObject(master, Branch.class);
            Master.setActivity(true);
            readCurBrunch();
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
        HEAD = c;
        curBranch.commit = HEAD;
        Utils.writeObject(head, HEAD);
        Utils.writeObject(curBranch.getBranchPath(), curBranch);
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

    public static void readCurBrunch() {
        List<String> branchList = Utils.plainFilenamesIn(CommitTree.heads);
        for (String c : branchList){
            File c1 = Utils.join(heads, c);
            Branch c2 = Utils.readObject(c1, Branch.class);
            if (c2.getActivity()) {
                curBranch.branchPath = c1;
                curBranch = c2;
            }
        }
    }


    class Branch implements Serializable {
        Commit commit;
        Boolean activity;

        File branchPath;

        public Branch (Commit com, Boolean act, File path) {
            this.commit = com;
            this.activity = act;
            this.branchPath = path;
        }

        public void setBranchPath(File branchPath) {
            this.branchPath = branchPath;
        }

        public Commit getCommit() {
            return commit;
        }

        public String getName() {
            return commit.getShaName();
        }

        public Boolean getActivity() {
            return activity;
        }

        public void setActivity(Boolean activity) {
            this.activity = activity;
        }

        public void setActivity() {
            activity = !activity;
        }

        public File getBranchPath() {
            return branchPath;
        }
    }
}
