package sk.upjs.ics.utulkacik.activity;

import android.Manifest;
import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.commonsware.cwac.cam2.CameraActivity;
import com.commonsware.cwac.cam2.CameraEngine;

import java.io.File;
import java.util.ArrayList;

import sk.upjs.ics.utulkacik.GaleriaGridViewAdapter;
import sk.upjs.ics.utulkacik.R;
import sk.upjs.ics.utulkacik.Zviera;
import sk.upjs.ics.utulkacik.sql.Provider;

public class GaleriaActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Zviera zviera;
    private Context context;
    private GaleriaGridViewAdapter adapter;
    private ArrayList<String> listFotiek;
    private GridView gridView;
    private int cisloFotky = 0;
    private static final int PERMISSION_CAMERA = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);

        context = this;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            zviera = (Zviera) bundle.getSerializable(getString(R.string.zviera));
        }

        Bundle args = new Bundle();
        String selection = " zviera =  ? ";
        args.putString(getString(R.string.selection), selection);
        String[] selectionArgs = {zviera.getId().toString()};
        args.putStringArray(getString(R.string.selectionArgs), selectionArgs);
        getLoaderManager().initLoader(0, args, this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, "Obrázok bol zvolený za profilový.", Toast.LENGTH_LONG).show();
                nastavProfilovku(position);
            }
        });
        listFotiek = new ArrayList<>();
    }

    public void pridajFotku(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA);
        } else {
            odfot();
        }
    }

    private void odfot() {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        cisloFotky++;
        File image = new File(dir, "utulkacik/" + zviera.getMeno() + zviera.getId() + "-" + cisloFotky + ".jpg");
        Intent intent = new CameraActivity.IntentBuilder(this).skipConfirm().forceEngine(CameraEngine.ID.CLASSIC)
                .to(image).updateMediaStore().build();
        startActivityForResult(intent, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    odfot();
                } else {
                    //nada
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == 0) && (resultCode == RESULT_OK)) {
            Uri cestaKObrazku = data.getData();
            if (data != null) {
                AsyncQueryHandler handler = new AsyncQueryHandler(getContentResolver()) {
                };
                ContentValues values = new ContentValues();
                values.put(Provider.Fotka.CESTA, cestaKObrazku.toString());
                values.put(Provider.Fotka.ZVIERA, zviera.getId());
                handler.startInsert(0, null, Provider.CONTENT_URI_FOTKA, values);
            }
        }
        Bundle args = new Bundle();
        String selection = " zviera =  ? ";
        args.putString(getString(R.string.selection), selection);
        String[] selectionArgs = {zviera.getId().toString()};
        args.putStringArray(getString(R.string.selectionArgs), selectionArgs);
        getLoaderManager().restartLoader(0, args, this);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        Bundle extras = new Bundle();
        extras.putSerializable(getString(R.string.zviera), zviera);
        intent.putExtras(extras);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    private void nastavProfilovku(int position) {
        zviera.setFotka(listFotiek.get(position));
        ContentValues values = new ContentValues();
        values.put(Provider.Zvieratko.FOTKA, listFotiek.get(position));

        AsyncQueryHandler handler = new AsyncQueryHandler(getContentResolver()) {
            //nada
        };

        Uri uri = Uri.withAppendedPath(Provider.CONTENT_URI_ZVIERA, "" + zviera.getId());
        handler.startUpdate(0, null, uri, values, null, null);

    }


    /*@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(getString(R.string.zviera), zviera);
    }*/


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader;
        if (args != null) {
            String[] projection = args.getStringArray(getString(R.string.projection));
            String selection = args.getString(getString(R.string.selection));
            String[] selectionArgs = args.getStringArray(getString(R.string.selectionArgs));
            String sortOrder = args.getString(getString(R.string.sortOrder));
            loader = new CursorLoader(this, Provider.CONTENT_URI_FOTKA, projection, selection, selectionArgs, sortOrder);
        } else {
            loader = new CursorLoader(this);
            loader.setUri(Provider.CONTENT_URI_FOTKA);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        listFotiek.clear();
        data.moveToPosition(-1);
        String nazov = null;

        while (data.moveToNext()) {
            String cesta = data.getString(data.getColumnIndex(Provider.Fotka.CESTA));
            //testuje sa existencia suboru
            //treba odstranit "file:"
            if (cesta != null) {
                String[] token = cesta.split(":");
                if (token.length > 1) {
                    nazov = token[token.length-1];
                    if (new File(nazov).isFile()) {
                        listFotiek.add(cesta);
                    }
                }
            }
            if (nazov != null) {
                String cisloFotkySPriponou = nazov.split("-")[1];
                int cislo = Integer.parseInt(cisloFotkySPriponou.replaceAll("[^0-9]", ""));
                if (cislo > cisloFotky) {
                    cisloFotky = cislo;
                }
            }
        }
        adapter = new GaleriaGridViewAdapter(this, listFotiek);
        gridView.setAdapter(adapter);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        gridView.setAdapter(null);
    }

}
