package xuanniao.transmission.trclient;

public class Msg {
    public final int TYPE_RECEIVED = 0;
    public final int TYPE_SENT = 1;
    private final String content;
    private final int RSType;
    private final String time;
    private final int fileType;
    private final int fileSize;

    public Msg(String content, int RSType, String time, int fileType, int fileSize) {
        this.content = content;
        this.RSType = RSType;
        this.time = time;
        this.fileType = fileType;
        this.fileSize = fileSize;
    }

    public String getContent() {
        return content;
    }

    public int getRSType() {
        return RSType;
    }

    public String getTime() {
        return time;
    }

    public int getFileType() {
        return fileType;
    }

    public int getFileSize() {
        return fileSize;
    }
}