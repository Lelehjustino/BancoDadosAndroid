package com.example.myapplication

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query

@Dao
interface FilmesDAO {

    // Funcao inserir filme
    @Insert
    fun inserir(filme: Filmes)

    // Funcao buscar todos os filmes
    @Query("SELECT * FROM filmes")
    fun buscarTodosFilmes() : List<Filmes>

}