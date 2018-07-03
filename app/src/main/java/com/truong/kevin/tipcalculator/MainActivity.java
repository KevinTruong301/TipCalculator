package com.truong.kevin.tipcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    private TextView tip;
    private TextView total;
    private TextView percent;
    private Button addPercent;
    private Button subPercent;
    private EditText bill;

    float tipNum;
    float billNum;
    float percentNum;

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

    }

    float calculateTip(float billAmount, float percentNum){
        return billAmount*percentNum;
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if(i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_UNSPECIFIED){
            billNum = parseDollar(bill.getText().toString());
            percentNum = parsePercent(percent.getText().toString());

            tipNum = calculateTip(billNum, percentNum);
            tip.setText(Float.toString(tipNum));
        }

        return false;
    }

    float parsePercent(String percent){
        String noPercentSign;
        noPercentSign = percent.substring(0, percent.length()-1);
        return Float.valueOf(noPercentSign)/100;
    }

    float parseDollar(String dollar){
        String noDollarSign;
        noDollarSign = dollar.substring(1, dollar.length());
        return Float.valueOf(noDollarSign);
    }
}
