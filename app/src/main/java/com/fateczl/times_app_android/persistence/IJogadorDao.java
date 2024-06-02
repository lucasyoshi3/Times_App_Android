package com.fateczl.times_app_android.persistence;

import android.annotation.SuppressLint;

import com.fateczl.times_app_android.model.Jogador;

import java.sql.SQLException;
import java.util.List;

public interface IJogadorDao {
    public JogadorDao open() throws SQLException;
    public void close();

    @SuppressLint("Range")
    List<Jogador> findAll() throws SQLException;
}
