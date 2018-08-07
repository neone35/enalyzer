package com.github.neone35.enalyzer.ui.additive;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.neone35.enalyzer.R;
import com.github.neone35.enalyzer.ui.main.MainActivity;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Activities that contain this fragment must implement the
 * {@link OnAdditiveFragmentListener} interface
 * to handle interaction events.
 * Use the {@link AdditiveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdditiveFragment extends Fragment {

    @BindView(R.id.iv_additive_photo)
    ImageView ivAdditivePhoto;
    @BindView(R.id.tv_additive_code)
    TextView tvAdditiveCode;
    @BindView(R.id.tv_additive_known_label)
    TextView tvAdditiveKnownLabel;
    @BindView(R.id.tv_additive_known)
    TextView tvAdditiveKnown;
    @BindView(R.id.tv_additive_about_label)
    TextView tvAdditiveAboutLabel;
    @BindView(R.id.tv_additive_about)
    TextView tvAdditiveAbout;
    @BindView(R.id.btn_previous_additive)
    Button btnPreviousAdditive;
    @BindView(R.id.btn_next_additive)
    Button btnNextAdditive;
    @BindView(R.id.nsv_additive)
    NestedScrollView nsvAdditive;

    private String mSelectedEcode;
    private String mTabSource;
    private String mPhotoTransitionName;
    private String mEcodeTransitionName;
    private OnAdditiveFragmentListener mListener;

    public AdditiveFragment() {
    }

    public static AdditiveFragment newInstance(String eCode, String tabSource,
                                               String photoTransitionName, String ecodeTransitionName) {
        AdditiveFragment fragment = new AdditiveFragment();
        Bundle args = new Bundle();
        args.putString(MainActivity.KEY_SELECTED_ECODE, eCode);
        args.putString(MainActivity.KEY_TAB_SOURCE, tabSource);
        args.putString(MainActivity.KEY_PHOTO_TRANSITION_VIEW, photoTransitionName);
        args.putString(MainActivity.KEY_ECODE_TRANSITION_VIEW, ecodeTransitionName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSelectedEcode = getArguments().getString(MainActivity.KEY_SELECTED_ECODE);
            mTabSource = getArguments().getString(MainActivity.KEY_TAB_SOURCE);
            mPhotoTransitionName = getArguments().getString(MainActivity.KEY_PHOTO_TRANSITION_VIEW);
            mEcodeTransitionName = getArguments().getString(MainActivity.KEY_ECODE_TRANSITION_VIEW);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_additive, container, false);
        ButterKnife.bind(this, rootView);
        // set transition names received from recyclerView adapter
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ivAdditivePhoto.setTransitionName(mPhotoTransitionName);
            tvAdditiveCode.setTransitionName(mEcodeTransitionName);
        }

        scrollNestedScrollViewToTop(nsvAdditive);
        setupAdditiveSwitchButtons(null, btnPreviousAdditive, btnNextAdditive);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAdditiveFragmentListener) {
            mListener = (OnAdditiveFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAdditiveFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // scroll entire NestedScrollView to top after load (default - loaded part top (recylerView))
    private void scrollNestedScrollViewToTop(NestedScrollView nsv) {
        final Handler handler = new Handler();
        handler.postDelayed(() ->
                        nsv.scrollTo(0, 0),
                500);
    }

    private void setupAdditiveSwitchButtons(String replaceWithData, Button btnPreviousAdditive, Button btnNextAdditive) {
        int eCodeID = 0;
        // If source is 'scans' tab, switch between scanDetail items
        if (mTabSource.equals(MainActivity.SCANS_DETAIL)) {
            // TODO: Add 'ScanDetail' additive switch logic
            btnPreviousAdditive.setOnClickListener(v -> {
                if (null != mListener) {
                    mListener.onAdditiveFragmentInteraction(replaceWithData, eCodeID, AdditiveActivity.KEY_PREVIOUS);
                }
            });
            btnNextAdditive.setOnClickListener(v -> {
                if (null != mListener) {
                    mListener.onAdditiveFragmentInteraction(replaceWithData, eCodeID, AdditiveActivity.KEY_NEXT);
                }
            });
        } else if (mTabSource.equals(MainActivity.CODES_DETAIL)) {
            // TODO: Add 'CodeDetail' additive switch logic
            btnPreviousAdditive.setOnClickListener(v -> {
                if (null != mListener) {
                    mListener.onAdditiveFragmentInteraction(replaceWithData, eCodeID, AdditiveActivity.KEY_PREVIOUS);
                }
            });
            btnNextAdditive.setOnClickListener(v -> {
                if (null != mListener) {
                    mListener.onAdditiveFragmentInteraction(replaceWithData, eCodeID, AdditiveActivity.KEY_NEXT);
                }
            });
        }
        // control button visibility
//            int firstStepID = oneRecipe.getSteps().get(0).getId();
//            int lastStepID = oneRecipe.getSteps().size() - 1;
//            if (mStepID == lastStepID) {
//                btnNextAdditive.setVisibility(View.INVISIBLE);
//            } else if (mStepID == firstStepID) {
//                btnPreviousAdditive.setVisibility(View.INVISIBLE);
//            }
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
    public interface OnAdditiveFragmentListener {
        void onAdditiveFragmentInteraction(String replaceWithData, int currentAdditiveID, String whichBtn);
    }
}
