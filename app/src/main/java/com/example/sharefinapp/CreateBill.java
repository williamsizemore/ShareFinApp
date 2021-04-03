package com.example.sharefinapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.material.datepicker.MaterialStyledDatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateBill extends AppCompatActivity {

    private final Calendar calendar = Calendar.getInstance();
    private EditText reminderDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bill);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // add the back arrow button
        getSupportActionBar().setIcon(R.drawable.save_icon);    //todo change out to do a custom action bar with save icon included

        reminderDate = findViewById(R.id.create_bill_reminder_date);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel();
            }
        };

        reminderDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new DatePickerDialog(CreateBill.this, date, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }

    public void updateDateLabel()
    {
        String dateFormat = "dd-MMM-YYYY";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

        reminderDate.setText(sdf.format(calendar.getTime()));
    }

    // add the back arrow functionality
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // get and save the fields to the server
    public void onBillSave(View view)
    {
        //todo implement
    }

}