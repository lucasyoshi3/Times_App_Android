package com.fateczl.times_app_android;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fateczl.times_app_android.controller.JogadorController;
import com.fateczl.times_app_android.controller.TimeController;
import com.fateczl.times_app_android.model.Jogador;
import com.fateczl.times_app_android.model.Time;
import com.fateczl.times_app_android.persistence.JogadorDao;
import com.fateczl.times_app_android.persistence.TimeDao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class JogadorFragment extends Fragment {

    private View view;
    private EditText etCodigo, etNome, etDataNasc, etAltura, etPeso, etTime;
    private Button btnBuscar, btnInserir, btnModificar, btnExcluir, btnListar;
    private TextView tvListarJog;
    private Spinner spJogTime;
    private JogadorController jCont;
    private TimeController tCont;
    private List<Time> times;
    public JogadorFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_jogador, container, false);
        etCodigo=view.findViewById(R.id.etCodigoJog);
        etNome=view.findViewById(R.id.etNomeJog);
        etAltura=view.findViewById(R.id.etAlturaJog);
        etPeso=view.findViewById(R.id.etPesoJog);
        etTime=view.findViewById(R.id.etTimeJog);
        etDataNasc=view.findViewById(R.id.etDataNascJog);
        btnBuscar=view.findViewById(R.id.btnBuscarJog);
        btnExcluir=view.findViewById(R.id.btnExcluirJog);
        btnInserir=view.findViewById(R.id.btnInserirJog);
        btnListar=view.findViewById(R.id.btnListarJog);
        btnModificar=view.findViewById(R.id.btnModificarJog);
        tvListarJog=view.findViewById(R.id.tvListarJog);
        tvListarJog.setMovementMethod(new ScrollingMovementMethod());

        jCont = new JogadorController(new JogadorDao(view.getContext()));
        tCont = new TimeController(new TimeDao(view.getContext()));
        preencheSpinner();

        btnInserir.setOnClickListener(op -> acaoInserir());
        btnModificar.setOnClickListener(op -> acaoModificar());
        btnExcluir.setOnClickListener(op -> acaoExcluir());
        btnBuscar.setOnClickListener(op -> acaoBuscar());
        btnListar.setOnClickListener(op -> acaoListar());

        return view;
    }

    private void acaoInserir() {
        int spPos  = spJogTime.getSelectedItemPosition();
        if(spPos > 0){
            Jogador jogador = montaJogador();
            try {
                jCont.inserir(jogador);
                Toast.makeText(view.getContext(), "Jogador inserido com sucesso",
                        Toast.LENGTH_LONG).show();
            } catch (SQLException e) {
                Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
            limpaCampos();
        }else{
            Toast.makeText(view.getContext(), "Selecione um Time",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void acaoModificar() {
        int spPos  = spJogTime.getSelectedItemPosition();
        if(spPos > 0){
            Jogador jogador = montaJogador();
            try {
                jCont.modificar(jogador);
                Toast.makeText(view.getContext(), "Jogador atualizado com sucesso",
                        Toast.LENGTH_LONG).show();
            } catch (SQLException e) {
                Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
            limpaCampos();
        }else{
            Toast.makeText(view.getContext(), "Selecione um Time",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void acaoExcluir() {
        Jogador j = montaJogador();
        try {
            jCont.deletar(j);
            Toast.makeText(view.getContext(), "Jogador excluida com sucesso",
                    Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        limpaCampos();

    }

    private void acaoBuscar() {
        Jogador jogador = montaJogador();
        try {
            times = tCont.listar();
            jogador = jCont.buscar(jogador);
            if(jogador.getNome() != null){
                preencheCampos(jogador);
            }else{
                Toast.makeText(view.getContext(), "Jogador não encontrado",
                        Toast.LENGTH_LONG).show();
                limpaCampos();
            }
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void acaoListar() {
        try {
            List<Jogador> jogadores = jCont.listar();
            StringBuffer buffer = new StringBuffer();
            for(Jogador j : jogadores){
                buffer.append(j.toString()+"\n");
            }
            tvListarJog.setText(buffer.toString());
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void preencheSpinner() {
        Time t0 = new Time();
        t0.setCodigo(0);
        t0.setNome("Selecione um time");
        t0.setCidade("");

        try {
            times = tCont.listar();
            times.add(0,t0);

            ArrayAdapter ad = new ArrayAdapter(view.getContext(),
                    android.R.layout.simple_spinner_item,
                    times);
            ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spJogTime.setAdapter(ad);

        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private Jogador montaJogador(){
        Jogador j = new Jogador();
        j.setId(Integer.parseInt(etCodigo.getText().toString()));
        j.setNome(etNome.getText().toString());
        j.setAltura(Float.parseFloat(etAltura.getText().toString()));
        j.setDataNasc(LocalDate.parse(etDataNasc.getText().toString()));
        j.setPeso(Float.parseFloat(etPeso.getText().toString()));
        j.setTime((Time) spJogTime.getSelectedItem());

        return j;
    }

    public void limpaCampos(){
        etCodigo.setText("");
        etNome.setText("");
        etDataNasc.setText("");
        etPeso.setText("");
        etAltura.setText("");

        spJogTime.setSelection(0);
    }

    public void preencheCampos(Jogador j){
        etCodigo.setText(String.valueOf(j.getId()));
        etNome.setText(String.valueOf(j.getNome()));
        etDataNasc.setText(String.valueOf(j.getDataNasc()));
        etPeso.setText(String.valueOf(j.getPeso()));
        etAltura.setText(String.valueOf(j.getAltura()));

        int cta = 1;
        for(Time t : times){
            if(t.getCodigo() == j.getTime().getCodigo()){
                spJogTime.setSelection(cta);
            }else{
                cta++;
            }
        }
        if(cta>times.size()){
            spJogTime.setSelection(0);
        }
    }
}