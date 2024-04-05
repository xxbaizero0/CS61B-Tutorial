package gitlet;
import java.io.File;
import java.io.Serializable;
import java.util.*;

public class CommitTree {
    static final String OVER = "over";
    static final String WRITE = "writer";
    static final String DELETE = "delete";
    public static Commit HEAD;

    private static String curBranchName;

    static File indexFold = StagingArea.indexFold;

    static File refs = Utils.join(Repository.GITLET_DIR, "refs"); //folder
    static File heads = Utils.join(refs, "heads"); //folder
    static File master = Utils.join(heads, "master"); // file
    static File head = Utils.join(Repository.GITLET_DIR, "HEAD"); //file

    static File CURBranch = Utils.join(Repository.GITLET_DIR, "curBranch"); //file

    static File commitList = Utils.join(refs, "cList");
    static List<String> cList = new ArrayList<>();

    static StringBuilder logSB = new StringBuilder();

    public static void addCList(String name) {
        cList.add(name);
        Utils.writeObject(commitList, (Serializable) cList);
    }

    @SuppressWarnings("unchecked")
    public static void readCList() {
        cList = (List<String>) Utils.readObject(commitList, ArrayList.class);
    }

    public static void init() {
        try {
            readHEAD();
            curBranchName = "master";
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
        if (commit.getParent(0) == null) {
            logSB.append(commit);
            logSB.delete(logSB.length() - 2, logSB.length());
            String log1 = logSB.toString();

            logSB = new StringBuilder();
            return log1;
        }
        logSB.append(commit);
        return getLog(fromFile(commit.getParent(0)));
    }

    public static void addCommit(Commit c) {
        // During the init time, the CT and SA have not yet init;
        if (HEAD == null) {
            curBranchName = "master";
            Utils.writeObject(head, c);
            Utils.writeObject(Utils.join(heads, curBranchName), c);
            saveCommit(c);
            addCList(c.getShaName());
            return;
        }
        if (StagingArea.checkAddFileExistFile() && StagingArea.checkRemFileExistFile()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        c.setParent(HEAD.getShaName());
        HEAD.copyMapTo(c);
        c.setShaName();
        c.updateVersion();
        StagingArea.cleanStage();
        Utils.writeObject(head, c);
        Utils.writeObject(Utils.join(heads, curBranchName), c);
        //log += c.toString();
        saveCommit(c);
        addCList(c.getShaName());
    }

    public static Commit fromFile(String sha) {
        String commitSha2 = sha.substring(0,2);
        String fileName = sha.substring(2);
        File storeFile = Utils.join(indexFold, commitSha2, fileName);
        if (!storeFile.exists()) {
            return null;
        }

        return Utils.readObject(storeFile, Commit.class);
    }

    private static void saveCommit(Commit commit) {
        String cSha = commit.getShaName();
        Repository.storeInObjectFile(cSha, commit);
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
    }

    public static void creatBranch(String name) {
        File b = Utils.join(heads, name);
        if (b.exists()) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        try {
            b.createNewFile();
            readHEAD();
            Utils.writeObject(b, HEAD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void rmBranch(String name) {
        List<String> headList = Utils.plainFilenamesIn(heads);
        for (String h : headList) {
            if (h.equals(name)) {
                File b = Utils.join(heads, h);
                Commit c = Utils.readObject(b, Commit.class);
                if (c.getShaName().equals(HEAD.getShaName())) {
                    System.out.println("Cannot remove the current branch.");
                    System.exit(0);
                }
                b.delete();
                return;
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

    public static void checkout(String id, String name) {
        // provide a filename and id and put i in the CWD
        Commit c = fromFile(id);
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
        deleteNoExistFile(branch);
        overwriteFile(branch);
        writerFile(branch);
        setCurBranch(Branch);
        Utils.writeObject(head, branch);
    }

    private static void deleteNoExistFile(Commit branch) {
        //TODO: get noExistInCurBranchFIle;
        Set<String> files = getCompareFile(HEAD, branch, DELETE);
        for (String file : files) {
            Utils.join(Repository.CWD, file).delete();
        }
    }

    private static void writerFile(Commit branch) {
        //TODO: get InCurBranchFIleButHead;
        Set<String> files = getCompareFile(HEAD, branch, WRITE);
        List<String> CWDFile = Utils.plainFilenamesIn(Repository.CWD);
        for (String file : files) {
            if (CWDFile != null && CWDFile.contains(file)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }
            writerNewFile(branch, file);
        }
    }

    private static void overwriteFile(Commit branch) {
        Set<String> files = getCompareFile(HEAD, branch, OVER);
        for (String file : files) {
            Utils.join(Repository.CWD, file).delete();
            writerNewFile(branch, file);
        }
    }

    private static void writerNewFile(Commit branch, String file) {
        // this method is creat file in CWD.
        try {
            File newFile = Utils.join(Repository.CWD, file);
            newFile.createNewFile();
            String filePath = new String();
            filePath = branch.getFlieVersion(file);
            Blobs newBlobs = StagingArea.fromFile(filePath);
            byte[] bytes = newBlobs.getaByte();
            Utils.writeContents(newFile, bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Commit readBranch(String Branch) {
        File branch = Utils.join(heads, Branch);
        Commit newBranch;
        if (!branch.exists()) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        newBranch = Utils.readObject(branch, Commit.class);
        return newBranch;
    }

    private static Set<String> getCompareFile(Commit curBranch, Commit branch, String command) {
        Set<String> branchVersion = branch.getVersion().keySet();
        Set<String> headVersion = curBranch.getVersion().keySet();
        Set<String> noExistInCurBranchFile = new HashSet<>();
        Set<String> ExistBothBranchFile = new HashSet<>();
        Set<String> noExistInMasterBranchFile = new HashSet<>();
        switch (command) {
            case DELETE:
                for (String file : headVersion) {
                    if (!branchVersion.contains(file) && headVersion.contains(file)) {
                        noExistInCurBranchFile.add(file);
                    }
                }
                break;
            case OVER:
                for (String file : headVersion) {
                    if (branchVersion.contains(file) && headVersion.contains(file)) {
                        ExistBothBranchFile.add(file);
                    }
                }
                break;
            case WRITE:
                for (String file : branchVersion) {
                    if (branchVersion.contains(file) && !headVersion.contains(file)) {
                        noExistInMasterBranchFile.add(file);
                    }
                }
                break;
            default:
                return null;
        }
        switch (command) {
            case DELETE:
                return noExistInCurBranchFile;
            case OVER:
                return ExistBothBranchFile;
            case WRITE:
                return noExistInMasterBranchFile;
            default:
                return null;
        }
    }
    private static void checkidExist(String commitId) {
        List<Commit> commitList = Repository.getComitList();
        for (Commit c : commitList) {
            if (c.getShaName().equals(commitId)) {
                return;
            }
        }
        System.out.println("No commit with that id exists.");
        System.exit(0);
    }
    public static void reset(String commitId) {
        checkidExist(commitId);
        Commit commit = fromFile(commitId);
        deleteNoExistFile(commit);
        StagingArea.cleanAddStage();
        writerFile(commit);
        Utils.writeObject(head, commit);
        Utils.writeObject(Utils.join(heads, curBranchName), commit);
    }

    public static void merge(String branch) {
        // error check
        mergeErrCheck(branch);
        Commit branchCommit = readBranch(branch);
        HashMap<String, Integer> branchAncestors = new HashMap<>();
        HashMap<String, Integer> masterAncestors = new HashMap<>();
        CommitTree.readHEAD();
        findAncestors(branchCommit, branchAncestors, 0);
        findAncestors(HEAD, masterAncestors, 0);
        // get splitPoint
        Commit splitPoint = findSplitPoint(masterAncestors, branchAncestors);
        Set<String> splitSet = splitPoint.getVersion().keySet();
        HashMap<String, String> curVersion = HEAD.getVersion();
        // specialSituation check
        specialSituation(splitPoint, branchCommit, branch);
        // get changSet
        Set<String> changeFileSetOfBranch = getChangeSet(branchCommit, splitPoint);
        Set<String> noChangeFileSetOfBranch = opposeSet(changeFileSetOfBranch, branchCommit);
        Set<String> changeFileSetOfCurBranch = getChangeSet(HEAD, splitPoint);
        Set<String> noChangeFileSetOfCurBranch = opposeSet(changeFileSetOfCurBranch, HEAD);
        // get existSet
        Set<String> existFileSetOfBranch = getCompareFile(branchCommit, splitPoint, DELETE);
        Set<String> noExistFileSetOfBranch = noExistSet(splitSet, HEAD.getVersion().keySet(), branchCommit.getVersion().keySet());
        Set<String> existFileSetOfCurBranch = getCompareFile(HEAD, splitPoint, DELETE);
        //Set<String> noExistFileSetOfCurBranch = noExistSet(splitPoint.getVersion().keySet(), branchCommit.getVersion().keySet(), HEAD.getVersion().keySet());
        // normal situation
        branchChangeHeadKeep(branchCommit, noChangeFileSetOfCurBranch, changeFileSetOfBranch, curVersion);
        branchExistOthersNot(branchCommit, existFileSetOfBranch, existFileSetOfCurBranch, curVersion);
        OneNotExistOneKeep(splitSet, noExistFileSetOfBranch, noChangeFileSetOfCurBranch, curVersion);
//        OneNotExistOneKeep(noExistFileSetOfCurBranch, noChangeFileSetOfCurBranch, curVersion);
        checkTwoChangeIfSame(branchCommit, changeFileSetOfBranch,changeFileSetOfCurBranch ,curVersion);
        String message = "Merged " + branch + " into " + curBranchName + ".";
        creatMergeCommmit(message, curVersion, branchCommit.getShaName(), HEAD.getShaName());
    }

    private static void creatMergeCommmit(String message, HashMap<String, String> curVersion, String shaName, String shaName1) {
        List<String> parents = new ArrayList<>(List.of(shaName, shaName1));
        Commit commit = new Commit(message);
        commit.setParentID(parents);
        commit.setVersionMap(curVersion);
        commit.setShaName();
        Utils.writeObject(head, commit);
        Utils.writeObject(Utils.join(heads, curBranchName), commit);
    }

    private static Set<String> getChangeSet(Commit branchCommit, Commit splitPoint) {
        HashMap<String, String> branchVersion = branchCommit.getVersion();
        HashMap<String, String> splitPointVersion = splitPoint.getVersion();
        Set<String> changeSet = new HashSet<>();
        Set<String> bothExistSet = getCompareFile(branchCommit, splitPoint, OVER);
        if (bothExistSet != null) {
            for (String file : bothExistSet) {
                if (!branchVersion.get(file).equals(splitPointVersion.get(file))) {
                    changeSet.add(file);
                }
            }
        }
        return changeSet;
    }

    private static Set<String> opposeSet(Set<String> FileSet, Commit Branch) {
        Set<String> formerSet = Branch.getVersion().keySet();
        Set<String> copy = new HashSet<>(formerSet);
        copy.removeAll(FileSet);
        Set<String> result = new HashSet<>(copy);
        return result;
    }

    private static Set<String> noExistSet(Set<String> splitPointSet, Set<String> branchAllSet, Set<String> curBranchAllSet) {
        Set<String> copy = new HashSet<>(branchAllSet);
        copy.addAll(branchAllSet);
        copy.addAll(splitPointSet);
        copy.removeAll(curBranchAllSet);
        return copy;
    }

    private static void checkTwoChangeIfSame(Commit branchCommit,
                                             Set<String> changeFileSetOfBranch,
                                             Set<String> changeFileSetOfCurBranch,
                                             HashMap<String, String> curVersion) {
        Set<String> copy = new HashSet<>(changeFileSetOfBranch);
        copy.retainAll(changeFileSetOfCurBranch);
        for (String file : copy) {
            String branchVersion =  branchCommit.getFlieVersion(file);
            String masterVersion = HEAD.getFlieVersion(file);
            if (branchVersion.equals(masterVersion)) {
                return;
            } else if (masterVersion != null){
                System.out.print("Encountered a merge conflict.");
                twoChangeButDiff(branchCommit, file, curVersion);
                return;
            }
        }
    }

    private static void twoChangeButDiff(Commit branchCommit,String fileName, HashMap<String, String> curVersion) {
        String curFileContentId = HEAD.getFlieVersion(fileName);
        String curFileContent = Utils.readContentsAsString(StagingArea.getFile(curFileContentId));
        String branchFileContentId = branchCommit.getFlieVersion(fileName);
        String branchFileContent = Utils.readContentsAsString(StagingArea.getFile(branchFileContentId));
        String newContent = "<<<<<<< HEAD\n" +
                curFileContent + "\n" +
                "=======\n" +
                 branchFileContent + "\n" +
                ">>>>>>>";
        File newFile = Utils.join(Repository.CWD,fileName);
        Utils.writeContents(newFile, newContent);
        Blobs newBlobs = new Blobs(fileName, newFile);
        curVersion.put(fileName, newBlobs.getSha1ID());
    }

//    private static void twoChangeButSame(Commit splitPoint, Commit branchCommit, Set<String> changeFileSetOfBranch, Set<String> changeFileSetOfCurBranch, HashMap<String, String> curVersion) {
//    }

//    private static void BranchNotExistMasterKeep(Commit splitPoint, Commit branchCommit, Set<String> notExistFile, Set<String> changeFileSetOfCurBranch, HashMap<String, String> curVersion) {
//
//    }

    private static void OneNotExistOneKeep(Set<String> splitPointSet,
                                           Set<String> notExistFiles,
                                           Set<String> noChangeFiles,
                                           HashMap<String, String> curVersion) {
        Set<String> copy = new HashSet<>(splitPointSet);
        copy.retainAll(notExistFiles);
        copy.retainAll(noChangeFiles);
        if (copy == null) {
            return;
        }
        for (String file : copy) {
            curVersion.remove(file);
            File cwdFile =  Utils.join(Repository.CWD, file);
            if (cwdFile.exists()) {
                cwdFile.delete();
            }
        }
    }

    private static void branchExistOthersNot(Commit branchCommit,
                                             Set<String> existFileSetOfBranch,
                                             Set<String> existFileSetOfCurBranch,
                                             HashMap<String, String> curVersion) {
        if (existFileSetOfBranch == null) {
            return;
        }
        Set<String> copy = new HashSet<>(existFileSetOfBranch);
        copy.removeAll(existFileSetOfCurBranch);
        for (String file : copy) {
            String newVersion = branchCommit.getFlieVersion(file);
            curVersion.put(file, newVersion);
            writerNewFile(branchCommit, file);
        }
    }

    private static void branchChangeHeadKeep(Commit branchCommit,
                                             Set<String> curBranchCommit,
                                             Set<String> changeFileSetOfBranch,
                                             HashMap<String, String> curVersion) {
        if (changeFileSetOfBranch == null) {
            return;
        }
        Set<String> copy = new HashSet<>(changeFileSetOfBranch);
        copy.retainAll(curBranchCommit);
        for (String file : copy) {
            String newVersion = branchCommit.getFlieVersion(file);
            curVersion.put(file, newVersion);
            File CWDfile =  Utils.join(Repository.CWD, file);
            if (CWDfile.exists()) {
                CWDfile.delete();
            }
            writerNewFile(branchCommit, file);
        }
    }

    private static void specialSituation(Commit spPoint, Commit branchCommit, String branchName) {
        if (spPoint.getVersion().equals(branchCommit.getVersion())) {
            System.out.print("Given branch is an ancestor of the current branch.");
            System.exit(0);
        }
        if (spPoint.getShaName().equals(HEAD.getShaName())) {
            checkoutBranch(branchName);
            System.out.print("Current branch fast-forwarded.");
            System.exit(0);
        }
    }

    private static void mergeErrCheck(String branch) {
        List<String> branchList = Utils.plainFilenamesIn(heads);
        List<String> CWDFile = Utils.plainFilenamesIn(Repository.CWD);
        if (!StagingArea.checkAddEmpty() || !StagingArea.checkRemEmpty()) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }

        if (!branchList.contains(branch)) {
            System.out.print("A branch with that name does not exist.");
            System.exit(0);
        }
        if (branch.equals(curBranchName)) {
            System.out.print("Cannot merge a branch with itself.");
            System.exit(0);
        }
        for (String file : CWDFile) {
            if (CWDFile != null && !HEAD.getVersion().keySet().contains(file)) {
                System.out.print("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }
        }
    }

    private static Commit findSplitPoint(HashMap<String, Integer> masterAncestors, HashMap<String, Integer> branchAncestors) {
        Set<String> masters = masterAncestors.keySet();
        Set<String> branches = branchAncestors.keySet();
        branches.retainAll(masters);
        String latestCommit = "";
        int minDis = 10000;
        for (String name : branches) {
            if (masterAncestors.get(name) < minDis) {
                latestCommit = name;
                minDis = masterAncestors.get(name);
            }
        }
        return fromFile(latestCommit);
    }

    private static void findAncestors(Commit commit, HashMap<String, Integer> branches, Integer dis) {
        if (commit.getParent(0) == null) {
            branches.put(commit.getShaName(), dis);
            return;
        }
        branches.put(commit.getShaName(), dis);
        Commit parentCommit = fromFile(commit.getParent(0));
        findAncestors(parentCommit, branches, dis + 1);
    }
}
