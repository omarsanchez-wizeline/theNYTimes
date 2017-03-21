package com.wizeline.omarsanchez.nytimessearch.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import com.wizeline.omarsanchez.nytimessearch.R;
import com.wizeline.omarsanchez.nytimessearch.databinding.AlertFilterBinding;
import com.wizeline.omarsanchez.nytimessearch.interfaces.FilterSaved;
import com.wizeline.omarsanchez.nytimessearch.models.Filter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by omarsanchez on 3/19/17.
 */

public class FilterDialog extends AlertDialog.Builder implements DatePickerDialog.OnDateSetListener {
    FilterSaved result;
    Filter filter;
    DatePickerDialog datePickerDialog;
    AlertFilterBinding alertFilterBinding;

    public FilterDialog(@NonNull Context context, FilterSaved filterSaved, Filter filter) {
        super(context);
        result = filterSaved;
        this.filter = filter;
    }

    @Override
    public AlertDialog create() {
        alertFilterBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.alert_filter, null, false);
        alertFilterBinding.setFilter(filter);
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(getContext(), R.style.datepicker, this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        alertFilterBinding.spinnerSort.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,
                new String[]{getContext().getString(R.string.oldest), getContext().getString(R.string.newest)}));
        alertFilterBinding.spinnerSort.setSelection(filter.getSort());
        alertFilterBinding.filterDate.setOnClickListener(v -> datePickerDialog.show());
        alertFilterBinding.saveButton.setOnClickListener(v -> result.onFilterSaved(new Filter(alertFilterBinding.filterDate.getText().toString(), alertFilterBinding.spinnerSort.getSelectedItemPosition(),
                alertFilterBinding.checkArts.isChecked(), alertFilterBinding.checkFashion.isChecked(), alertFilterBinding.checkSport.isChecked())));
        setView(alertFilterBinding.getRoot());
        return super.create();
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.set(year, month, dayOfMonth);
        alertFilterBinding.filterDate.setText(dateFormatter.format(tempCalendar.getTime()));
    }
}
