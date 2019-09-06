package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText textoTarefa;
    private Button botaoAdicionar;
    private ListView listaTarefas;
    private SQLiteDatabase bancoDados;

    private ArrayAdapter<String> itensAdaptador;
    private ArrayList<String> itens;
    private  ArrayList<Integer> ids;

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
                    salvarTarefa(textoDigitado);


                }
            });

            /*listaTarefas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    removerTarefa(ids.get(i));
                    Toast.makeText(MainActivity.this, "Tarefa Removida", Toast.LENGTH_SHORT).show();
                }
            }); */

            //Remover com toque longo
            listaTarefas.setLongClickable(true);
            listaTarefas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                    removerTarefa(ids.get(i));
                    return true;
                }
            });

            recuperarTarefas();

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void salvarTarefa(String texto){

        try{

            if(texto.equals("")){
                Toast.makeText(this, "Informe uma tarefa", Toast.LENGTH_SHORT).show();
            }else{
                bancoDados.execSQL("INSERT INTO tarefas(tarefa) VALUES('" + texto + "')");
                Toast.makeText(this, "Tarefa salva com sucesso", Toast.LENGTH_SHORT).show();
                recuperarTarefas();
                textoTarefa.setText("");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void recuperarTarefas(){

        try{

            //Recpera as tarefas
            Cursor cursor = bancoDados.rawQuery("SELECT * FROM tarefas ORDER BY id DESC", null);

            //Recupera os ids das tarefas
            int indiceColunaId = cursor.getColumnIndex("id");
            int indiceColunaTarefa = cursor.getColumnIndex("tarefa");

            //Criar Adaptador
            itens = new ArrayList<String>();
            ids = new ArrayList<Integer>();
            itensAdaptador = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_list_item_2,
                    android.R.id.text2,
                    itens);

            listaTarefas.setAdapter(itensAdaptador);

            //Listar as tarefas
            cursor.moveToFirst();
            while (cursor != null){

                //Log.i("Resultado - ", "tarefa: " + cursor.getString(indiceColunaTarefa));
                itens.add(cursor.getString(indiceColunaTarefa));
                ids.add(Integer.parseInt(cursor.getString(indiceColunaId)));
                cursor.moveToNext();
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void removerTarefa(Integer id){

        try{

            bancoDados.execSQL("DELETE FROM tarefas WHERE id = "+id);
            recuperarTarefas();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
