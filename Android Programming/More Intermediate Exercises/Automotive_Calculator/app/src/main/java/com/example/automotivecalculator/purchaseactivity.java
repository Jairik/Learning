package com.example.automotivecalculator;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class purchaseactivity extends AppCompatActivity {

    //Creating an auto object that will hold the vehicle information
    Auto auto;

    //Creating variables to hold the data passed into loan activity
    String loanReport;
    String monthlyPayment;

    //Creating variables to hold references
    private EditText carPriceET;
    private EditText downPaymentET;
    private RadioGroup loanTermRG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_layout);

        //Establishing references
        carPriceET = (EditText) findViewById(R.id.editText1);
        downPaymentET = (EditText) findViewById(R.id.editText2);
        loanTermRG = (RadioGroup) findViewById(R.id.radioGroup1);

        //Creating an auto object to store data
        auto = new Auto();
    }

    /* Collects information from the editText-fields and sets it in auto object */
    private void collectAutoInputData() {
        //Set the car price
        auto.setPrice((double) Integer.valueOf(carPriceET.getText().toString()));

        //Set the down payment
        auto.setDownPayment((double) Integer.valueOf(downPaymentET.getText().toString()));

        //Set the loan amount
        int radioId = loanTermRG.getCheckedRadioButtonId();
        RadioButton term = (RadioButton) findViewById(radioId);
        auto.setLoanTerm(term.getText().toString());
    }

    /* Builds the loan report */
    private void buildLoanReport() {
        //Construct the monthly payment
        Resources res = getResources();
        monthlyPayment = res.getString(R.string.report_line1)
                + String.format("%.02f", auto.monthlyPayment());

        //Constructing the loan report
        loanReport = res.getString(R.string.report_line6)
                + String.format("%10.02f", auto.getPrice());
        loanReport += res.getString(R.string.report_line7)
                + String.format("%10.02f", auto.getDownPayment());

        loanReport += res.getString(R.string.report_line9)
                + String.format("%18.02f", auto.taxAmount());
        loanReport += res.getString(R.string.report_line10)
                + String.format("%18.02f", auto.totalCost());
        loanReport += res.getString(R.string.report_line11)
                + String.format("%18.02f", auto.borrowedAmount());
        loanReport += res.getString(R.string.report_line12)
                + String.format("%18.02f", auto.interestAmount());

        loanReport += "\n\n" + res.getString(R.string.report_line8)
                + " " + auto.getLoanTerm() + " years.";

        loanReport += "\n\n" + res.getString(R.string.report_line2);
        loanReport += "\n\n" + res.getString(R.string.report_line3);
        loanReport += "\n\n" + res.getString(R.string.report_line4);
        loanReport += "\n\n" + res.getString(R.string.report_line5);
    }


    public void activateLoanSummary(View view) {
        try {
            //Building the loan report
            collectAutoInputData();
            buildLoanReport();

            //Creating an intent to display the loan summary
            Intent launchReport = new Intent(this, loansummaryactivity.class);

            //Passing relevant data to the activity
            launchReport.putExtra("LoanReport", loanReport);
            launchReport.putExtra("MonthlyPayment", monthlyPayment);

            //Starting the activity
            startActivity(launchReport);
        }
        catch (Exception e) {}
    }
}