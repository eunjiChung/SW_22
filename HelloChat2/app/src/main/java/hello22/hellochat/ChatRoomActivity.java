package hello22.hellochat;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        final ArrayList<String> items = new ArrayList<String>();

        int type=0;
        final CustomAdapter adpater = new CustomAdapter(this,0,items);

        ListView listview = (ListView) findViewById(R.id.chatwindow);
        listview.setAdapter(adpater);

        //입력버튼 클릭시 리스트뷰에 에디트텍스트의 데이터가 전송되고, 에디트텍스트 초기화
        //빈 메세지는 전송하지 않게 하는 것이 필요
        Button inputButton = (Button) findViewById(R.id.inputmsgbutton);
        inputButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                EditText input = (EditText) findViewById(R.id.inputtext);
                items.add(input.getText().toString());
                input.setText("");
                adpater.notifyDataSetChanged();
            }
        });
    }
}
