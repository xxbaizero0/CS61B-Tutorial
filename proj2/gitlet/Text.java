package gitlet;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.util.HashMap;
import java.util.Set;

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

    private static void merge(String checkBranch) {
        Main.main(new String[]{"merge", checkBranch});
    }

    private static void status() {
        Main.main(new String[]{"status"});
    }

    private static void global_log() {
        Main.main(new String[]{"global-log"});
    }

    static String g = "g.txt";
    static String f = "f.txt";
    static String h = "h.txt";
    static String k = "k.txt";


    @Test
    public void setup() {
        File rep = Repository.GITLET_DIR;
        if (rep.exists()) {
            deleteFolder(rep);
        }
        creatNewFile(g);
        creatNewFile(f);
        creatNewFile(h);
        creatNewFile(k);
        Main.main(init);
    }

    @Test
    public void test2() {
        setup();
        HashMap<String, Integer> masterAncestors = new HashMap<>();
        masterAncestors.put("1", 4);
        masterAncestors.put("2", 3);
        masterAncestors.put("3", 2);
        masterAncestors.put("4", 1);
        masterAncestors.put("5", 0);
        HashMap<String, Integer> branchAncestors = new HashMap<>();
        branchAncestors.put("1", 5);
        branchAncestors.put("22", 4);
        branchAncestors.put("33", 3);
        branchAncestors.put("44", 2);
        branchAncestors.put("55", 1);
        branchAncestors.put("66", 0);
        String split = findSplitPoint(masterAncestors, branchAncestors);
        assertEquals(split, "1");
    }

    @Test
    public void test() {
        setup();
        add(g);
        add(f);
        commit("1");
        branch("other");
        add(h);
        rm(g);
        commit("add h, rm g");
        check("other");
        rm(f);
        add(k);
        commit("rm f, add k");
        check("master");
        global_log();
        merge("other");
        log();
    }

    private static String findSplitPoint(HashMap<String, Integer> masterAncestors, HashMap<String, Integer> branchAncestors) {
        Set<String> masters = masterAncestors.keySet();
        Set<String> branches = branchAncestors.keySet();
        branches.retainAll(masters);
        String latestCommit = "";
        int minDis = 10000;
        for (String name : branches) {
            if (masterAncestors.get(name) < minDis) {
                latestCommit = name;
            }
        }
        return latestCommit;
    }

    private static void test3() {
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

//    public static void main(String[] args) {
//        setup();
//        test2();
//        //Main.main(rm);
//    }

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
