package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                Repository.initCommand();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                StagingArea.add(args[1]);
                break;
            // TODO: FILL THE REST IN
            case "commit":
                Repository.commit(args[1]);
                break;
            case "log":
                Repository.getLog();
                break;
            case "global-log":
                Repository.getGlobalLog();
                break;
            case "find":
                Repository.find(args[1]);
                break;
            case "status":
                Repository.status();
                break;
            case "checkout":
                //Repository.checkout();
                break;
            case "branch":
                //Repository.branch();
                break;
            case "rm-branch":
                //Repository.rmBranch();
                break;
            case "reset":
                //Repository.reset();
                break;
            case "merge":
                //Repository.merge();
                break;
        }
    }
}
