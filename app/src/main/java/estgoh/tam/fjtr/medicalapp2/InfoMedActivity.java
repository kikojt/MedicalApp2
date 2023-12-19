package estgoh.tam.fjtr.medicalapp2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public class InfoMedActivity extends AppCompatActivity implements Serializable {

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_medicamento);

        Button buttonVoltar = findViewById(R.id.buttonVoltar);
        Button buttonEditar = findViewById(R.id.buttonEditar);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);

        // Criar uma instância do adaptador da base de dados
        MedDBAdapter medDBAdapter = new MedDBAdapter(this);
        medDBAdapter.open();

        // Obter os detalhes do medicamento com base no ID
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

            // Referenciar os TextViews no layout XML
            TextView infoNome = findViewById(R.id.infoNome);
            TextView infoDosagem = findViewById(R.id.infoDosagem);
            TextView infoFormaFarmaceutica = findViewById(R.id.infoFormaFarmaceutica);
            TextView infoPosologia = findViewById(R.id.infoPosologia);
            TextView infoHorario = findViewById(R.id.infoHorario);
            TextView infoQuantidade = findViewById(R.id.infoQuantidade);
            TextView infoDuracao = findViewById(R.id.infoDuracao);
            TextView infoDataInicio = findViewById(R.id.infoDataInicio);

            // Definir o texto dos TextViews com os detalhes do medicamento
            infoNome.setText(nome);
            infoDosagem.setText(dosagem);
            infoFormaFarmaceutica.setText(formaFarmaceutica);
            infoPosologia.setText(posologia);

            // Concatenar as quatro horas numa única string
            String horario = concatenaHoras(hora1, hora2, hora3, hora4);
            infoHorario.setText(horario);

            infoQuantidade.setText(Integer.toString(quantidade));
            infoDuracao.setText(duracao);
            infoDataInicio.setText(dataInicio);

            // Fechar o cursor após ser utilizado
            cursor.close();
        }

        // Botão para ir para a MedHistoric
        Button buttonHistorico = findViewById(R.id.buttonHistorico);
        buttonHistorico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoMedActivity.this, MedHistoric.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        // Botão para voltar à mainActivity
        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cria um Intent para retornar à MainActivity
                Intent intent = new Intent(InfoMedActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Botão para ir para a EditMedActivity
        buttonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obter o ID do medicamento do Intent
                Intent intent = getIntent();
                id = intent.getIntExtra("id", 0);

                // Criar um Intent para iniciar a atividade de edição
                Intent editarIntent = new Intent(InfoMedActivity.this, EditMedActivity.class);

                // Passar o ID do medicamento para a atividade de edição
                editarIntent.putExtra("id", id);

                // Iniciar a atividade de edição
                startActivity(editarIntent);
            }
        });
    }

    // Método para concatenar as horas
    private String concatenaHoras(String... horas) {
        StringBuilder horasConcatenadas = new StringBuilder();

        for (String hora : horas) {
            if (hora != null && !hora.isEmpty()) {
                if (horasConcatenadas.length() > 0) {
                    horasConcatenadas.append(", ");
                }
                horasConcatenadas.append(hora);
            }
        }
        return horasConcatenadas.toString();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Atualiza os dados quando a atividade é retomada
        atualizarDetalhes();
    }

    // Método para atualizar os dados na InfoMedActivity
    private void atualizarDetalhes() {
        // Criar uma instância do adaptador da base de dados
        MedDBAdapter medDBAdapter = new MedDBAdapter(this);
        medDBAdapter.open();

        // Obter os detalhes do medicamento com base no ID
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

            // Referenciar os TextViews no layout XML
            TextView infoNome = findViewById(R.id.infoNome);
            TextView infoDosagem = findViewById(R.id.infoDosagem);
            TextView infoFormaFarmaceutica = findViewById(R.id.infoFormaFarmaceutica);
            TextView infoPosologia = findViewById(R.id.infoPosologia);
            TextView infoHorario = findViewById(R.id.infoHorario);
            TextView infoQuantidade = findViewById(R.id.infoQuantidade);
            TextView infoDuracao = findViewById(R.id.infoDuracao);
            TextView infoDataInicio = findViewById(R.id.infoDataInicio);

            // Definir o texto dos TextViews com os detalhes do medicamento
            infoNome.setText(nome);
            infoDosagem.setText(dosagem);
            infoFormaFarmaceutica.setText(formaFarmaceutica);
            infoPosologia.setText(posologia);

            // Concatenar as quatro horas numa única string
            String horario = concatenaHoras(hora1, hora2, hora3, hora4);
            infoHorario.setText(horario);

            infoQuantidade.setText(Integer.toString(quantidade));
            infoDuracao.setText(duracao);
            infoDataInicio.setText(dataInicio);

            // Fechar o cursor após ser utilizado
            cursor.close();
        }

        // Fecha a conexão com a base de dados
        medDBAdapter.close();
    }
}
