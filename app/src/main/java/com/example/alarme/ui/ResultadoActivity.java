package com.example.alarme.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.alarme.service.MusicService;
import com.example.alarme.R;
import com.example.alarme.model.SorteioData;

import java.util.List;

public class ResultadoActivity extends AppCompatActivity {

    private LoteriaViewModel viewModel;
    private RadioGroup radioGroupCartelas;
    private Button btnConfirmar;
    private TextView tvResultado;
    private SorteioData dadosAtuais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        radioGroupCartelas = findViewById(R.id.radioGroupCartelas);
        btnConfirmar = findViewById(R.id.btnConfirmar);
        tvResultado = findViewById(R.id.tvResultado);

        Intent musicIntent = new Intent(this, MusicService.class);
        startService(musicIntent);

        viewModel = new ViewModelProvider(this).get(LoteriaViewModel.class);
        viewModel.carregarDados();

        viewModel.getDadosSorteio().observe(this, new Observer<SorteioData>() {
            @Override
            public void onChanged(SorteioData dados) {
                if (dados != null) {
                    dadosAtuais = dados;
                    popularRadioButtons(dados.getCartelas());
                }
            }
        });

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroupCartelas.getCheckedRadioButtonId();
                if (selectedId != -1) {
                    Intent stopMusic = new Intent(ResultadoActivity.this, MusicService.class);
                    stopService(stopMusic);
                    View radioButton = radioGroupCartelas.findViewById(selectedId);
                    int index = radioGroupCartelas.indexOfChild(radioButton);
                    if (dadosAtuais != null) {
                        List<String> cartelaEscolhida = dadosAtuais.getCartelas().get(index);
                        verificarResultado(cartelaEscolhida, dadosAtuais.getCombinacaoPremiada());
                    }
                } else {
                    Toast.makeText(ResultadoActivity.this, "Selecione uma cartela!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void popularRadioButtons(List<List<String>> cartelas) {
        radioGroupCartelas.removeAllViews();
        for (int i = 0; i < cartelas.size(); i++) {
            List<String> cartela = cartelas.get(i);
            RadioButton rb = new RadioButton(this);
            rb.setText("Cartela " + (i + 1) + ": " + cartela.toString());
            rb.setId(View.generateViewId());
            radioGroupCartelas.addView(rb);
        }
    }

    private void verificarResultado(List<String> escolhida, List<String> premiada) {
        int acertos = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("<b>").append(getString(R.string.label_sua_cartela)).append("</b> ");

        for (String num : escolhida) {
            if (premiada.contains(num)) {
                acertos++;
                sb.append("<font color='#00FF00'>").append(num).append("</font> ");
            } else {
                sb.append(num).append(" ");
            }
        }

        sb.append("<br/><br/><b>").append(getString(R.string.label_premiada)).append("</b> ").append(premiada.toString());
        sb.append("<br/><br/>");

        if (acertos >= 4) {
            sb.append("<b>").append(getString(R.string.msg_parabens, acertos)).append("</b>");
        } else {
            sb.append("<b>").append(getString(R.string.msg_quase, acertos)).append("</b>");
        }

        tvResultado.setVisibility(View.VISIBLE);
        tvResultado.setText(Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY));
        btnConfirmar.setEnabled(false);
        btnConfirmar.setText("Fechando em 5 segundos...");
        for(int i = 0; i < radioGroupCartelas.getChildCount(); i++){
            radioGroupCartelas.getChildAt(i).setEnabled(false);
        }

        //fecha o app dps de 5 sec
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                finishAffinity();
            }
        }, 5000);
    }
}