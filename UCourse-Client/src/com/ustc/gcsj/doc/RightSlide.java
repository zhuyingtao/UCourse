package com.ustc.gcsj.doc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ustc.gcsj.main.MainFrag;
import com.ustc.gcsj.ucoursenew.R;

public class RightSlide extends Fragment {
    private ListView lv;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slide_right, container, false);
        lv = (ListView) view.findViewById(R.id.slide_right_list);
        // 所有的列表项
        final List<Map<String, String>> items = new ArrayList<Map<String, String>>();
        this.initItems(items);

        String[] from = {"course_name"};
        int[] to = {R.id.slide_course_name};
        SimpleAdapter sa = new SimpleAdapter(view.getContext(), items,
                R.layout.slide_item, from, to);
        lv.setAdapter(sa);

        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // switch (position) {
                // case 0:
                // ((MainActivity) getActivity()).getSlidingMenu().toggle();
                // Intent intent = new Intent(getActivity(),
                // MainActivity.class);
                // intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                // view.getBackground();
                // startActivity(intent);
                // getActivity().overridePendingTransition(0, 0);
                // getActivity().finish();
                //
                // // ((MainActivity)getActivity()).init();
                // break;
                // case 1:
                /*
                 * ((MainActivity)getActivity()).getSlidingMenu().toggle();
				 * intent = new Intent(getActivity(),SetView.class);
				 * startActivity(intent);
				 */
                FileList fl = new FileList();
                Bundle b = new Bundle();
                b.putString("docCourse", items.get(position).get("course_name"));
                fl.setArguments(b);
                ((MainFrag) getActivity()).getSupportFragmentManager()
                        .beginTransaction().replace(R.id.main_frag, fl)
                        .commit();
                ((MainFrag) getActivity()).getSlidingMenu().showContent();
            }

        });

        return view;
    }

    public void initItems(List<Map<String, String>> items) {
        Map<String, String> item = new HashMap<String, String>();// 单个列表项组件
        item.put("course_name", "软件工程");
        items.add(item);
        item = new HashMap<String, String>();
        item.put("course_name", "手机应用");
        items.add(item);

        item = new HashMap<String, String>();
        item.put("course_name", "软件测试");
        items.add(item);

        item = new HashMap<String, String>();
        item.put("course_name", "离散数学");
        items.add(item);

        item = new HashMap<String, String>();
        item.put("course_name", "算法导论");
        items.add(item);
    }
}
