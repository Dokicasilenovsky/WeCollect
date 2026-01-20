package com.example.wecollectfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.DesignTool;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ItemView extends AppCompatActivity implements RecyclerViewInterface{

    FloatingActionButton btnAdd;

    RecyclerView recyclerView;
    ArrayList<User> userArrayList;
    ArrayList<User> filteredUserArrayList;
    MyAdapter myAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    String documentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);

        btnAdd = findViewById(R.id.btnAdd);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        userArrayList = new ArrayList<User>();
        filteredUserArrayList = new ArrayList<User>();

        EventChangeListener();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ItemView.this, MainActivity.class));
            }
        });
    }

    private void EventChangeListener() {

        db.collection("items").orderBy("Item", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){

                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }

                        for(DocumentChange dc : value.getDocumentChanges()){

                            if(dc.getType() == DocumentChange.Type.ADDED){

                                userArrayList.add(dc.getDocument().toObject(User.class));
                                myAdapter =  new MyAdapter(ItemView.this, userArrayList, ItemView.this);
                                recyclerView.setAdapter(myAdapter);

                                documentID = String.valueOf(dc.getDocument());

                            }

                            myAdapter.notifyDataSetChanged();
                            if(progressDialog.isShowing()) {
                                progressDialog.dismiss();

                            }

                        }


                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_item,menu);
        MenuItem menuItem = menu.findItem(R.id.search_action);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Search: ");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filteredUserArrayList.clear();

                for(User user : userArrayList) {
                    if(user.getItem().toLowerCase().contains(newText.toLowerCase())) {
                        filteredUserArrayList.add(user);
                    }
                }

                myAdapter.getFilter().filter(newText);
                myAdapter =  new MyAdapter(ItemView.this, filteredUserArrayList, ItemView.this);
                recyclerView.setAdapter(myAdapter);

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        /*switch (item.getItemId()){
            case R.id.settings_action:
                startActivity(new Intent(ItemView.this, Settings.class));
                break;

            default:
                break;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ItemView.this, ItemDetails.class);

        if(filteredUserArrayList.isEmpty()) {
            intent.putExtra("Name", userArrayList.get(position).getItem());
            intent.putExtra("Price", userArrayList.get(position).getPrice());
            intent.putExtra("Size", userArrayList.get(position).getSize());
            intent.putExtra("Store", userArrayList.get(position).getStore());
            intent.putExtra("Barcode", userArrayList.get(position).getBarcode());
            intent.putExtra("Date", userArrayList.get(position).getDate());

            startActivity(intent);
        }
        else {
            intent.putExtra("Name", filteredUserArrayList.get(position).getItem());
            intent.putExtra("Price", filteredUserArrayList.get(position).getPrice());
            intent.putExtra("Size", filteredUserArrayList.get(position).getSize());
            intent.putExtra("Store", filteredUserArrayList.get(position).getStore());
            intent.putExtra("Barcode", filteredUserArrayList.get(position).getBarcode());
            intent.putExtra("Date", filteredUserArrayList.get(position).getDate());

            startActivity(intent);
        }
    }
}















