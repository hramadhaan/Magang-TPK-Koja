package com.example.tpkkoja.Content;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tpkkoja.R;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

public class ListUpload extends AppCompatActivity {

    ListView listView;
    ArrayAdapter<String> adapter;
    Toolbar toolbar;
    TextView judul;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_upload);

        toolbar = findViewById(R.id.list_toolbar);
        judul = toolbar.findViewById(R.id.list_judul);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.list_upload);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        new Connection().execute();
    }

    class Connection extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            String host = "https://tpkkoja.nyoobie.com/connect/mobile/lihat.php";

            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(host));
                HttpResponse response = client.execute(request);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                StringBuffer stringBuffer = new StringBuffer("");

                String line= "";

                while ((line = reader.readLine())!=null){
                    stringBuffer.append(line);
                    break;
                }
                reader.close();
                result = stringBuffer.toString();

            } catch (Exception e) {
                return new String("There exceptopn"+e.getMessage());
            }


            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONObject jsonObject = new JSONObject(result);
                int success = jsonObject.getInt("success");
                if (success == 1) {
                    JSONArray upload = jsonObject.getJSONArray("upload");
                    for (int i=0; i<upload.length();i++){
                        JSONObject up = upload.getJSONObject(i);
                        String name = up.getString("nama");
                        String description = up.getString("deskripsi");
                        String line = name + " - " + description;
                        adapter.add(line);

                    }
                } else {
                    Toast.makeText(ListUpload.this,"No Data",Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
            }
        }
    }
}
