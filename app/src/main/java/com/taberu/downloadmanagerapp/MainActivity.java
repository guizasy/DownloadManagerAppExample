package com.taberu.downloadmanagerapp;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    private long myDownloadId;
    TextView textView;

    private IntentFilter dci = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
    private BroadcastReceiver dcr = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0L);
            if (id == myDownloadId) {
                textView.setText("Finalizado ***");
                Log.i(TAG, "Download finalizado: " + myDownloadId);
            } else {
                Log.i(TAG, "Download nao relacionado" + id);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        this.registerReceiver(dcr, dci);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_download) {
            Intent intent = new Intent(MainActivity.this, HttpExampleActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.menu_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isNetworkAvailable() {
        boolean retVal;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            retVal = true;
        } else {
            retVal = false;
        }

        return retVal;
    }

    public void myClickHandler(View view) {
        DownloadManager dm;

        if (isNetworkAvailable()) {
            textView.setText("Iniciado +++");
            Uri uri = Uri.parse("http://robocupssl.cpe.ku.ac.th/_media/rules:ssl-rules-2015.pdf");
            DownloadManager.Request request = new DownloadManager.Request(uri);

            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);

            request.setTitle("Exemplo download");
            request.setDescription("Download somente no wi-fi");

            dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            myDownloadId = dm.enqueue(request);

            Log.i(TAG, "Pedido de download realizado: " + myDownloadId);
        }
    }


}
