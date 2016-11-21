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

        //버튼 클릭시 입력한 데이터를 전송
        //데이터를 동시에 서버에 전송함
        Button inputButton = (Button) findViewById(R.id.inputmsgbutton);
        inputButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                EditText input = (EditText) findViewById(R.id.inputtext);
                String inputdata = input.getText().toString();

                if(inputdata.getBytes().length <= 0){
                    //공백처리 : 아무것도 하지 않는다
                    //근데 스페이스바는 전송됨ㅋㅋ...
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
