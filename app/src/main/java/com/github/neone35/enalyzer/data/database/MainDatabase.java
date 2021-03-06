package com.github.neone35.enalyzer.data.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;

import com.github.neone35.enalyzer.data.models.room.Additive;
import com.github.neone35.enalyzer.data.models.room.CodeCategory;
import com.github.neone35.enalyzer.data.models.room.Hazard;
import com.github.neone35.enalyzer.data.models.room.ScanPhoto;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

// version number needs to be incremented if schema models change
@Database(entities = {Additive.class, ScanPhoto.class, CodeCategory.class, Hazard.class}, version = 1)
@TypeConverters({MainConverters.class})
public abstract class MainDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "mainDB";
    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static MainDatabase sInstance;

    public static MainDatabase getInstance(Context context) {
//        Logger.d("Getting the database");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        MainDatabase.class, MainDatabase.DATABASE_NAME).build();
                Logger.d("Made new database");
            }
        }
        return sInstance;
    }

    // The associated DAOs for the database
    public abstract AdditiveDao additiveDao();
    public abstract ScanPhotoDao scanPhotoDao();
    public abstract CodeCategoryDao codeCategoryDao();
    public abstract HazardDao hazardDao();


    public void insertInitialAdditives(ArrayList<Integer> codes, ArrayList<String> eCodes,
                                       ArrayList<String> wikiQCodes, ArrayList<String> categories) {
        ArrayList<Additive> additiveList = new ArrayList<>();
        for (int i = 0; i < codes.size(); i++) {
            int code = codes.get(i);
            String category = null;
            if (code >= 100 && code <= 199) {
                category = categories.get(0);
            } else if (code >= 200 && code <= 299) {
                category = categories.get(1);
            } else if (code >= 300 && code <= 399) {
                category = categories.get(2);
            } else if (code >= 400 && code <= 499) {
                category = categories.get(3);
            } else if (code >= 500 && code <= 599) {
                category = categories.get(4);
            } else if (code >= 600 && code <= 699) {
                category = categories.get(5);
            } else if (code >= 700 && code <= 799) {
                category = categories.get(6);
            } else if (code >= 900 && code <= 999) {
                category = categories.get(7);
            } else if (code >= 1000 && code <= 1599) {
                category = categories.get(8);
            }
            Additive additive = new Additive(eCodes.get(i), code, wikiQCodes.get(i),
                    null, 0, null, null, category,
                    null, null, null, null, null);
            additiveList.add(additive);
        }
        additiveDao().bulkInsert(additiveList);
    }

    public void insertCodeCategories(ArrayList<String> categories, ArrayList<String> categoryRanges,
                                     ArrayList<Integer> codes, ArrayList<String> eCodes) {
        ArrayList<CodeCategory> codeCategoryList = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++) {
            // "Colours"
            String category = categories.get(i);
            // "E100-E199"
            String categoryRange = categoryRanges.get(i);
            ArrayList<Integer> categoryCodes = new ArrayList<>();
            ArrayList<String> categoryEcodes = new ArrayList<>();
            for (int j = 0; j < eCodes.size(); j++) {
                // "E160a"
                String eCode = eCodes.get(j);
                // "160"
                int code = codes.get(j);
                // "Colours"
                Additive additive = additiveDao().getOneStaticByEcode(eCode);
                String additiveCategory = additive.getCategory();
                if (additiveCategory != null) {
                    // "Colours" == "Colours"
                    if (additiveCategory.equals(category) && eCode != null) {
                        categoryCodes.add(code);
                        categoryEcodes.add(eCode);
                    }
                } else {
                    Logger.d(additive.getEcode() + " has no category assigned");
                }
            }
            CodeCategory codeCategory = new CodeCategory(categoryRange, category, categoryCodes, categoryEcodes);
            // not all categories are filled with additives, so skip them
            if (!codeCategory.getECodes().isEmpty()) {
                codeCategoryList.add(codeCategory);
            }
        }
        codeCategoryDao().bulkInsert(codeCategoryList);
    }

    public void insertHazards(ArrayList<String> hazardCodeList,
                              ArrayList<String> hazardStatementList) {
        ArrayList<Hazard> hazardList = new ArrayList<>();
        for (int i = 0; i < hazardCodeList.size(); i++) {
            // "H200"
            String hazardCode = hazardCodeList.get(i);
            // "Unstable Explosive""
            String hazardStatement = hazardStatementList.get(i);
            Hazard hazard = new Hazard(hazardCode, hazardStatement);
            hazardList.add(hazard);
        }
        hazardDao().bulkInsert(hazardList);
    }

    public ArrayList<String> getCategories() {
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Colours");
        categories.add("Preservatives");
        categories.add("Antioxidants, acidity regulators");
        categories.add("Thickeners, stabilisers, emulsifiers");
        categories.add("Acidity regulators, anti-caking agents");
        categories.add("Flavour enhancers");
        categories.add("Antibiotics");
        categories.add("Glazing agents, gases & sweeteners");
        categories.add("Additional");
        return categories;
    }

    public ArrayList<String> getCategoryRanges() {
        ArrayList<String> categoryRanges = new ArrayList<>();
        categoryRanges.add("E100-E199");
        categoryRanges.add("E200-E299");
        categoryRanges.add("E300-E399");
        categoryRanges.add("E400-E499");
        categoryRanges.add("E500-E599");
        categoryRanges.add("E600-E699");
        categoryRanges.add("E700-E799");
        categoryRanges.add("E900-E999");
        categoryRanges.add("E1000-E1599");
        return categoryRanges;
    }

}
