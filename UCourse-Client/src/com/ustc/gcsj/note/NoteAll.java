package com.ustc.gcsj.note;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ustc.gcsj.ucoursenew.R;

public class NoteAll extends Fragment {
	private NoteDB mNoteDB;
	private Cursor mCursor;
	private ListView noteslist;
	private ImageView addNote;
	private List<HashMap<String, Object>> listData;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.note_all, container, false);
		noteslist = (ListView) view.findViewById(R.id.allnote_list);
		addNote = (ImageView) view.findViewById(R.id.addNote);
		addNote(); // 添加笔记按钮单击事件
		// 所有的列表项
		listData = new ArrayList<HashMap<String, Object>>();
		setUpViews();
		String[] from = { "note_xl", "note_classname", "note_notedetail",
				"note_date", "note_id" };
		int[] to = { R.id.noteNo, R.id.classNote, R.id.noteDetail,
				R.id.noteTime };
		SimpleAdapter sa = new SimpleAdapter(view.getContext(), listData,
				R.layout.note_item, from, to);
		noteslist.setAdapter(sa);
		showNote();
		return view;
	}

	public void addNote() {
		addNote.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), AddNote.class);
				startActivity(intent);

			}

		});
	}

	public void showNote() {
		noteslist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(), ShowNote.class);
				intent.putExtra("noteID",
						(String) listData.get(position).get("note_id"));
				intent.putExtra("noteDetail", (String) listData.get(position)
						.get("note_notedetail"));
				intent.putExtra("classNote", (String) listData.get(position)
						.get("note_classname"));
				startActivity(intent);
			}
		});

	}

	public void setUpViews() {
		mNoteDB = new NoteDB(getActivity());
		mCursor = mNoteDB.select();
		int i = 1;
		while (mCursor.moveToNext()) {
			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("note_xl", Integer.toString(i++));
			map.put("note_classname", mCursor.getString(1));
			map.put("note_notedetail", mCursor.getString(2));
			map.put("note_date", mCursor.getString(3));
			map.put("note_id", mCursor.getString(0));

			listData.add(map);
		}
		mCursor.close();
		mNoteDB.close();

	}
}
