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
            case "rm":
                Repository.rm(args[1]);
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
                switch (args.length) {
                    case 3:
                        if (!args[1].equals("--")) {
                            System.out.println("Incorrect operands.");
                            System.exit(0);
                        }
                        /* * checkout -- [file name] */
                        Repository.checkout(args[2]);
                        break;

                    case 4:
                        if (!args[2].equals("--")) {
                            System.out.println("Incorrect operands.");
                            System.exit(0);
                        }
                        /* * checkout [commit id] -- [file name] */
                        Repository.checkout(args[1], args[3]);
                        break;

                    case 2:
                        /* * checkout [branch name] */
                        Repository.checkoutBranch(args[1]);
                        break;

                    default:
                        System.out.println("Incorrect operands.");
                        System.exit(0);
                }
                break;
            case "branch":
                Repository.branch(args[1]);
                break;
            case "rm-branch":
                Repository.rmBranch(args[1]);
                break;
            case "reset":
                //Repository.reset();
                break;
            case "merge":
                //Repository.merge();
                break;
            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
        }
    }
}
