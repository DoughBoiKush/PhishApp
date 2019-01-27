package tcss450.uw.edu.phishapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import tcss450.uw.edu.phishapp.model.Credentials;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnLoginFragmentInteractionListener
        , RegisterFragment.OnRegisterFragmentInteractionListener{


   // LoginFragment lf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            if (findViewById(R.id.frame_main_container) != null) {
                //lf = new LoginFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frame_main_container, new LoginFragment())
                        .commit();
            } }

    }




    @Override
    public void onLoginSuccess(Credentials id, String jwt) {
        //Log.wtf("MainACTIVITY", "inside login sucess");
//        HomeActivity success;
//        success= new HomeActivity();

        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.putExtra("info",id.getEmail().toString() );
        intent.putExtra(getString(R.string.keys_intent_jwt), jwt);

        startActivity(intent);
//
//        Bundle args = new Bundle();
//        args.putSerializable("info", id.getEmail().toString());
//        successFragment.setArguments(args);
//        FragmentTransaction transaction = getSupportFragmentManager()
//                .beginTransaction() .replace(R.id.frame_main_container, successFragment) .addToBackStack(null);
//        // Commit the transaction
//        transaction.commit();
        //successFragment.updateContent("u got it!");
        //}
    }

    @Override
    public void onRegisterClicked() {

        RegisterFragment registerFragment;
        registerFragment = new RegisterFragment();
//
//        Bundle args = new Bundle();
//        args.putSerializable("default", "default");
//        registerFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction() .replace(R.id.frame_main_container, registerFragment) .addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }


    @Override
    public void onRegisterSuccess(Credentials id) {

        //Log.wtf("MainACTIVITY", id.getEmail());
        //Log.wtf("MainACTIVITY", id.getPassword());


        LoginFragment loginFragment;
        loginFragment = new LoginFragment();

        Bundle args = new Bundle();
        args.putSerializable("info", id.getEmail() + ", " + id.getPassword());
        loginFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction() .replace(R.id.frame_main_container, loginFragment) .addToBackStack(null);
        // Commit the transaction
        transaction.commit();


//        LoginFragment lf = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.login_fragment);
//        lf.updateInfo("ahhah","hahh");
//
//
//
//        View theV = findViewById(R.id.frame_main_container);
//
//        theV.findViewById(R.id.login_editText_password);
//
//        EditText myEmail = (EditText)theV.findViewById(R.id.login_editText_email);
//        EditText myPw = (EditText)theV.findViewById(R.id.login_editText_password);
//
//        //LoginFragment lf = getSupportFragmentManager().getBackStackEntryAt;
//
//        if(myEmail ==null) {
//            Log.wtf("MainACTIVITY", "null email");
//        }


//
//        if (lf ==null) {
//            Log.wtf("MainACTIVITY", "lf null");
//        }


        //lf.updateInfo(id.getEmail().toString(),id.getPassword().toString());
    }

    @Override
    public void onWaitFragmentInteractionShow() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame_main_container, new WaitFragment(), "WAIT")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onWaitFragmentInteractionHide() {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(getSupportFragmentManager().findFragmentByTag("WAIT"))
                .commit();
    }
}
