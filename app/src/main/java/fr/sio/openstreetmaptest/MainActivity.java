package fr.sio.openstreetmaptest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;


import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import org.osmdroid.api.IMapController;
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
import org.osmdroid.views.overlay.mylocation.DirectedLocationOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.Polyline.*;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
private MapView map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Configuration.getInstance().load( getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.activity_main);


        map = findViewById(R.id.map);

        map.setTileSource(TileSourceFactory.MAPNIK); //render
        map.setBuiltInZoomControls( true );         //zoomable
        GeoPoint startPoint = new GeoPoint(46.6333, 0.3167); //start point


        IMapController mapController = map.getController();
        mapController.setZoom(18.0); //zoom de départ
        mapController.setCenter(startPoint);

        ArrayList<OverlayItem> items = new ArrayList<>();
        OverlayItem home = new OverlayItem("Chez hugo", "le Gozo",new GeoPoint(46.6333,0.31800)); // ajout d'un point sur la carte
        Drawable m = home.getMarker(0);
        items.add(home);
        items.add(new OverlayItem("Resto","Chez loic", new GeoPoint(46.6333,0.3168)));

        map.setBuiltInZoomControls(true); //zoom avec 2 doigts
        map.setMultiTouchControls(true); //zoom avec 2 doigts

        ScaleBarOverlay myScaleBarOverlay = new ScaleBarOverlay(map); // barre en haut a gauche de scale
        map.getOverlays().add(myScaleBarOverlay);                     // barre en haut a gauche de scale

        CompassOverlay mCompassOverlay = new CompassOverlay(getApplicationContext(), new InternalCompassOrientationProvider(getApplicationContext()), map); // boussole
        mCompassOverlay.enableCompass(); // boussole
        map.getOverlays().add(mCompassOverlay); // boussole


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