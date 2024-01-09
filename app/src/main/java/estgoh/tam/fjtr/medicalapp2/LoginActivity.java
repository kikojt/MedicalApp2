package estgoh.tam.fjtr.medicalapp2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private MedicamentoService medicamentoService;
    String host_port = "10.0.2.2:5000";
    EditText editNome;
    EditText editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        editNome = (EditText) findViewById(R.id.nomeUtilizador);
        editPassword = (EditText) findViewById(R.id.password);


        Button buttonRegisto = findViewById(R.id.buttonRegisto);
        buttonRegisto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistarActivity.class);
                startActivity(intent);
            }
        });

        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = editNome.getText().toString();
                String password = editPassword.getText().toString();

                if (nome.equals("") || password.equals("")) {
                    if (nome.equals(""))
                        showToast("Campo 'nome' em falta!");
                    if (password.equals(""))
                        showToast("Campo 'password' em falta!");
                    return;
                }

                Utilizador utilizador = new Utilizador();
                utilizador.setU_nome(nome);
                utilizador.setU_password(password);

                Call<LoginResponse> call = medicamentoService.login(utilizador);

                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        //Log.d("response: ", response.body().toString());
                        if (response.body().getCode() == 200) {
                            LoginResponse loginResponse = response.body();
                            String token = loginResponse.getToken();

                            showToast("Login efetuado com sucesso!");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("token", token);
                            startActivity(intent);
                            finish();
                        } else {
                            showToast("Login sem sucesso!");
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        t.printStackTrace();
                        Log.i("TAG", "Erro: " + t.getMessage());
                    }
                });
            }
        });


    }

    public void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}

class LoginResponse {
    private int Code;
    private String Token;

    public String getToken() {
        return Token;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "Code=" + Code +
                ", Token='" + Token + '\'' +
                '}';
    }
}
