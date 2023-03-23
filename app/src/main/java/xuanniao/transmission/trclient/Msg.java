package xuanniao.transmission.trclient;

import java.sql.Time;

public class Msg {
    public final int TYPE_RECEIVED = 0;
    public final int TYPE_SENT = 1;
    private final String content;
    private final int type;
    private final String time;

    public Msg(String content, int type, String time) {
        this.content = content;
        this.type = type;
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }

    public String getTime() {
        return time;
    }
}