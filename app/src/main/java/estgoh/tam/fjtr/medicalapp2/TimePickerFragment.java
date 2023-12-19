package estgoh.tam.fjtr.medicalapp2;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar horario = Calendar.getInstance();
        int hora = horario.get(Calendar.HOUR_OF_DAY);
        int minuto = horario.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener) getActivity(), hora, minuto, DateFormat.is24HourFormat(getActivity()));
    }
}
