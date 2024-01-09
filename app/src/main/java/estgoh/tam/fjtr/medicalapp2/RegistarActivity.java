package estgoh.tam.fjtr.medicalapp2;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegistarActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registo);

        Button buttonRegistarUtilizador = findViewById(R.id.buttonRegistar);
        EditText nomeUtilizador = findViewById(R.id.nomeUtilizador);
        EditText password = findViewById(R.id.password);

        buttonRegistarUtilizador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button buttonVoltarLogin = findViewById(R.id.buttonVoltarLogin);
        buttonVoltarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cria um Intent para retornar à MainActivity
                Intent intent = new Intent(RegistarActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
