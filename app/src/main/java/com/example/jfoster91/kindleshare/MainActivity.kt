package com.example.jfoster91.kindleshare

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import android.R.attr.password
import android.support.v4.app.FragmentActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.R.attr.password
import android.content.Intent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.firestore.DocumentReference
import com.google.android.gms.tasks.OnSuccessListener



class MainActivity : AppCompatActivity() {

    val mAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentUser = mAuth.currentUser

        if(currentUser != null){
            login()
        }


    }


    fun clickedLoginOrRegister(view: View){

        var tag = goBtn.getTag()
        val userEmail = emailEditText.text.toString()
        val userPassword = passwordEditText.text.toString()

        if (tag == "Register") {

            mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            addToDB()
                            login()

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }

        } else if (tag == "SignIn") {
            mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            login()
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
        }
    }

    fun login(){
        Log.i("Login", "Success")
        val loginIntent = Intent(this, IndexActivity::class.java)
        startActivity(loginIntent)
    }

    fun addToDB(){
        // Create a new user with a first and last name
        val userEmail = emailEditText.text.toString()
        val user = HashMap<String, Any>()
        user.put("email", userEmail)
        user.put("tokens", 4)


// Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener { documentReference -> Log.i("Result", "DocumentSnapshot added with ID: " + documentReference.id) }
                .addOnFailureListener { e -> Log.i("Result", "Error adding document", e) }
    }
}


