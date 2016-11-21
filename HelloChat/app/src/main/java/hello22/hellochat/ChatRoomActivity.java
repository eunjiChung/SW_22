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
                    //서버에 데이터를 전송하는 코드를 넣는다
                    adapter.notifyDataSetChanged();
                }
            }
        });

        /*
        서버에서 데이터가 들어오면
        adapter.add(data,1)
         */

    }
}