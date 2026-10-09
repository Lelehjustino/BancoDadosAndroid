package com.example.myapplication

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase

@Database(
    entities = [Filmes:: class], // informa as tabelas
    version = 1 // versao do banco
)
abstract class AppDataBase : RoomDatabase(){

    // para criar banco so uma vez
    companion object{

        // var database
        @Volatile
        private var INSTACE: AppDataBase? = null

        // pegar a base
        fun getDatabase(context: Context) : AppDataBase{

            var temInstance = INSTACE

            if(temInstance != null){
                // se o banco existe, devolve banco
                return temInstance
            } else {
                // cria o banco
                synchronized(this){
                    val instance = Room.databaseBuilder(
                        context,
                        AppDataBase::class.java,
                        "app_database"
                    )
                }
            }
        }
    }

}