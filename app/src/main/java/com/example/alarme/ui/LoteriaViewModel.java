package com.example.alarme.ui;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.alarme.repository.LoteriaRepository;
import com.example.alarme.model.SorteioData;

public class LoteriaViewModel extends AndroidViewModel {

    private final LoteriaRepository repository;
    private final MutableLiveData<SorteioData> dadosSorteio;

    public LoteriaViewModel(@NonNull Application application) {
        super(application);
        repository = new LoteriaRepository();
        dadosSorteio = new MutableLiveData<>();
    }

    public LiveData<SorteioData> getDadosSorteio() {
        return dadosSorteio;
    }

    public void carregarDados() {
        repository.buscarDados(dadosSorteio);
    }
}