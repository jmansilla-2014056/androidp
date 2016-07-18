package com.mansilla.admin.cursoedx;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;

/**
 * Created by admin on 16/07/2016.
 */
public class UserAndPass {
    public static boolean complete = false;
    public static boolean sing = false;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public void CreteUser(final String email, final  String password) {
        mAuth.createUserWithEmailAndPassword(email, password);
    }

}
