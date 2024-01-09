package estgoh.tam.fjtr.medicalapp2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private MedicamentoService medicamentoService;
    String host_port = "10.0.2.2:5000";
    String token;

    ListView medicamentoslistView;
    List<Medicamento> medicamentos = new ArrayList<>();

    MedDBAdapter medDBAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        token = (String) getIntent().getSerializableExtra("token");

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level (others: NONE, BASIC, HEADERS)
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        // add logging as a interceptor (it should be the last one)
        httpClient.addInterceptor(logging);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://" + host_port)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build());

        Retrofit retrofit = builder.build();

        medicamentoService = retrofit.create(MedicamentoService.class);

        medDBAdapter = new MedDBAdapter(this);
        medDBAdapter.open();
        Cursor cursor = medDBAdapter.obterTodosMedicamentos();

        ListAdapter adapter = new MedicamentoAdapter(this, medicamentos);
        medicamentoslistView = findViewById(R.id.medicamentos_lv);
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
                int ativo = cursor.getInt(12);

                // Cria um objeto Medicamento com os valores extraídos
                Medicamento medicamento = new Medicamento(id, nome, dosagem, formaFarmaceutica, posologia, hora1, hora2, hora3, hora4, quantidade, duracao, dataInicio, ativo);

                // Adiciona o objeto Medicamento à lista
                medicamentos.add(medicamento);
            } while (cursor.moveToNext());
            // Fecha o cursor após ser utilizado
            cursor.close();
        }

        // Configurar um OnItemClickListener para o ListView
        medicamentoslistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Obter o objeto Medicamento selecionado
                Medicamento medicamentoSelecionado = medicamentos.get(position);

                // Criar um Intent para iniciar a atividade InfosMed
                Intent intent = new Intent(MainActivity.this, InfoMedActivity.class);

                // Passar o ID do Medicamento selecionado para a atividade InfoMedActivity
                intent.putExtra("id", medicamentoSelecionado.getId());

                // Iniciar a atividade InfosMed
                startActivity(intent);
            }
        });

        FloatingActionButton buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, AddMedActivity.class), 1);
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.itemMedCurrentDay) {
            Intent intent = new Intent(MainActivity.this, MedCurrentDay.class);
            startActivity(intent);
        } else if (itemId == R.id.itemAbout) {
            Intent intent = new Intent(MainActivity.this, About.class);
            startActivity(intent);
        } else if (itemId == R.id.itemConta) {
            Intent intent = new Intent(MainActivity.this, InfoUtilizador.class);
            intent.putExtra("token", token);
            startActivity(intent);
        } else if (itemId == R.id.itemLogout) {
            Utilizador utilizador = new Utilizador();
            utilizador.setU_token(token);

            Call<ResponseCode> call = medicamentoService.logout(utilizador);

            call.enqueue(new Callback<ResponseCode>() {
                @Override
                public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                    if (response.isSuccessful()) {
                        showToast("Logout efetuado com sucesso!");
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        showToast("Erro ao realizar logout!");
                    }
                }

                @Override
                public void onFailure(Call<ResponseCode> call, Throwable t) {
                    t.printStackTrace();
                    Log.i("TAG", "Erro: " + t.getMessage());
                }
            });
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    // Método chamado quando a AddMedActivity termina
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            // O medicamento foi adicionado com sucesso, então atualize à lista
            atualizarListaMedicamentos();
        }
    }

    // Método para atualizar a lista de medicamentos
    private void atualizarListaMedicamentos() {
        // Limpa a lista atual
        medicamentos.clear();

        // Obtém todos os medicamentos da base de dados novamente
        Cursor cursor = medDBAdapter.obterTodosMedicamentos();

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
                int ativo = cursor.getInt(12);

                // Cria um objeto Medicamento com os valores extraídos
                Medicamento medicamento = new Medicamento(id, nome, dosagem, formaFarmaceutica, posologia, hora1, hora2, hora3, hora4, quantidade, duracao, dataInicio, ativo);
                // Adiciona o objeto Medicamento à lista
                medicamentos.add(medicamento);

            } while (cursor.moveToNext());
            // Fecha o cursor após ser utilizado
            cursor.close();
        }
        // Notifica o adaptador de que os dados foram alterados
        ((BaseAdapter) medicamentoslistView.getAdapter()).notifyDataSetChanged();
    }

    class MedicamentoAdapter extends BaseAdapter {
        Context context;
        List<Medicamento> adaptMedicamentos;

        public MedicamentoAdapter(Context ctx, List<Medicamento> list) {
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

        // Verifica se o medicamento está a ser administrado no dia atual
        private void verificarTempoAdministracao(Medicamento aMed, ImageView aImgAdministrado) {
            String dataInicio = aMed.getDataInicio();
            String stringDuracao = aMed.getDuracao();

            // Formata a data de início recebe: "dd/MM/yyyy" retorna no formato adequado para comparar
            Calendar timestamp = Calendar.getInstance();
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            Date dataFormatada = null;
            Date dataAtual = timestamp.getTime();
            try {
                dataFormatada = formato.parse(dataInicio);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            // Extrai o inteiro da string
            int duracao;

            if (stringDuracao.equalsIgnoreCase("habitual")) {
                duracao = -1;
            } else {
                duracao = Integer.parseInt(stringDuracao.split(" ")[0]);
            }

            // Calcula o último dia de administração
            Calendar ultimoDia = Calendar.getInstance();
            ultimoDia.setTime(dataFormatada);

            if (duracao != -1) {
                ultimoDia.add(Calendar.DAY_OF_MONTH, duracao);
            }

            // Verifica se o medicamento está sendo administrado no dia atual
            if (dataAtual.after(dataFormatada) && (duracao == -1 || dataAtual.before(ultimoDia.getTime()))) {
                aImgAdministrado.setImageResource(R.drawable.checked);
                aImgAdministrado.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exibirMensagemAlerta("O medicamento '" + aMed.getNome() + "' está sendo administrado hoje!");
                    }
                });
                // Define o atributo "ativo" como 1, ou seja, o medicamento está sendo administrado
                aMed.setAtivo(1);

            } else {
                aImgAdministrado.setImageResource(R.drawable.not_checked);
                aImgAdministrado.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exibirMensagemAlerta("O medicamento '" + aMed.getNome() + "' não está sendo administrado hoje!");
                    }
                });
                // Define o atributo "ativo" como 1, ou seja, o medicamento está sendo administrado
                aMed.setAtivo(0);
            }
            
            // Atualiza o atributo "ativo" na base de dados
            medDBAdapter.atualizarAtivoMedicamento(aMed.getId(), aMed.getAtivo());
        }

        private void exibirMensagemAlerta(String aMensagem) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Informação");
            builder.setMessage(aMensagem);

            builder.setNegativeButton("Sair", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            if (rowView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.activity_list_item, parent, false);
            }

            ImageView imgFormaFarmaceutica = rowView.findViewById(R.id.imgFormaFarmaceutica);
            TextView nome = rowView.findViewById(R.id.nome);
            TextView formaFarmaceutica = rowView.findViewById(R.id.formaFarmaceutica);
            TextView horario = rowView.findViewById(R.id.horario);
            TextView quantidade = rowView.findViewById(R.id.quantidade);
            ImageView imgAdministrado = rowView.findViewById(R.id.administrado);
            ImageView excluirItem = rowView.findViewById(R.id.excluirItem);

            Medicamento med = adaptMedicamentos.get(position);

            String medNome = "Nome: " + med.getNome();
            String medFormaFarmaceutica = "Tipo: " + med.getFormaFarmaceutica();
            // Concatenar as quatro horas numa única string
            String medHorario = "Horário: " + concatenaHoras(med.getHora1(), med.getHora2(), med.getHora3(), med.getHora4());
            String medQuantidade = "Quantidade: " + med.getQuantidade();

            verificarFormaFarmaceutica(med, imgFormaFarmaceutica);
            nome.setText(medNome);
            formaFarmaceutica.setText(medFormaFarmaceutica);
            horario.setText(medHorario);
            quantidade.setText(medQuantidade);
            verificarTempoAdministracao(med, imgAdministrado);

            // Remover um item, neste caso o medicamento em questão
            excluirItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Confirmação");
                    builder.setMessage("Prentende mesmo remover o medicamento '" + med.getNome() + "' da lista?");

                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Remove o medicamento da base de dados
                            long idToRemove = med.getId();
                            MedDBAdapter medDBAdapter = new MedDBAdapter(context);
                            medDBAdapter.open();
                            medDBAdapter.removerMedicamento(idToRemove);
                            medDBAdapter.close();
                            // Remove o medicamento da lista
                            adaptMedicamentos.remove(position);
                            notifyDataSetChanged();
                        }
                    });

                    builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
            return rowView;
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