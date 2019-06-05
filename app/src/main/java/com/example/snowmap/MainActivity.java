package com.example.snowmap;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback
        ,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener
{

    private GoogleMap mMap;
    private ImageButton butonLista;

    private static final String TAG = "MainActivity";
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 100000;
    private long FASTEST_INTERVAL = 20000000; //mareste timpul aici
    private LocationManager locationManager;
    private double lat;
    private double lon;
    private LatLng latLng;
    private boolean isPermission;
    private ArrayList<String> numelePartiilor;

    private EditText mSearchField;
    private ImageButton mSearchButton;
    private RecyclerView mResultList;
    private DatabaseReference mPartiiDatabase;
    private ArrayList<String> fullNamePartii;
    private SearchFirebaseAdapter searchFirebaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchButton = findViewById(R.id.imageButtonsearch);
        mSearchField = findViewById(R.id.search_field);
        mResultList = findViewById(R.id.result_list);





        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        mPartiiDatabase = FirebaseDatabase.getInstance().getReference().child("Partii");
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(this));
        //mResultList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        fullNamePartii = new ArrayList<>();

        mSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                setAdapter(s.toString());
            }
        });

        butonLista = findViewById(R.id.butonLista);
        butonLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openListaPartii();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (requestSinglePermission()) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            checkLocation(); //check whether location service is enable or not in your  phone
        }

        readFromDB();


    }
// De aici incerc search

    private void setAdapter(final String searchedString) {
        mPartiiDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             //eliberez lista de fiecare data cand fac un nou search
                fullNamePartii.clear();
                mResultList.removeAllViews();
                if (!searchedString.isEmpty()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String full_partiiName = snapshot.getKey();
                        if (full_partiiName.toLowerCase().startsWith(searchedString.toLowerCase())) {
                            fullNamePartii.add(full_partiiName);
                        }
                    }
                }
                //SEARCH BAR
                searchFirebaseAdapter = new SearchFirebaseAdapter(MainActivity.this,fullNamePartii);
                mResultList.setAdapter(searchFirebaseAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("DEBUGING", "Failed to read value.", databaseError.toException());
            }
        });
    }

//pana aici



    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        goToLocationZoom(46.185551, 24.781264,6);

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle));

            if (!success) {
                Log.e("mapsActivity", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("mapsActivity", "Can't find style. Error: ", e);
        }

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));




       mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {


            @Override
            public void onInfoWindowClick(Marker marker) { //trimit spre detalii partie intent-ul cu titlul partiei, pentru personalizarea layputului
                String numePartie = marker.getTitle();

                Intent intent = new Intent(MainActivity.this, DetaliiPartie.class);
                intent.putExtra("NUME_PARTIE", numePartie);
                startActivity(intent);
                
            }
        });




        if (latLng != null) {
           //mMap.addMarker(new MarkerOptions().position(latLng));

           //goToLocationZoom(lat,lon,7);


             mMap.addCircle(new CircleOptions()
                    .center(latLng)
                    .radius(15000)
                    .strokeWidth(12)
                    .strokeColor(Color.WHITE)
                    .fillColor(Color.parseColor("#6D6FFF")));


       }
    }


    public void readFromDB(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference partiiRef = database.getReference().child("Partii");

        partiiRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Map<String, Object> mapPartii = (Map<String, Object>) dataSnapshot.getValue();

                for (Map.Entry<String, Object> entry : mapPartii.entrySet()) {
                   final String numePartie = entry.getKey();

                    Map<String, Object> partie = (Map<String, Object>)entry.getValue();
                    double longitudine = (double)partie.get("Long");
                    double latitudine = (double)partie.get("Lat");

                    mMap.addMarker(new MarkerOptions().
                            position(new LatLng(latitudine, longitudine)).
                            title(numePartie)).setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mountain));


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("DEBUGING", "Failed to read value.", databaseError.toException());
            }
        });
   }


    private boolean checkLocation() { //verifica daca este enalble in telefonul meu
        //pot trata si cazul in care nu este enable in telefon
        return isLocationEnabled();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private boolean requestSinglePermission() {
        //o sa returneze o variabila booleana adevarata sau falsa
        //in cazul in care utilizatorul acorda permisiunea de localizare
        //daca se returneaza true, compilatorul va porni procesul pentru a prelua locatia curenta
        //si apoi va crea goolge map cu ajutorul acelei locatii curente

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        isPermission = true;
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            isPermission = false;
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission,
                                                                   PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
        return isPermission;
    }

    private void goToLocationZoom(double lat, double lng, float zoom) {
        //ma duce cu zoom la coordonatele alea, pt a vedea doar harta romaniei
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mMap.moveCamera(update);

    }
    public void openListaPartii() {
        //acesta metoda va fi aplicata la apasarea butonului si v-a crea o noua activitate,
        // cea cu lista de partii
        Intent intent = new Intent(this, ListaPartii.class);
        intent.putExtra("latCurenta",lat);
        intent.putExtra("lonCurenta",lon);
        startActivity(intent);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) { //verific daca am permisiunea
            return;
        }
        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLocation == null) {
            startLocationUpdates();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();

        //lat , lon trebuie trimise in listapartii


        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //it was pre written
       SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
               .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    protected void startLocationUpdates() {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }


}



