package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private EditText textoTarefa;
    private Button botaoAdicionar;
    private ListView listaTarefas;
    private SQLiteDatabase bancoDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{

            textoTarefa = findViewById(R.id.text_id);
            botaoAdicionar = findViewById(R.id.botao_id);
            listaTarefas = findViewById(R.id.list_id);

            //Banco de dados
            bancoDados = openOrCreateDatabase("tarefas", MODE_PRIVATE, null);

            //Tabela Tarefa
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS tarefas(id INTEGER PRIMARY KEY AUTOINCREMENT, tarefa VARCHAR)");

            botaoAdicionar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String textoDigitado = textoTarefa.getText().toString();
                    bancoDados.execSQL("INSERT INTO tarefas(tarefa) VALUES('" + textoDigitado + "')");



                }
            });

            //Recpera as tarefas
            Cursor cursor = bancoDados.rawQuery("SELECT * FROM tarefas", null);

            //Recupera os ids das tarefas
            int indiceColunaId = cursor.getColumnIndex("id");
            int indiceColunaTarefa = cursor.getColumnIndex("tarefa");

            //Listar as tarefas
            cursor.moveToFirst();
            while (cursor != null){

                Log.i("Resultado - ", "tarefa: " + cursor.getString(indiceColunaTarefa));

                cursor.moveToNext();
            }


        }catch(Exception e){
            e.printStackTrace();
        }




    }
}
