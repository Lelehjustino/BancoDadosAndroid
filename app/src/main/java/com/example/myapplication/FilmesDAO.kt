package com.example.myapplication

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update

@Dao
interface FilmesDAO {

    // Funcao inserir filme
    @Insert
    fun inserir(filme: Filmes)

    // Funcao buscar todos os filmes
    @Query("SELECT * FROM filmes")
    fun buscarTodosFilmes() : List<Filmes>

    // Função editar um filme
    @Update
    fun editar(filme: Filmes)

    // Função excluir um filme
    @Delete
    fun excluir(filme: Filmes)
}