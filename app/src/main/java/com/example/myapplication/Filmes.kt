
package com.example.myapplication

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "filmes")
data class Filmes(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nome: String,
    val desc: String
)