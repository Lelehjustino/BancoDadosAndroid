package com.example.myapplication

import android.util.Log

class OperacoesDoBanco {
    suspend fun buscarFilmes(filmesDao: FilmesDAO): List<Filmes> {
        return try {
            filmesDao.buscarTodosFilmes()
        } catch (e: Exception) {
            Log.e("Erro ao buscar", "${e.message}")
            emptyList()
        }
    }

    suspend fun inserirFilme(nome: String, desc: String, filmesDao: FilmesDAO) {
        try {
            val filme = Filmes(
                nome = nome,
                desc = desc
            )

            filmesDao.inserir(filme)
        } catch (e: Exception) {
            Log.e("Erro ao adicionar", "Msg: ${e.message}")
        }
    }

    // Funcao para deletar filme
    suspend fun deletarFilme(filme: Filmes, filmesDao: FilmesDAO){
        try {
            filmesDao.excluir(filme)
        } catch (e: Exception){
            Log.e("Erro ao deletar", "Msg ${e.message}")
        }

    }

    // Funcao para atualizar
    suspend fun atualizarFilme(filme: Filmes, filmesDao: FilmesDAO){
        try {
            filmesDao.editar(filme)
        } catch (e: Exception){
            Log.e("Erro ao atualizar", "Msg ${e.message}")
        }

    }
}