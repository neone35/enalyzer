package com.github.neone35.enalyzer.ui.additive;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;

import com.github.neone35.enalyzer.AppExecutors;
import com.github.neone35.enalyzer.R;
import com.github.neone35.enalyzer.data.database.MainDatabase;
import com.github.neone35.enalyzer.data.models.room.Additive;
import com.github.neone35.enalyzer.data.models.room.ScanPhoto;
import com.github.neone35.enalyzer.ui.main.MainActivity;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.ButterKnife;

public class AdditiveActivity extends AppCompatActivity implements
        AdditiveFragment.OnAdditiveFragmentListener {

    private FragmentManager mFragmentManager;
    public static final String KEY_PREVIOUS = "previous_click";
    public static final String KEY_NEXT = "next_click";
    private MainDatabase mMainDB;
    private final AppExecutors mExecutors = AppExecutors.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additive);
        ButterKnife.bind(this);

        mFragmentManager = getSupportFragmentManager();
        mMainDB = MainDatabase.getInstance(this);

        setupActionBar();

        // check if intent bundle received successfully and setup view
        Bundle mainExtrasBundle = getIntent().getExtras();
        if (mainExtrasBundle != null) {
            String selectedEcode = mainExtrasBundle.getString(MainActivity.KEY_SELECTED_ECODE);
            String tabSource = mainExtrasBundle.getString(MainActivity.KEY_TAB_SOURCE);
            int scanOrCodeID = mainExtrasBundle.getInt(MainActivity.KEY_SCAN_CODE_ID);
            String photoTransitionName = mainExtrasBundle.getString(MainActivity.KEY_PHOTO_TRANSITION_VIEW);
            String ecodeTransitionName = mainExtrasBundle.getString(MainActivity.KEY_ECODE_TRANSITION_VIEW);
            // only create fragment if there was no configuration change
            if (savedInstanceState == null) {
                AdditiveFragment additiveFragment =
                        AdditiveFragment.newInstance(selectedEcode, tabSource, scanOrCodeID, photoTransitionName, ecodeTransitionName);
                mFragmentManager.beginTransaction()
                        .add(R.id.frag_additive, additiveFragment)
                        .commit();
            }
        }
    }

    public void setupActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    public void switchAdditive(String nextEcode, String tabSource, int photoOrCategoryID) {
        AdditiveFragment stepsListFragment = AdditiveFragment.newInstance(nextEcode, tabSource, photoOrCategoryID, null, null);
        mFragmentManager.beginTransaction()
                .replace(R.id.frag_additive, stepsListFragment)
                .commit();

    }

    // performs additive fragment switch on next or previous button click
    @Override
    public void onAdditiveFragmentInteraction(Additive currentAdditive, int photoOrCategoryID, String tabSource, String whichBtn) {
        Logger.d("Additive switch button is clicked");
        String currentEcode = currentAdditive.getEcode();

        if (tabSource.equals(MainActivity.SCANS_DETAIL)) {
            mExecutors.diskIO().execute(() -> {
                ScanPhoto currentScanPhoto = mMainDB.scanPhotoDao().getOneStaticById(photoOrCategoryID);
                List<String> scanPhotoEcodes = currentScanPhoto.getECodes();
                int currentEcodesNum = scanPhotoEcodes.size();
                for (int i = 0; i < currentEcodesNum; i++) {
                    // determine which ecode from current scan photo is selected
                    if (currentEcode.equals(scanPhotoEcodes.get(i))) {
                        runOnUiThread(() -> {
                            String nextEcode = null;
                            // calc next eCode depending on button click
                            if (whichBtn.equals(KEY_PREVIOUS)) {
                                Button prevBtn = findViewById(R.id.btn_previous_additive);
                                nextEcode = prevBtn.getText().toString();
                            } else if (whichBtn.equals(KEY_NEXT)) {
                                Button nextBtn = findViewById(R.id.btn_next_additive);
                                nextEcode = nextBtn.getText().toString();
                            }
                            // if next step exists, switch step
                            if (nextEcode != null)
                                switchAdditive(nextEcode, tabSource, photoOrCategoryID);
                        });
                    }
                }
            });
        }

//        } else if (tabSource.equals(MainActivity.CODES_DETAIL)) {
//            List<String> codeCategoryEcodes = mMainDB.codeCategoryDao().getCategoryEcodesStaticById(photoOrCategoryID);
//            int currentEcodesNum = codeCategoryEcodes.size();
//            for (int i = 0; i < currentEcodesNum; i++) {
//                // determine which ecode from current scan photo is selected
//                if (currentEcode.equals(codeCategoryEcodes.get(i))) {
//                    // calc next eCode depending on button click
//                    if (whichBtn.equals(KEY_PREVIOUS)) {
//                        if (i > 0)
//                            nextEcode = codeCategoryEcodes.get(i - 1);
//                    } else if (whichBtn.equals(KEY_NEXT)) {
//                        if (i < currentEcodesNum)
//                            nextEcode = codeCategoryEcodes.get(i + 1);
//                    }
//                }
//            }
//        }
    }
}
