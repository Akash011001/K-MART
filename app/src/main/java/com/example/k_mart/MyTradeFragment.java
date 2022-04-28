package com.example.k_mart;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyTradeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyTradeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FirebaseFirestore db;
    FirebaseAuth mauth;

    RecyclerView recyclerView;
    ArrayList<DataToShow> list;
    BuyCardAdapter adapter;

    public MyTradeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyTradeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyTradeFragment newInstance(String param1, String param2) {
        MyTradeFragment fragment = new MyTradeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_trade, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        mauth = FirebaseAuth.getInstance();
        list = new ArrayList<>();

        recyclerView = view.findViewById(R.id.mytrade_frag_recyclerview);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(lm);
        recyclerView.setHasFixedSize(true);

        adapter = new BuyCardAdapter(getContext(), list, new BuyCardAdapter.ItemClickListener() {
            @Override
            public void OnItemClick(int pos) {
                Toast.makeText(getContext(), pos+"", Toast.LENGTH_SHORT).show();
                final Dialog d = new Dialog(getContext());
                d.setContentView(R.layout.delete_mytrade_dialog_view);
                d.show();
                Button delete = d.findViewById(R.id.mytrade_dialog_delete_btn);
                Button cancel = d.findViewById(R.id.mytrade_dialog_cancel_btn);

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String[] v = list.get(pos).getId().split("@");
                        db.collection(v[0]).document(v[1]).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                d.dismiss();
                                Toast.makeText(getContext(), "deleted", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });


            }
        });

        recyclerView.setAdapter(adapter);
        eventChangeListener();


    }

    public void eventChangeListener(){
        db.collection("sells").whereEqualTo("Uid", mauth.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange d : queryDocumentSnapshots.getDocumentChanges()) {
                    if (d.getType() == DocumentChange.Type.ADDED) {
                        DataToShow t = new DataToShow();

                        //Toast.makeText(getContext(), d.getDocument().getData().get("Image").toString(), Toast.LENGTH_SHORT).show();

                        t.addFirst(d.getDocument().getData().get("Name")+"");
                        t.addSecond(d.getDocument().getData().get("Location")+"");
                        t.addThird(d.getDocument().getData().get("Item")+"-Rs"+d.getDocument().getData().get("Price")+" Stock-"+d.getDocument().getData().get("Amount"));
                        t.addImageUrl(d.getDocument().getData().get("Image")+"");
                        t.addUid(d.getDocument().getData().get("Uid")+"");
                        t.addId("sells@"+d.getDocument().getData().get("Id")+"");
                        list.add(t);
                        adapter.notifyDataSetChanged();

                    }
                }
            }

        });

        db.collection("demands").whereEqualTo("Uid", mauth.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                        t.addId("demands@"+d.getDocument().getData().get("Id")+"");
                        list.add(t);
                        adapter.notifyDataSetChanged();

                    }
                }
            }

        });

    }
}