package com.mansilla.admin.cursoedx;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class Home extends AppCompatActivity {
    String name;
    String user;
    String proveedor;
    String dominio;
    String fin;

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    private Button add_room;
    private EditText room_name;

    private ListView listView;
    private ArrayAdapter<String> arrayAdapterList;
    private ArrayList<String> list_of_rooms = new ArrayList<>();

    String possibleEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        add_room = (Button) findViewById(R.id.add_room);
        room_name = (EditText) findViewById(R.id.room_name);
        listView = (ListView) findViewById(R.id.listView);

        arrayAdapterList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_of_rooms);
        listView.setAdapter(arrayAdapterList);

        request_user_name();


        add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(room_name.getText().toString(), "");
                    root.updateChildren(map);
                    room_name.setText("");
            }
        });

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();
                while (i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());
                }
                list_of_rooms.clear();
                list_of_rooms.addAll(set);

                arrayAdapterList.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Chat_Room.class);
                intent.putExtra("room_name",((TextView)view).getText().toString());
                intent.putExtra("user_name", name);
                startActivity(intent);
            }
        });


    }

    private void request_user_name() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Usuario");

        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                possibleEmail = account.name;
                break;
            }
        }

        if(possibleEmail.length() > 1){
            StringTokenizer st = new StringTokenizer(possibleEmail, "@");
            user = st.nextToken();
            proveedor = st.nextToken();

            StringTokenizer jh = new StringTokenizer(proveedor, ".");
            dominio = jh.nextToken();
            fin = jh.nextToken();
        }
        

        LinearLayout X = new LinearLayout(this);

        final EditText input_user = new EditText(this);
        input_user.setText(user + "-" + proveedor.substring(0,1)+ fin.substring(0,1));
        input_user.setEnabled(false);
        X.addView(input_user);

        builder.setView(X);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name = input_user.getText().toString();
                if(name.length() < 1){
                    request_user_name();
                }
            }
        }).setCancelable(false);

        builder.show();
    }



}
