package com.example.wecollectfirebase;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity{

    TextView etBarcode;
    EditText etName, etPrice, etSize;
    Button btnAdd, btnScaner;
    String barcode, item, price, size, store, date, documentId;

    String[] stores = {"Ribola", "Tommy", "Studenac", "Interspar", "Konzum", "Lidl"};
    AutoCompleteTextView autoComplete;
    ArrayAdapter<String> adapterStore;

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = (EditText) findViewById(R.id.etName);
        etPrice = (EditText) findViewById(R.id.etPrice);
        etSize = (EditText) findViewById(R.id.etSize);
        etBarcode = (TextView) findViewById(R.id.etBarcode);

        btnAdd = (Button) findViewById(R.id.btnADD);
        btnScaner = (Button) findViewById(R.id.btnScaner);

        autoComplete = findViewById(R.id.autocomplete);
        adapterStore = new ArrayAdapter<String>(this, R.layout.list_item, stores);

        autoComplete.setAdapter(adapterStore);

        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                store = adapterView.getItemAtPosition(i).toString();
            }
        });

        btnScaner.setOnClickListener(v->
        {
            scanCode();
        });

        firestore = FirebaseFirestore.getInstance();
        CollectionReference ref = firestore.collection("items");

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DateFormat newDate = new SimpleDateFormat("MM/dd/yyyy");
                Date Date = new Date ();
                date = newDate.format(Date);

                item = etName.getText().toString();
                price = etPrice.getText().toString();
                size = etSize.getText().toString();
                barcode = etBarcode.getText().toString();
                documentId = barcode + store;

                if (item.isEmpty() || String.valueOf(etPrice).isEmpty() || barcode.isEmpty() || store == null) {
                    Toast.makeText(getApplicationContext(),"Fill all data", Toast.LENGTH_LONG).show();
                }

                else{

                    firestore.collection("items").document(documentId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (!document.exists()) {
                                    // Field with the specified value does not exist, add the data
                                    Map<String,Object> items = new HashMap<>();
                                    items.put("Item", item);
                                    items.put("Price", price + " €");
                                    items.put("Size", size);
                                    items.put("Barcode", barcode);
                                    items.put("Store", store);
                                    items.put("Date", date);

                                    firestore.collection("items").document(documentId).set(items)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_LONG).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getApplicationContext(),"Failure", Toast.LENGTH_LONG).show();
                                                }
                                            });

                                } else {
                                    Toast.makeText(getApplicationContext(),"Item already exists in this store", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Toast.makeText(getApplicationContext(),"Error getting document: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

    }



    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLaucher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result ->
    {
        if(result.getContents() != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Result");
            builder.setMessage(result.getContents());
            etBarcode.setText(result.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        }
    });
}

























