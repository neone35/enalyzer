package com.github.neone35.enalyzer.ui.additive;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.neone35.enalyzer.HelpUtils;
import com.github.neone35.enalyzer.InjectorUtils;
import com.github.neone35.enalyzer.R;
import com.github.neone35.enalyzer.ui.main.MainActivity;
import com.google.common.base.Joiner;
import com.orhanobut.logger.Logger;

import java.util.Objects;

import at.blogc.android.views.ExpandableTextView;
import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

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
    @BindView(R.id.tv_additive_ecode)
    TextView tvAdditiveEcode;
    @BindView(R.id.tv_additive_known)
    TextView tvAdditiveKnown;
    @BindView(R.id.etv_additive_about)
    ExpandableTextView etvAdditiveAbout;
    @BindView(R.id.btn_previous_additive)
    Button btnPreviousAdditive;
    @BindView(R.id.btn_next_additive)
    Button btnNextAdditive;
    @BindView(R.id.nsv_additive)
    NestedScrollView nsvAdditive;
    @BindView(R.id.tv_about_read_more)
    TextView tvAboutReadMore;
    @BindView(R.id.pb_about_more)
    ProgressBar pbAboutMore;
    @BindView(R.id.fl_buttons_holder)
    ConstraintLayout flButtonsHolder;
    @BindView(R.id.fl_etv_about_holder)
    FrameLayout flEtvAboutHolder;

    @BindInt(R.integer.read_more_expand_duration)
    int mReadMoreExpandDuration;

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
            tvAdditiveEcode.setTransitionName(mEcodeTransitionName);
        }

        // setup views
        scrollNestedScrollViewToTop(nsvAdditive);
        setupAdditiveSwitchButtons(null, btnPreviousAdditive, btnNextAdditive);
        setupExpandableTextView(etvAdditiveAbout, flEtvAboutHolder, tvAboutReadMore, pbAboutMore);

        // bind data
        // Get repository instance (start observing MutableLiveData trigger)
        AdditiveVMF factory =
                InjectorUtils.provideAdditiveVMF(Objects.requireNonNull(this.getContext()), mSelectedEcode);
        // Tie fragment & ViewModel together
        AdditiveVM viewModel = ViewModelProviders.of(this, factory).get(AdditiveVM.class);
        // Trigger LiveData notification on fragment creation & observe change in DB calling DAO
        viewModel.getOneAdditive().observe(this, additive -> {
            if (additive != null) {
                tvAdditiveEcode.setText(additive.getEcode());
                // load image
                if (additive.getImageURL() != null) {
                    loadImageUrlInto(ivAdditivePhoto, additive.getImageURL());
                }
                // load other views
                if (additive.getKnownAs() != null) {
                    String knownAsJoined = Joiner.on(", ").join(additive.getKnownAs());
                    tvAdditiveKnown.setText(knownAsJoined);
                }
                if (additive.getDescription() != null) {
                    etvAdditiveAbout.setText(additive.getDescription());
                }
            }
        });

        return rootView;
    }

    private void loadImageUrlInto(ImageView iv, String imgUrl) {
        if (imgUrl != null) {
            if (HelpUtils.imageTypeSupported(imgUrl)) {
                try {
                    Glide.with(iv)
                            .load(imgUrl)
                            .apply(fitCenterTransform())
                            .into(iv);
                } catch (Exception e) {
                    Logger.d(e.getMessage());
                }
            }
        }
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

    private void setupExpandableTextView(ExpandableTextView etv, FrameLayout flReadMoreHolder,
                                         TextView tvReadMore, ProgressBar pbExpand) {
        // run after layout pass (to get correct line count)
        etv.post(() -> {
            int etvLineCount = etv.getLineCount();
            int maxEtvLines = etv.getMaxLines();
            if (etvLineCount > maxEtvLines) {
                // setup expand animation
                etv.setExpandInterpolator(new OvershootInterpolator());
                // run expansion on 'read more' layout click
                flReadMoreHolder.setOnClickListener(v -> etv.expand());
                etv.addOnExpandListener(new ExpandableTextView.OnExpandListener() {
                    @Override
                    public void onExpand(@NonNull ExpandableTextView view) {
                        // show loading indicator instead of 'read more' text
                        tvReadMore.setVisibility(View.INVISIBLE);
                        pbExpand.setVisibility(View.VISIBLE);
                        // check if expanded (after expand duration) and hide loading layout
                        final Handler handler = new Handler();
                        handler.postDelayed(() -> {
                            if (etv.isExpanded()) {
                                flReadMoreHolder.setVisibility(View.GONE);
                            }
                        }, mReadMoreExpandDuration + 50);
                    }

                    @Override
                    public void onCollapse(@NonNull ExpandableTextView view) {

                    }
                });
            } else {
                flReadMoreHolder.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
