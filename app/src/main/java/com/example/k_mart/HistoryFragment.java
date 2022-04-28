package com.example.k_mart;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {

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
    ArrayList<RequestDataToShow> list;
    RequestCardAdapter adapter;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.history_frag_recyclerview);
        db = FirebaseFirestore.getInstance();
        mauth = FirebaseAuth.getInstance();
        list = new ArrayList<>();

        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(lm);
        recyclerView.setHasFixedSize(true);

        adapter = new RequestCardAdapter(getContext(), list, new RequestCardAdapter.ItemClickListener() {
            @Override
            public void OnItemClick(int pos) {

            }
        });
        recyclerView.setAdapter(adapter);
        eventChangeListener();


    }

    public void eventChangeListener(){
        db.collection("trade").whereEqualTo("BuyerId", mauth.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange d : queryDocumentSnapshots.getDocumentChanges()) {
                    if (d.getType() == DocumentChange.Type.ADDED) {
                        RequestDataToShow t = new RequestDataToShow();

                        //Toast.makeText(getContext(), d.getDocument().getData().get("Image").toString(), Toast.LENGTH_SHORT).show();
                        String buyerId = d.getDocument().getData().get("BuyerId").toString();
                        db.collection("users").document(buyerId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                if (d.getDocument().getData().get("isCompleted").toString().equals("T")) {
                                    String buyerName = documentSnapshot.getData().get("Name") + "";

                                    t.setFirst(buyerName);
                                    t.setSecond(d.getDocument().getData().get("Item") + "");
                                    t.setTradeId(d.getDocument().getData().get("TradeId") + "");
                                    list.add(t);
                                    adapter.notifyDataSetChanged();
                                }
                            }

                        });



                    }
                }
            }

        });

        db.collection("trade").whereEqualTo("SellerId", mauth.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange d : queryDocumentSnapshots.getDocumentChanges()) {
                    if (d.getType() == DocumentChange.Type.ADDED) {
                        RequestDataToShow t = new RequestDataToShow();

                        //Toast.makeText(getContext(), d.getDocument().getData().get("Image").toString(), Toast.LENGTH_SHORT).show();
                        String sellerId = d.getDocument().getData().get("SellerId").toString();
                        db.collection("users").document(sellerId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                if (d.getDocument().getData().get("isCompleted").toString().equals("T")) {
                                    String buyerName = documentSnapshot.getData().get("Name") + "";

                                    t.setFirst(buyerName);
                                    t.setSecond(d.getDocument().getData().get("Item") + "");
                                    t.setTradeId(d.getDocument().getData().get("TradeId") + "");
                                    list.add(t);
                                    adapter.notifyDataSetChanged();
                                }
                            }

                        });



                    }
                }
            }

        });
    }

}