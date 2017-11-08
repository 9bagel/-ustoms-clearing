package com.bagel91.tamojka.currency;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.bagel91.tamojka.InternetChecker;
import com.bagel91.tamojka.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrencyParser extends AsyncTask<Object, Currency, Currency> {

    private Context context;
    private Activity activity;

    public CurrencyParser(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public Currency parseCurrency() {

        String usdCur = null;
        String rubCur = null;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        InternetChecker checker = new InternetChecker(context);
        if (checker.isInternet()) {

            URL bank = null;
            try {
                bank = new URL("http://www.cbr.ru/currency_base/D_print.aspx?date_req=" + dateFormat.format(new Date()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            BufferedReader br = null;
            try {
                if (bank != null) {
                    br = new BufferedReader(new InputStreamReader(bank.openStream(), "UTF-8"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                if (br != null) {
                    while (!(usdCur = br.readLine()).contains("USD")) ;
                    br.readLine();
                    br.readLine();
                    usdCur = br.readLine();
                    usdCur = usdCur.substring(usdCur.indexOf('>') + 1, usdCur.indexOf('<', usdCur.indexOf('>') + 1)).replace(",", ".");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (br != null) {
                    while (!(rubCur = br.readLine()).contains("EUR")) ;
                    br.readLine();
                    br.readLine();
                    rubCur = br.readLine();
                    rubCur = rubCur.substring(rubCur.indexOf('>') + 1, rubCur.indexOf('<', rubCur.indexOf('>') + 1)).replace(",", ".");
                    //usdCur = String.valueOf(((double) ((int) (Double.parseDouble(rubCur) / Double.parseDouble(usdCur) * 10000))) / 10000);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return new Currency(usdCur, rubCur, dateFormat.format(new Date()));
        } else {
            return null;
        }
    }

    @Override
    protected Currency doInBackground(Object[] params) {
        return parseCurrency();
    }

    private ProgressDialog progressDialog;

    @Override
    protected void onPreExecute() {

        progressDialog = ProgressDialog.show(activity, "", activity.getResources().getString(R.string.getCurrencyMsg), true);
    }

    @Override
    protected void onPostExecute(Currency currency) {
        progressDialog.dismiss();
    }
}
