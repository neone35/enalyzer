package com.github.neone35.enalyzer.data.network;

import com.github.neone35.enalyzer.data.models.remotejson.pubchemcid.PubchemCIDResponse;
import com.github.neone35.enalyzer.data.models.remotejson.pubchemdetails.PubchemDetailsResponse;
import com.github.neone35.enalyzer.data.models.remotejson.pubchemhazards.PubchemHazardsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PubchemEndpointInterface {

    @GET("/rest/pug/compound/name/{title}/cids/JSON")
    Call<PubchemCIDResponse> getPubchemCIDByAdditiveTitle(@Path("title") String additiveTitle);

    @GET("/rest/pug/compound/cid/{cID}/property/MolecularFormula,MolecularWeight,Complexity/JSON")
    Call<PubchemDetailsResponse> getPubchemDetailsByCID(@Path("cID") String cID);

    @GET("/rest/pug_view/data/compound/{cID}/JSON?heading=Hazards+Identification")
    Callback<PubchemHazardsResponse> getPubchemHazardsByCID(@Path("cID") String cID);
}
