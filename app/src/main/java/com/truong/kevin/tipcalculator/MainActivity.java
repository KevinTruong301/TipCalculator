package com.truong.kevin.tipcalculator;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements TextView.OnEditorActionListener, View.OnClickListener {

    private TextView tip;
    private TextView total;
    private TextView percent;
    private Button addPercent;
    private Button subPercent;
    private EditText bill;
    private SharedPreferences savedValues;
    CharSequence c;

    float tipNum;
    float billNum;
    float percentNum;
    float totalNum;

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

        savedValues = getSharedPreferences("savedValues", MODE_PRIVATE);
        //probably should just make the app keep the keyboard out or tap on the text and itll come up prob can do edit text like this
        //Make our own keyboard?
        bill.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                Log.d("wat", charSequence.toString());
                c = charSequence;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                tip.setText(c);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putFloat("tipNum", tipNum);
        editor.putFloat("billNum", billNum);
        editor.putFloat("percentNum" , percentNum);
        editor.putFloat("totalNum",totalNum);
        editor.commit();
        Toast.makeText(this, "Pause", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tipNum = savedValues.getFloat("tipNum", 0);
        billNum = savedValues.getFloat("billNum", 0);
        percentNum = savedValues.getFloat("percentNum", 0);
        totalNum = savedValues.getFloat("totalNum", 0);
        calculateTotal();
        calculateTip();
        Toast.makeText(this, "Resume", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        Toast.makeText(this, "Ended", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    void calculateTip(){
        tipNum = billNum * percentNum/100;
        tip.setText(Float.toString(tipNum));
    }

    void calculateTotal(){

        totalNum = tipNum + billNum;

        total.setText(Float.toString(totalNum));
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if(i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_UNSPECIFIED){
            billNum = Float.valueOf(bill.getText().toString());
            percentNum = parsePercent(percent.getText().toString());

            calculateTip();

            calculateTotal();
        }

        return false;
    }

    float parsePercent(String percent){
        String noPercentSign;
        noPercentSign = percent.substring(0, percent.length()-1);
        return Float.valueOf(noPercentSign);
    }

    void billAmountFormat(String currentTotal, String input){
        //need to know how to use keyEvent to get the number typed

        //replace the zeros
            //need to analyze the current situation
            //keep a stack of the numbers?
            //keeping a stack of all numbers would probably be easier

        //skip the period

        //keep adding numbers

        //max? do we handle it here?

        //delete and put back zeros

        //negatives?

        //update editText
    }

    float parseDollar(String dollar){
        String noDollarSign;
        noDollarSign = dollar.substring(1, dollar.length());
        return Float.valueOf(noDollarSign);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.addPercent:

                if( percentNum < 100){
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
