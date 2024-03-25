package gitlet;

import java.io.File;
import java.io.Serializable;

import java.util.HashMap;

import static gitlet.Utils.join;

public class StagingArea {
    private static final File CWD = Repository.CWD;

    public static final File indexFold = Utils.join(Repository.GITLET_DIR, ".index");

    private HashMap<String, Blobs> index = new HashMap<String, Blobs>();

    public static void init() {
        //TODO:creat the StagingArea
        indexFold.mkdir();
    }

    private void add(String name) {
        //TODO:Serialize the file added and then store in the StagingArea
        //TODO:How to get the file of the name?
        /*
        将文件的当前副本添加到暂存区（参见 commit 命令的描述）。因此，添加文件也称为为添加而暂存文件。将已经暂存的文件暂存会使用新内容覆盖暂存区中的先前条目。
        暂存区应该位于.gitlet的某个地方。如果文件的当前工作版本与当前提交中的版本相同，则不要将其暂存以添加，
        并且如果已经存在（当文件更改、添加，然后更改回其原始版本时可能会发生）的情况下，从暂存区删除它。
        文件将不再被暂存以删除（请参见 gitlet rm），如果在命令时处于该状态。
        如果文件不存在，则打印错误消息 File does not exist. 并且退出而不更改任何内容。
         */
        File addFile = findAddFile(name);
        if (addFile == null) {
            System.out.println("File does not exist.");
            return;
        }
        Blobs blob = new Blobs(name, addFile);
        String blobSha2 = blob.getSha1ID().substring(0,2);
        String blobSha = blob.getSha1ID();
        index.put(blob.getSha1ID(), blob);
        File storeFile = Utils.join(indexFold,blobSha2, blobSha);
        Utils.writeObject(storeFile, blob);
    }

    private static File findAddFile(String name) {
        File[] files = CWD.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().equals(name)) {
                    return file;
                }
            }
        }
        return null;
    }


    public class Blobs implements Serializable {
        //TODO: a Blobs should contain the file, fileName, sha1ID
        /*
        TODO: should the Blobs contain a file?
        think about the commit's stuff
         */
        String fileName;
        String sha1ID;

        File file;

        public Blobs(String name, File file) {
            this.fileName = name;
            this.file = file;
            this.sha1ID = Utils.sha1(this.file);
        }

        public File getFile() {
            return file;
        }

        public String getFileName() {
            return fileName;
        }

        public String getSha1ID() {
            return sha1ID;
        }
    }
}


