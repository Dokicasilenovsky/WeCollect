package com.example.wecollectfirebase;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ItemDetails extends AppCompatActivity {

    ImageView imgBarcode, imgItem;
    Button btnUpdate, btnReport;
    TextView ItemName, ItemPrice, ItemSize, ItemBarcode, ItemStore, ItemLink, tvUpdated;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterStore;
    String Item_Updated;

    String Name, Price, Store, Barcode, Date, Size;
    String[] stores = {"Ribola", "Tommy", "Studenac", "Interspar", "Konzum", "Lidl"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        Store = getIntent().getStringExtra("Store");
        Barcode = getIntent().getStringExtra("Barcode");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("items");
        DocumentReference docRef = db.collection("items").document(Barcode + Store);

        ItemName = findViewById(R.id.ItemName);
        ItemSize = findViewById(R.id.ItemSize);
        ItemPrice = findViewById(R.id.ItemPrice);
        //ItemStore = findViewById(R.id.ItemStore);
        ItemBarcode = findViewById(R.id.ItemBarcode);
        tvUpdated = findViewById(R.id.tvUpdated);
        //ItemLink = findViewById(R.id.ItemLink);
        imgBarcode = findViewById(R.id.imgBarcode);
        //imgItem = findViewById(R.id.imgItem);
        btnUpdate = findViewById(R.id.Update);
        Item_Updated = getString(R.string.Item_Updated);

        autoCompleteTextView = findViewById(R.id.auto_complete_txt);
        adapterStore = new ArrayAdapter<String>(this, R.layout.list_item, stores);

        autoCompleteTextView.setAdapter(adapterStore);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Store = adapterView.getItemAtPosition(i).toString();
                DocumentReference docRef = db.collection("items").document(Barcode + Store);

                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Map<String, Object> data = documentSnapshot.getData();
                            Name = documentSnapshot.getString("Item");
                            Price = documentSnapshot.getString("Price");
                            Size = documentSnapshot.getString("Size");
                            Date = documentSnapshot.getString("Date");

                            ItemName.setText(Name);
                            ItemSize.setText(Size);
                            ItemPrice.setText(Price);
                            //ItemStore.setText(Store);
                            ItemBarcode.setText(Barcode);

                            String Day = Date.substring(3, 5);
                            String Month = Date.substring(0, 2);
                            String Year = Date.substring(6, 10);

                            DateFormat newDate = new SimpleDateFormat("MM/dd/yyyy");
                            java.util.Date CDate = new Date ();
                            String date = newDate.format(CDate);

                            String CDay = date.substring(3, 5);
                            String CMonth = date.substring(0, 2);
                            String CYear = date.substring(6, 10);

                            int day = Integer.parseInt(Day);
                            int month = Integer.parseInt(Month);
                            int year = Integer.parseInt(Year);
                            int cday = Integer.parseInt(CDay);
                            int cmonth = Integer.parseInt(CMonth);
                            int cyear = Integer.parseInt(CYear);

                            btnUpdate.setVisibility(View.INVISIBLE);

                            if(year == cyear){
                                if (month == cmonth){
                                    if (day < cday){
                                        btnUpdate.setVisibility(View.VISIBLE);
                                    }
                                }
                                else if (month < cmonth){
                                    btnUpdate.setVisibility(View.VISIBLE);
                                }
                            }
                            else if (year < cyear){
                                btnUpdate.setVisibility(View.VISIBLE);
                            }

                            tvUpdated.setText(Item_Updated + " " + day + "/" + month + "/" + year);


                            MultiFormatWriter writer = new MultiFormatWriter();
                            try {
                                BitMatrix matrix = writer.encode(String.valueOf(Barcode), BarcodeFormat.CODE_128, 900,200);

                                BarcodeEncoder encoder = new BarcodeEncoder();
                                Bitmap bitmap = encoder.createBitmap(matrix);
                                imgBarcode.setImageBitmap(bitmap);



                            } catch (WriterException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error getting document: " + e.getMessage());
                    }
                });

            }
        });

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Map<String, Object> data = documentSnapshot.getData();
                    Name = documentSnapshot.getString("Item");
                    Price = documentSnapshot.getString("Price");
                    Size = documentSnapshot.getString("Size");
                    Date = documentSnapshot.getString("Date");

                    ItemName.setText(Name);
                    ItemSize.setText(Size);
                    ItemPrice.setText(Price);
                    //ItemStore.setText(Store);
                    ItemBarcode.setText(Barcode);

                    String Day = Date.substring(3, 5);
                    String Month = Date.substring(0, 2);
                    String Year = Date.substring(6, 10);

                    DateFormat newDate = new SimpleDateFormat("MM/dd/yyyy");
                    java.util.Date CDate = new Date ();
                    String date = newDate.format(CDate);

                    String CDay = date.substring(3, 5);
                    String CMonth = date.substring(0, 2);
                    String CYear = date.substring(6, 10);

                    int day = Integer.parseInt(Day);
                    int month = Integer.parseInt(Month);
                    int year = Integer.parseInt(Year);
                    int cday = Integer.parseInt(CDay);
                    int cmonth = Integer.parseInt(CMonth);
                    int cyear = Integer.parseInt(CYear);

                    btnUpdate.setVisibility(View.INVISIBLE);

                    if(year == cyear){
                        if (month == cmonth){
                            if (day < cday){
                                btnUpdate.setVisibility(View.VISIBLE);
                            }
                        }
                        else if (month < cmonth){
                            btnUpdate.setVisibility(View.VISIBLE);
                        }
                    }
                    else if (year < cyear){
                        btnUpdate.setVisibility(View.VISIBLE);
                    }

                    tvUpdated.setText(Item_Updated + " " + day + "/" + month + "/" + year);


                    MultiFormatWriter writer = new MultiFormatWriter();
                    try {
                        BitMatrix matrix = writer.encode(String.valueOf(Barcode), BarcodeFormat.CODE_128, 900,200);

                        BarcodeEncoder encoder = new BarcodeEncoder();
                        Bitmap bitmap = encoder.createBitmap(matrix);
                        imgBarcode.setImageBitmap(bitmap);



                    } catch (WriterException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.d(TAG, "No such document");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error getting document: " + e.getMessage());
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemDetails.this, ItemUpdate.class);

                intent.putExtra("Name", Name);
                intent.putExtra("Price", Price);
                intent.putExtra("Size", Size);
                intent.putExtra("Store", Store);
                intent.putExtra("Barcode", Barcode);
                intent.putExtra("Date", Date);

                startActivity(intent);
            }
        });

    }

    /*private void searchImage(String PicName) {
        String apiKey = "AIzaSyCOkthPoOP1irjxrbUp8VNg5P5il9Qq6_c";
        String searchEngineId = "8105cb7d47527407f";
        String searchUrl = "https://www.googleapis.com/customsearch/v1?key=" + apiKey + "&cx=" + searchEngineId + "&q=" + PicName + "&imgSize=large&imgType=photo&num=1";

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, searchUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray items = response.getJSONArray("items");
                    JSONObject item = items.getJSONObject(0);
                    String imageUrl = item.getString("link");

                    ItemLink.setText(imageUrl);

                    Picasso.get().load(imageUrl).into(imgItem);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(jsonObjectRequest);
    }*/
}