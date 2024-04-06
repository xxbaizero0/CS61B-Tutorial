package gitlet;

// TODO: any imports you need here.
import java.util.Date;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class.
 *  does at a high level.
 *
 *  @author bai
 */
public class Commit implements Serializable {
    /*
      TODO: add instance variables here.

      List all instance variables of the Commit class here with a useful
      comment above them describing what that variable represents and how that
      variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    private List<String> parentID = new ArrayList<>();
    private String timestamp;
    private String shaName;

    private HashMap<String, String> version;

    /*
    personal perspective is that we firstly creat a commit
    containing message and timestamp, and then when we
    add the Commit to the CommitTree,
    we firstly set the ParentID of HEAD pointer, a
    nd then we cope the version map of parent,
    update the version map according to the Stage.
     */

    public Commit(String message) {
        this.message = message;
        this.timestamp = formatDate(true);
        this.version = new HashMap<>();
    }

    public String getFlieVersion(String name) {
        return version.get(name);
    }

    public void setParent(String p) {
        this.parentID.add(p);
    }

    public Commit() {
        this.message = "initial commit";
        this.parentID = null;
        this.timestamp = formatDate(false);
        this.shaName = getSha1ID();
        this.version = new HashMap<>();
    }

    public void setShaName() {
        this.shaName = getSha1ID();
    }

    public boolean checkVersionEmpty() {
        return version.isEmpty();
    }

    public void updateVersion() {
        if (version.isEmpty()) {
            addToVersion();
            Set<String> rmList = StagingArea.removalStage.keySet();
            for (String rmFile : rmList) {
                version.remove(rmFile);
            }
            return;
        }
        List<String> rmList = new ArrayList<>();
        for (String na : version.keySet()) {
            String preVersion = version.get(na);
            String curVersion = StagingArea.getAddStage(na);
            String rmVersion = StagingArea.getRevStage(na);
            if (preVersion != null && !preVersion.equals(curVersion) && curVersion != null) {
                version.put(na, curVersion);
            }
            if (rmVersion != null) {
                rmList.add(na);
            }
        }
        rmFromVersion(rmList);
        addToVersion();
    }

    private void rmFromVersion(List<String> rmlist) {
        for (String rm : rmlist) {
            version.remove(rm);
        }
    }

    private void addToVersion() {
        Set<String> addStage = StagingArea.additionStage.keySet();
        for (String addition : addStage) {
            if (!version.containsKey(addition)) {
                String addVersion = StagingArea.getAddStage(addition);
                version.put(addition, addVersion);
            }
        }
    }
    public void copyMapTo(Commit c) {
        HashMap<String, String> copiedVersion = new HashMap<>(this.version);
        c.setVersionMap(copiedVersion);
    }

    public void setVersionMap(HashMap<String, String> version) {
        this.version = new HashMap<>(version);
    }

    private String getSha1ID() {
        if (parentID != null && version != null) {
            return Utils.sha1(formatDate(true), message, parentID.toString(), version.toString());
        }
        return Utils.sha1(formatDate(true), message);
    }

    // toString 方法
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("===");
        sb.append("\ncommit ").append(shaName);
        if (parentID != null && parentID.size() > 1) {
            sb.append("\nMerge: ");
            Commit par1 = CommitTree.fromFile(parentID.get(0));
            Commit par2 = CommitTree.fromFile(parentID.get(1));
            sb.append(par1.getShaName(), 0, 7);
            sb.append(" ").append(par2.getShaName(), 0, 7);
        }
        sb.append("\nDate: ").append(timestamp);
        sb.append("\n").append(message);
        sb.append("\n");
        sb.append("\n");
        return sb.toString();
    }


    public String getParent(int n) {
        if (parentID == null) {
            return null;
        }
        return parentID.get(n);
    }

    public int getParentCount() {
        if (parentID == null) {
            return 0;
        }
        return parentID.size();
    }

    public void setParentID(List<String> parentID) {
        this.parentID = parentID;
    }

    public String getMessage() {
        return message;
    }

    public HashMap<String, String> getVersion() {
        return version;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getShaName() {
        return shaName;
    }

    public boolean ifVersionContain(String name) {
        return version.containsKey(name);
    }

    private String formatDate(boolean isCurrentTime) {
        long millis = isCurrentTime ? System.currentTimeMillis() : 0L;
        Date currentDate = new Date(millis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy Z", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT-8"));
        return dateFormat.format(currentDate);
    }
    /* TODO: fill in the rest of this class. */
}
