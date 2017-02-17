package com.example.saad.jspart3;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Map;

public class employer_employee_relation extends AppCompatActivity {

    Intent myintent;
    Firebase ref;
    Firebase EmployeeId;

    String employeer_key_data;
    String employer_Jobkey;
    TextView eeEmployerName;
    ImageView eeEmployerDP;

    String employee_Email;
    TextView eeEmployeeName;
    ImageView eeEmployeeDP;
    TextView eeEmployeeAccount;
    TextView eeEmployeeEmail;
    TextView eeEmployeeDOB;
    TextView eeEmployeeGender;
    TextView eeEmployeeCity;
    TextView eeEmployeeCountry;

    Button eeEmployeeSkype;
    Button eeEmployeeMessage;
    Button eeEmployeeCV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_employer_employee_relation);
        eeEmployerName = (TextView)findViewById(R.id.eeEmployerName);
        eeEmployerDP = (ImageView)findViewById(R.id.eeEmployerDP);
        eeEmployeeName = (TextView)findViewById(R.id.eeEmployeeName);
        eeEmployeeDP = (ImageView)findViewById(R.id.eeEmployeeDP);
            eeEmployeeAccount = (TextView) findViewById(R.id.eeEmployeeAccount);
            eeEmployeeEmail = (TextView) findViewById(R.id.eeEmployeeEmail);
            eeEmployeeDOB = (TextView) findViewById(R.id.eeEmployeeDOB);
            eeEmployeeGender = (TextView) findViewById(R.id.eeEmployeeGender);
            eeEmployeeCity = (TextView) findViewById(R.id.eeEmployeeCity);
            eeEmployeeCountry = (TextView) findViewById(R.id.eeEmployeeCountry);
            eeEmployeeSkype = (Button)findViewById(R.id.eeEmployeeSkype);
            eeEmployeeMessage = (Button)findViewById(R.id.eeEmployeeMessage);
            eeEmployeeCV = (Button)findViewById(R.id.eeEmployeeCV);

        Firebase.setAndroidContext(this);
        ref = new Firebase("https://js-part-3.firebaseio.com/SignUp_Database");
        myintent = getIntent();

        String employees_list_data = myintent.getStringExtra("data");
        employeer_key_data = myintent.getStringExtra("employer_key");
        employer_Jobkey    = myintent.getStringExtra("jobkey");

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
            EmployeeId = ref.child(employees_list_data.replace("()","/"));
            EmployeeId.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String,String> map = dataSnapshot.getValue(Map.class);
                    eeEmployeeName.setText(map.get("First_Name")+" "+map.get("Last_Name"));
                    Picasso.with(employer_employee_relation.this).load(map.get("Image_URL")).into(eeEmployeeDP);
                    eeEmployeeDP.setScaleType(ImageView.ScaleType.FIT_XY);
                    if(map.get("Is_Employee").equals("true")) {
                        eeEmployeeAccount.setText("Employee");
                    }
                    else {
                        eeEmployeeAccount.setText("Employer");
                    }
                    employee_Email = map.get("Email_Address");
                    eeEmployeeEmail.setText(employee_Email);
                    eeEmployeeDOB.setText(map.get("Date_of_Birth"));
                    if(map.get("Is_Male").equals("true")) {
                        eeEmployeeGender.setText("Male");
                    }
                    else{
                        eeEmployeeGender.setText("Female");
                    }
                    eeEmployeeCity.setText(map.get("City"));
                    eeEmployeeCountry.setText(map.get("Country"));
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        }
        catch (Exception ex){
            Log.d("Exception: "," "+ex.getMessage());
        }

        eeEmployeeSkype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    initiateSkypeUri(employer_employee_relation.this,"assaykhan");
            }
        });
        eeEmployeeMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    //Firebase jobkey = ref.child(employeer_key_data+"/"+employer_Jobkey+"/");

                   /* Firebase messageKey = EmployeeId.child("Messages/"+employer_Jobkey);
                    String generatedKey = messageKey.push().getKey();
                    Firebase employerKey = messageKey.child(employeer_key_data);
                    Firebase employeeMessagekey = employerKey.child(generatedKey);
                    Firebase source = employeeMessagekey.child("Source");
                    source.setValue(employeer_key_data);
                    Firebase destination = employeeMessagekey.child("Destination");
                    destination.setValue(employee_Email);
                    Firebase message = employeeMessagekey.child("Message");
                    message.setValue("The message");
                    Time now = new Time();
                    now.setToNow();
                    Firebase date = employeeMessagekey.child("Posted_Date");
                    date.setValue(now);*/
                Firebase messageKey = EmployeeId.child("Messages");
                String generatedKey = messageKey.push().getKey();
                Firebase RandomKey = messageKey.child(generatedKey);
                Firebase source = RandomKey.child("Source");
                source.setValue(employeer_key_data);
                Firebase destination = RandomKey.child("Destination");
                destination.setValue(employee_Email);
                Firebase message = RandomKey.child("Message");
                message.setValue("The message");
                Time now = new Time();
                now.setToNow();
                Firebase date = RandomKey.child("Posted_Date");
                date.setValue(now);



            }
        });
        eeEmployeeCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri webpage = Uri.parse("google.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

    }
    /**
     * Initiate the actions encoded in the specified URI.
     */
    public void initiateSkypeUri(Context myContext, String mySkypeUri) {

        // Make sure the Skype for Android client is installed.
        if (!isSkypeClientInstalled(myContext)) {
            goToMarket(myContext);
            return;
        }

        // Create the Intent from our Skype URI.
        Uri skypeUri = Uri.parse(mySkypeUri);
        Intent myIntent = new Intent(Intent.ACTION_VIEW, skypeUri);

        // Restrict the Intent to being handled by the Skype for Android client only.
        myIntent.setComponent(new ComponentName("com.skype.raider", "com.skype.raider.Main"));
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Initiate the Intent. It should never fail because you've already established the
        // presence of its handler (although there is an extremely minute window where that
        // handler can go away).
        myContext.startActivity(myIntent);

        return;
    }

    public boolean isSkypeClientInstalled(Context myContext) {
        PackageManager myPackageMgr = myContext.getPackageManager();
        try {
            myPackageMgr.getPackageInfo("com.skype.raider", PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            return (false);
        }
        return (true);
    }
    public void goToMarket(Context myContext) {
        Uri marketUri = Uri.parse("market://details?id=com.skype.raider");
        Intent myIntent = new Intent(Intent.ACTION_VIEW, marketUri);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myContext.startActivity(myIntent);

        return;
    }
 }
