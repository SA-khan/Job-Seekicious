package com.example.saad.jspart3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class employer_employee_relation extends AppCompatActivity {

    Intent myintent;
    Firebase ref;

    TextView eeEmployerName;
    ImageView eeEmployerDP;

    TextView eeEmployeeName;
    ImageView eeEmployeeDP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_employer_employee_relation);
        eeEmployerName = (TextView)findViewById(R.id.eeEmployerName);
        eeEmployerDP = (ImageView)findViewById(R.id.eeEmployerDP);
        eeEmployeeName = (TextView)findViewById(R.id.eeEmployeeName);
        eeEmployeeDP = (ImageView)findViewById(R.id.eeEmployeeDP);

        Firebase.setAndroidContext(this);
        ref = new Firebase("https://js-part-3.firebaseio.com/SignUp_Database");
        myintent = getIntent();

        String employees_list_data = myintent.getStringExtra("data");
        String employeer_key_data = myintent.getStringExtra("employer_key");
        Firebase employerId = ref.child(employeer_key_data);
        employerId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,String> map = dataSnapshot.getValue(Map.class);
                eeEmployerDP.bringToFront();
                Picasso.with(employer_employee_relation.this).load(map.get("Image_URL")).into(eeEmployerDP);
                eeEmployerName.setText(map.get("First_Name"));

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
            Log.d("employee-->>"," "+employees_list_data);
            Firebase EmployeeId = ref.child(employees_list_data.replace("()","/"));
            EmployeeId.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String,String> map = dataSnapshot.getValue(Map.class);
                    eeEmployeeName.setText(map.get("First_Name")+" "+map.get("Last_Name"));
                    Picasso.with(employer_employee_relation.this).load(map.get("Image_URL")).into(eeEmployeeDP);
                    eeEmployeeDP.setScaleType(ImageView.ScaleType.FIT_XY);

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        }
        catch (Exception ex){
            Log.d("Exception: "," "+ex.getMessage());
        }

    }
}
