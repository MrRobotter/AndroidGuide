package com.joinyon.androidguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.Toast;

import com.joinyon.androidguide.HRichEditorView.HRichEditorActivity;
import com.joinyon.androidguide.WordPressEditor.activity.MainExampleActivity;
import com.joinyon.androidguide.android.NotifyActivity;
import com.joinyon.androidguide.android.UI.MagicIndicatorActivity;
import com.joinyon.androidguide.android.UI.PhotoFlowActivity;
import com.joinyon.androidguide.android.WebActivity;
import com.joinyon.androidguide.net.VolleyActivity;
import com.joinyon.androidguide.notice.AlarmActivity;
import com.joinyon.androidguide.tt.BaiDuActivity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuideActivity extends AppCompatActivity implements GuideAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private String[] ITEMS = new String[]{
            "一个跑马灯广告的实现",//0
            "一款指示器",        //1
            "富文本编辑器",      //2
            "HRichEdit",       //3
            "流布局",           //4
            "Volley",           //5
            "百度语音",         //6
            "通知",            //7
            "广播",            //8
            "自定义圆环(已上传GitHub)"//9
    };
    private List<String> itemList = Arrays.asList(ITEMS);
    private GuideAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
    }

    private void initView() {
        Map<String, String> s = new HashMap<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GuideAdapter(this, itemList);
        recyclerView.setAdapter(adapter);
        adapter.setListener(this);
    }


    @Override
    public void onClick(int position) {
        Intent intent;
        switch (position) {
            case 0:
                Toast.makeText(this, "还未实现", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                intent = new Intent(GuideActivity.this, MagicIndicatorActivity.class);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(GuideActivity.this, MainExampleActivity.class);
                startActivity(intent);
                break;
            case 3:
                intent = new Intent(GuideActivity.this, HRichEditorActivity.class);
                startActivity(intent);
                break;
            case 4:
                intent = new Intent(GuideActivity.this, PhotoFlowActivity.class);
                startActivity(intent);
                break;
            case 5:
                intent = new Intent(GuideActivity.this, VolleyActivity.class);
                startActivity(intent);
                break;
            case 6:
                intent = new Intent(GuideActivity.this, BaiDuActivity.class);
                startActivity(intent);
                break;
            case 7:
                intent = new Intent(GuideActivity.this, NotifyActivity.class);
                startActivity(intent);
                break;
            case 8:
                intent = new Intent(GuideActivity.this, AlarmActivity.class);
                startActivity(intent);
                break;
            case 9:
                intent = new Intent(GuideActivity.this, WebActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
