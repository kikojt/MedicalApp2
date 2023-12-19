package estgoh.tam.fjtr.medicalapp2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Locale;

public class EditMedActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    // Chaves para guardar e restaurar os dados introduzidos ou selecionados
    private static final String KEY_NOME_EDITADO = "nome";
    private static final String KEY_DOSAGEM_EDITADA = "dosagem";
    private static final String KEY_FORMA_FARMACEUTICA_EDITADA = "formaFarmaceutica";
    private static final String KEY_POSOLOGIA_EDITADA = "posologia";
    private static final String KEY_HORA_1_EDITADA = "hora1";
    private static final String KEY_HORA_2_EDITADA = "hora2";
    private static final String KEY_HORA_3_EDITADA = "hora3";
    private static final String KEY_HORA_4_EDITADA = "hora4";
    private static final String KEY_QUANTIDADE_EDITADA = "quantidade";
    private static final String KEY_DURACAO_EDITADA = "duracao";
    private static final String KEY_DATA_INICIO_EDITADA = "dataInicio";

    int horaSelecionada = 0;
    boolean dataSelecionada = false;
    private EditText editNome;
    private EditText editDosagem;
    private Spinner editFormaFarmaceutica;
    private EditText editPosologia;
    private NumberPicker editQuantidade;
    private NumberPicker editDuracao;
    private String[] opDuracao;
    private Button buttonEditarHora1;
    private TextView editHora1;
    private Button buttonEditarHora2;
    private TextView editHora2;
    private Button buttonEditarHora3;
    private TextView editHora3;
    private Button buttonEditarHora4;
    private TextView editHora4;
    private Button buttonEditarData;
    private TextView editDataInicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_medicamento);

        Button buttonCancelar = findViewById(R.id.buttonCancelar);
        Button buttonGuardarEdicao = findViewById(R.id.buttonGuardarEdicao);

        // Criar uma instância do adaptador da base de dados
        MedDBAdapter medDBAdapter = new MedDBAdapter(this);
        medDBAdapter.open();

        // Obter os detalhes do medicamento com base no ID
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        Cursor cursor = medDBAdapter.obterMedicamento(id);

        if (cursor != null && cursor.moveToFirst()) {
            // Extrair detalhes do cursor
            String nome = cursor.getString(1);
            String dosagem = cursor.getString(2);
            String formaFarmaceutica = cursor.getString(3);
            String posologia = cursor.getString(4);
            String hora1 = cursor.getString(5);
            String hora2 = cursor.getString(6);
            String hora3 = cursor.getString(7);
            String hora4 = cursor.getString(8);
            int quantidade = cursor.getInt(9);
            String duracao = cursor.getString(10);
            String dataInicio = cursor.getString(11);

            // Referenciar os EditTexts, Spinner e NumberPickers no layout XML
            editNome = findViewById(R.id.editNome);
            editDosagem = findViewById(R.id.editDosagem);
            editFormaFarmaceutica = findViewById(R.id.editFormaFarmaceutica);
            editPosologia = findViewById(R.id.editPosologia);
            editQuantidade = findViewById(R.id.editQuantidade);
            editDuracao = findViewById(R.id.editDuracao);
            buttonEditarHora1 = findViewById(R.id.buttonEditarHora1);
            editHora1 = findViewById(R.id.editHora1);
            buttonEditarHora2 = findViewById(R.id.buttonEditarHora2);
            editHora2 = findViewById(R.id.editHora2);
            buttonEditarHora3 = findViewById(R.id.buttonEditarHora3);
            editHora3 = findViewById(R.id.editHora3);
            buttonEditarHora4 = findViewById(R.id.buttonEditarHora4);
            editHora4 = findViewById(R.id.editHora4);
            buttonEditarData = findViewById(R.id.buttonEditarData);
            editDataInicio = findViewById(R.id.editDataInicio);

            // Preencher os campos de edição com os dados do medicamento
            editNome.setText(nome);
            editDosagem.setText(dosagem);

            String[] formasFarmaceuticas = {"Comprimido", "Cápsula", "Colírio (gotas)", "Xarope", "Injeção", "Pomada", "Efervescente"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, formasFarmaceuticas);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            editFormaFarmaceutica.setAdapter(adapter);
            // Obtém o índice da forma farmacêutica selecionada
            int indiceFormaFarmaceutica = -1;
            for (int i = 0; i < formasFarmaceuticas.length; i++) {
                if (formasFarmaceuticas[i].equals(formaFarmaceutica)) {
                    indiceFormaFarmaceutica = i;
                    break;
                }
            }
            editFormaFarmaceutica.setSelection(indiceFormaFarmaceutica);

            editPosologia.setText(posologia);

            // Botões para editar o horário da administração do medicamento
            buttonEditarHora1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    horaSelecionada = 1;
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "TimePicker");
                }
            });
            editHora1.setText(hora1);

            buttonEditarHora2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    horaSelecionada = 2;
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "TimePicker");
                }
            });
            editHora2.setText(hora2);

            buttonEditarHora3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    horaSelecionada = 3;
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "TimePicker");
                }
            });
            editHora3.setText(hora3);

            buttonEditarHora4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    horaSelecionada = 4;
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "TimePicker");
                }
            });
            editHora4.setText(hora4);

            editQuantidade.setMinValue(1);
            editQuantidade.setMaxValue(10);
            editQuantidade.setValue(quantidade);

            opDuracao = new String[] {"Habitual", "1 Dia", "2 Dias", "3 Dias", "4 Dias", "5 Dias", "6 Dias", "7 Dias", "8 Dias", "9 Dias", "10 Dias", "11 Dias", "12 Dias", "13 Dias", "14 Dias", "15 Dias", "16 Dias", "17 Dias", "18 Dias", "19 Dias", "20 Dias", "21 Dias", "22 Dias", "23 Dias", "24 Dias", "25 Dias", "26 Dias", "27 Dias", "28 Dias", "29 Dias", "30 Dias", "31 Dias"};

            editDuracao.setMinValue(0);
            editDuracao.setMaxValue(opDuracao.length - 1);

            String defaultValue = duracao;

            int defaultIndex = 0;
            for (int i = 0; i < opDuracao.length; i++) {
                if (opDuracao[i].equals(defaultValue)) {
                    defaultIndex = i;
                    break;
                }
            }
            editDuracao.setValue(defaultIndex);
            editDuracao.setDisplayedValues(opDuracao);

            editDataInicio.setText(dataInicio);
            buttonEditarData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment datePicker = new DatePickerFragment();
                    datePicker.show(getSupportFragmentManager(), "DatePicker");
                }
            });
            // Fechar o cursor após ser utilizado
            cursor.close();
        }

        // Botão para voltar à InfoMedActivity
        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cria um Intent para retornar à InfoMedActivity
                Intent intent = new Intent(EditMedActivity.this, InfoMedActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        // Botão para editar o medicamento
        buttonGuardarEdicao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtém os valores inseridos da edição do medicamento
                String nomeEditado = editNome.getText().toString();
                String dosagemEditada = editDosagem.getText().toString();
                String formaFarmaceuticaEditada = editFormaFarmaceutica.getSelectedItem().toString();
                String posologiaEditada = editPosologia.getText().toString();
                String hora1Editada = editHora1.getText().toString();
                String hora2Editada = editHora2.getText().toString();
                String hora3Editada = editHora3.getText().toString();
                String hora4Editada = editHora4.getText().toString();
                int quantidadeEditada = editQuantidade.getValue();
                String duracaoEditada = opDuracao[editDuracao.getValue()];
                String dataInicioEditada = editDataInicio.getText().toString();

                // Cria uma instância do MedDBAdapter e estabelece a ligação à base de dados
                MedDBAdapter dbAdapter = new MedDBAdapter(EditMedActivity.this);
                dbAdapter.open();

                // Verifica se o novo nome ou a nova data de início já existe na base de dados
                if (!dbAdapter.verificarExistenciaMedEdicao(id, nomeEditado, dataInicioEditada)) {
                    // Caso não exista, faz a edição do medicamento
                    dbAdapter.editarMedicamento(id, nomeEditado, dosagemEditada, formaFarmaceuticaEditada, posologiaEditada, hora1Editada, hora2Editada, hora3Editada, hora4Editada, quantidadeEditada, duracaoEditada, dataInicioEditada);
                    // Fecha a conexão com a base de dados
                    dbAdapter.close();
                    // Após editar com sucesso, inicia a InfoMedActivity
                    Intent intent = new Intent(EditMedActivity.this, InfoMedActivity.class);
                    showToast("Medicamento editado com sucesso!");
                    intent.putExtra("id", id);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    // Fecha a conexão com a base de dados
                    dbAdapter.close();
                    // Caso já exista, exibe uma mensagem informativa
                    Toast.makeText(EditMedActivity.this, "Já existe um medicamento com o mesmo nome e com a mesma data de início.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Restaura os dados guardados após a recriação da activity
        if (savedInstanceState != null) {
            editNome.setText(savedInstanceState.getString(KEY_NOME_EDITADO, ""));
            editDosagem.setText(savedInstanceState.getString(KEY_DOSAGEM_EDITADA, ""));
            editFormaFarmaceutica.setSelection(getIndex(editFormaFarmaceutica, savedInstanceState.getString(KEY_FORMA_FARMACEUTICA_EDITADA, "")));
            editPosologia.setText(savedInstanceState.getString(KEY_POSOLOGIA_EDITADA, ""));
            editHora1.setText(savedInstanceState.getString(KEY_HORA_1_EDITADA, ""));
            editHora2.setText(savedInstanceState.getString(KEY_HORA_2_EDITADA, ""));
            editHora3.setText(savedInstanceState.getString(KEY_HORA_3_EDITADA, ""));
            editHora4.setText(savedInstanceState.getString(KEY_HORA_4_EDITADA, ""));
            editQuantidade.setValue(savedInstanceState.getInt(KEY_QUANTIDADE_EDITADA, 1));
            editDuracao.setValue(savedInstanceState.getInt(KEY_DURACAO_EDITADA, 7));
            editDataInicio.setText(savedInstanceState.getString(KEY_DATA_INICIO_EDITADA, ""));
        }
    }

    // Método auxiliar para obter o índice de item selecionado da forma farmacêutica
    private int getIndex(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(value)) {
                return i;
            }
        }
        return 0; // valor padrão se não for encontrado
    }

    // Método para o estado da activity recuperando os dados introduzidos ou selecionados
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Guarda os dados para restaurar o estado da activity
        outState.putString(KEY_NOME_EDITADO, editNome.getText().toString());
        outState.putString(KEY_DOSAGEM_EDITADA, editDosagem.getText().toString());
        outState.putString(KEY_FORMA_FARMACEUTICA_EDITADA, (String) editFormaFarmaceutica.getSelectedItem());
        outState.putString(KEY_POSOLOGIA_EDITADA, editPosologia.getText().toString());
        outState.putString(KEY_HORA_1_EDITADA, editHora1.getText().toString());
        outState.putString(KEY_HORA_2_EDITADA, editHora2.getText().toString());
        outState.putString(KEY_HORA_3_EDITADA, editHora3.getText().toString());
        outState.putString(KEY_HORA_4_EDITADA, editHora4.getText().toString());
        outState.putInt(KEY_QUANTIDADE_EDITADA, editQuantidade.getValue());
        outState.putInt(KEY_DURACAO_EDITADA, editDuracao.getValue());
        outState.putString(KEY_DATA_INICIO_EDITADA, (String) editDataInicio.getText());
    }

    // Método para lidar com o horário a quando esta é selecionado no 'TimePicker'
    @Override
    public void onTimeSet(TimePicker view, int hora, int minuto) {
        String horaFormatada = String.format(Locale.getDefault(), "%02d:%02d", hora, minuto);

        // Atualiza apenas o TextView associado à hora selecionada
        switch (horaSelecionada) {
            case 1:
                TextView textViewNewHora1 = findViewById(R.id.editHora1);
                textViewNewHora1.setText(horaFormatada);
                break;
            case 2:
                TextView textViewNewHora2 = findViewById(R.id.editHora2);
                textViewNewHora2.setText(horaFormatada);
                break;
            case 3:
                TextView textViewNewHora3 = findViewById(R.id.editHora3);
                textViewNewHora3.setText(horaFormatada);
                break;
            case 4:
                TextView textViewNewHora4 = findViewById(R.id.editHora4);
                textViewNewHora4.setText(horaFormatada);
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        dataSelecionada = true;
        Calendar data = Calendar.getInstance();
        data.set(Calendar.YEAR, year);
        data.set(Calendar.MONTH, month);
        data.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String dataFormatada = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
        TextView textView = findViewById(R.id.editDataInicio);
        textView.setText(dataFormatada);
    }

    public void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
