package com.example.user.trackgooglemaps;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {
    Location location;//create your own lcation class
    private FusedLocationProviderClient mFusedLocationClient;
    private DatabaseReference databaseReference;
    Button sendUpdate,updateButton;
    String busRoute;
    LocationObject locationObject;//this is my own object see now. you need to save this object and retrieve this ... dont save Locaation object, it do onot have default constructor.

   TextView textShow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        textShow = (TextView) findViewById(R.id.TextField);

        databaseReference = FirebaseDatabase.getInstance().getReference("Locations");
        //databaseReference.setValue("Hi,Hello");
        // setting the location
        sendUpdate = (Button)findViewById(R.id.update_button);
        sendUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // getBusRoute();
                UpdateLocation();
            }
        });
        // retrieving the location
        updateButton = (Button)findViewById(R.id.retrieve_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               retrieveLocation();
            }
        });
    }

    private void retrieveLocation() {
        //DatabaseReference locationsReference =  FirebaseDatabase.getInstance().getReference("Locations/user1");
                //databaseReference.child("Locations/user1");
        final DatabaseReference root = FirebaseDatabase.getInstance().getReference("Locations/User 1");
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                   /* LocationObject locationObject = new LocationObject();
                    locationObject.setUserID(dataSnapshot.getKey());
                    locationObject.setLatitude(String.valueOf(dataSnapshot.getValue(Location.class).getLatitude()));
                    locationObject.setLongitude(String.valueOf(dataSnapshot.getValue(Location.class).getLongitude()));
                    */
                   LocationObject lo = new LocationObject();
                 LocationObject locationThis = dataSnapshot.getValue(LocationObject.class);
                Log.d("onDataChange",locationThis.toString());
                Intent mapsIntent = new Intent(Intent.ACTION_VIEW);
                mapsIntent.setData(Uri.parse("geo:" + locationThis.getLatitude() + "," + locationThis.getLongitude()+ "?q="+ locationThis.getLatitude() + "," + locationThis.getLongitude()+"Bus is Here"));
                startActivity(mapsIntent);



                  //textShow.setText(String.valueOf(locationThis.getLatitude()));
             /*   AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(String.valueOf(locationThis.getLatitude()));
                builder.setIcon(R.drawable.common_google_signin_btn_icon_light);
                final EditText input = new EditText(getBaseContext());
                builder.setView(input);
                builder.show();
                 */

            }
            @Override
             public void onCancelled(DatabaseError databaseError) {
                databaseError.getMessage();
            }
        });


        /* Intent googleMapIntent = new Intent(Intent.ACTION_VIEW);
      //  googleMapIntent.setData(Uri.parse("geo:" +location.getLatitude()+"," + location.getLongitude()));
       // googleMapIntent.setData(Uri.parse("geo:"+ lat + "," + longi));
        googleMapIntent.setPackage("com.google.android.apps.maps");
        //this.startActivity(googleMapIntent);
   */



    }

    private void collectLocation(Map<String, Object> value) {


        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : value.entrySet()){
            ArrayList<Long> Long = new ArrayList<>();
            ArrayList<Long> Lat = new ArrayList<>();
            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            Long.add((Long) singleUser.get("longitude"));
            Lat.add((Long)singleUser.get("latitude"));
            location.setLatitude(Double.longBitsToDouble((Long)singleUser.get("latitude")));
            location.setLongitude(Double.longBitsToDouble((Long)singleUser.get("longitude")));
            Log.d("Latitude",String.valueOf(Long.get(0)));
            System.out.print(Long.get(0));
        }



    }

    private void requestPermission() {
    ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},1);
    }


    public void UpdateLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            Toast.makeText(MainActivity.this,"Location Not Captured",Toast.LENGTH_LONG).show();

            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                         /*   String Latitude,Longitude;
                            Latitude = Double.toString(location.getLatitude());
                            Longitude = Double.toString(location.getLongitude());


                            LocationObject locationObject = new LocationObject(Latitude,Longitude);

                            */
                            Random random = new Random();
                            int value= random.nextInt(10);
                            String boss = "User " + String.valueOf(value) ;
                            LocationObject object = new LocationObject();//this is my own location object
                            object.setLongitude(location.getLongitude());//setting values
                            object.setLatitude(location.getLatitude());
                            object.setUserID("User Is " + String.valueOf(value));
                            //String id = String.valueOf(value);
                            databaseReference.child(boss).setValue(object);// ok ? .. retrieve function also

                        }else{
                            Toast.makeText(MainActivity.this,"Location Not Captured",Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void getBusRoute() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Enter the Bus Number");
            builder.setIcon(R.drawable.common_google_signin_btn_icon_light);
            final String[] BusRoute = new String[1];

// Set up the input
            final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME | InputType.TYPE_CLASS_NUMBER);
            builder.setView(input);

// Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                     busRoute = input.getText().toString();
                }
            });


            builder.show();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
