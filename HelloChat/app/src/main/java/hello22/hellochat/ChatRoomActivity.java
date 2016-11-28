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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {

    String fromserverdata = null;
    String inputdata = null;
    Socket socket;

    private String ServerIP = "127.0.0.1";
    private int ServerPort = 10001;

    @Override
    protected void onStop(){
        super.onStop();
        try{
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

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

        try{
            socket = new Socket(ServerIP,ServerPort);
            adapter.add("success",1);

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(out));

            //????
            while(inputdata != null)
            {
                writer.println(inputdata);
                writer.flush();
                fromserverdata = reader.readLine();
                adapter.add(fromserverdata, 1);
                adapter.notifyDataSetChanged();
                inputdata = null;
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        //Button Click Event
        Button inputButton = (Button) findViewById(R.id.inputmsgbutton);
        inputButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                EditText input = (EditText) findViewById(R.id.inputtext);
                String inputdata = input.getText().toString();

                if(inputdata.getBytes().length <= 0){
                    //Input Blank -> Do Nothing
                }
                else {
                    input.setText(null);
                    adapter.add(inputdata, 0);
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