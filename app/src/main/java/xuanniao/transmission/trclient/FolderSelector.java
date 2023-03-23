package xuanniao.transmission.trclient;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FolderSelector extends AppCompatActivity implements
        OnItemClickListener {

    private TextView toolbar_title;
    private ListView mlistview;
    private TextView fullPath;
    private Button btnCancel, btnConfirm, btnReturn;
    //根节点路径
    private static final String rootDirectory = Environment
            .getExternalStorageDirectory().getAbsolutePath();
    List<FolderInfo> folderlist;
    //默认打开路径
    String path = Environment.getExternalStorageDirectory().getAbsolutePath();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.folder_selector);
        initView();
        refreshListItems(path);
    }

    private void initView() {
        toolbar_title = (TextView) findViewById(R.id.f_toolbar_title);
        mlistview = (ListView) findViewById(R.id.folder_list);
        fullPath = (TextView) findViewById(R.id.path_select);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnReturn = (Button) findViewById(R.id.f_toolbar_return);
        toolbar_title.setText("选择存储目录");
        btnCancel.setOnClickListener(cancelListener);
        btnConfirm.setOnClickListener(confirmListener);
        btnReturn.setOnClickListener(returnListener);
        // 获取Setting的Intent传递过来的路径
        path = getIntent().getStringExtra("base_path");
    }

    private OnClickListener confirmListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            String receive_path = (String) fullPath.getText();
            Log.i("receive_path",receive_path);
//            SharedPreferences SP = getSharedPreferences(
//                    "config", Context.MODE_PRIVATE);
//            SharedPreferences.Editor SPeditor = SP.edit();
//            SPeditor.putString("receive_path", receive_path);
//            SPeditor.apply();
            Intent intent = new Intent();
//            Uri uri = Uri.parse(receive_path);
            intent.putExtra("data", receive_path);
            setResult(RESULT_OK, intent);
            FolderSelector.this.finish();
        }
    };

    private OnClickListener cancelListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            FolderSelector.this.finish();
        }
    };

    private OnClickListener returnListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            FolderSelector.this.finish();
        }
    };

    // 根据path更新路径列表
    private void refreshListItems(String path) {
        fullPath.setText(path);
        folderlist = getFolderList(path);
        FolderAdapter folderAdapter = new FolderAdapter(folderlist,
                FolderSelector.this);
        mlistview.setAdapter(folderAdapter);
        mlistview.setOnItemClickListener(this);
        mlistview.setSelection(0);
    }

    // 根据路径生成一个包含路径的列表
    private List<FolderInfo> getFolderList(String path) {
        Log.i("path", path);
        Log.i("root",rootDirectory);
        File[] files = new File(path).listFiles();
        Log.i("files", String.valueOf(files.length));
        List<FolderInfo> list = new ArrayList<FolderInfo>();
        if (!rootDirectory.equals(path)) {
            FolderInfo fi = new FolderInfo();
            fi.setFolderIcon(R.drawable.folder);
            fi.setFolderName("...");
            fi.setFolderPath("");
            fi.setFolderToTal("父目录");
            list.add(fi);
        }
        Log.i("list", list.toString());
        List<File> fileList = Arrays.asList(files);
        //文件排序--按照名称排序
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isFile())
                    return -1;
                if (o1.isFile() && o2.isDirectory())
                    return 1;
                return o1.getName().compareTo(o2.getName());
            }

        });
        for (File file : files) {
            //判断文件是否是文件还是文件夹&&隐藏首字母名称为“.”
            if (file.isDirectory() && file.getName().indexOf(".") != 0) {
                FolderInfo fol = new FolderInfo();
                fol.setFolderIcon(R.drawable.folder);
                fol.setFolderName(file.getName());
                fol.setFolderPath(file.getPath());
                fol.setFolderTime(longDateToString(file.lastModified()));
                fol.setFolderToTal(getSubfolder(file.getPath()));
                list.add(fol);
//            } else {
//                //判断文件是否为图片
//                if (MediaFileUtil.isImageFileType(file.getPath())) {
//                    FolderInfo fol = new FolderInfo();
//                    fol.setFolderIcon(R.drawable.image_logo);
//                    fol.setFolderName(file.getName());
//                    fol.setFolderPath(file.getPath());
//                    fol.setFolderTime(longDateToString(file.lastModified()));
//                    fol.setFolderToTal("大小:"
//                            + FileSizeUtil.FormetFileSize(file.length()));
//                    list.add(fol);
//                }
//                //判断文件是否为视频文件
//                if (MediaFileUtil.isVideoFileType(file.getPath())) {
//                    FolderInfo fol = new FolderInfo();
//                    fol.setFolderIcon(R.drawable.video_logo);
//                    fol.setFolderName(file.getName());
//                    fol.setFolderPath(file.getPath());
//                    fol.setFolderTime(longDateToString(file.lastModified()));
//                    fol.setFolderToTal("大小:"
//                            + FileSizeUtil.FormetFileSize(file.length()));
//                    list.add(fol);
//                }
            }
        }
        return list;
    }

    /* 跳转到上一层 */
    private void goToParent() {
        if (!rootDirectory.equals(path)) {
            File file = new File(path);
            File str_pa = file.getParentFile();
            if (str_pa.equals(rootDirectory)) {
                Toast.makeText(FolderSelector.this, "已经是根目录",
                        Toast.LENGTH_SHORT).show();
                refreshListItems(path);
            } else {
                path = str_pa.getAbsolutePath();
                refreshListItems(path);
            }
        } else {
            Toast.makeText(FolderSelector.this, "已经是根目录",
                    Toast.LENGTH_SHORT).show();
            refreshListItems(path);
        }
    }

    private String getSubfolder(String path) {
        int i = 0;
        File[] files = new File(path).listFiles();
        for (File file : files) {
            if (file.isDirectory() && file.getName().indexOf(".") != 0) {
                i++;
//            } else {
//                if (MediaFileUtil.isImageFileType(file.getPath())
//                        || MediaFileUtil.isVideoFileType(file.getPath())) {
//                    i++;
//                }
            }
        }
        return "共" + i + "项";
    }

    private String longDateToString(long time) {
        try {
            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String dateTime = df.format(new Date(time));
            return dateTime;
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        if (folderlist.get(position).getFolderPath().isEmpty())
            goToParent();
        else {
            path = folderlist.get(position).getFolderPath();
            File file = new File(path);
            if (file.isDirectory())
                refreshListItems(path);
        }
    }
}