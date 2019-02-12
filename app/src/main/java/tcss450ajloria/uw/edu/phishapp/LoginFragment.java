package tcss450ajloria.uw.edu.phishapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
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

import me.pushy.sdk.Pushy;
import tcss450ajloria.uw.edu.phishapp.model.Credentials;
import tcss450ajloria.uw.edu.phishapp.model.Credentials.Builder;
import tcss450ajloria.uw.edu.phishapp.utils.SendPostAsyncTask;


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
    private String mJwt;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnLoginFragmentInteractionListener mListener;
    public Credentials mCredentials;

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

        /*email.setText("test@test");
        pw.setText("test123");*/

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
        SharedPreferences prefs =
                getActivity().getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        //retrieve the stored credentials from SharedPrefs
        if (prefs.contains(getString(R.string.keys_prefs_email)) &&
                prefs.contains(getString(R.string.keys_prefs_password))) {
            final String email = prefs.getString(getString(R.string.keys_prefs_email), "");
        final String password = prefs.getString(getString(R.string.keys_prefs_password), "");
        //Load the two login EditTexts with the credentials found in SharedPrefs
        EditText emailEdit = getActivity().findViewById(R.id.login_editText_email); //edit_login_email
        emailEdit.setText(email);
        EditText passwordEdit = getActivity().findViewById(R.id.login_editText_password); //edit_login_password
        passwordEdit.setText(password);

        doLogin(new Credentials.Builder(
                emailEdit.getText().toString(),
                passwordEdit.getText().toString())
                .build());
        }
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
                case R.id.login_button_login:
                    //mListener.onLoginSuccess(b.build(), "this is the jwt");
                    validate(email, password);
                    break;
                case R.id.login_button_register:
                    mListener.onRegisterClicked();
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

    private void doLogin(Credentials credentials) {
        //build the web service URL
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_login)).build();

        //build the JSONObject
        JSONObject msg =credentials.asJSONObject();
        mCredentials = credentials;
        Log.d("JSON Credentials", msg.toString());

        //instantiate and execute the AsyncTask.
        // Feel free to add a handler for onPreExecution so that a progress bar
        // is displayed or maybe disable buttons.
        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPreExecute(this::handleLoginOnPre)
                .onPostExecute(this::handleLoginOnPost)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();

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

        if (result) {
            doLogin(new Credentials.Builder(email.getText().toString(),password.getText().toString()).build());
            /*Log.wtf("ACTACT","I wanna fix this damn it!!!!!!!!");

            Credentials credentials = new Credentials.Builder(
                    email.getText().toString(),
                    password.getText().toString())
                    .build();
            //build the web service URL
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url)) .appendPath(getString(R.string.ep_login)) .build();
            //build the JSONObject
            JSONObject msg = credentials.asJSONObject();
            mCredentials = credentials;
            //instantiate and execute the AsyncTask.
            new SendPostAsyncTask.Builder(uri.toString(), msg)
                    .onPreExecute(this::handleLoginOnPre)
                    .onPostExecute(this::handleLoginOnPost)
                    .onCancelled(this::handleErrorsInTask)
                    .build().execute();*/
        }



        return result;
    }

    /**
     * Handle errors that may occur during the AsyncTask.
     * @param result the error message provide from the AsyncTask */
    private void handleErrorsInTask(String result) {
        Log.e("ASYNC_TASK_ERROR", result);
    }

    /**
     * Handle the setup of the UI before the HTTP call to the webservice.
     */
    private void handleLoginOnPre() {
        mListener.onWaitFragmentInteractionShow();
    }

    private void handleLoginOnPost(String result) {
        try {
            JSONObject resultsJSON = new JSONObject(result);
            boolean success =
                    resultsJSON.getBoolean(
                            getString(R.string.keys_json_login_success));
            if (success) {
                //Login was successful. Switch to the loadSuccessFragment.
                mJwt = resultsJSON.getString(getString(R.string.keys_json_login_jwt));
                new RegisterForPushNotificationsAsync().execute();
                return;
            } else {
                //Login was unsuccessful. Don’t switch fragments and
                // inform the user
                System.out.println("login was unsuccessful");
                ((TextView) getView().findViewById(R.id.login_editText_email))
                        .setError("Login Unsuccessful");
            }
            mListener.onWaitFragmentInteractionHide();
        } catch (JSONException e) {
            //It appears that the web service did not return a JSON formatted
            //String or it did not have what we expected in it.
            Log.e("JSON_PARSE_ERROR",  result
                    + System.lineSeparator()
                    + e.getMessage());
            mListener.onWaitFragmentInteractionHide();
            ((TextView) getView().findViewById(R.id.login_editText_email))
                    .setError("Login Unsuccessful");
        }
    }

    private void handlePushyTokenOnPost(String result) {
        try {
            Log.d("JSON result", result);
            JSONObject resultsJSON = new JSONObject(result);
            boolean success = resultsJSON.getBoolean("success");

            if (success) {
                saveCredentials(mCredentials);
                mListener.onLoginSuccess(mCredentials, mJwt);
                return;
            } else {
                //Saving the token wrong. Don’t switch fragments and inform the user
                ((TextView) getView().findViewById(R.id.login_editText_email))
                        .setError("Login Unsuccessful");
            }
            mListener.onWaitFragmentInteractionHide();
        } catch (JSONException e) {
            //It appears that the web service didn’t return a JSON formatted String
            // or it didn’t have what we expected in it.
            Log.e("JSON_PARSE_ERROR", result
                    + System.lineSeparator()
                    + e.getMessage());
            mListener.onWaitFragmentInteractionHide();
            ((TextView) getView().findViewById(R.id.login_editText_email))
                    .setError("Login Unsuccessful");
        }
    }

    private void saveCredentials(final Credentials credentials) {
        SharedPreferences prefs =getActivity().getSharedPreferences(getString(R.string.keys_shared_prefs),Context.MODE_PRIVATE);
                //Store the credentials in SharedPrefs
        prefs.edit().putString(getString(R.string.keys_prefs_email), credentials.getEmail()).apply();
        prefs.edit().putString(getString(R.string.keys_prefs_password), credentials.getPassword()).apply();
    }



    private class RegisterForPushNotificationsAsync extends AsyncTask<Void, String, String> {
                protected String doInBackground(Void... params) {
                    String deviceToken = "";
                    try {
                        // Assign a unique token to this device
                        deviceToken = Pushy.register(getActivity().getApplicationContext());

                        //subscribe to a topic (this is a Blocking call)
                        Pushy.subscribe("all", getActivity().getApplicationContext());
                    }
                    catch (Exception exc) {
                        cancel(true);
                        // Return exc to onCancelled
                        return exc.getMessage();
                    }
                    // Success
                    return deviceToken;
                }
                @Override
                protected void onCancelled(String errorMsg) {
                    super.onCancelled(errorMsg);
                    Log.d("PhishApp", "Error getting Pushy Token: " + errorMsg);
                }

                @Override
                protected void onPostExecute(String deviceToken) {
                    // Log it for debugging purposes
                    Log.d("PhishApp", "Pushy device token: " + deviceToken);

                    //build the web service URL
                    Uri uri = new Uri.Builder()
                            .scheme("https")
                            .appendPath(getString(R.string.ep_base_url))
                            .appendPath(getString(R.string.ep_pushy))
                            .appendPath(getString(R.string.ep_token))
                            .build();
                    //build the JSONObject
                    JSONObject msg = mCredentials.asJSONObject();

                    try {
                        msg.put("token", deviceToken);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //instantiate and execute the AsyncTask.
                    new SendPostAsyncTask.Builder(uri.toString(), msg)
                            .onPostExecute(LoginFragment.this::handlePushyTokenOnPost)
                            .onCancelled(LoginFragment.this::handleErrorsInTask)
                            .addHeaderField("authorization", mJwt)
                            .build().execute();

                    /*saveCredentials(mCredentials);    mListener.onLoginSuccess(mCredentials, mJwt);*/
                }
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
    public interface OnLoginFragmentInteractionListener extends WaitFragment.OnFragmentInteractionListener{
        void onLoginSuccess(Credentials id, String jwt);

        void onRegisterClicked();
    }
}

