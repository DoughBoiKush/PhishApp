package tcss450ajloria.uw.edu.phishapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuccessFragment extends Fragment {


    public SuccessFragment() {
        // Required empty public constructor
    }



    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            //int color = getArguments().getInt(getString(R.string.all_color_key));
            TextView tv = getActivity().findViewById(R.id.success_textView);
            tv.setText(getArguments().getString("info")); }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        System.exit(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_success, container, false);
    }

}
