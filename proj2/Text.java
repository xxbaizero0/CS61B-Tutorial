import gitlet.*;
public class Text {
    public static void main(String[] args) {
        String[] arg = new String[]{"init"};
        String[] arg3 = new String[]{"log"};
        String[] arg4 = new String[]{"global-log"};
        String[] arg5 = new String[]{"status"};
        Main.main(arg);
        Main.main(arg5);
        String[] arg1 = new String[]{"add", "hello.txt"};
        Main.main(arg1);
        Main.main(arg5);
//        String[] arg2 = new String[]{"commit", "hello"};
//        Main.main(arg2);
//
//        Main.main(arg4);
    }
}
