package estgoh.tam.fjtr.medicalapp2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar data = Calendar.getInstance();
        int ano = data.get(Calendar.YEAR);
        int mes = data.get(Calendar.MONTH);
        int dia = data.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), ano, mes, dia);
    }
}
