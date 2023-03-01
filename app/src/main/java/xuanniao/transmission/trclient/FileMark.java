package xuanniao.transmission.trclient;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileMark {
    static String mark1 = "@FileMark@";
    static String mark2 = "@FileMarkEnd@";
    static int check(String send_text) {
        int mark;
        String regex = mark1+".*"+mark2;
        if (Pattern.matches(regex, send_text)){
            Log.i("判定标记","1");
            mark = 1;
        } else {
            Log.i("判定标记","3");
            mark = 0;
        }
        return mark;
    }

    static String fpath(String path) {
        String fpath = path.replaceAll(mark1, "");
        return fpath.replaceAll(mark2, "");
    }

    static String name(String path) {
        // 把规则编译成模式对象
        Pattern regex3 = Pattern.compile("[^/]+(?!.*/)");
        // 找出所有符合规则的字符串
        Matcher name_mat = regex3.matcher(path);
        if(name_mat.find()){
            for(int i=0; i<=name_mat.groupCount(); i++){
                Log.i("name文件名",i+":"+name_mat.group(i));
            }
        }
        String name = name_mat.group(0);
        return name;
    }
}
