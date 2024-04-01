import gitlet.*;

import java.io.File;
import java.io.IOException;

public class Text {
    static String[] init = new String[]{"init"};
    static String[] add = new String[]{"add", "hello.txt"};
    static String[] commit = new String[]{"commit", "hello"};
    static String[] log = new String[]{"log"};
    static String[] rm = new String[]{"rm", "hello.txt"};
    static String[] check = new String[]{"checkout", "--", "hello.txt"};
    String[] global_log = new String[]{"global-log"};
    static String[] status = new String[]{"status"};


    private static String[] branch(String name) {
        String[] branch = new String[]{"branch", name};
        return branch;
    }

    private static void creatNewFile(String name) {
        File file = Repository.CWD;
        File newFile = Utils.join(file, name);
        if (!newFile.exists()) {
            try {
                newFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void test1() {
        Main.main(init);
        creatNewFile("f.txt");
        creatNewFile("g.txt");
        creatNewFile("h.txt");
        Main.main(add);
        Main.main(status);
//
        Main.main(commit);
        Main.main(status);
        Main.main(check);

        Main.main(log);
    }

    private static void test2() {
        Main.main(check);
    }

    public static void main(String[] args) {
        test1();
        //test2();
        //Main.main(rm);
    }
}
