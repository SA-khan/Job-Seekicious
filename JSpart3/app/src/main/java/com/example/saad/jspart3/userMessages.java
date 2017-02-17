package com.example.saad.jspart3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class userMessages extends AppCompatActivity {

    Firebase ref;
    ArrayList<MessageWord> list = new ArrayList<MessageWord>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_messages);

        Intent myintent = getIntent();
        String email = myintent.getStringExtra("Email_Address");

        String remSpace = email.replace("%40","");
        String dataEmail = remSpace.replace(".","/");

        Firebase.setAndroidContext(this);
        ref = new Firebase("https://js-part-3.firebaseio.com/SignUp_Database/"+dataEmail+"/");
        Log.d("user", " "+ dataEmail);
        final Firebase message = ref.child("Messages");
        final MessageAdaptor adapter = new MessageAdaptor(this, list);
        message.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //String value = dataSnapshot.getValue(String.class);
                //Log.d("data---???>>>", " "+value);
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {

                    final String clubkey = childSnapshot.getKey();
                    Log.d("Message--->>>"," "+clubkey);
                    Firebase Message_Ref = message.child(clubkey);
                    Message_Ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Map<String, String> map = dataSnapshot.getValue(Map.class);
                            String sender = map.get("Source");
                            Firebase SenderRef = new Firebase("https://js-part-3.firebaseio.com/SignUp_Database/"+sender+"/");
                            SenderRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Map<String,String> map = dataSnapshot.getValue(Map.class);
                                    String fname = map.get("First_Name");
                                    String lname = map.get("Last_Name");
                                    list.add(new MessageWord(fname+" "+lname,""));

                                    adapter.notifyDataSetChanged();
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
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        ListView rootView = (ListView) findViewById(R.id.mymessagesall);

        rootView.setAdapter(adapter);

        rootView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}
