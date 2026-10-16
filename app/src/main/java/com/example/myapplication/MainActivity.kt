package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.nio.file.WatchEvent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
                TelaCadastroFilmes()
            }
        }
    }
@Composable
fun TelaCadastroFilmes(){

    var estouEditando by remember { mutableStateOf(false) }
    var filmeEmEdicao by remember {
        mutableStateOf<Filmes?>(value = null)
    }

    var nome by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }

    var filmes by remember { mutableStateOf<List<Filmes>>(emptyList()) }

    val contex = LocalContext.current
    val db = AppDataBase.getDatabase(contex)
    val filmesDao = db.filmesDAO()
    val operacoesDoBanco = OperacoesDoBanco()

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        filmes = withContext(Dispatchers.IO) {
            operacoesDoBanco.buscarFilmes(filmesDao)
        }

        Log.d("Busca ok", "Filmes encontrados: ${filmes.size}")
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            modifier = Modifier.fillMaxWidth()
                .padding(10.dp)
                .background(Color.LightGray, RectangleShape),
            border = BorderStroke(1.dp, Color.Black)
        ) {

            TextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome do Filme") }
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descricao") }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    if(nome.isNotBlank() && descricao.isNotBlank()){
                        // CoroutineScope -> faz a rotina separada para não travar a tela
                        CoroutineScope(Dispatchers.IO).launch {
                            if (estouEditando) {
                                val filmeTemp = filmeEmEdicao!!.copy(nome = nome, desc = descricao)

                                operacoesDoBanco.atualizarFilme(filmeTemp, filmesDao)
                                estouEditando = false
                            } else {
                                operacoesDoBanco.inserirFilme(nome, descricao, filmesDao)
                            }
                            filmes = operacoesDoBanco.buscarFilmes(filmesDao)
                            nome = ""
                            descricao = ""
                        }
                    }
                }
            ) {
               if(estouEditando) Text("Editar Filme") else Text("Adicionar Filme")
            }

        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn {
            items(filmes){ filme ->
                umFilme(
                    umFilme = filme,
                    aoRemover = {
                        CoroutineScope(Dispatchers.IO).launch {
                            operacoesDoBanco.deletarFilme(filme, filmesDao)
                            filmes = operacoesDoBanco.buscarFilmes(filmesDao)
                        }
                    },
                    aoEditar = {
                        nome = filme.nome
                        descricao = filme.desc
                        estouEditando = true
                        filmeEmEdicao = filme
                    }
                )
            }
        }


    }

}

@Composable
fun umFilme(
    umFilme: Filmes,
    aoRemover: (Filmes) -> Unit,
    aoEditar: (Filmes) -> Unit,
){
    Card(
        modifier = Modifier.height(80.dp).fillMaxWidth().padding(5.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        border = BorderStroke(2.dp, Color.Black)
    ) {
        Row(
            modifier = Modifier.padding(10.dp).fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "${umFilme.id}", style = MaterialTheme.typography.bodyMedium)

            Column {
                Text(text = umFilme.nome, style = MaterialTheme.typography.titleLarge)
                Text(text = umFilme.desc, style = MaterialTheme.typography.titleSmall)
            }

            Icon(
                Icons.Default.Edit,
                "",
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        aoEditar(umFilme)
                    }
            )
            Icon(
                Icons.Default.Close,
                "",
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        aoRemover(umFilme)
                    }
            )
        }
    }
}
