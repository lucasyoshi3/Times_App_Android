package com.fateczl.times_app_android;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fateczl.times_app_android.controller.TimeController;
import com.fateczl.times_app_android.model.Time;
import com.fateczl.times_app_android.persistence.TimeDao;

import java.sql.SQLException;
import java.util.List;

public class TimeFragment extends Fragment {

    private View view;
    private EditText etCodigo, etNome, etCidade;
    private Button btnBuscar, btnListar,btnExcluir, btnModificar, btnInserir;
    private TextView tvListarTime;
    private TimeController tCont;

    public TimeFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_time, container, false);
        etCodigo=view.findViewById(R.id.etCodigoTime);
        etNome=view.findViewById(R.id.etNomeTime);
        etCidade=view.findViewById(R.id.etCidadeTime);
        btnBuscar=view.findViewById(R.id.btnBuscarTime);
        btnExcluir=view.findViewById(R.id.btnExcluirTime);
        btnInserir=view.findViewById(R.id.btnInserirTime);
        btnListar=view.findViewById(R.id.btnListarTime);
        btnModificar=view.findViewById(R.id.btnModificarTime);
        tvListarTime=view.findViewById(R.id.tvListarTime);
        tvListarTime.setMovementMethod(new ScrollingMovementMethod());

        tCont = new TimeController(new TimeDao(view.getContext()));

        btnInserir.setOnClickListener(op -> acaoInserir());
        btnModificar.setOnClickListener(op -> acaoModificar());
        btnExcluir.setOnClickListener(op -> acaoExcluir());
        btnBuscar.setOnClickListener(op -> acaoBuscar());
        btnListar.setOnClickListener(op -> acaoListar());

        return view;
    }

    private void acaoInserir() {
        Time time = montaTime();
        try {
            tCont.inserir(time);
            Toast.makeText(view.getContext(), "Time inserido com sucesso",
                    Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        limparCampos();
    }

    private void acaoModificar() {
        Time time = montaTime();
        try {
            tCont.modificar(time);
            Toast.makeText(view.getContext(), "Time atualizado com sucesso",
                    Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        limparCampos();
    }

    private void acaoExcluir() {
        Time time = montaTime();
        try {
            tCont.deletar(time);
            Toast.makeText(view.getContext(), "Time excluido com sucesso",
                    Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        limparCampos();
    }

    private void acaoBuscar() {
        Time time = montaTime();
        try {
            time = tCont.buscar(time);
            if(time.getNome() != null){
                preencheCampos(time);
            }else{
                Toast.makeText(view.getContext(), "Time não encontrado!", Toast.LENGTH_LONG).show();
                limparCampos();
            }
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void acaoListar() {
        try {
            List<Time> times = tCont.listar();
            StringBuffer buffer = new StringBuffer();
            for(Time t : times){
                buffer.append(t.toString()+"\n");
            }
            tvListarTime.setText(buffer.toString());
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private Time montaTime(){
        Time t = new Time();
        t.setCodigo(Integer.parseInt(etCodigo.getText().toString()));
        t.setNome(etNome.getText().toString());
        t.setCidade(etCidade.getText().toString());

        return t;
    }

    private void preencheCampos(Time t){
        etCodigo.setText(String.valueOf(t.getCodigo()));
        etNome.setText(String.valueOf(t.getNome()));
        etCidade.setText(String.valueOf(t.getCidade()));
    }

    private void limparCampos(){
        etCodigo.setText("");
        etNome.setText("");
        etCidade.setText("");
    }
}