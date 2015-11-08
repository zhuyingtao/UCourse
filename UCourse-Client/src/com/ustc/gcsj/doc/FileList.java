package com.ustc.gcsj.doc;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ustc.gcsj.doc.network.ClientSocket;
import com.ustc.gcsj.ucoursenew.R;

public class FileList extends Fragment {

    private ListView lv;
    private DBHelper db;
    private String docURL = null;
    private String docCourse = null;
    private List<Map<String, Object>> list;
    private View view;

    // show the file list.
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.doc_list, container, false);
        lv = (ListView) view.findViewById(R.id.doc_list_listview);
        System.out.println("FileList --> ==== Doc List ====");

        db = new DBHelper();
        this.showList();
        this.setAdder();
        this.setListItem();

        return view;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        this.showList();
    }

    public void showList() {
        list = this.getList();
        String[] from = {"docIcon", "docName", "docTime", "docSize"};
        int[] to = {R.id.docImage, R.id.docName, R.id.docTime, R.id.docSize};
        SimpleAdapter sa = new SimpleAdapter(view.getContext(), list,
                R.layout.doc_item, from, to);
        lv.setAdapter(sa);
    }

    public void setAdder() {
        ImageButton ib = (ImageButton) view.findViewById(R.id.doc_list_adder);
        ib.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub\
                Intent intent = new Intent(getActivity(), FileAdder.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    public void setListItem() {

        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String docName = (String) list.get(position).get("docName");
                System.out.println("FileList --> " + docName);

                docURL = (String) list.get(position).get("docURL");
                FileReader fr = new FileReader();
                System.out.println("FileList --> docURL: " + docURL);
                Intent intent = fr.createIntent(new File(docURL));
                startActivity(intent);
            }
        });

        lv.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                // TODO Auto-generated method stub
                docURL = (String) list.get(position).get("docURL");
                docCourse = (String) list.get(position).get("docCourse");
                return false;
            }
        });
        registerForContextMenu(lv);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("请选择要执行的操作：");
        // add context menu item
        menu.add(0, 1, Menu.NONE, "查看");
        menu.add(0, 2, Menu.NONE, "删除");
        menu.add(0, 3, Menu.NONE, "编辑");
        menu.add(0, 4, Menu.NONE, "发送");
        menu.add(0, 5, Menu.NONE, "上传");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case 1:
                FileReader fr = new FileReader();
                System.out.println("docURL" + docURL);
                Intent intent = fr.createIntent(new File(docURL));
                startActivity(intent);
                break;
            case 2:
                Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("删除提示框");
                alert.setMessage("确认删除该课件？");
                alert.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                db.deleteData(docURL);
                                Toast.makeText(view.getContext(), "删除课件成功 ！",
                                        Toast.LENGTH_SHORT).show();
                                showList();
                            }
                        });
                alert.setNegativeButton("取消", null);
                alert.show();
                break;
            case 3:
                FileReader fr1 = new FileReader();
                Intent intent1 = fr1.createIntent(new File(docURL));
                startActivity(intent1);
                break;
            case 4:
                Builder alert1 = new AlertDialog.Builder(getActivity());
                alert1.setTitle("提示框");
                alert1.setMessage("通过蓝牙发送此课件？");
                alert1.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                // Intent intent = new Intent(Intent.ACTION_SEND);
                                // startActivity(intent);
                            }
                        });
                alert1.setNegativeButton("取消", null);
                alert1.show();
                break;
            case 5:
                Builder alert2 = new AlertDialog.Builder(getActivity());
                alert2.setTitle("提示框");
                alert2.setMessage("上传此课件到服务器？");
                alert2.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {

                            Handler handler = new Handler() {
                                public void handleMessage(Message msg) {
                                    // TODO Auto-generated method stub
                                    if (msg.what == 10) {
                                        Toast.makeText(getActivity(),
                                                "正在上传文件中......", Toast.LENGTH_LONG)
                                                .show();
                                    }
                                    if (msg.what == 111) {
                                        Utility.sleep(3000);
                                        Toast.makeText(getActivity(),
                                                "连接服务器失败，请稍后重试！", Toast.LENGTH_LONG)
                                                .show();
                                    }
                                    if (msg.what == 222) {
                                        Utility.sleep(3000);
                                        Toast.makeText(getActivity(), "上传课件成功！",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            };

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                Runnable run = new Runnable() {
                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        ClientSocket cs = new ClientSocket();
                                        handler.sendEmptyMessage(10);
                                        boolean isSend = cs.sendFile(docURL,
                                                docCourse);
                                        if (isSend)
                                            handler.sendEmptyMessage(222);
                                        else
                                            handler.sendEmptyMessage(111);
                                    }
                                };
                                new Thread(run).start();
                            }
                        });
                alert2.setNegativeButton("取消", null);
                alert2.show();
                break;
            default:
                break;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("FileList --> " + "result:" + resultCode
                + "  request:" + requestCode);
        if (resultCode == Activity.RESULT_OK) {
            Toast.makeText(getActivity(), "添加课件成功 ！", Toast.LENGTH_SHORT)
                    .show();
            this.showList();
        }
    }

    // get the file list from directory.
    @SuppressLint("SimpleDateFormat")
    public List<Map<String, Object>> getList() {
        Bundle bundle = this.getArguments();
        String docCourse = null;
        if (bundle != null)
            docCourse = bundle.getString("docCourse");

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Cursor cursor = db.getData(docCourse);
        if (cursor == null)
            return null;
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            docURL = cursor.getString(0);
            docCourse = cursor.getString(1);
            File file = new File(docURL);

            Map<String, Object> map = new HashMap<String, Object>();
            String docName = file.getName();
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(new Date(file.lastModified()));
            String size = Utility.formatFileSize(file.length());
            String[] docNameSplit = docName.split("\\.");
            String type = docNameSplit[docNameSplit.length - 1];
            int imageId = 0;
            if (type.equals("doc") || type.equals("docx")) {
                imageId = R.drawable.doc;
            } else if (type.equals("txt")) {
                imageId = R.drawable.txt;
            } else if (type.equals("ppt")) {
                imageId = R.drawable.ppt;
            } else if (type.equals("xls")) {
                imageId = R.drawable.xls;
            } else if (type.equals("pdf")) {
                imageId = R.drawable.pdf;
            } else {
                imageId = R.drawable.txt;
            }

            if (imageId == 0)
                continue;

            map.put("docName", docName);
            map.put("docIcon", imageId);
            map.put("docTime", time);
            map.put("docSize", size);
            map.put("docURL", docURL);
            map.put("docCourse", docCourse);
            list.add(map);
        }
        return list;
    }
}
