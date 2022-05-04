package com.example.k_mart;

import android.app.Dialog;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SellFragment extends Fragment {

    RecyclerView recyclerView;

    BuyCardAdapter adapter;
    ArrayList<DataToShow> list;


    FirebaseFirestore db;
    FirebaseAuth mauth;
    StorageReference storageRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sell, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();
        mauth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.recycler_view_sell);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(lm);
        recyclerView.setHasFixedSize(true);

        list = new ArrayList<>();
        adapter = new BuyCardAdapter(getContext(), list, new BuyCardAdapter.ItemClickListener() {
            @Override
            public void OnItemClick(int pos) {
                final Dialog d = new Dialog(getContext());
                d.setContentView(R.layout.sell_dialog);
                Button sell = d.findViewById(R.id.sell_dialog_sell_btn);
                Button cancel = d.findViewById(R.id.sell_dialog_cancel_btn);

                sell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), "sold", Toast.LENGTH_SHORT).show();
                        //not
                        d.dismiss();
                        if(!mauth.getUid().equals(list.get(pos).getUid())){
                            Map<String, Object> map = new HashMap<>();
                            map.put("SellerId", mauth.getUid());
                            map.put("ReqFor", list.get(pos).getUid());
                            map.put("isCompleted", "F");
                            map.put("Item", list.get(pos).getThird());
                            db.collection("trade").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(getContext(), "Buy Request Sent", Toast.LENGTH_LONG).show();

                                    documentReference.update("TradeId", documentReference.getId()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getContext(), "Updated trade", Toast.LENGTH_LONG).show();
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            Toast.makeText(getContext(), "This is your Item!", Toast.LENGTH_LONG).show();
                            d.dismiss();
                        }
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });

                d.show();

            }
        });
        eventChangeListener();
        recyclerView.setAdapter(adapter);




    }

    public void eventChangeListener(){
        db.collection("demands").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange d : queryDocumentSnapshots.getDocumentChanges()) {
                    if (d.getType() == DocumentChange.Type.ADDED) {
                        DataToShow t = new DataToShow();

                        //Toast.makeText(getContext(), d.getDocument().getData().get("Image").toString(), Toast.LENGTH_SHORT).show();

                        t.addFirst(d.getDocument().getData().get("Name")+"");
                        t.addSecond(d.getDocument().getData().get("Location")+"");
                        t.addThird(d.getDocument().getData().get("Item")+"-Rs"+d.getDocument().getData().get("Price")+" Req-"+d.getDocument().getData().get("Amount"));
                        t.addImageUrl(d.getDocument().getData().get("Image")+"");
                        t.addUid(d.getDocument().getData().get("Uid")+"");
                        list.add(t);
                        adapter.notifyDataSetChanged();

                    }
                }
            }

        });
    }


}
