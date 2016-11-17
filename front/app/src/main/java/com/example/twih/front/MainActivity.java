package com.example.twih.front;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listview1);
        ListViewAdapter adapter = new ListViewAdapter();

        listView.setAdapter(adapter);

        //아이템 추가부분 (나중에는 사용자 정보를 받아와서 추가한다)
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.dummy_person), "Dummy", "this is dummy person");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //get item
                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position);

                String nameStr = item.getNameStr();
                String descStr = item.getDescStr();
                Drawable iconDrawable = item.getIcon();

                // TODO : use item data.
            }
        });
    }
}
