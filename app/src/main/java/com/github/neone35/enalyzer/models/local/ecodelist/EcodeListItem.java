package com.github.neone35.enalyzer.models.local.ecodelist;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Generated("com.robohorse.robopojogenerator")
public class EcodeListItem {

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private String id;

    @SerializedName("url")
    private String url;

    @SerializedName("products")
    private int products;

    @SerializedName("sameAs")
    private List<String> sameAs;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setProducts(int products) {
        this.products = products;
    }

    public int getProducts() {
        return products;
    }

    public void setSameAs(List<String> sameAs) {
        this.sameAs = sameAs;
    }

    public List<String> getSameAs() {
        return sameAs;
    }

    @Override
    public String toString() {
        return
                "EcodeListItem{" +
                        "name = '" + name + '\'' +
                        ",id = '" + id + '\'' +
                        ",url = '" + url + '\'' +
                        ",products = '" + products + '\'' +
                        ",sameAs = '" + sameAs + '\'' +
                        "}";
    }
}