package com.example.k_mart;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;

public class MainFragment extends Fragment {
    Button buybtn,sellBtn,addBtn;
    FirebaseAuth mauth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        buybtn = (Button) view.findViewById(R.id.buy_button_main_frag);
        sellBtn = (Button) view.findViewById(R.id.sell_button_main_frag);
        addBtn = (Button) view.findViewById(R.id.add_button_main_frag);

        mauth = FirebaseAuth.getInstance();

        if(mauth.getCurrentUser()!=null){
            loadChildFragment(new BuyFragment());
        }

        sellBtn.setBackgroundColor(Color.parseColor("#1A759F"));
        buybtn.setBackgroundColor(Color.parseColor("#52B69A"));
        buybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadChildFragment(new BuyFragment());
                sellBtn.setBackgroundColor(Color.parseColor("#1A759F"));
                buybtn.setBackgroundColor(Color.parseColor("#52B69A"));
            }
        });

        sellBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadChildFragment(new SellFragment());
                buybtn.setBackgroundColor(Color.parseColor("#1A759F"));
                sellBtn.setBackgroundColor(Color.parseColor("#52B69A"));
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());

                dialog.setContentView(R.layout.dialog_custom);

                Button demand = dialog.findViewById(R.id.demand_btn);
                Button sell = dialog.findViewById(R.id.sell_btn);

                demand.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        getParentFragmentManager().beginTransaction().replace(R.id.frameLayout,new DemandTradeFragment()).commit();
                    }
                });

                sell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        //loadChildFragment(new SellTradeFragment());
                        getParentFragmentManager().beginTransaction().replace(R.id.frameLayout,new SellTradeFragment()).commit();
                    }
                });
                dialog.show();
            }

        });

    }

    private void loadChildFragment(Fragment fragment){
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.main_fragment_child, fragment).commit();
    }
}
