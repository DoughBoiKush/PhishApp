package tcss450.uw.edu.phishapp;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import tcss450.uw.edu.phishapp.model.Credentials;
import tcss450.uw.edu.phishapp.model.Credentials.Builder;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnLoginFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnLoginFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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

        View v = inflater.inflate(R.layout.fragment_login, container, false);

        Button b = (Button) v.findViewById(R.id.login_button_login);
        b.setOnClickListener(this);

        //add this Fragment Object as the OnClickListener
        b = (Button) v.findViewById(R.id.login_button_register);

        b.setOnClickListener(this);

        //Use a Lamda expression to add the OnClickListener
        //b.setOnClickListener(view -> mListener.onRegisterClicked());
        //b = (Button) v.findViewById(R.id.button_first_blue);

        //Use a method reference to add the OnClickListener
        //b.setOnClickListener(this::setBlue);

        EditText email = (EditText)v.findViewById(R.id.login_editText_email);
        EditText pw = (EditText)v.findViewById(R.id.login_editText_password);


        return v;



        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_login, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //updateInfo();
        if (context instanceof OnLoginFragmentInteractionListener) {
            mListener = (OnLoginFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            //int color = getArguments().getInt(getString(R.string.all_color_key));

            updateInfo(getArguments().getString("info")); }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

        //Log.wtf("Main ACTIVITY", "inside onclick");
        EditText email = (EditText)getView().findViewById(R.id.login_editText_email);
        EditText password = (EditText)getView().findViewById(R.id.login_editText_password);

        if((v.getId() == R.id.login_button_login) && !validate(email,password)) {
            //Log.wtf("Login Act", "you did not pass the validation");
            return;
        }






        //Credentials current;
        Builder b = new Builder(email.getText().toString()
        ,password.getText().toString() );




        if (mListener != null) {
            switch (v.getId()) {
                case R.id.login_button_login: mListener.onLoginSuccess(b.build(), "hah");
                    break;
                case R.id.login_button_register: mListener.onRegisterClicked();
                    break;
                default:
                    Log.wtf("", "Didn't expect to see me...");
            }
        }
    }


    public void updateInfo(String s){
        EditText email = (EditText)getView().findViewById(R.id.login_editText_email);
        EditText pw = (EditText)getView().findViewById(R.id.login_editText_password);

        String [] list = s.split(", ");

        email.setText(list[0]);
        pw.setText(list[1]);

        //Log.wtf("Login ", "inside");
    }

    private boolean validate(EditText email, EditText password) {
        boolean result = true;
        if(email.getText().toString().isEmpty()) {
            //Log.wtf("Login Act", "Email is empty");
            email.setError("Empty email address");
            result = false;
        }
        if(password.getText().toString().isEmpty()) {
            //Log.wtf("Login Act", "Password is empty");
            password.setError("Empty password");

            result = false;
        }
        String emailStr = email.getText().toString();

        int counter = 0;

        for (int i=0; i<emailStr.length(); i++ ) {
            if(emailStr.charAt(i) == '@') {
                counter++;
            }
        }

        if(counter != 1) {
            //Log.wtf("Login Act", "Email address is not valid");
            email.setError("Not valid email address");
            result = false;
        }


        return result;
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
    public interface OnLoginFragmentInteractionListener {

        void onLoginSuccess(Credentials id, String jwt);

        void onRegisterClicked();
    }
}
