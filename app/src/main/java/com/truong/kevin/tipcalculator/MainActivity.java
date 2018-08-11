package com.truong.kevin.tipcalculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements TextView.OnEditorActionListener, View.OnClickListener {

    private TextView tip;
    private TextView total;
    private TextView percent;
    private Button addPercent;
    private Button subPercent;
    private EditText bill;

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

        bill.setOnEditorActionListener(this);

        addPercent.setOnClickListener(this);
        subPercent.setOnClickListener(this);
        percentNum = parsePercent(percent.getText().toString());
        billNum = 0;

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
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
