package hello22.hellochat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ChatRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
    }


    //TEXT 부분에 상대방 이름이 떠야함

    /*
    edittext 에서 입력받은 내용을 서버로 보냄
    입력 후 엔터 된 메세지는 edittext 창에서 없어져야함
    수신크기제한필요함(무한엔터가능하므로)
    서버에서 들어오는 스트림(상대방의 메세지/입력받아 돌아오는 내 매세지)을 ListView에 출력
    나인패치이용
     */

    //SET Button : 기타기능부여(창 혹은 팝업창)

}
