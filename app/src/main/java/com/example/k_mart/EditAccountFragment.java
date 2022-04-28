package com.example.k_mart;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditAccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FirebaseFirestore db;
    FirebaseAuth mauth;

    EditText name, mobile, pan, email, location;
    Button update;

    public EditAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditAccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditAccountFragment newInstance(String param1, String param2) {
        EditAccountFragment fragment = new EditAccountFragment();
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
        return inflater.inflate(R.layout.fragment_edit_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        mauth = FirebaseAuth.getInstance();

        name = view.findViewById(R.id.edit_acc_frag_name_input);
        mobile = view.findViewById(R.id.edit_acc_frag_email_input);
        pan = view.findViewById(R.id.edit_acc_frag_pan_input);
        email = view.findViewById(R.id.edit_acc_frag_email_input);
        location = view.findViewById(R.id.edit_acc_frag_location_input);
        update = view.findViewById(R.id.edit_acc_frag_update_btn);


        db.collection("users").document(mauth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                name.setText(documentSnapshot.getData().get("Name")+"");
                mobile.setText(documentSnapshot.getData().get("Mobile")+"");
                pan.setText(documentSnapshot.getData().get("Pan")+"");
                email.setText(documentSnapshot.getData().get("Email")+"");
                location.setText(documentSnapshot.getData().get("Location")+"");


            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameText = name.getText().toString().trim();
                String mobileText = mobile.getText().toString().trim();
                String panText = pan.getText().toString().trim();
                String emailText = email.getText().toString().trim();
                String locationText = location.getText().toString().trim();

                if(!(nameText.isEmpty() && mobileText.isEmpty() && panText.isEmpty() && emailText.isEmpty() && locationText.isEmpty()))
                {
                    String[] emailTest = emailText.split("@");
                    if (emailTest != null && emailTest.length == 2 && emailTest[1].equals("gmail.com")) {

                        HashMap<String, Object> data = new HashMap<>();
                        data.put("Name", name.getText().toString());
                        data.put("Mobile", mobile.getText().toString());
                        data.put("Pan", pan.getText().toString());
                        data.put("Email", pan.getText().toString());
                        data.put("Location", location.getText().toString());

                        db.collection("users").document(mauth.getUid()).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                            }
                        });


                    } else {
                        Toast.makeText(getContext(), "Email is incorrect", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(), "Fields Cannot be empty!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}