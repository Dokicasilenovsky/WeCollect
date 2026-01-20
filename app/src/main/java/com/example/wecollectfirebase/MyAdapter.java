package com.example.wecollectfirebase;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kotlin.jvm.internal.Ref;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements Filterable {

    private final RecyclerViewInterface recyclerViewInterface;

    Context context;
    ArrayList<User> userArrayList;
    ArrayList<User> userArrayListFull;

    public MyAdapter(Context context, ArrayList<User> userArrayList, RecyclerViewInterface recyclerViewInterface) {

        this.context = context;
        this.userArrayListFull = userArrayList;
        this.userArrayList = new ArrayList<>(userArrayListFull);
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);

        return new MyViewHolder(v, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {

        User user = userArrayList.get(position);
        holder.itemName.setText(user.getItem());
        holder.itemPrice.setText(user.getPrice());
        holder.itemSize.setText(user.getSize());
        holder.itemStore.setText(user.getStore());

        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.recyclerviewanimation));
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return userFilter;
    }

    private final Filter userFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<User> filteredUserList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredUserList.addAll(userArrayListFull);
            }
            else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(User user : userArrayListFull) {

                    if (user.Item.toLowerCase().contains(filterPattern)){
                        filteredUserList.add(user);
                    }
                }

            }

            FilterResults results = new FilterResults();
            results.values = filteredUserList;
            results.count = filteredUserList.size();
            return results;

        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            userArrayList.clear();
            userArrayList.addAll((ArrayList)filterResults.values);
            notifyDataSetChanged();

        }
    };

    public static class MyViewHolder extends RecyclerView.ViewHolder{


        ImageView Update;

        TextView itemName, itemPrice, itemSize, itemStore;


        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            itemName = itemView.findViewById(R.id.tvItemName);
            itemPrice = itemView.findViewById(R.id.tvItemPrice);
            itemSize = itemView.findViewById(R.id.tvItemSize);
            itemStore = itemView.findViewById(R.id.tvItemStore);

            Update = itemView.findViewById(R.id.Update);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
