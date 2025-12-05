package com.example.alarme;

import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.MutableLiveData;
import com.google.gson.Gson;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoteriaRepository {

    // Substitua SEU_USUARIO e SEU_REPO pelos seus dados reais do GitHub/MyJsonServer
    // Exemplo: https://my-json-server.typicode.com/typicode/demo/db
    // Usando um placeholder conforme pedido:
    private static final String URL_API = "https://my-json-server.typicode.com/SEU_USUARIO/SEU_REPO/db";

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public void buscarDados(final MutableLiveData<SorteioData> liveData) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Conexao conexao = new Conexao();
                InputStream inputStream = conexao.obterRespostaHTTP(URL_API);

                Conversao conversao = new Conversao();
                String jsonString = conversao.converter(inputStream);

                if (jsonString != null) {
                    Gson gson = new Gson();
                    final SorteioData dados = gson.fromJson(jsonString, SorteioData.class);

                    // Postar na thread principal
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            liveData.setValue(dados);
                        }
                    });
                }
            }
        });
    }
}