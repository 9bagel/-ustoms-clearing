package com.bagel91.tamojka.currency;

/**
 * Created by Denis on 8/19/2014.
 */
public class Currency {
    private String usdCur;
    private String rubCur;
    private String date;

    public Currency(String usdCur, String rubCur, String date) {
        this.usdCur = usdCur;
        this.rubCur = rubCur;
        this.date = date;
    }

    public String getUsdCur() {
        return usdCur;
    }

    public String getRubCur() {
        return rubCur;
    }

    public String getDate() {
        return date;
    }
}
