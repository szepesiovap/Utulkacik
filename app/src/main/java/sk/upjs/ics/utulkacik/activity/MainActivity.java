package sk.upjs.ics.utulkacik.activity;

import android.Manifest;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import sk.upjs.ics.utulkacik.sql.Provider;
import sk.upjs.ics.utulkacik.R;
import sk.upjs.ics.utulkacik.ZoznamZvieratCursorAdapter;
import sk.upjs.ics.utulkacik.Zviera;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    private ListView zvierataListView;
    private SimpleCursorAdapter listViewAdapter;
    private EditText filterEditText;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private ListView drawerListView;

    private static final int PERMISSION_READ = 0;
    private static final int PERMISSION_WRITE = 1;

    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(getString(R.string.zvierata));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ);
        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerListView = (ListView)  findViewById(R.id.drawerListView);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerListView.setOnItemClickListener(this);

        loaderCallbacks = this;
        zvierataListView = (ListView) findViewById(R.id.zvierataListView);
        filterEditText = (EditText) findViewById(R.id.filterEditText);
        filterEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pattern = filterEditText.getText().toString();
                pattern = pattern.trim();
                String selection = " meno LIKE ? ";
                String[] selectionArgs = {"%" + pattern + "%"};
                Bundle args = new Bundle();
                args.putString(getString(R.string.selection), selection);
                args.putStringArray(getString(R.string.selectionArgs), selectionArgs);
                getLoaderManager().restartLoader(0, args, loaderCallbacks);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        String[] from = {Provider.Zvieratko.MENO};
        int[] to = {android.R.id.text1};

        listViewAdapter = new ZoznamZvieratCursorAdapter(this, R.layout.zoznam_zvierat_item,
                null, from, to);
        getLoaderManager().initLoader(0, Bundle.EMPTY, this);

        zvierataListView.setAdapter(listViewAdapter);

        zvierataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = (Cursor) zvierataListView.getAdapter().getItem(position);

                Integer idZ = c.getInt(c.getColumnIndex(Provider.Zvieratko._ID));
                String meno = c.getString(c.getColumnIndex(Provider.Zvieratko.MENO));
                Integer rok = c.getInt(c.getColumnIndex(Provider.Zvieratko.ROK_NAR));
                String pohlavie = c.getString(c.getColumnIndex(Provider.Zvieratko.POHLAVIE));
                String popis = c.getString(c.getColumnIndex(Provider.Zvieratko.POPIS));
                String druh = c.getString(c.getColumnIndex(Provider.Zvieratko.DRUH_ZVIERATA));
                String rasa = c.getString(c.getColumnIndex(Provider.Zvieratko.RASA));
                Integer docaska = c.getInt(c.getColumnIndex(Provider.Zvieratko.ID_DOCASKAR));
                String fotka = c.getString(c.getColumnIndex(Provider.Zvieratko.FOTKA));

                Zviera zviera = new Zviera();
                zviera.setId(idZ);
                zviera.setMeno(meno);
                zviera.setRok(rok);
                zviera.setPohlavie(pohlavie);
                zviera.setPopis(popis);
                zviera.setDruh(druh);
                zviera.setRasa(rasa);
                zviera.setDocaskar(docaska);
                zviera.setFotka(fotka);

                Intent i = new Intent(getApplicationContext(), DetailZvierataActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(getString(R.string.zviera), zviera);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onPostCreate( Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_WRITE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //nada
                } else {
                    finish();
                }
            case PERMISSION_READ:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //nada
                } else {
                    finish();
                }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader;
        if (args != null) {
            String[] projection = args.getStringArray(getString(R.string.projection));
            String selection = args.getString(getString(R.string.selection));
            String[] selectionArgs = args.getStringArray(getString(R.string.selectionArgs));
            String sortOrder = args.getString(getString(R.string.projection));
            loader = new CursorLoader(this, Provider.CONTENT_URI_ZVIERA, projection, selection, selectionArgs, sortOrder);
        } else {
            loader = new CursorLoader(this);
            loader.setUri(Provider.CONTENT_URI_ZVIERA);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        listViewAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        listViewAdapter.swapCursor(null);
    }


    public void pridajZvieratko(View view) {
        Intent i = new Intent(getApplicationContext(), ZvieraActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.zviera), new Zviera());
        i.putExtras(bundle);
        startActivity(i);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String selectedItemTitle = (String) parent.getAdapter().getItem(position);
        if (selectedItemTitle.equals(getString(R.string.zvierata))){
            //nada
        } else if (selectedItemTitle.equals(getString(R.string.docaskari))){
            Intent i = new Intent(getApplicationContext(), ZoznamDocaskarovActivity.class);
            startActivity(i);
        }
        drawerLayout.closeDrawers();
    }
}
