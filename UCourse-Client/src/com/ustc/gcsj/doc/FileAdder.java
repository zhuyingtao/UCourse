package com.ustc.gcsj.doc;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ustc.gcsj.doc.network.ClientSocket;
import com.ustc.gcsj.doc.network.ListFromServer;
import com.ustc.gcsj.ucoursenew.R;

public class FileAdder extends Activity {

    private String docDir = Environment.getExternalStorageDirectory().getPath()
            + "/ucourse";
    private String filepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_adder);
        this.setButtonFile();
        this.setButtonOK();
        this.setButtonCancel();
        this.setButtonUpload();
        this.setButtonDownload();
        this.setSpinner();
    }

    public void setButtonFile() {
        Button button = (Button) findViewById(R.id.doc_adder_choose);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(FileAdder.this, FormFiles.class);
                startActivityForResult(intent, 2);
            }
        });
    }

    public void setButtonOK() {
        Button button = (Button) findViewById(R.id.doc_adder_ok);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (filepath == null || filepath.length() == 0) {
                    Toast.makeText(getBaseContext(), "请选择课件!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                DBHelper db = new DBHelper();
                String docName = filepath.substring(filepath.lastIndexOf("/") + 1);
                String docCourse = ((Spinner) findViewById(R.id.doc_adder_course))
                        .getSelectedItem().toString();
                String docURL = filepath;
                System.out.println("FileAdder --> " + docName + "--"
                        + docCourse + "--" + docURL);
                db.insertData(docName, docCourse, docURL);

                setResult(RESULT_OK);
                finish();
            }
        });
    }

    public void setButtonCancel() {
        Button button = (Button) findViewById(R.id.doc_adder_cancel);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    public void setButtonUpload() {
        final Button button = (Button) findViewById(R.id.doc_adder_upload);

        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (filepath == null || filepath.length() == 0) {
                    Toast.makeText(getBaseContext(), "请选择课件!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                final Handler handler = new Handler() {
                    public void handleMessage(Message msg) {
                        // TODO Auto-generated method stub
                        if (msg.what == 10) {
                            Toast.makeText(FileAdder.this, "正在上传文件中......",
                                    Toast.LENGTH_LONG).show();
                        }
                        if (msg.what == 111) {
                            Utility.sleep(3000);
                            Toast.makeText(FileAdder.this, "连接服务器失败，请稍后重试！",
                                    Toast.LENGTH_LONG).show();
                        }
                        if (msg.what == 222) {
                            Utility.sleep(3000);
                            Toast.makeText(FileAdder.this, "上传课件成功！",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                };

                Runnable run = new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        ClientSocket cs = new ClientSocket();
                        handler.sendEmptyMessage(10);
                        String docCourse = ((Spinner) findViewById(R.id.doc_adder_course))
                                .getSelectedItem().toString();
                        boolean isSend = cs.sendFile(filepath, docCourse);
                        if (isSend)
                            handler.sendEmptyMessage(222);
                        else
                            handler.sendEmptyMessage(111);
                    }
                };
                new Thread(run).start();
            }
        });

    }

    public void setButtonDownload() {
        Button button = (Button) findViewById(R.id.doc_adder_download);
        button.setOnClickListener(new OnClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                System.out.println("FileAdder --> Click download");
                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // TODO Auto-generated method stub
                        if (msg.what == 111)
                            Toast.makeText(FileAdder.this, "连接服务器失败，请稍后重试！",
                                    Toast.LENGTH_LONG).show();
                    }
                };
                Runnable run = new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        ClientSocket cs = new ClientSocket();
                        if (cs.isConnected) {
                            ArrayList<Map<String, Object>> list = ((ArrayList<Map<String, Object>>) cs
                                    .getList());
                            Intent intent = new Intent(FileAdder.this,
                                    ListFromServer.class);
                            intent.putExtra("fileList", list);
                            startActivity(intent);
                        } else {
                            handler.sendEmptyMessage(111);
                        }
                    }
                };
                new Thread(run).start();
            }
        });
    }

    public void setSpinner() {
        Spinner spin = (Spinner) findViewById(R.id.doc_adder_course);
        spin.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                TextView tv = (TextView) view;
                tv.setTextColor(0xff000000);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == 2 && data != null) {
            filepath = data.getStringExtra("filepath");
            if (filepath.length() > 0) {
                EditText et = (EditText) findViewById(R.id.doc_adder_file);
                et.setText(filepath.substring(filepath.lastIndexOf("/") + 1));
                System.out.println("FileAdder --> filePath :" + filepath);
            }
        }
    }

    // this method is not used.
    public void addFile() {

        DBHelper db = new DBHelper();

        String sdState = Environment.getExternalStorageState();
        if (sdState.equals(Environment.MEDIA_MOUNTED)) {
            File docs = new File(docDir);
            System.out.println(docDir);

            if (docs.listFiles().length > 0) {
                File[] files = docs.listFiles();
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    String docName = file.getName();
                    if (i == 0 | i == 1)
                        db.insertData(docName, "离散数学", docDir + "/" + docName);
                    if (i == 2 | i == 3)
                        db.insertData(docName, "算法导论", docDir + "/" + docName);
                    if (i == 4 | i == 5)
                        db.insertData(docName, "软件工程", docDir + "/" + docName);
                    if (i > 5)
                        db.insertData(docName, "手机应用", docDir + "/" + docName);
                }
            }
        }
    }
}
