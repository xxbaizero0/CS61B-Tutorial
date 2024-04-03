package gitlet;

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


    private static void branch(String name) {
        String[] branch = new String[]{"branch", name};
        Main.main(branch);
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
    private static void add(String name) {
        Main.main(new String[]{"add", name});
    }
    private static void log() {
        Main.main(new String[]{"log"});
    }
    private static void glog() {
        Main.main(new String[]{"global-log"});
    }

    private static void rm(String name) {
        Main.main(new String[]{"rm", name});
    }

    private static void commit(String name) {
        Main.main(new String[]{"commit", name});
    }

    private static void find(String name) {
        Main.main(new String[]{"find", name});
    }
    private static void check(String checkBranch) {
        Main.main(new String[]{"checkout", checkBranch});
    }

    private static void status() {
        Main.main(new String[]{"status"});
    }


    public static void setup() {
        File rep = Repository.GITLET_DIR;
        if (rep.exists()) {
            deleteFolder(rep);
        }
        creatNewFile("hello.txt");
        creatNewFile("hello2.txt");
        creatNewFile("hello3.txt");
        Main.main(init);
//        Main.main(add);
//        Main.main(status);
//
//        Main.main(commit);
//        Main.main(status);
//        Main.main(check);
//
//        Main.main(log);
    }

    public static void test2() {
        String branch = "new";
        branch(branch);
        add("hello.txt");
        add("hello2.txt");
        commit("2");
        check(branch);
        creatNewFile("hello2.txt");
        add("hello2.txt");
        commit("3");
        check("master");
    }

    private static void test3() {
        Main.main(check);
    }

    public static void main(String[] args) {
        setup();
        test2();
        //Main.main(rm);
    }

    public static void deleteFolder(File folder) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteFolder(file);
                }
            }
        }
        folder.delete();
    }
}
