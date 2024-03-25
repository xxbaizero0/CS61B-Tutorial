package gitlet;

// TODO: any imports you need here

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date; // TODO: You'll likely use this in this class
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
    private String parentID;
    private String timestamp;

    public Commit(String message, String parentID) {
        this.message = message;
        this.parentID = parentID;
    }

    public Commit() {
        this.message = "initial commit";
        this.parentID = null;
        this.timestamp = formatDate(false);
    }

    public String getParent() {
        return parentID;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    private String formatDate(boolean isCurrentTime) {
        long millis = isCurrentTime ? System.currentTimeMillis() : 0L;
        Date currentDate = new Date(millis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss zzz, EEEE, d MMMM yyyy", Locale.ENGLISH);
        return dateFormat.format(currentDate);
    }
    /* TODO: fill in the rest of this class. */
}
