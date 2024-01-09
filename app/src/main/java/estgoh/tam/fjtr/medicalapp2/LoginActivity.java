package estgoh.tam.fjtr.medicalapp2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Configurar Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("URL_DA_SUA_API")  // Substitua pela URL real da sua API
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Criar uma instância da interface da API
        apiInterface = retrofit.create(ApiInterface.class);

        Button buttonRegistar = findViewById(R.id.buttonRegistar);
        buttonRegistar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Criar um Intent para iniciar a RegistarActivity
                Intent intent = new Intent(LoginActivity.this, RegistarActivity.class);

                // Iniciar a RegistarActivity
                startActivity(intent);
            }
        });

        // Adicionar código para o botão de login
        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Adicionar código para fazer a solicitação de login usando Retrofit
                fazerLogin("nome_de_usuario", "senha_do_usuario");
            }
        });
    }
