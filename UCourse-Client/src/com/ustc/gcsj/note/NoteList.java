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
import android.widget.TextView;

import com.ustc.gcsj.ucoursenew.R;

public class NoteList extends Fragment {
    private static Bundle bundle;
    private NoteDB mNoteDB;
    private Cursor mCursor;
    private ListView noteslist;
    private ImageView addNote;
    private TextView note_classname; // 某门课程对应的所有笔记
    private String nclass;
    private List<HashMap<String, Object>> listData;

    static NoteList newInstance(Bundle d) { // 两个fragment之间传值
        NoteList nl = new NoteList();
        bundle = d;
        return nl;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.note_list, container, false);
        noteslist = (ListView) view.findViewById(R.id.notelist);
        addNote = (ImageView) view.findViewById(R.id.addnote1);
        note_classname = (TextView) view.findViewById(R.id.tvnote);
        // System.out.println("gggggggggg"+bundle.getString("viewname2"));
        nclass = bundle.getString("viewname2");
        note_classname.setText(nclass); // 显示某门课程对应的笔记列表，按时间顺序排列
        listData = new ArrayList<HashMap<String, Object>>();
        // this.initItems(items);
        setViewByclassName(nclass);
        String[] from = {"note_id", "note_notedetail", "note_date"};
        int[] to = {R.id.cnoteNo, R.id.cnoteDetail, R.id.cnoteTime};
        SimpleAdapter sa = new SimpleAdapter(view.getContext(), listData,
                R.layout.note_listitem, from, to);
        noteslist.setAdapter(sa);
        addNote();
        showNote();
        return view;
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
                intent.putExtra("classNote", nclass); // 将当前页面的内容传到查看笔记页面中
                startActivity(intent);
            }
        });

    }

    public void addNote() {
        addNote.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), AddNote.class);
                startActivity(intent);
                getActivity().finish();
            }

        });
    }

    public void setViewByclassName(String nclass) {
        mNoteDB = new NoteDB(getActivity());
        mCursor = mNoteDB.selectbyClassName(nclass);
        int i = 1;
        while (mCursor.moveToNext()) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("note_id", Integer.toString(i++));
            // map.put("note_classname", mCursor.getString(1));
            map.put("note_notedetail", mCursor.getString(2));
            map.put("note_date", mCursor.getString(3));
            listData.add(map);
        }
        mCursor.close();
        mNoteDB.close();
    }

}
