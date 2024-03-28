package gitlet;

// TODO: any imports you need here

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author bai
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    private List<String> parentID;
    private String timestamp;
    private String shaName;

    public HashMap<String, String> version = new HashMap<String, String>();

    /*
    personal perspective is that we firstly creat a commit containing message and timestamp, and then when we
    add the Commit to the CommitTree, we firstly set the ParentID of HEAD pointer, and then we cope the version map of
    parent, update the version map according to the Stage.
     */

    public Commit(String message) {
        this.message = message;
        this.timestamp = formatDate(true);
    }

    public void setParent(String p) {
        this.parentID.add(p);
    }



    public Commit() {
        this.message = "initial commit";
        this.parentID = null;
        this.timestamp = formatDate(false);
    }

    public void checkLegal() {
        if (this.message.isEmpty()) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }
        if (StagingArea.additionStage.isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
    }

    public void updateVersion() {
        for (String na : version.keySet()) {
            String preVersion = version.get(na);
            String curVersion = StagingArea.additionStage.get(na);
            String rmVersion = StagingArea.removalStage.get(na);
            if (!preVersion.equals(curVersion)) {
                version.put(na, curVersion);
            }
            version.remove(rmVersion);
        }
    }

    public void copyMapTo(Commit c) {
        HashMap<String, String> copiedVersion = new HashMap<>(this.version);
        c.setVersionMap(copiedVersion);
    }

    public void setVersionMap(HashMap<String, String> version) {
        this.version = version;
    }

    public String getSha1ID() {
        return Utils.sha1(this);
    }

    // toString 方法
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("===");
        sb.append("\ncommit ").append(shaName);
        sb.append("\nDate: ").append(timestamp);
        sb.append("\n").append(message);
        if (parentID.size() > 1) {
            sb.append("\nMerge: ");
            Commit par1 = CommitTree.fromFile(parentID.get(0));
            Commit par2 = CommitTree.fromFile(parentID.get(1));
            sb.append(par1.getSha1ID(), 0, 7);
            sb.append(" ").append(par2.getSha1ID(), 0, 7);
        }
        sb.append("\n");
        return sb.toString();
    }


    public String getParent() {
        return parentID.get(0);
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getShaName() {
        return shaName;
    }

    private String formatDate(boolean isCurrentTime) {
        long millis = isCurrentTime ? System.currentTimeMillis() : 0L;
        Date currentDate = new Date(millis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss zzz, EEEE, d MMMM yyyy", Locale.ENGLISH);
        return dateFormat.format(currentDate);
    }
    /* TODO: fill in the rest of this class. */
}
