package com.example.requisieshttp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button botaoRecuperar;
    private TextView textoResultado;
    private EditText textoCep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botaoRecuperar = findViewById(R.id.buttonRecuperar);
        textoResultado = findViewById(R.id.textResultado);
        textoCep = findViewById(R.id.editTextCep);

        botaoRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyTask task = new MyTask();
                String cep = textoCep.getText().toString(); // Recuperar CEP do editText
                String urlApiCEP = "https://viacep.com.br/ws/" + cep + "/json/";
                task.execute(urlApiCEP);
            }
        });
    }

    class MyTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            String stringUrl = strings[0];
            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;

            // String para armazenar linha a linha da String
            StringBuffer buffer = null;

            try {
                URL url = new URL(stringUrl);

                //Criando e recuperando a requisição
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

                //Recuperando os dados que foram enviados (Em bytes)
                inputStream = conexao.getInputStream();

                //Decodificando os dados de byte para caracteres
                inputStreamReader = new InputStreamReader(inputStream);

                //Objeto para ler os caracteres anteriores
                BufferedReader reader = new BufferedReader(inputStreamReader);
                buffer = new StringBuffer();

                String linha = "";
                //Ler linha a linha
                while ( (linha = reader.readLine()) != null ){
                    //Adiciona cada linha em ordem
                    buffer.append(linha);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);

            //Recuperando dados
            String cep = null;
            String logradouro = null;
            String complemento = null;
            String bairro = null;
            String localidade = null;
            String uf = null;

            try {
                //Criando objeto JSon para recuperar dados
                JSONObject jsonObject = new JSONObject(resultado);

                //Passando a chave de cada item
                cep = jsonObject.getString("cep");
                logradouro = jsonObject.getString("logradouro");
                complemento = jsonObject.getString("complemento");
                bairro = jsonObject.getString("bairro");
                localidade = jsonObject.getString("localidade");
                uf = jsonObject.getString("uf");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            textoResultado.setText("CEP: " + cep + "\n" + "Logradouro: " + logradouro + "\n" +"Complemento: " + complemento +
                    "\n" + "Bairro: " + bairro + "\n" + "Localidade: " + localidade + "\n" + "UF: " + uf);
        }
    }
}