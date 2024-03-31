import gitlet.*;
public class Text {
    public static void main(String[] args) {
        String[] init = new String[]{"init"};
        String[] add = new String[]{"add", "hello.txt"};
        String[] commit = new String[]{"commit", "hello"};
        String[] log = new String[]{"log"};
        String[] global_log = new String[]{"global-log"};
        String[] status = new String[]{"status"};
        Main.main(init);
        Main.main(status);

        Main.main(add);
        Main.main(status);

        Main.main(commit);
        Main.main(status);

        Main.main(log);
    }
}
