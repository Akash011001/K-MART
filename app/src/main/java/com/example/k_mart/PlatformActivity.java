package com.example.k_mart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PlatformActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView nv;
    FirebaseAuth mauth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platform);

        mauth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadFragment(new MainFragment());

        nv = (NavigationView) findViewById(R.id.navigation_platform);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Toast.makeText(PlatformActivity.this, item.getItemId(), Toast.LENGTH_SHORT).show();
                switch (item.getItemId())
                {
                    case R.id.nav_home:
                        loadFragment(new MainFragment());
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    
                    case R.id.nav_setting:

                        break;

                    case R.id.nav_logout:

                        mauth.signOut();
                        Intent i = new Intent(PlatformActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                        break;

                    case R.id.nav_request:
                        loadFragment(new RequestFragment());
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_history:
                        loadFragment(new HistoryFragment());
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_my_trade:
                        loadFragment(new MyTradeFragment());
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_account:
                        loadFragment(new AccountFragment());
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            // Toast.makeText(this, "pressed", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFragment(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.commit();
    }

    private void addData( String name, String pan, String email, String location) {
        Map<String, Object> user = new HashMap<>();
        user.put("Name", name);
        user.put("Pan", pan);
        user.put("Email", email);
        user.put("Location", location);

        db.collection("users").document(mauth.getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(PlatformActivity.this, "inserted", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PlatformActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}