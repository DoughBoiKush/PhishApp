package tcss450ajloria.uw.edu.phishapp;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.Serializable;

import me.pushy.sdk.Pushy;
import tcss450ajloria.uw.edu.phishapp.model.Credentials;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnLoginFragmentInteractionListener
        , RegisterFragment.OnRegisterFragmentInteractionListener{


   // LoginFragment lf;

    private boolean mLoadFromChatNotification = false;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Pushy.listen(this);
        setContentView(R.layout.activity_main);

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey("type")) {
                mLoadFromChatNotification = getIntent().getExtras().getString("type").equals("msg");
            }
        }

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
        intent.putExtra(getString(R.string.keys_intent_notification_msg), mLoadFromChatNotification);

        startActivity(intent);
        finish();
<<<<<<< HEAD:app/src/main/java/tcss450ajloria/uw/edu/phishapp/MainActivity.java


=======
>>>>>>> bbd0136b073379a155b34495dd655067c50ad96c:app/src/main/java/tcss450/uw/edu/phishapp/MainActivity.java
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
                .beginTransaction()
                .replace(R.id.frame_main_container, loginFragment)
                .addToBackStack(null);
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
