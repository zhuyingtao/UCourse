package com.ustc.gcsj.doc.network;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ustc.gcsj.ucoursenew.R;

public class ListFromServer extends Activity {

	private ListView lv;
	private ArrayList<Map<String, Object>> list;
	private ArrayList<Map<String, Object>> dirs;
	private ArrayList<Map<String, Object>> files;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doc_from_server);
		Intent intent = getIntent();
		list = ((ArrayList<Map<String, Object>>) intent.getExtras().get(
				"fileList"));
		lv = (ListView) findViewById(R.id.doc_list_server_listview);
		this.showList();
		this.setListItem();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.file_list_from_server, menu);
		return true;
	}

	public void showList() {
		modifyList();
		dirs = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			System.out.println("-----" + list.get(i).get("name"));
			if ((Boolean) list.get(i).get("isDir")) {
				dirs.add(list.get(i));
			}
		}

		String[] from = { "icon", "name", "time", "size" };
		int[] to = { R.id.docImage, R.id.docName, R.id.docTime, R.id.docSize };
		SimpleAdapter sa = new SimpleAdapter(ListFromServer.this, dirs,
				R.layout.doc_item, from, to);
		lv.setAdapter(sa);
	}

	public void modifyList() {
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			if ((Boolean) map.get("isDir")) {
				map.put("icon", R.drawable.folder);
			} else {
				int imageId = (Integer) map.get("icon");
				if (imageId == 1)
					map.put("icon", R.drawable.doc);
				else if (imageId == 2)
					map.put("icon", R.drawable.txt);
				else if (imageId == 3)
					map.put("icon", R.drawable.ppt);
				else if (imageId == 4)
					map.put("icon", R.drawable.xls);
				else if (imageId == 5)
					map.put("icon", R.drawable.pdf);
				else
					map.put("icon", R.drawable.doc);
			}
		}
	}

	public void setListItem() {
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Map<String, Object> map = dirs.get(position);
				String name = (String) map.get("name");
				System.out.println("ListFromServer -->name:" + name);

				files = new ArrayList<Map<String, Object>>();
				for (int i = 0; i < list.size(); i++) {
					String docDir = (String) list.get(i).get("docDir");
					// System.out.println("ListFromServer -->docDir:"+docDir);
					if (docDir == null)
						continue;
					if (docDir.equals(name))
						files.add(list.get(i));
				}
				System.out.println("ListFromServer -->size:" + files.size());
				Intent intent = new Intent(ListFromServer.this,
						FilesDownload.class);
				intent.putExtra("files", files);
				intent.putExtra("dirName", name);
				startActivity(intent);
			}
		});
	}
}
