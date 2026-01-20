package com.example.wecollectfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ItemUpdate extends AppCompatActivity {

    Button Update;
    TextView tvName, tvPrice, tvSize;

    String item, size, price, DocumentID;

    FirebaseFirestore firestore;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_update);

        String Name = getIntent().getStringExtra("Name");
        String Size = getIntent().getStringExtra("Size");
        String Price = getIntent().getStringExtra("Price");
        String Store = getIntent().getStringExtra("Store");
        String Barcode = getIntent().getStringExtra("Barcode");

        DocumentID = Barcode + Store;

        tvName = findViewById(R.id.UpdateName);
        tvPrice = findViewById(R.id.UpdatePrice);
        tvSize = findViewById(R.id.UpdateSize);
        Update = findViewById(R.id.btnUpdate);

        String Price2 = Price.substring(0, Price.length() - 2);

        tvName.setText(Name);
        tvPrice.setText(Price2);
        tvSize.setText(Size);

        firestore = FirebaseFirestore.getInstance();

        DocumentReference documentRef = firestore.collection("items").document(DocumentID);

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                item = tvName.getText().toString();
                price = tvPrice.getText().toString();
                size = tvSize.getText().toString();

                DateFormat newDate = new SimpleDateFormat("MM/dd/yyyy");
                java.util.Date Date = new Date ();
                String date = newDate.format(Date);

                documentRef.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Document was successfully deleted
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // An error occurred while deleting the document
                            }
                        });

                Map<String,Object> items = new HashMap<>();
                items.put("Item", item);
                items.put("Price", price + " €");
                items.put("Size", size);
                items.put("Barcode", Barcode);
                items.put("Store", Store);
                items.put("Date", date);

                firestore.collection("items").document(Barcode + Store).set(items)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@androidx.annotation.NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Failure", Toast.LENGTH_LONG).show();
                            }
                        });

            }
        });


    }
}