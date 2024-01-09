package estgoh.tam.fjtr.medicalapp2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistarActivity extends Activity {

    private MedicamentoService medicamentoService;
    String host_port = "10.0.2.2:5000";
    EditText newNomeUtilizador;
    EditText newPassword;
    EditText confirmNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registo);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://" + host_port)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build());

        Retrofit retrofit = builder.build();

        medicamentoService = retrofit.create(MedicamentoService.class);

        newNomeUtilizador = findViewById(R.id.newNomeUtilizador);
        newPassword = findViewById(R.id.newPassword);
        confirmNewPassword = findViewById(R.id.confirmarPassword);

        Button buttonRegistarUtilizador = findViewById(R.id.buttonRegistar);
        buttonRegistarUtilizador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = newNomeUtilizador.getText().toString();
                String password = newPassword.getText().toString();
                String confirmPass = confirmNewPassword.getText().toString();

                if (nome.isEmpty() || password.isEmpty() || confirmPass.isEmpty()) {
                    if (nome.isEmpty())
                        showToast("Campo 'nome' em falta!");
                    else if (password.isEmpty())
                        showToast("Campo 'password' em falta!");
                    return;
                }

                if (!password.equals(confirmPass)) {
                    showToast("As passwords não coincidem!");
                    return;
                }

                Utilizador utilizador = new Utilizador();
                utilizador.setU_nome(nome);
                utilizador.setU_password(password);

                Call<ResponseCode> call = medicamentoService.registar(utilizador);

                call.enqueue(new Callback<ResponseCode>() {
                    @Override
                    public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                        if (response.body().getCode() == 200) {
                            showToast("Utilizador registado com sucesso!");
                            Intent intent = new Intent(RegistarActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            showToast("Não foi possivel registar o utilizador!");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseCode> call, Throwable t) {
                        t.printStackTrace();
                        showToast("onFailure");
                    }
                });
            }
        });

        Button buttonVoltarLogin = findViewById(R.id.buttonVoltarLogin);
        buttonVoltarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistarActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}

class ResponseCode {

    private int Code;

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }
}
