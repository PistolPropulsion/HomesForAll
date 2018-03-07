package edu.gatech.pistolpropulsion.homesforall.Controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.*;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.gatech.pistolpropulsion.homesforall.Models.Administrator;
import edu.gatech.pistolpropulsion.homesforall.Models.StoreEmployee;
import edu.gatech.pistolpropulsion.homesforall.Models.User;
import edu.gatech.pistolpropulsion.homesforall.R;

import static android.content.ContentValues.TAG;

public class RegisterActivity extends Activity {

    private EditText editUser;
    private EditText editPass;
    private TextView enter;
    private TextView cancel;
    private Spinner userSpinner;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

        enter = (TextView) findViewById(R.id.enter_textView);
        cancel = (TextView) findViewById(R.id.cancel_textView);
        userSpinner = (Spinner) findViewById(R.id.user_spinner);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.user_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSpinner.setAdapter(adapter);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUser = (EditText) findViewById(R.id.editUser);
                editPass = (EditText) findViewById(R.id.editPass);
                final String username = editUser.getText().toString();
                final String pass = editPass.getText().toString();
                final int type = userSpinner.getSelectedItemPosition();
                try {
                    mAuth.createUserWithEmailAndPassword(username, pass)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        final String userKey = username.replaceAll(".", "");

                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        if (type == 2) {
                                            Administrator userObject = new Administrator(username, pass);
                                            mDatabase.child("users").child("administrators").child(user.getUid()).setValue(userObject);
                                        } else if (type == 1) {
                                            StoreEmployee userObject = new StoreEmployee(username, pass);
                                            mDatabase.child("users").child("storeEmployees").child(user.getUid()).setValue(userObject);
                                        } else {
                                            User userObject = new User(username, pass);
                                            mDatabase.child("users").child("standardUsers").child(user.getUid()).setValue(userObject);
                                        }

                                        Toast.makeText(getApplicationContext(), "You can now login with this user.",
                                                Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(getApplicationContext(), "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });
                } catch (IllegalArgumentException e) {
                    Log.w(TAG, "signInWithEmail:failure", e);
                    Toast.makeText(getApplicationContext(), "Please enter all fields.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }
}
