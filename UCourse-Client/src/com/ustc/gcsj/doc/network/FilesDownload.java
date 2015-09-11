package com.ustc.gcsj.doc.network;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ustc.gcsj.doc.CONSTANT;
import com.ustc.gcsj.doc.DBHelper;
import com.ustc.gcsj.doc.Utility;
import com.ustc.gcsj.ucoursenew.R;

public class FilesDownload extends Activity {

	private ArrayList<Map<String, Object>> list;
	private ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doc_files_download);
		this.setTitle();
		this.showList();
		this.setListItem();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.files_download, menu);
		return true;
	}

	public void setTitle() {
		Intent intent = getIntent();
		String dirName = intent.getExtras().getString("dirName");
		TextView tv = (TextView) findViewById(R.id.doc_files_dir);
		tv.setText(dirName);
	}

	@SuppressWarnings("unchecked")
	public void showList() {
		Intent intent = getIntent();
		list = (ArrayList<Map<String, Object>>) intent.getExtras().get("files");
		lv = (ListView) findViewById(R.id.doc_files_listview);
		String[] from = { "icon", "name", "time", "size" };
		int[] to = { R.id.docImage, R.id.docName, R.id.docTime, R.id.docSize };
		SimpleAdapter sa = new SimpleAdapter(FilesDownload.this, list,
				R.layout.doc_item, from, to);
		lv.setAdapter(sa);
	}

	public void setListItem() {
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				final int pos = position;
				final Handler handler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						if (msg.what == 10) {
							Toast.makeText(FilesDownload.this, "正在下载文件中......",
									Toast.LENGTH_LONG).show();
						}
						if (msg.what == 111) {
							Utility.sleep(4000);
							Toast.makeText(FilesDownload.this,
									"下载文件成功，已添加至课件列表！", Toast.LENGTH_LONG)
									.show();
						}
						if (msg.what == 100) {
							Utility.sleep(4000);
							Toast.makeText(FilesDownload.this, "下载文件失败，请稍后再试！",
									Toast.LENGTH_LONG).show();
						}
					}
				};

				new AlertDialog.Builder(FilesDownload.this)
						.setTitle("提示框")
						.setMessage("确定下载此课件？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Runnable run = new Runnable() {
											@Override
											public void run() {
												// TODO Auto-generated method
												// stub
												String docURL = (String) list
														.get(pos).get("docURL");
												System.out
														.println("FilesDownload --> "
																+ docURL);
												ClientSocket cs = new ClientSocket();
												handler.sendEmptyMessage(10);
												boolean hasDownload = cs
														.getFile(docURL);
												if (hasDownload)
													handler.sendEmptyMessage(111);
												else
													handler.sendEmptyMessage(100);

												addIntoDB(list.get(pos));
											}
										};
										new Thread(run).start();
									}
								}).setNegativeButton("取消", null).show();
			}

			public void addIntoDB(Map<String, Object> file) {
				DBHelper db = new DBHelper();
				String docName = (String) file.get("name");
				String docCourse = (String) file.get("docDir");
				String docURL = CONSTANT.SAVE_PATH + "/" + docName;
				System.out.println("FilesDownload --> " + docName + "--"
						+ docCourse + "--" + docURL);
				db.insertData(docName, docCourse, docURL);
			}
		});
	}
}
