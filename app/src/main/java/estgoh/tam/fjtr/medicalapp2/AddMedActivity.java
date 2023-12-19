package estgoh.tam.fjtr.medicalapp2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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

import java.io.Serializable;
import java.util.Calendar;
import java.util.Locale;

public class AddMedActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, Serializable {

    // Chaves para guardar e restaurar os dados introduzidos ou selecionados
    private static final String KEY_NOME = "nome";
    private static final String KEY_DOSAGEM = "dosagem";
    private static final String KEY_FORMA_FARMACEUTICA = "formaFarmaceutica";
    private static final String KEY_POSOLOGIA = "posologia";
    private static final String KEY_HORA_1 = "hora1";
    private static final String KEY_HORA_2 = "hora2";
    private static final String KEY_HORA_3 = "hora3";
    private static final String KEY_HORA_4 = "hora4";
    private static final String KEY_QUANTIDADE = "quantidade";
    private static final String KEY_DURACAO = "duracao";
    private static final String KEY_DATA_INICIO = "dataInicio";

    int horaSelecionada = 0;
    boolean dataSelecionada = false;
    EditText newNome;
    EditText newDosagem;
    Spinner newFormaFarmaceutica;
    EditText newPosologia;
    Button buttonHora1;
    TextView newHora1;
    Button buttonHora2;
    TextView newHora2;
    Button buttonHora3;
    TextView newHora3;
    Button buttonHora4;
    TextView newHora4;
    NumberPicker newQuantidade;
    NumberPicker newDuracao;
    Button buttonDataInicio;
    TextView newDataInicio;
    Button buttonCancelar;
    Button buttonAdicionarMedicamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicamento);

        newNome = findViewById(R.id.newNome);
        newDosagem = findViewById(R.id.newDosagem);
        newFormaFarmaceutica = findViewById(R.id.newFormaFarmaceutica);
        newPosologia = findViewById(R.id.newPosologia);
        buttonHora1 = findViewById(R.id.buttonHora1);
        newHora1 = findViewById(R.id.newHora1);
        buttonHora2 = findViewById(R.id.buttonHora2);
        newHora2 = findViewById(R.id.newHora2);
        buttonHora3 = findViewById(R.id.buttonHora3);
        newHora3 = findViewById(R.id.newHora3);
        buttonHora4 = findViewById(R.id.buttonHora4);
        newHora4 = findViewById(R.id.newHora4);
        newQuantidade = findViewById(R.id.newQuantidade);
        newDuracao = findViewById(R.id.newDuracao);
        buttonDataInicio = findViewById(R.id.buttonData);
        newDataInicio = findViewById(R.id.newDataInicio);
        buttonCancelar = findViewById(R.id.buttonCancelar);
        buttonAdicionarMedicamento = findViewById(R.id.buttonAdicionar);

        // Adicionar a forma farmacêutica do novo medicamento
        String[] formasFarmaceuticas = {"Selecione a Forma Farmacêutica", "Comprimido", "Cápsula", "Colírio (gotas)", "Xarope", "Injeção", "Pomada", "Efervescente"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, formasFarmaceuticas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newFormaFarmaceutica.setAdapter(adapter);
        newFormaFarmaceutica.setSelection(0);

        // Botões para definir o horário da administração do medicamento
        buttonHora1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horaSelecionada = 1;
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "TimePicker");
            }
        });

        buttonHora2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!newHora2.getText().toString().isEmpty()) {
                    newHora2.setText("");
                    newHora3.setText("");
                    newHora4.setText("");
                    showToast("Todas as horas depois da 'Hora2' foram removidas!");
                } else if (!newHora1.getText().toString().isEmpty()) {
                    horaSelecionada = 2;
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "TimePicker");
                } else {
                    showToast("Defina a 'Hora1' antes de inserir a 'Hora2'!");
                }
            }
        });

        buttonHora3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!newHora3.getText().toString().isEmpty()) {
                    newHora3.setText("");
                    newHora4.setText("");
                    showToast("Todas as horas depois da 'Hora3' foram removidas!");
                } else if (!newHora2.getText().toString().isEmpty()) {
                    horaSelecionada = 3;
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "TimePicker");
                } else {
                    showToast("Defina a 'Hora2' antes de inserir a 'Hora3'!");
                }
            }
        });

        buttonHora4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!newHora4.getText().toString().isEmpty()) {
                    newHora4.setText("");
                    showToast("A 'hora4' foi removida!");
                } else if (!newHora3.getText().toString().isEmpty()) {
                    horaSelecionada = 4;
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "TimePicker");
                } else {
                    showToast("Defina a 'Hora3' antes de inserir a 'Hora4'!");
                }
            }
        });

        // NumberPicker para definir a quantidade de medicamentos
        newQuantidade.setMinValue(1);
        newQuantidade.setMaxValue(10);

        // NumberPicker para definir a duração da toma do medicamento
        String[] opDuracao = {"Habitual", "1 Dia", "2 Dias", "3 Dias", "4 Dias", "5 Dias", "6 Dias", "7 Dias", "8 Dias", "9 Dias", "10 Dias", "11 Dias", "12 Dias", "13 Dias", "14 Dias", "15 Dias", "16 Dias", "17 Dias", "18 Dias", "19 Dias", "20 Dias", "21 Dias", "22 Dias", "23 Dias", "24 Dias", "25 Dias", "26 Dias", "27 Dias", "28 Dias", "29 Dias", "30 Dias", "31 Dias"};

        newDuracao.setMinValue(0);
        newDuracao.setMaxValue(opDuracao.length - 1);

        String defaultValue = "7 Dias";
        // Obtém o índice do 'defaultValue', para depois o utilizar como valor predefinido
        int defaultIndex = 0;
        for (int i = 0; i < opDuracao.length; i++) {
            if (opDuracao[i].equals(defaultValue)) {
                defaultIndex = i;
                break;
            }
        }
        newDuracao.setValue(defaultIndex);
        newDuracao.setDisplayedValues(opDuracao);

        // Botão para definir a data de início da administração do medicamento
        buttonDataInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "DatePicker");
            }
        });

        // Botão para cancelar a adição de um medicamento e volta à mainActivity
        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cria um Intent para retornar à MainActivity
                Intent intent = new Intent(AddMedActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        // Botão para adicionar e enviar os dados inseridos do novo medicamento para a base de dados
        buttonAdicionarMedicamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtém os valores inseridos do novo medicamento
                String nome = newNome.getText().toString();
                String dataInicio = newDataInicio.getText().toString();

                // Cria uma instância do MedDBAdapter e estabelce a conexão à base de dados
                MedDBAdapter dbAdapter = new MedDBAdapter(AddMedActivity.this);
                dbAdapter.open();

                //Verifica se o medicamento já existe na base de dados
                if (dbAdapter.verificarExistenciaMedicamento(nome, dataInicio)) {
                    showToast("Já existe um medicamento com o mesmo nome e data de início.");
                } else {
                    // Continua com a adição do medicamento à base de dados
                    String dosagem = newDosagem.getText().toString();
                    String formaFarmaceutica = newFormaFarmaceutica.getSelectedItem().toString();
                    String posologia = newPosologia.getText().toString();
                    String hora1 = newHora1.getText().toString();
                    String hora2 = newHora2.getText().toString();
                    String hora3 = newHora3.getText().toString();
                    String hora4 = newHora4.getText().toString();
                    int quantidade = newQuantidade.getValue();
                    String duracao = opDuracao[newDuracao.getValue()];

                    // Validação de todos os campos
                    if (nome.isEmpty()) {
                        showToast("Preencha o campo 'Nome'!");
                    } else if (dosagem.isEmpty()) {
                        showToast("Preencha o campo 'Dosagem'!");
                    } else if (formaFarmaceutica.equals("Selecione a Forma Farmacêutica")) {
                        showToast("Selecione a Forma Farmacêutica!");
                    } else if (posologia.isEmpty()) {
                        showToast("Preencha o campo 'Posologia'!");
                    } else if (hora1.isEmpty() ) {
                        showToast("Defina pelo menos uma hora!");
                    } else if (dataInicio.isEmpty()) {
                        showToast("Defina a 'Data de Início'");
                    } else {
                        // Adiciona o medicamento à base de dados
                        long idMedicamento = dbAdapter.adicionarMedicamento(nome, dosagem, formaFarmaceutica, posologia, hora1, hora2, hora3, hora4, quantidade, duracao, dataInicio);

                        // Fecha a conexão com o banco de dados
                        dbAdapter.close();

                        // Se o medicamento foi adicionado com sucesso, exibe a mensagem e encerra a atividade
                        if (idMedicamento != -1) {
                            showToast("Medicamento adicionado com sucesso!");
                            setResult(Activity.RESULT_OK);
                            finish();
                        } else {
                            showToast("Erro ao adicionar o medicamento. Tente novamente!");
                        }
                    }
                }
            }
        });

        // Restaura os dados guardados após a recriação da activity
        if (savedInstanceState != null) {
            newNome.setText(savedInstanceState.getString(KEY_NOME, ""));
            newDosagem.setText(savedInstanceState.getString(KEY_DOSAGEM, ""));
            newFormaFarmaceutica.setSelection(getIndex(newFormaFarmaceutica, savedInstanceState.getString(KEY_FORMA_FARMACEUTICA, "")));
            newPosologia.setText(savedInstanceState.getString(KEY_POSOLOGIA, ""));
            newHora1.setText(savedInstanceState.getString(KEY_HORA_1, ""));
            newHora2.setText(savedInstanceState.getString(KEY_HORA_2, ""));
            newHora3.setText(savedInstanceState.getString(KEY_HORA_3, ""));
            newHora4.setText(savedInstanceState.getString(KEY_HORA_4, ""));
            newQuantidade.setValue(savedInstanceState.getInt(KEY_QUANTIDADE, 1));
            newDuracao.setValue(savedInstanceState.getInt(KEY_DURACAO, 7));
            newDataInicio.setText(savedInstanceState.getString(KEY_DATA_INICIO, ""));
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
        outState.putString(KEY_NOME, newNome.getText().toString());
        outState.putString(KEY_DOSAGEM, newDosagem.getText().toString());
        outState.putString(KEY_FORMA_FARMACEUTICA, (String) newFormaFarmaceutica.getSelectedItem());
        outState.putString(KEY_POSOLOGIA, newPosologia.getText().toString());
        outState.putString(KEY_HORA_1, newHora1.getText().toString());
        outState.putString(KEY_HORA_2, newHora2.getText().toString());
        outState.putString(KEY_HORA_3, newHora3.getText().toString());
        outState.putString(KEY_HORA_4, newHora4.getText().toString());
        outState.putInt(KEY_QUANTIDADE, newQuantidade.getValue());
        outState.putInt(KEY_DURACAO, newDuracao.getValue());
        outState.putString(KEY_DATA_INICIO, (String) newDataInicio.getText());
    }

    // Método para lidar com o horário a quando esta é selecionado no 'TimePicker'
    @Override
    public void onTimeSet(TimePicker view, int hora, int minuto) {
        String horaFormatada = String.format(Locale.getDefault(), "%02d:%02d", hora, minuto);

        // Atualiza apenas o TextView associado à hora selecionada
        switch (horaSelecionada) {
            case 1:
                TextView textViewNewHora1 = findViewById(R.id.newHora1);
                textViewNewHora1.setText(horaFormatada);
                break;
            case 2:
                TextView textViewNewHora2 = findViewById(R.id.newHora2);
                textViewNewHora2.setText(horaFormatada);
                break;
            case 3:
                TextView textViewNewHora3 = findViewById(R.id.newHora3);
                textViewNewHora3.setText(horaFormatada);
                break;
            case 4:
                TextView textViewNewHora4 = findViewById(R.id.newHora4);
                textViewNewHora4.setText(horaFormatada);
                break;
        }
    }

    // Método para lidar com a data a quando esta é selecionada no 'DatePicker'
    @Override
    public void onDateSet(DatePicker view, int ano, int mes, int dia) {
        // Indíca que foi selecionada uma data
        dataSelecionada = true;

        Calendar data = Calendar.getInstance();
        data.set(Calendar.YEAR, ano);
        data.set(Calendar.MONTH, mes);
        data.set(Calendar.DAY_OF_MONTH, dia);

        String dataFormatada = String.format(Locale.getDefault(), "%02d/%02d/%04d", dia, mes + 1, ano);

        TextView textView = findViewById(R.id.newDataInicio);
        textView.setText(dataFormatada);
    }

    // Método para exibir um 'Toast' com uma determinada mensagem
    public void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
