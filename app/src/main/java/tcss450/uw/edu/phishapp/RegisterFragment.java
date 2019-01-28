package tcss450.uw.edu.phishapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import tcss450.uw.edu.phishapp.model.Credentials;
import tcss450.uw.edu.phishapp.utils.SendPostAsyncTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnRegisterFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    private OnRegisterFragmentInteractionListener mListener;
    public Credentials mCredentials;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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


        View v = inflater.inflate(R.layout.fragment_register, container, false);
        Button b = (Button) v.findViewById(R.id.register_button_register);
        b.setOnClickListener(this);
        // Inflate the layout for this fragment
        return v;
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
        if (context instanceof OnRegisterFragmentInteractionListener) {
            mListener = (OnRegisterFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        EditText username = (EditText)getView().findViewById(R.id.register_editText_username);
        EditText fname = (EditText)getView().findViewById(R.id.register_editText_fname);
        EditText lname = (EditText)getView().findViewById(R.id.register_editText_lname);
        EditText email = (EditText)getView().findViewById(R.id.register_editText_email);
        EditText password1 = (EditText)getView().findViewById(R.id.register_editText_pw1);
        EditText password2 = (EditText)getView().findViewById(R.id.register_editText_pw2);

        if(!validate(username, fname, lname, email,password1, password2)) {
            //Log.wtf("Login Act", "you did not pass the validation");
            return;
        }

        /*Credentials.Builder b = new Credentials.Builder(email.getText().toString()
                ,password1.getText().toString() );

        mListener.onRegisterSuccess(b.build());*/
        ///////////////////////////////////////////////////////////////
        Credentials credentials = new Credentials.Builder(
                email.getText().toString(),
                password1.getText().toString())
                .addFirstName(fname.getText().toString())
                .addLastName(lname.getText().toString())
                .addUsername(username.getText().toString())
                .build();

        //build the web service URL
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_register))
                .build();

        //build the JSONObject
        JSONObject msg = credentials.asJSONObject();
        mCredentials = credentials;

        //instantiate and execute the AsyncTask.
        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPreExecute(this::handleRegOnPre)
                .onPostExecute(this::handleRegOnPost)
                .onCancelled(this::handleErrorsInTask)
                .build()
                .execute();
    }

    private boolean validate(EditText username, EditText fname, EditText lname, EditText email, EditText password1, EditText password2) {
        boolean result = true;

        if(username.getText().toString().isEmpty()) {
            //Log.wtf("Login Act", "Email is empty");
            username.setError("Empty username");
            result = false;
        }
        if(fname.getText().toString().isEmpty()) {
            //Log.wtf("Login Act", "Password is empty");
            fname.setError("Empty first name");

            result = false;
        }

        if(lname.getText().toString().isEmpty()) {
            //Log.wtf("Login Act", "Password is empty");
            lname.setError("Empty last name");

            result = false;
        }

        if(email.getText().toString().isEmpty()) {
            //Log.wtf("Login Act", "Email is empty");
            email.setError("Empty email address");
            result = false;
        }
        if(password1.getText().toString().isEmpty()) {
            //Log.wtf("Login Act", "Password is empty");
            password1.setError("Empty password");

            result = false;
        }

        if(password2.getText().toString().isEmpty()) {
            //Log.wtf("Login Act", "Password is empty");
            password2.setError("Empty password");

            result = false;
        }

        if(password1.getText().toString().length() < 6) {
            //Log.wtf("Login Act", "Password is empty");
            password1.setError("At least 6 digits");

            result = false;
        }

        if(password2.getText().toString().length() < 6) {
            //Log.wtf("Login Act", "Password is empty");
            password2.setError("At least 6 digits");

            result = false;
        }

        if(!password2.getText().toString().equals(password1.getText().toString())) {
            //Log.wtf("Login Act", "Password is empty");
            password1.setError("Passwords don't match");
            password2.setError("Passwords don't match");

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

    /** Handle errors that may occur during the AsyncTask.
     * @param result the error message provide from the AsyncTask
     */
    private void handleErrorsInTask(String result) {
        Log.e("ASYNC_TASK_ERROR",  result);
    }

    /**
     * Handle the setup of the UI before the HTTP call to the webservice.
     */
    private void handleRegOnPre() {
        mListener.onWaitFragmentInteractionShow();
    }
    private void handleRegOnPost(String result) {
        try {
            JSONObject resultsJSON = new JSONObject(result);
            boolean success = resultsJSON.getBoolean(getString(R.string.keys_json_reg_success));
            if (success) {
                //Register was successful. Switch to the loadSuccessFragment.
                mListener.onRegisterSuccess(mCredentials);
                return;
            } else {
                //Registration was unsuccessful. Don’t switch fragments and inform the user
                ((TextView) getView().findViewById(R.id.register_editText_username))
                        .setError("Registration Unsuccessful");
            }
            mListener.onWaitFragmentInteractionHide();
        } catch (JSONException e) {
            //It appears that the web service did not return a JSON formatted
            //String or it did not have what we expected in it.
            Log.e("JSON_PARSE_ERROR",  result
                    + System.lineSeparator()
                    + e.getMessage());
            mListener.onWaitFragmentInteractionHide();
            ((TextView) getView().findViewById(R.id.register_editText_username))
                    .setError("Registration Unsuccessful");
        } }


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
    public interface OnRegisterFragmentInteractionListener extends WaitFragment.OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onRegisterSuccess(Credentials id);
    }
}
