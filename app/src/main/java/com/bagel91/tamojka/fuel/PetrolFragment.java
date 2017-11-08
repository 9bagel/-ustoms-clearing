package com.bagel91.tamojka.fuel;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bagel91.tamojka.CallBack;
import com.bagel91.tamojka.GenericTextWatcher;
import com.bagel91.tamojka.R;
import com.bagel91.tamojka.currency.Currency;
import com.bagel91.tamojka.currency.CurrencyParser;

import java.util.concurrent.ExecutionException;

public class PetrolFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private Spinner spinAutoAge;
    private Spinner spinCurrency;

    private double multi = 1;

    private EditText edEngValue;
    private EditText edCostAuto;

    private TextView tvResult;
    private TextView tvCurrency;

    public PetrolFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fuel, container, false);

        view.findViewById(R.id.engLayout).setVisibility(View.GONE);

        String usdCur = getResources().getString(R.string.usd);
        String eurCur = getResources().getString(R.string.rub);
        String date = getResources().getString(R.string.date);

        tvCurrency = (TextView) view.findViewById(R.id.currency);

        if (Double.parseDouble(loadData("USD")) != 0)
            setTvCurrency();
        else
            tvCurrency.setText("USD/RUB = " + usdCur + '\n' + "EUR/RUB = " + eurCur + '\n' + date);

        view.findViewById(R.id.btnUpdate).setOnClickListener(this);

        ArrayAdapter<String> adapterAutoAge = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.listAge));
        adapterAutoAge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinAutoAge = (Spinner) view.findViewById(R.id.spinner1);
        spinAutoAge.setAdapter(adapterAutoAge);
        spinAutoAge.setOnItemSelectedListener(this);

        ArrayAdapter<String> adapterCurrency = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.currency));
        adapterCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinCurrency = (Spinner) view.findViewById(R.id.spinCurrency);
        spinCurrency.setAdapter(adapterCurrency);
        spinCurrency.setOnItemSelectedListener(this);

        edEngValue = (EditText) view.findViewById(R.id.edEngValue);
        edCostAuto = (EditText) view.findViewById(R.id.edAutoCost);
        tvResult = (TextView) view.findViewById(R.id.tvResult);

        edEngValue.addTextChangedListener(new GenericTextWatcher(edEngValue, mCallBack));

        edCostAuto.addTextChangedListener(new GenericTextWatcher(edCostAuto, mCallBack));

        return view;
    }

    private CallBack mCallBack = new CallBack() {
        @Override
        public void OnDataChanged() {
            tvResult.setText(String.valueOf((int) (getCostValue() * multi)) + spinCurrency.getSelectedItem());
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdate:
                Context context = getActivity().getApplicationContext();
                CurrencyParser parser = new CurrencyParser(context, getActivity());
                Currency currency = null;
                try {
                    currency = parser.execute(0).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                if (currency == null) {
                    Toast.makeText(context, context.getResources().getString(R.string.noInternet), Toast.LENGTH_LONG).show();
                }
                if (currency != null) {
                    saveData(currency);
                    tvCurrency.setText("USD = " + loadData("USD") + " RUB" + "\n" + "EUR = " + loadData("RUB") + " RUB" + "\n" + loadData("DATE"));
                }
                break;

        }
    }

    private int getCostValue() {

        int engValue = Integer.valueOf(edEngValue.getText().toString());
        int costAuto = Integer.valueOf(edCostAuto.getText().toString());
        int sum1 = 0;
        int sum2 = 0;

        switch (spinAutoAge.getSelectedItemPosition()) {

            case 0:
                if (costAuto <= 8500) {
                    sum1 = (int) (costAuto * 0.54);
                    sum2 = (int) (engValue * 2.5);
                }
                if (costAuto > 8500 & costAuto <= 16700) {
                    sum1 = (int) (costAuto * 0.48);
                    sum2 = (int) (engValue * 3.5);
                }
                if (costAuto > 16700 & costAuto <= 42300) {
                    sum1 = (int) (costAuto * 0.48);
                    sum2 = (int) (engValue * 5.5);
                }
                if (costAuto > 42300 & costAuto <= 84500) {
                    sum1 = (int) (costAuto * 0.48);
                    sum2 = (int) (engValue * 7.5);
                }
                if (costAuto > 84500 & costAuto <= 169000) {
                    sum1 = (int) (costAuto * 0.48);
                    sum2 = (engValue * 15);
                }
                if (costAuto > 169000) {
                    sum1 = (int) (costAuto * 0.48);
                    sum2 = (engValue * 20);
                }
                break;
            case 1:
                if (engValue <= 1000)
                    sum2 = (int) (engValue * 1.5);
                if (engValue > 1000 & engValue <= 1500)
                    sum2 = (int) (engValue * 1.7);
                if (engValue > 1500 & engValue <= 1800)
                    sum2 = (int) (engValue * 2.5);
                if (engValue > 1800 & engValue <= 2300)
                    sum2 = (int) (engValue * 2.7);
                if (engValue > 2300 & engValue <= 3000)
                    sum2 = (engValue * 3);
                if (engValue > 3000)
                    sum2 = (int) (engValue * 3.6);
                break;
            case 2:
                if (engValue <= 1000)
                    sum2 = (engValue * 3);
                if (engValue > 1000 & engValue <= 1500)
                    sum2 = (int) (engValue * 3.2);
                if (engValue > 1500 & engValue <= 1800)
                    sum2 = (int) (engValue * 3.5);
                if (engValue > 1800 & engValue <= 2300)
                    sum2 = (int) (engValue * 4.8);
                if (engValue > 2300 & engValue <= 3000)
                    sum2 = (engValue * 5);
                if (engValue > 3000)
                    sum2 = (int) (engValue * 5.7);
                break;
        }

        return sum1 > sum2 ? sum1 : sum2;
    }

    private String loadData(String text) {
        SharedPreferences sPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        return sPref.getString(text, "0");
    }

    private void saveData(Currency currency) {
        SharedPreferences sPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("USD", currency.getUsdCur());
        ed.putString("RUB", currency.getRubCur());
        ed.putString("DATE", currency.getDate());
        ed.apply();
    }

    void setTvCurrency() {
        tvCurrency.setText("USD = " + loadData("USD") + " RUB" + "\n" + "EUR = " + loadData("RUB") + " RUB" + "\n" + loadData("DATE"));
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;

        if (spinner.getId() == R.id.spinCurrency) {
            switch (position) {
                case 0:
                    multi = 1;
                    break;
                case 1:
                    multi = Double.parseDouble(loadData("USD"));

                    if (multi == 0) {
                        multi = Double.parseDouble(getResources().getString(R.string.usd));
                    } else setTvCurrency();
                    break;
                case 2:
                    multi = Double.parseDouble(loadData("RUB"));

                    if (multi == 0) {
                        multi = Double.parseDouble(getResources().getString(R.string.rub));
                    } else setTvCurrency();
                    break;
            }
        }
        mCallBack.OnDataChanged();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}