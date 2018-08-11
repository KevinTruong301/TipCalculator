package com.truong.kevin.tipcalculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
//Round with a trailing zero, add more function in spinner
public class MainActivity extends AppCompatActivity implements TextView.OnEditorActionListener, View.OnClickListener {

    private TextView tip;
    private TextView total;
    private TextView percent;
    private TextView splitTotal;
    private Button addPercent;
    private Button subPercent;
    private EditText bill;
    private Spinner split;

    int splitNum;

    double splitTotalNum;
    double tipNum;
    double billNum;
    double percentNum;
    double totalNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tip = (TextView) findViewById(R.id.tip);
        total = (TextView) findViewById(R.id.total);
        percent = (TextView) findViewById(R.id.percent);
        addPercent = (Button) findViewById(R.id.addPercent);
        subPercent = (Button) findViewById(R.id.subPercent);
        bill = (EditText) findViewById(R.id.billAmountEdit);
        split = (Spinner) findViewById(R.id.splitSpinner);
        splitTotal = (TextView) findViewById(R.id.splitTotal);

        bill.setOnEditorActionListener(this);

        addPercent.setOnClickListener(this);
        subPercent.setOnClickListener(this);
        percentNum = parsePercent(percent.getText().toString());
        billNum = 0;
        totalNum = 0;
        splitNum = 0;

        setupSpinner();

        split.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i){
                    case 0: splitNum = 1; splitTotal(); break;
                    case 1: splitNum = 2; splitTotal(); break;
                    case 2: splitNum = 3; splitTotal(); break;
                    case 3: splitNum = 4; splitTotal(); break;
                    case 4: splitNum = 5; splitTotal(); break;
                    case 5: splitNum = 6; splitTotal(); break;
                    case 6: splitTotal(); break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bill.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().matches("")){
                    billNum = Double.valueOf(charSequence.toString());
                }
                else{
                    billNum = 0;
                }


                percentNum = parsePercent(percent.getText().toString());

                calculateTip();
                calculateTotal();
                splitTotal();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    void splitTotal(){
        if(splitNum != 0){
            splitTotalNum = (double) Math.round((totalNum/splitNum)*100)/100;
            splitTotal.setText('$' + Double.toString(splitTotalNum));
        }
    }

    void setupSpinner(){
        ArrayAdapter<CharSequence> adapterSplit = ArrayAdapter.createFromResource(this, R.array.split_array, android.R.layout.simple_spinner_item);

        adapterSplit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        split.setAdapter(adapterSplit);

        split.setSelection(0);
    }

    void calculateTip(){
        tipNum = (double) Math.round((billNum * percentNum/100)*100)/100;
        tip.setText('$'+Double.toString(tipNum));
    }

    void calculateTotal(){

        totalNum = (double) Math.round((tipNum + billNum) *100)/100;

        total.setText('$'+Double.toString(totalNum));
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if(i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_UNSPECIFIED){
            if(!bill.getText().toString().matches("")){
                billNum = Double.valueOf(bill.getText().toString());
            }
            else{
                billNum = 0;
            }


            percentNum = parsePercent(percent.getText().toString());


            calculateTip();
            calculateTotal();
            splitTotal();


        }

        return false;
    }



    double parsePercent(String percent){
        String noPercentSign;
        noPercentSign = percent.substring(0, percent.length()-1);
        return Double.valueOf(noPercentSign);
    }

    double parseDollar(String dollar){
        String noDollarSign;
        noDollarSign = dollar.substring(1, dollar.length());
        return Double.valueOf(noDollarSign);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.addPercent:

                if( percentNum < 200){
                    percentNum += 5;
                    percent.setText(Integer.toString((int)(percentNum))+"%");
                    calculateTip();
                    calculateTotal();
                }
                break;
            case R.id.subPercent:
                if(percentNum > 0){
                    percentNum -= 5;
                    percent.setText(Integer.toString((int)(percentNum))+"%");
                    calculateTip();
                    calculateTotal();
                }
                break;
        }
    }
}
