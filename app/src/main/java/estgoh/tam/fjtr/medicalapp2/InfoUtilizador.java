package estgoh.tam.fjtr.medicalapp2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InfoUtilizador extends AppCompatActivity {

    String host_port = "10.0.2.2:5000";
    private String token;
    private MedicamentoService medicamentoService;
    private TextView nomeUtilizador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_user);

        token = getIntent().getStringExtra("token");

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

        Utilizador utilizador = new Utilizador();
        utilizador.setU_token(token);

        Call<ResponseCode> call = medicamentoService.informacaoUtilizador(utilizador);

        call.enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (response.isSuccessful()) {
                    ResponseCode responseCode = response.body();
                    String nome = responseCode.getNomeUtilizador();
                    nomeUtilizador.setText(nome);
                } else {
                    showToast("Erro ao obter informações do utilizador!");
                }
            }

            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                t.printStackTrace();
                Log.i("TAG", "Erro: " + t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_back, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.itemBack) {
            // Cria um Intent para retornar à MainActivity
            Intent intent = new Intent(InfoUtilizador.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        } else {
            return false;
        }
        return true;
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
