package com.icti.tudoauto;

import android.content.Intent;

import androidx.annotation.NonNull;

import java.util.Objects;

public class LoginController {



    // Check if user is signed in (non-null) and update UI accordingly.
//    FirebaseUser currentUser = mAuth.getCurrentUser();
//    updateUI(currentUser);

//    private void login(String email, String senha) {
//        auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    Query query;
//                    String id = Objects.requireNonNull(auth.getCurrentUser()).getUid();
//                    databaseReference.child("tipoperfil").addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
//                                TipoPerfil p = objSnapshot.getValue(TipoPerfil.class);
//                                if (p.getUsuarioId().equals(id)) {
//                                    sUsuarioId = p.getUsuarioId();
//                                    String tipoPerfilUsuario = p.getPerfil();
//
//                                    if (tipoPerfilUsuario.equals(TipoPerfil.PERFIL_EMPRESA)) {
//                                        Intent i = new Intent(Login.this, EmpresaMain.class);
//                                        startActivity(i);
//                                    } else if (tipoPerfilUsuario.equals(TipoPerfil.PERFIL_PESQUISA)) {
//                                        Intent i = new Intent(Login.this, PesquisaMainActivity.class);
//                                        startActivity(i);
//                                    } else {
//                                        alert("Tipo de Perfil desconhecido: " + tipoPerfilUsuario);
//                                    }
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//
//
//                    });
//
//                } else {
//                    alert("E-mail ou senha incorretos");
//                }
//            }
//        });
//    }


}
