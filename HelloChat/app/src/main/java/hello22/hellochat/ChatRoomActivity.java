package hello22.hellochat;

import android.content.Context;
import android.content.Intent;
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

        //Setting title of ChatRoom
        Intent intent = getIntent();
        TextView text = (TextView) findViewById(R.id.textView2);
        text.setText(intent.getStringExtra("name"));

        final CustomAdapter adapter = new CustomAdapter();
        ListView listview = (ListView) findViewById(R.id.chatwindow);
        listview.setAdapter(adapter);

        /*
            화면에 데이터 띄우는 코드, data는 받아와야함
            1-내가쓴글 : adpater.add(data,0);      //처리완료
            2-상대방이쓴글 : adapter.add(data,1);   //데이터를 받는 코드와 같이 써주면 됩니다
         */

        adapter.add("Hello",1);
        adapter.add("World",0);

        //Button Click Event
        Button inputButton = (Button) findViewById(R.id.inputmsgbutton);
        inputButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                EditText input = (EditText) findViewById(R.id.inputtext);
                String inputdata = input.getText().toString();

                if(inputdata.getBytes().length <= 0){
                    //Input Blank -> Do Nothing
                    //근데 스페이스바는 전송됨...
                }
                else {
                    input.setText(null);
                    adapter.add(inputdata, 0);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}