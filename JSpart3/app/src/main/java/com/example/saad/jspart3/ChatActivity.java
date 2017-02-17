package com.example.saad.jspart3;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    Firebase ref;
    Intent myintent;
    String sender_data;
    String key_data;
    String reciever_data;

    ListView MessageView;
    EditText chatappWriteMessage;
    Button chatappSendButton;

    ArrayList<MessageWord> list = new ArrayList<MessageWord>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Firebase.setAndroidContext(this);
        ref = new Firebase("https://js-part-3.firebaseio.com/SignUp_Database/");


        myintent = getIntent();
        sender_data = myintent.getStringExtra("Sender");
        key_data = myintent.getStringExtra("Key");
        reciever_data = myintent.getStringExtra("Reciever");
        Log.d("reciever-->>", " "+reciever_data);
        Log.d("sender-->>", " "+sender_data);

        MessageView = (ListView)findViewById(R.id.chatappMessagesView);
        chatappWriteMessage = (EditText)findViewById(R.id.chatappMessage);
        chatappSendButton = (Button)findViewById(R.id.chatappSend);
        final MessageAdaptor adaptor = new MessageAdaptor(this, list);
        try {
            Firebase userTo = ref.child(reciever_data);
            Firebase message = userTo.child("Messages");
            final Firebase key = message.child(key_data);
            key.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, String> map = dataSnapshot.getValue(Map.class);
                    final String Msg = map.get("Message");
                    String Destination = map.get("Destination");
                    String Source = map.get("Source");
                    Log.d("Source-->>", " " + Source);
                    Log.d("Destination-->>", " " + Destination);
                    Log.d("Message-->>", " " + Msg);
                    Firebase date = key.child("Posted_Date");
                    date.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Map<String, String> map = dataSnapshot.getValue(Map.class);
                            String date = String.valueOf(map.get("monthDay"));
                            String month = String.valueOf(map.get("month"));
                            String year = String.valueOf(map.get("year"));
                            Log.d("date-->>", " " + date);
                            Log.d("month-->>", " " + month);
                            Log.d("yeare-->>", " " + year);
                            list.add(new MessageWord(Msg, "" + date + "-" + month + "-" + year));
                            adaptor.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });


                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            key.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        final Firebase thread = key.child("Thread");
                        thread.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    final String clubkey = childSnapshot.getKey();
                                    Firebase messagekey = thread.child(clubkey);
                                    messagekey.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String value = dataSnapshot.getValue(String.class);
                                            list.add(new MessageWord(value, ""));
                                            adaptor.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(FirebaseError firebaseError) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });


                    } catch (Exception ex) {

                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            chatappSendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String Message = chatappWriteMessage.getText().toString();
                    if (Message != null) {
                        Firebase newMessage = key.child("Thread");
                        newMessage.push().setValue(Message);
                    }
                }
            });

            MessageView.setAdapter(adaptor);

        }
        catch (Exception ex){
            Log.d("Exception: ",""+ex.getMessage());
        }
    }
}
