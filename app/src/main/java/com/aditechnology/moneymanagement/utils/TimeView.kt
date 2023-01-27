package com.aditechnology.moneymanagement.utils

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.text.SimpleDateFormat
import java.util.*

class TimeView(val textView: TextView) : DialogFragment(), TimePickerDialog.OnTimeSetListener{


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val cal = Calendar.getInstance()
        val hour = cal[Calendar.HOUR_OF_DAY]
        val minute = cal[Calendar.MINUTE]
        return TimePickerDialog(requireContext(), this, hour, minute, true)
    }

    override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        val dateFormat = SimpleDateFormat("hh:mm a")
        textView.text = dateFormat.format(cal.time)

    }
}