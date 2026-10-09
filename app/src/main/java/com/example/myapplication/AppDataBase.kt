package com.example.myapplication

import androidx.room3.Database
import androidx.room3.RoomDatabase

@Database(
    entities = [Filmes:: class], // informa as tabelas
    version = 1 // versao do banco
)
abstract class AppDataBase : RoomDatabase(){

}