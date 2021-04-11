package com.example.sharefinapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MakePayment extends AppCompatActivity {
    private final Calendar calendar = Calendar.getInstance();
    private String billID;
    private Bill bill;
    private DocumentReference documentReference;   // reference ID for the bill document in the database to update
    private EditText paymentAmount, description;
    private EditText paymentDateField;
    private Payment payment;
    private DatePickerDialog.OnDateSetListener date;
    private double amountDue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setEnterTransition(new Slide());
        getWindow().setExitTransition(new Slide());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // add the back arrow

        billID = getIntent().getStringExtra("billID");
        Log.v("makePayment", billID);

        getBillData();

        paymentAmount = findViewById(R.id.make_payment_pay_amount);
        description = findViewById(R.id.make_payment_description);
        paymentDateField = findViewById(R.id.make_payment_calendar);

        String dateFormat = "dd-MMM-YYYY";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

        paymentDateField.setText(sdf.format(calendar.getTime()));

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                paymentDateField.setText(sdf.format(calendar.getTime()));
            }
        };

    }

    public void setPaymentDate(View vew){
        new DatePickerDialog(this, date, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    // add the back arrow functionality
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getBillData()
    {
        DBManager.getInstance().getDb().collection("bills").whereEqualTo("billID",billID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                bill = queryDocumentSnapshots.getDocuments().get(0).toObject(Bill.class);
                documentReference = queryDocumentSnapshots.getDocuments().get(0).getReference();
                setFields();
            }
        });
    }

    public void setFields() {
        TextView billName = findViewById(R.id.make_payment_bill_name);
        billName.setText(bill.getName());

        TextView amountOwed = findViewById(R.id.make_payment_amount_owed);
        amountDue = bill.getBillSplit().get(DBManager.getInstance().getCurrentUserID());

        DecimalFormat df = new DecimalFormat("###.##");
        amountOwed.setText("You Owe $" + df.format(amountDue));
    }

    public void savePayment(View view) {
//        Date paymentDate = new Date();
        payment = new Payment();

        if (isValidFields())
        {
            double amountPaid = Double.parseDouble(paymentAmount.getText().toString());
            payment.setAmountPaid(amountPaid);

            double amountRemaining = amountDue - amountPaid;
            payment.setAmountRemaining(amountRemaining);

            payment.setPaymentDate(calendar.getTime());
            payment.setMetaLoadDate(Calendar.getInstance().getTime());
            payment.setDescription(description.getText().toString());
            payment.setBillID(bill.getBillID());
            payment.setUserID(DBManager.getInstance().getCurrentUserID());

            // generate a paymentID
            payment.setPaymentID(DBManager.getInstance().generateKey("payments"));

            /* make the call to insert the payment data to the table */
            DBManager.getInstance().insertData("payments",payment);
            Log.v("test savePayment","saving the payment");

            /* update the bill amount */
            double updatedAmountPaid = bill.getAmountPaid() + amountPaid;

            Log.v("test about to try updating" , "- updating doc: " + documentReference + " with value" + updatedAmountPaid);
//            DBManager.getInstance().getDocumentReference("bills","billID",bill.getBillID(), updatedAmountPaid);

            DBManager.getInstance().updateDataField("bills",documentReference,"amountPaid",updatedAmountPaid);

            finish();
        }
    }


    /**
     *
     * @return true if necessary fields are filled in, false otherwise
     */
    public boolean isValidFields() {

        if (paymentAmount.getText().toString().isEmpty()) {
            paymentAmount.setError("Please enter an amount");
            return false;
        }


        return true;
    }

    public void getEnteredData() {
        while (bill == null) {
            try {
                wait(100);
            } catch (Exception e) {
                Log.e("MakePayment - getEnteredData: ", e.getStackTrace().toString());
            }
        }
        if (paymentAmount.getText().toString().isEmpty()) {

        }
    }
}