package com.taek_aaa.locationdiary;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import static com.google.android.gms.maps.CameraUpdateFactory.newLatLng;
import static com.taek_aaa.locationdiary.DataSet.category_arr;
import static com.taek_aaa.locationdiary.DataSet.dbiter;
import static com.taek_aaa.locationdiary.DataSet.itc;
import static com.taek_aaa.locationdiary.DataSet.sllDBData;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    final DBManager dbManager = new DBManager(this, "logger.db", null, 1);
    private GoogleMap mMap;
    Spinner spinner;
    String type_str = "";
    EditText editText;
    LinearLayout type_ll;
    static String outermemo;
    static int temp;
    //LinkedList<DBData> sllDBData = new LinkedList<DBData>();
    Bitmap photo;
    final int PICK_FROM_ALBUM = 101;
    AlertDialog tempad;
    Dialog dialog;
    ImageView imageView;
    //IterationClass itc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //itc = new IterationClass();

        MapsInitializer.initialize(getApplicationContext());
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_FROM_ALBUM:
                Uri uri = data.getData();
                try {
                    photo = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    dialog = new Dialog(this);
                    dialog.setOwnerActivity(this);
                    dialog.setContentView(R.layout.activity_dialog);
                    imageView = (ImageView) dialog.findViewById(R.id.imageview);
                    //type_ll.addView(imageView);
                    imageView.setImageBitmap(photo);
                    //setContentView(imageView);
////이부분수정
                    dialog.show();
                    //tempad.show();


                } catch (Exception e) {
                    e.getStackTrace();
                }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        sllDBData.clear();
        dbManager.getResult(sllDBData);
        mMap = googleMap;
        dbiter = dbManager.getIter();
        try {
            int count = Integer.parseInt(sllDBData.getLast().curNum) + 1;
            Log.e("value", String.valueOf(itc.getIteration()));
            Log.e("bbb",""+itc.getIteration()+"맵액티비티 트라이문안에");
            for (int i = 0; i < itc.getIteration(); i++) {
                Log.e("value", String.valueOf(sllDBData.get(i).curlatitude));
                Log.e("value", String.valueOf(sllDBData.get(i).curlongitude));
                Log.e("value", sllDBData.get(i).curTodoOrEvent);
                Log.e("value", String.valueOf(sllDBData.get(i).curCategory));
                Log.e("value", String.valueOf(sllDBData.get(i).curHowLong));
                Log.e("value", sllDBData.get(i).curNum);
                Log.e("value", sllDBData.get(i).curText);
                Log.e("value", sllDBData.get(i).curTime);
            }


            for (int i = 0; i < itc.getIteration(); i++) {
                Log.e("bbb",""+itc.getIteration()+"맵액티비티 포문안에");
                MarkerOptions opt = new MarkerOptions();
                opt.position(new LatLng(sllDBData.get(i).curlatitude, sllDBData.get(i).curlongitude));
                opt.title(sllDBData.get(i).curNum);
                opt.snippet(sllDBData.get(i).curText + "@" + sllDBData.get(i).curTime);

                mMap.addMarker(opt).showInfoWindow();
                if (i != 0) {
                    mMap.addPolyline(new PolylineOptions().geodesic(true).add(new LatLng(Double.valueOf(sllDBData.get(i - 1).curlatitude), Double.valueOf(sllDBData.get(i - 1).curlongitude)), new LatLng(Double.valueOf(sllDBData.get(i).curlatitude), Double.valueOf(sllDBData.get(i).curlongitude))).width(5).color(Color.RED));
                }
                Log.e("value", "한바퀴돔");
            }
            mMap.moveCamera(newLatLng(new LatLng(sllDBData.get(0).curlatitude, sllDBData.get(0).curlongitude)));
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(MapsActivity.this);
                    type_ll = new LinearLayout(MapsActivity.this);

                    //setSpinner();
                    //setEditText();
                    //type_ll.addView(spinner);
                    //type_ll.addView(editText);
                    //type_ll.setPadding(50, 0, 0, 0);
                    type_ll.setPadding(0, 0, 0, 0);
//                type_ll.addView(tempImageView = (ImageView)dialog.findViewById(R.id.imageview));
                    int a = Integer.valueOf(marker.getTitle());
                    temp = a;
                    adb
                            .setTitle("정보창")
                            .setCancelable(false)
                            .setMessage("종류 : " + sllDBData.get(a).curTodoOrEvent + "\n" + "카테고리 : " + category_arr[sllDBData.get(a).curCategory] + "\n" + "소요시간 : " + sllDBData.get(a).curHowLong + "초" + "\n" + "" + "내용 : " + sllDBData.get(a).curText + "\n" + "시간 : " + sllDBData.get(a).curTime)
                            .setView(type_ll)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setNeutralButton("사진 추가", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent intent = new Intent(Intent.ACTION_PICK);
                                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(intent, PICK_FROM_ALBUM);

                                }
                            })
                            .setNegativeButton("사진 촬영", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    intent.setAction("android.media.action.IMAGE_CAPTURE");
                                    startActivity(intent);
                                }
                            });

                    AlertDialog ad = adb.create();
                    tempad = ad;
                    ad.show();
                }

            });
        } catch (Exception e) {
            e.getMessage();
            Toast.makeText(this, "먼저 값을 받아주세요.", Toast.LENGTH_SHORT).show();
        }

    }

    /*public void setSpinner() {
        spinner = new Spinner(this);
        ArrayAdapter memoAdapter = new ArrayAdapter(MapsActivity.this, android.R.layout.simple_spinner_item, category_arr);
        memoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(memoAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type_str = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    */


}
