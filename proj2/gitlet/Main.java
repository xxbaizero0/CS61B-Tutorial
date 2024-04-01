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
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                validArgs(args, 1);
                Repository.initCommand();
                break;
            case "add":
                Repository.checkIfInitialized();
                // TODO: handle the `add [filename]` command
                validArgs(args, 2);
                StagingArea.add(args[1]);
                break;
            // TODO: FILL THE REST IN
            case "commit":
                Repository.checkIfInitialized();
                validArgs(args, 2);
                Repository.commit(args[1]);
                break;
            case "rm":
                Repository.checkIfInitialized();
                validArgs(args, 2);
                Repository.rm(args[1]);
                break;
            case "log":
                Repository.checkIfInitialized();
                validArgs(args, 1);
                Repository.getLog();
                break;
            case "global-log":
                Repository.checkIfInitialized();
                validArgs(args, 1);
                Repository.getGlobalLog();
                break;
            case "find":
                Repository.checkIfInitialized();
                validArgs(args, 2);
                Repository.find(args[1]);
                break;
            case "status":
                Repository.checkIfInitialized();
                validArgs(args, 1);
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
                Repository.checkIfInitialized();
                validArgs(args, 2);
                Repository.branch(args[1]);
                break;
            case "rm-branch":
                Repository.checkIfInitialized();
                validArgs(args, 2);
                Repository.rmBranch(args[1]);
                break;
            case "reset":
                Repository.checkIfInitialized();
                validArgs(args, 2);
                Repository.reset(args[1]);
                break;
            case "merge":
                Repository.checkIfInitialized();
                validArgs(args, 2);
                Repository.merge(args[1]);
                break;
            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
        }
    }
    private static void validArgs(String[] args, int num) {
        if (args.length != num) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }
}
