package fr.sio.openstreetmaptest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_LOCATION = 1;
    private MyLocationNewOverlay myLocationOverlay;
    private LocationManager locationManager;
    private MapView map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        Configuration.getInstance().load( getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.activity_main);

        map = findViewById(R.id.map);

        map.setTileSource(TileSourceFactory.MAPNIK); //render
        map.setBuiltInZoomControls( true );         //zoomable
        map.setMultiTouchControls(true);


        IMapController mapController = map.getController();
        mapController.setZoom(18.0); //zoom de départ

        new RoadTask(this).execute();

        myLocationOverlay = new MyLocationNewOverlay(map);
        map.getOverlays().add(myLocationOverlay);



        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                GeoPoint point = new GeoPoint(location.getLatitude(), location.getLongitude());
                myLocationOverlay.setEnabled(true);
                myLocationOverlay.enableMyLocation();
                myLocationOverlay.setDrawAccuracyEnabled(true);
                map.getController().animateTo(point);

            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override
            public void onProviderEnabled(String provider) {}
            @Override
            public void onProviderDisabled(String provider) {}
        };
        // Vérifier si l'application a la permission d'accéder à la localisation de l'utilisateur
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
        } else {
            // Si l'application a la permission, commencer à écouter les mises à jour de la localisation de l'utilisateur
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }

       ArrayList<OverlayItem> items = new ArrayList<>();
//       OverlayItem home = new OverlayItem("Chez hugo", "l'eglise",new GeoPoint(46.6333,0.31800)); // ajout d'un point sur la carte
//       Drawable m = home.getMarker(0);
//       items.add(home);
//       items.add(new OverlayItem("Resto","Chez loic", new GeoPoint(46.6333,0.3168)));
//
//       Créer un marqueur
        Drawable marker1Drawable = ContextCompat.getDrawable(this, R.drawable.pinsmarker);
        Drawable marker2Drawable = ContextCompat.getDrawable(this, R.drawable.quizzpins);

        // Marker 1 station marquage au sol
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        // Créer un bouton personnalis


        Marker marker1 = new Marker(map);
        double latitude = 46.6333;
        double longitude = 0.31800;
        String nom = "Station 1";
        String description = "Bois 1";
        if (!dbHelper.coordExist(latitude, longitude)) {
            dbHelper.ajouterCoordonnees(latitude, longitude, nom, description);
        }
        marker1.setPosition(new GeoPoint(latitude, longitude));
        marker1.setTitle(nom);
        marker1.setSnippet(description);
        marker1.setIcon(marker1Drawable);
        map.getOverlays().add(marker1);


// Marker 2 station marquage au sol
        Marker marker2 = new Marker(map);
        double latitude2 = 46.6333;
        double longitude2 = 0.3168;
        if (!dbHelper.coordExist(latitude2, longitude2)) {
            dbHelper.ajouterCoordonnees(latitude2, longitude2, null, null);
        }
        marker2.setPosition(new GeoPoint(latitude2, longitude2));
        marker2.setIcon(marker2Drawable);
        map.getOverlays().add(marker2);
        ScaleBarOverlay myScaleBarOverlay = new ScaleBarOverlay(map); // barre en haut a gauche de scale
        map.getOverlays().add(myScaleBarOverlay);                     // barre en haut a gauche de scale

        //Définir un listener de clic sur le Marker 2
        marker2.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                // Lancer l'activité QuizzActivity au clic sur le Marker
                Intent intent = new Intent(getApplicationContext(), QuizzActivity.class);
                startActivity(intent);
                return true;
            }
        });

        CompassOverlay mCompassOverlay = new CompassOverlay(getApplicationContext(), new InternalCompassOrientationProvider(getApplicationContext()), map); // boussole
        mCompassOverlay.enableCompass(); // boussole
        map.getOverlays().add(mCompassOverlay); // boussole


        //affichage des overlays OverlayItem home = new OverlayItem("Chez hugo", "le Gozo",new GeoPoint(46.6333,0.31800)); // ajout d'un point sur la carte

        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(getApplicationContext(), items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                return true;
            }

            @Override
            public boolean onItemLongPress(int index, OverlayItem item) {
                return false;
            }
        });

        mOverlay.setFocusItemsOnTap(true);
        map.getOverlays().add(mOverlay);

    }

    // requete http recuperation de la road
    private class RoadTask extends AsyncTask<Void, Void, Road> {

        private ProgressDialog dialog;
        private Context context;

        public RoadTask(Context context) {
            this.context = context;
        }
        // chargement du trajet msg d'attente
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setMessage("Calcul en cours du trajet...");

            dialog.setCancelable(false);
            dialog.show();
        }


        // recupere le trajet en background
        @Override
        protected Road doInBackground(Void... params)  {

            RoadManager roadManager = new OSRMRoadManager(context, null);
            ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();

            GeoPoint startPoint = new GeoPoint(46.6333, 0.31800); //start point
            waypoints.add(startPoint);
            GeoPoint endPoint = new GeoPoint(46.6333,0.3168);
            waypoints.add(endPoint);
//            GeoPoint middlepoint = new GeoPoint(46.5802596,0.340196);
//            waypoints.add(middlepoint);
            return roadManager.getRoad(waypoints);


        }

        // recupere la route et l'affiche
        @Override
        protected void onPostExecute(Road road) {
            super.onPostExecute(road);
            dialog.dismiss();
            Polyline roadOverlay = RoadManager.buildRoadOverlay(road, Color.rgb(148,0,211), 10);
            map.getOverlays().add(roadOverlay);
        }
    }

    @Override
    public void onPause(){

        super.onPause();
        map.onPause();

    }


    @Override
    public void onResume(){

        super.onResume();
        map.onResume();

    }


}