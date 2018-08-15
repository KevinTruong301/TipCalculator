package com.truong.kevin.tipcalculator;

import android.content.SharedPreferences;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

//Round with a trailing zero, fix shared preferences edittext doesnt hold value and split holds value even when in no
public class MainActivity extends AppCompatActivity implements TextView.OnEditorActionListener, View.OnClickListener {

    private TextView tip;
    private TextView total;
    private TextView percent;
    private TextView splitTotal;
    private Button addPercent;
    private Button subPercent;
    private EditText bill;
    private SharedPreferences savedValues;

    private Spinner split;

    int splitNum;

    float splitTotalNum;
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
        split = (Spinner) findViewById(R.id.splitSpinner);
        splitTotal = (TextView) findViewById(R.id.splitTotal);

        bill.setOnEditorActionListener(this);

        addPercent.setOnClickListener(this);
        subPercent.setOnClickListener(this);
        percentNum = parsePercent(percent.getText().toString());
        billNum = 0;


        savedValues = getSharedPreferences("savedValues", MODE_PRIVATE);
        //probably should just make the app keep the keyboard out or tap on the text and itll come up prob can do edit text like this
        //Make our own keyboard?

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
                    case 6: splitMore(); break;

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
                    billNum = Float.valueOf(charSequence.toString());
                }
                else{
                    billNum = 0;
                }

                percentNum = parsePercent(percent.getText().toString());

                updateValues();

            }

            @Override
            public void afterTextChanged(Editable editable) {

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

        updateValues();

        Toast.makeText(this, "Resume", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        Toast.makeText(this, "Ended", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    void splitMore(){
        final AlertDialog.Builder moreDialogBuilder = new AlertDialog.Builder(this);
        final AlertDialog moreDialog = moreDialogBuilder.create();
        final EditText moreDialogInput = new EditText(this);


        moreDialogBuilder.setCancelable(true);
        moreDialogBuilder.setTitle("More");
        moreDialogBuilder.setMessage("Enter number of people");
        moreDialogBuilder.setView(moreDialogInput);
        moreDialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                splitNum = Integer.valueOf(moreDialogInput.getText().toString());

                splitTotal();
            }
        });
        moreDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });



        moreDialog.show();

    }

    void updateValues(){
        calculateTip();
        calculateTotal();
        splitTotal();
    }

    void splitTotal(){
        if(splitNum != 0 && splitNum != 0){
            splitTotalNum = (float) Math.round((totalNum/splitNum)*100)/100;
            splitTotal.setText('$' + Float.toString(splitTotalNum));
        }
    }

    void setupSpinner(){
        ArrayAdapter<CharSequence> adapterSplit = ArrayAdapter.createFromResource(this, R.array.split_array, android.R.layout.simple_spinner_item);

        adapterSplit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        split.setAdapter(adapterSplit);

        split.setSelection(0);
    }

    void calculateTip(){
        tipNum = (float) Math.round((billNum * percentNum/100)*100)/100;
        tip.setText('$'+Float.toString(tipNum));
    }

    void calculateTotal(){

        totalNum = (float) Math.round((tipNum + billNum) *100)/100;

        total.setText('$'+Float.toString(totalNum));
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if(i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_UNSPECIFIED){

            if(!bill.getText().toString().matches("")){
                billNum = Float.valueOf(bill.getText().toString());
            }
            else{
                billNum = 0;
            }



            updateValues();

        }

        return false;
    }



    float parsePercent(String percent){
        String noPercentSign;
        noPercentSign = percent.substring(0, percent.length()-1);
        return Float.valueOf(noPercentSign);
    }



    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.addPercent:

                if( percentNum < 50){
                    percentNum += 5;
                    percent.setText(Integer.toString((int)(percentNum))+"%");
                    updateValues();
                }
                break;
            case R.id.subPercent:
                if(percentNum > 0){
                    percentNum -= 5;
                    percent.setText(Integer.toString((int)(percentNum))+"%");
                    updateValues();
                }
                break;
        }
    }
}
