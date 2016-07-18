package com.mansilla.admin.cursoedx;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by admin on 16/07/2016.
 */
public class Chat_Room extends AppCompatActivity {
    private Button btn_sende_smg;
    private EditText input_msg;
    private TextView chat_conversation;

    private DatabaseReference root;

    private String usern_name, room_name;
    String temp_key;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setContentView(R.layout.chat_room);

        btn_sende_smg = (Button) findViewById(R.id.btn_send);
        input_msg = (EditText)findViewById(R.id.input_msg);
        chat_conversation = (TextView) findViewById(R.id.chat);

        usern_name = getIntent().getExtras().get("user_name").toString();
        room_name = getIntent().getExtras().get("room_name").toString();

        setTitle("Pregunta: " + room_name);

        root = FirebaseDatabase.getInstance().getReference().child(room_name);

        btn_sende_smg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<String, Object>();
                temp_key = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference messege_root = root.child(temp_key);
                Map<String, Object> map2 = new HashMap<String, Object>();
                map2.put("name", usern_name);
                map2.put("msg", input_msg.getText().toString());

                messege_root.updateChildren(map2);
                input_msg.setText("");
            }
        });

            root.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    append_chat_conversation(dataSnapshot);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    append_chat_conversation(dataSnapshot);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

    }

    private String chatsmsg, chatusername;

    private void append_chat_conversation(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();

            while(i.hasNext()){
                chatsmsg = (String)((DataSnapshot)i.next()).getValue();
                chatusername = (String)((DataSnapshot)i.next()).getValue();

                chat_conversation.append(chatusername +": " + chatsmsg + "\n" + "\n");
        }

    }
}
