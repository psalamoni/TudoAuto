package com.icti.tudoauto;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.icti.tudoauto.Model.Login;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnChangeFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends Fragment {

    private OnChangeFragmentInteractionListener mChangeFragmentListener;
    private OnLoginInteractionListener mLoginListener;
    private EditText logemail;
    private EditText logpassword;
    private Button loglogin;
    private ImageButton logfb;
    private ImageButton logggl;
    private ImageButton logcreateuser;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        startComps(view);
        eventClicks();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event

    private void eventClicks() {
        logcreateuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mChangeFragmentListener != null) {

                    RegisterFragment registerFragment = new RegisterFragment();

                    mChangeFragmentListener.onChangeFragmentInteraction(registerFragment);

                }
            }
        });
        loglogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLoginListener != null) {

                    Login login = new Login();

                    login.setEmail(logemail.getText().toString());
                    login.setPassword(logpassword.getText().toString());

                    mLoginListener.onLoginInteraction(login);

                }
            }
        });
    }
//    public void onButtonPressed() {
//        if (mRegisterPageListener != null) {
//            mRegisterPageListener.onRegisterPageFragmentInteraction();
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnChangeFragmentInteractionListener) {
            mChangeFragmentListener = (OnChangeFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnChangeFragmentInteractionListener");
        }
        if (context instanceof OnLoginInteractionListener) {
            mLoginListener = (OnLoginInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLoginInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mChangeFragmentListener = null;
        mLoginListener = null;
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

    private void startComps(View view) {
        logemail = view.findViewById(R.id.log_email);
        logpassword = view.findViewById(R.id.log_password);
        loglogin = (Button) view.findViewById(R.id.log_loginbutton);
        logfb = (ImageButton) view.findViewById(R.id.log_fbbutton);
        logggl = (ImageButton) view.findViewById(R.id.log_gglButton);
        logcreateuser = (ImageButton) view.findViewById(R.id.log_createuser);
    }


    public interface OnChangeFragmentInteractionListener {
        // TODO: Update argument type and name
        void onChangeFragmentInteraction(Fragment newFragment);
    }
    public interface OnLoginInteractionListener {
        // TODO: Update argument type and name
        void onLoginInteraction(Login login);
    }
}
