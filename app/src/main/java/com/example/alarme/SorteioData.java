package com.example.alarme;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SorteioData {

    @SerializedName("cartelas")
    @Expose
    private List<List<String>> cartelas;

    @SerializedName("combinacao_premiada")
    @Expose
    private List<String> combinacaoPremiada;

    public List<List<String>> getCartelas() {
        return cartelas;
    }

    public void setCartelas(List<List<String>> cartelas) {
        this.cartelas = cartelas;
    }

    public List<String> getCombinacaoPremiada() {
        return combinacaoPremiada;
    }

    public void setCombinacaoPremiada(List<String> combinacaoPremiada) {
        this.combinacaoPremiada = combinacaoPremiada;
    }
}