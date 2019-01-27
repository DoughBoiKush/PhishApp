package tcss450.uw.edu.phishapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BlogPostFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class BlogPostFragment extends Fragment {

    private String  url;

    private OnFragmentInteractionListener mListener;

    public BlogPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_blog_post, container, false);






        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView title = (TextView) getActivity().findViewById(R.id.post_title);
        TextView date = (TextView) getActivity().findViewById(R.id.post_publish_date);
        TextView teaser = (TextView) getActivity().findViewById(R.id.post_teaser);
        //TextView url = (TextView) getActivity().findViewById(R.id.post_url);
//
//        if (getArguments() != null) {
//            //int color = getArguments().getInt(getString(R.string.all_color_key));
//            TextView tv = getActivity().findViewById(R.id.success_textView);
//            tv.setText(getArguments().getString("info"));
//        }



        String msg = getArguments().getString("info");

        String [] list = msg.split("\\$ ");

//
//        Log.wtf("sdsdsd", list[0]);
//        Log.wtf("sdsdsd", list[1]);
//        Log.wtf("sdsdsd", list[2]);
//        Log.wtf("sdsdsd", list[3]);

        //String htmltext = mValues.get(position).getTeaser();
        Spanned sp = Html.fromHtml(list[2]);
        //holder.mContentView.setText(sp);


        title.setText(list[0]);
        date.setText(list[1]);
        teaser.setText(sp);
        url = list[3];
        //url.setText(list[3]);
        Button btn = (Button) getActivity().findViewById(R.id.full_post_btn);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                //String [] list = getArguments().getString("info").split("\\$");
                Log.wtf("a", url);
                i.setData(Uri.parse(url));
                getActivity().startActivity(i);

            }
        });




    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
