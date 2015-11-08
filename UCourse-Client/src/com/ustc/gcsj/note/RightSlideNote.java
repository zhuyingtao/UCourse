package com.ustc.gcsj.note;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ustc.gcsj.main.MainFrag;
import com.ustc.gcsj.ucoursenew.R;

public class RightSlideNote extends Fragment {
    private ListView lv;
    private Button btnNoteAll;
    private NoteDB mNoteDB;
    private Cursor mCursor;
    private MainFrag myactivity = MainFrag.activity;
    private List<HashMap<String, Object>> items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_slide2, container, false);
        lv = (ListView) view.findViewById(R.id.slidelistView2);
        btnNoteAll = (Button) view.findViewById(R.id.noteallbtn);
        noteAll();
        // 所有的列表项
        items = new ArrayList<HashMap<String, Object>>();
        // this.initItems(items);
        setViewList();
        String[] from = {"note_classname"};
        int[] to = {R.id.viewname2};
        SimpleAdapter sa = new SimpleAdapter(view.getContext(), items,
                R.layout.main_slideitem2, from, to);
        lv.setAdapter(sa);
        showNote();
        return view;
    }

    public void showNote() {
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                @SuppressWarnings("unchecked")
                HashMap<String, Object> view1 = (HashMap<String, Object>) parent
                        .getItemAtPosition(position);
                String viewname2 = view1.get("note_classname").toString();
                Bundle b = new Bundle();
                b.putString("viewname2", viewname2);
                NoteList nl = NoteList.newInstance(b);
                // System.out.println("课程名称："+viewname2);
                myactivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frag, nl).commit();
                myactivity.getSlidingMenu().showContent();
            }
        });

    }

    public void setViewList() {

        mNoteDB = new NoteDB(getActivity());
        mCursor = mNoteDB.selectclasslist();
        int columnsSize = mCursor.getColumnCount();
        while (mCursor.moveToNext()) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            for (int i = 0; i < columnsSize; i++) {
                // map.put("note_id", mCursor.getString(0));
                map.put("note_classname", mCursor.getString(mCursor
                        .getColumnIndex("note_classname")));
            }
            items.add(map);
        }
        mCursor.close();
        mNoteDB.close();
    }

    public void noteAll() { // 点击右滑出现的所有课程跳转到另一个fragment
        btnNoteAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                NoteAll al = new NoteAll();
                myactivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frag, al).commit();
                myactivity.getSlidingMenu().showContent();

            }

        });
    }
}
