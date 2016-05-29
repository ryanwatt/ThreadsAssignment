/*collaborators: Raleigh Wayland from class helped me and i watched Youtube tutorials about AsyncTask*/
package com.threadsassignment.ryanwatt.threadsassignment;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

   List<String> numList = new ArrayList<String>();
   ArrayAdapter<String> adapter;
   ProgressBar progressBar;
   Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    class Create extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            String fileName = "numbers.txt";
            String numbers;
            FileOutputStream outputStream;
            try {
                outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                for (int i = 1; i <= 10; i++){
                    numbers = String.valueOf(i) + "\n";
                    outputStream.write(numbers.getBytes());
                    publishProgress(i * 10);
                    Thread.sleep(250);
                }
                outputStream.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setProgress(0);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getApplicationContext(), "File written", Toast.LENGTH_SHORT).show();
        }
    }

    class Load extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            String fileName = "numbers.txt";
            FileInputStream inputStream;
            try {
                inputStream = openFileInput(fileName);
                InputStreamReader reader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String fileLine;
                for (int i = 1; (fileLine = bufferedReader.readLine()) != null; i++){
                    numList.add(fileLine);
                    publishProgress(i * 10);
                    Thread.sleep(250);
                }
            } catch (Exception ex){
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ListView numListView = (ListView) findViewById(R.id.numberListView);

            adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, numList);

            numListView.setAdapter(adapter);
        }
    }

    public void create(View vu) {
        new Create().execute();
    }

    public void load(View vu) {
        context = this;
        new Load().execute();
    }

    public void clear(View view) {
        adapter.clear();
        ListView newListView = (ListView) findViewById(R.id.numberListView);
        newListView.setAdapter(adapter);
        Toast.makeText(getApplicationContext(), "Cleared File", Toast.LENGTH_SHORT).show();
    }


}
