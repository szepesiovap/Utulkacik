package sk.upjs.ics.utulkacik.activity;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
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

import sk.upjs.ics.utulkacik.Docaskar;
import sk.upjs.ics.utulkacik.sql.Provider;
import sk.upjs.ics.utulkacik.R;

public class ZoznamDocaskarovActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    private Docaskar docaskar;
    private ListView docaskariListView;
    private ListView drawerListView;
    private SimpleCursorAdapter listViewAdapter;
    private EditText filterEditText;
    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoznam_docaskarov);
        setTitle(getString(R.string.docaskari));
        loaderCallbacks = this;

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerListView = (ListView)  findViewById(R.id.drawerListView);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerListView.setOnItemClickListener(this);

        docaskariListView = (ListView) findViewById(R.id.docaskariListView);
        filterEditText = (EditText) findViewById(R.id.filterEditText);
        filterEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pattern = filterEditText.getText().toString();
                pattern = pattern.trim();
                String selection = " meno LIKE ? AND _id > 2";
                String [] selectionArgs = { "%"+pattern+"%"};
                Bundle args = new Bundle();
                args.putString(getString(R.string.selection), selection);
                args.putStringArray(getString(R.string.selectionArgs), selectionArgs);
                getLoaderManager().restartLoader(0, args, loaderCallbacks);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        Bundle args = new Bundle();
        //  1,2 - "v utulku", "neevidovany" - nezobrazuju sa v zozname docaskarov
        String selection = " _id > 2 ";
        args.putString(getString(R.string.selection), selection);
        getLoaderManager().initLoader(0, args, this);

        String[] from = {Provider.Docaska.MENO};
        int[] to = {android.R.id.text1};

        listViewAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1,
                null, from, to, 0);

        //getLoaderManager().initLoader(0, Bundle.EMPTY, this);

        docaskariListView.setAdapter(listViewAdapter);


        docaskariListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = (Cursor) docaskariListView.getAdapter().getItem(position);

                Integer idD =c.getInt(0);
                String meno =c.getString(1);
                String kontakt = c.getString(2);

                docaskar = new Docaskar();
                docaskar.setId(idD);
                docaskar.setMeno(meno);
                docaskar.setKontakt(kontakt);

                zobrazDetailDocaskara();
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(getString(R.string.docaskar), docaskar);
    }

    private void zobrazDetailDocaskara() {
        Intent i = new Intent(getApplicationContext(), DetailDocaskaraActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.docaskar), docaskar);
        i.putExtras(bundle);
        startActivity(i);
    }

    public void pridajDocaskara(View view) {
        Intent i = new Intent(getApplicationContext(), DetailDocaskaraActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.docaskar), new Docaskar());
        i.putExtras(bundle);
        startActivity(i);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader;
        if (args != null) {
            String[] projection = args.getStringArray(getString(R.string.projection));
            String selection = args.getString(getString(R.string.selection));
            String[] selectionArgs = args.getStringArray(getString(R.string.selectionArgs));
            String sortOrder = args.getString(getString(R.string.sortOrder));
            loader = new CursorLoader(this, Provider.CONTENT_URI_DOCASKAR,projection,selection,selectionArgs, sortOrder);
        } else {
            loader = new CursorLoader(this);
            loader.setUri(Provider.CONTENT_URI_DOCASKAR);
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
            finish();
        } else if (selectedItemTitle.equals(getString(R.string.docaskari))){
            //nada
        }
        drawerLayout.closeDrawers();
    }
}
