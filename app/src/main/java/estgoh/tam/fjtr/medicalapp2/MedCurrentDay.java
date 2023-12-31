package estgoh.tam.fjtr.medicalapp2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.sql.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MedCurrentDay extends AppCompatActivity {

    ListView medicamentoslistView;
    List<MedicamentoPerTime> medicamentos = new ArrayList<>();

    MedDBAdapter medDBAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_current_day);

        // Cria uma instância do adaptador da base de dados
        medDBAdapter = new MedDBAdapter(this);
        // Abre a base de dados
        medDBAdapter.open();
        // Obter todos os medicamentos através do cursor
        Cursor cursor = medDBAdapter.obterMedicamentosAtivos();

        ListAdapter adapter = new MedicamentoPerTimeAdapter(this, medicamentos);
        medicamentoslistView = findViewById(R.id.medicamentos_lv_current_day);
        medicamentoslistView.setAdapter(adapter);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Extrair os valores do cursor para criar um objeto Medicamento
                int id = cursor.getInt(0);
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
                int administrado = cursor.getInt(12);

                // Cria um objeto Medicamento com os valores extraídos
                Medicamento medicamento = new Medicamento(id, nome, dosagem, formaFarmaceutica, posologia, hora1, hora2, hora3, hora4, quantidade, duracao, dataInicio, administrado);

                // Adiciona o objeto Medicamento à lista
                for (int i = 0; i <= 4; i++) {
                    String hora = medicamento.getHora(i);
                    if (hora != null && !hora.isEmpty()) {
                        MedicamentoPerTime medicamentoPerTime = new MedicamentoPerTime(hora, medicamento);
                        medicamentos.add(medicamentoPerTime);
                    }
                }
            } while (cursor.moveToNext());
            // Fecha o cursor após ser utilizado
            cursor.close();
        }

        // Configurar um OnItemClickListener para o ListView
        medicamentoslistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Obter o objeto Medicamento selecionado
                MedicamentoPerTime horaSelecionada = medicamentos.get(position);

                // Criar um Intent para iniciar a atividade InfosMed
                Intent intent = new Intent(MedCurrentDay.this, MedAdministrationActivity.class);

                // Passar o objeto MedicamentoPerTime serializado para a atividade InfosMed com a chave "medicamento"
                intent.putExtra("medicamento", (CharSequence) horaSelecionada);

                // Iniciar a atividade InfosMed
                startActivity(intent);
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
        // Fecha a base de dados quando a atividade for destruída
        medDBAdapter.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_back, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.itemBack) {
            // Cria um Intent para retornar à MainActivity
            Intent intent = new Intent(MedCurrentDay.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        } else {
            return false;
        }
        return true;
    }

    class MedicamentoPerTimeAdapter extends BaseAdapter {
        Context context;
        List<MedicamentoPerTime> adaptMedicamentos;

        public MedicamentoPerTimeAdapter(Context ctx, List<MedicamentoPerTime> list) {
            context = ctx;
            adaptMedicamentos = list;
        }

        // Verifica qual o tipo de forma farmacêutica para definir a img correta
        private void verificarFormaFarmaceutica(Medicamento aMed, ImageView aImgFormaFarmaceutica) {
            switch (aMed.getFormaFarmaceutica()) {
                case "Comprimido":
                    aImgFormaFarmaceutica.setImageResource(R.drawable.comprimido);
                    break;
                case "Cápsula":
                    aImgFormaFarmaceutica.setImageResource(R.drawable.capsula);
                    break;
                case "Colírio (gotas)":
                    aImgFormaFarmaceutica.setImageResource(R.drawable.colirio);
                    break;
                case "Xarope":
                    aImgFormaFarmaceutica.setImageResource(R.drawable.xarope);
                    break;
                case "Injeção":
                    aImgFormaFarmaceutica.setImageResource(R.drawable.injecao);
                    break;
                case "Pomada":
                    aImgFormaFarmaceutica.setImageResource(R.drawable.pomada);
                    break;
                case "Efervescente":
                    aImgFormaFarmaceutica.setImageResource(R.drawable.efervescente);
                    break;
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            if (rowView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.activity_list_current_day, parent, false);
            }

            ImageView imgFormaFarmaceutica = rowView.findViewById(R.id.imgFormaFarmaceutica);
            TextView nome = rowView.findViewById(R.id.nome);
            TextView formaFarmaceutica = rowView.findViewById(R.id.formaFarmaceutica);
            TextView horario = rowView.findViewById(R.id.horario);
            TextView quantidade = rowView.findViewById(R.id.quantidade);

            MedicamentoPerTime mpt = adaptMedicamentos.get(position);
            Medicamento med = mpt.getMedicamento();

            String medNome = "Nome: " + med.getNome();
            String medFormaFarmaceutica = "Tipo: " + med.getFormaFarmaceutica();
            // Concatenar as quatro horas numa única string
            String medHorario = "Horário: " + mpt.getHora();
            String medQuantidade = "Quantidade: " + med.getQuantidade();

            verificarFormaFarmaceutica(med, imgFormaFarmaceutica);
            nome.setText(medNome);
            formaFarmaceutica.setText(medFormaFarmaceutica);
            horario.setText(medHorario);
            quantidade.setText(medQuantidade);

            return rowView;
        }

        @Override
        public int getCount() {
            return adaptMedicamentos.size();
        }

        @Override
        public Object getItem(int position) {
            return adaptMedicamentos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }
}
