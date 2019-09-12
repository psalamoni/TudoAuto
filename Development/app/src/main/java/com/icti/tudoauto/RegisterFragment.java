package com.icti.tudoauto;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.icti.tudoauto.Model.Login;
import com.icti.tudoauto.Model.Register;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnCreateButtonFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RegisterFragment extends Fragment {

    private EditText name;
    private EditText email;
    private EditText password;
    private EditText ckpassword;
    private Button regback;
    private Button regcreate;
    private OnCreateButtonFragmentInteractionListener mCreateButtonListener;
    private OnChangeFragmentInteractionListener mChangeFragmentListener;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        this.startComps(view);
        eventClicks();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    private void eventClicks() {
        regback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mChangeFragmentListener != null) {

                    LoginFragment login = new LoginFragment();
                    mChangeFragmentListener.onChangeFragmentInteraction(login);
                }
            }
        });
        regcreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCreateButtonListener != null) {
                    Register register = new Register();
                    Login login = new Login();

                    register.setName(name.getText().toString());
                    login.setEmail(email.getText().toString());
                    login.setPassword(password.getText().toString());

                    mCreateButtonListener.onCreateButtonFragmentInteraction(login, register);
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCreateButtonFragmentInteractionListener) {
            mCreateButtonListener = (OnCreateButtonFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCreateButtonFragmentInteractionListener");
        }
        if (context instanceof OnChangeFragmentInteractionListener) {
            mChangeFragmentListener = (OnChangeFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnChangeFragmentInteractionListener");
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//
//            case R.id.btn_regcreate:
//                if (password.getText().toString() == ckpassword.getText().toString()) {
//                    Register register = new Register();
//                    Login login = new Login();
//
//                    register.setName(name.getText().toString());
//                    login.setEmail(email.getText().toString());
//                    login.setPassword(password.getText().toString());
//
//                    mListener.onCreateButtonFragmentInteraction(login,register);
//                    return true;
//
//                } else {
//                    String msg = "Senhas digitadas não conferem";
//                    //MainActivity.alert(msg);
//                }
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCreateButtonListener = null;
        mChangeFragmentListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnCreateButtonFragmentInteractionListener {
        // TODO: Update argument type and name
        void onCreateButtonFragmentInteraction(Login login, Register register);
    }

    public interface OnChangeFragmentInteractionListener {
        // TODO: Update argument type and name
        void onChangeFragmentInteraction(Fragment newFragment);
    }

    private void startComps(View view) {
        name = view.findViewById(R.id.reg_name);
        email = view.findViewById(R.id.reg_email);
        password = view.findViewById(R.id.reg_password);
        ckpassword = view.findViewById(R.id.reg_ckpassword);
        regcreate = (Button) view.findViewById(R.id.btn_regcreate);
        regback = (Button) view.findViewById(R.id.btn_regback);
    }


}
