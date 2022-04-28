package com.example.k_mart;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DemandTradeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DemandTradeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageView img;
    EditText item, amount, price;
    Button put, load, cancel;
    ProgressBar bar;
    Spinner spinner;
    Spinner spinner2;

    String[] array = {"kg", "unit", "g", "q", "ton", "doz", "L", "mL"};

    String priceU;
    String amountU;

    Uri imgLink;

    FirebaseFirestore db;
    FirebaseAuth mauth;
    StorageReference storageRef;

    public DemandTradeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DemandTradeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DemandTradeFragment newInstance(String param1, String param2) {
        DemandTradeFragment fragment = new DemandTradeFragment();
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
        return inflater.inflate(R.layout.fragment_demand_trade, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        img = view.findViewById(R.id.demand_trade_imageview);
        item = view.findViewById(R.id.demand_trade_item_input);
        amount = view.findViewById(R.id.demand_trade_amount_input);
        price = view.findViewById(R.id.demand_trade_price_input);
        load = view.findViewById(R.id.demand_trade_load_btn);
        put = view.findViewById(R.id.demand_trade_put_btn);
        cancel = view.findViewById(R.id.demand_trade_frag_cancel_btn);
        bar = view.findViewById(R.id.frag_demand_progressbar);
        spinner = view.findViewById(R.id.demand_frag_spinner);
        spinner2 = view.findViewById(R.id.demand_frag_spinner2);



        mauth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        bar.setVisibility(View.GONE);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, array);

        adapter1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);
        spinner2.setAdapter(adapter1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                amountU = array[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                priceU = array[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().replace(R.id.frameLayout, new MainFragment()).commit();
            }
        });

        put.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bar.setVisibility(View.VISIBLE);
                if(imgLink!=null){
                    StorageReference upload = storageRef.child("images/"+imgLink.getLastPathSegment());
                    upload.putFile(imgLink).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            upload.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    db.collection("users").document(mauth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            Map<String, Object> data = new HashMap<>();
                                            data.put("Name", documentSnapshot.getData().get("Name").toString());
                                            data.put("Uid", mauth.getUid().toString());
                                            data.put("Item", item.getText().toString());
                                            data.put("Price", price.getText().toString()+"/"+priceU);
                                            data.put("Amount", amount.getText().toString()+amountU);
                                            data.put("Image", uri.toString());
                                            data.put("Location", documentSnapshot.getData().get("Location").toString());

                                            db.collection("demands").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {

                                                    documentReference.update("Id", documentReference.getId()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(getContext(), "sells added", Toast.LENGTH_SHORT).show();
                                                            bar.setVisibility(View.GONE);
                                                            getParentFragmentManager().beginTransaction().replace(R.id.frameLayout, new MainFragment()).commit();
                                                        }
                                                    });
                                                    //Toast.makeText(getContext(), uri.toString(), Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                        }
                                    });


                                    Log.d("Link",uri.toString());

                                }
                            });
                        }
                    });

                }

            }

        });


    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri

                    imgLink = uri;
                    Toast.makeText(getContext(), uri.toString(), Toast.LENGTH_LONG).show();
                    img.setImageURI(uri);
                }
            });
}