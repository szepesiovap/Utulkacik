package sk.upjs.ics.utulkacik.activity;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import sk.upjs.ics.utulkacik.Docaskar;
import sk.upjs.ics.utulkacik.sql.Provider;
import sk.upjs.ics.utulkacik.R;

public class DetailDocaskaraActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private Docaskar docaskar;
    private EditText menoEditText;
    private EditText kontaktEditText;
    private ListView zvierataListView;
    private SimpleCursorAdapter zvierataAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_docaskara);

        Bundle bundle = getIntent().getExtras();
        if (savedInstanceState != null) {
            docaskar = (Docaskar) savedInstanceState.getSerializable(getString(R.string.docaskar));
        } else if ((bundle != null) && (docaskar == null)) {
            docaskar = (Docaskar) bundle.getSerializable(getString(R.string.docaskar));
        } else {
            docaskar = new Docaskar();
        }

        menoEditText = (EditText) findViewById(R.id.menoEditText);
        kontaktEditText = (EditText) findViewById(R.id.kontaktEditText);
        zvierataListView = (ListView) findViewById(R.id.zvierataListView);

        String[] from = {Provider.Zvieratko.MENO};
        int[] to = {android.R.id.text1};

        zvierataAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, from, to, 0);
        zvierataListView.setAdapter(zvierataAdapter);

        menoEditText.setText(docaskar.getMeno());
        kontaktEditText.setText(docaskar.getKontakt());
        //pridelene zvierata
        if (docaskar.getId()!= null) {
            Bundle args = new Bundle();
            String selection = " docaskar = ?";
            String[] selectionArgs = {docaskar.getId().toString()};
            args.putString(getString(R.string.selection), selection);
            args.putStringArray(getString(R.string.selectionArgs), selectionArgs);
            getLoaderManager().initLoader(0, args, this);
        }
    }

    public void ulozDocaskara() {
        AsyncQueryHandler handler = new AsyncQueryHandler(getContentResolver()){
            //nada
        };

        docaskar.setMeno(menoEditText.getText().toString());
        docaskar.setKontakt(kontaktEditText.getText().toString());

        ContentValues values = new ContentValues();
        values.put(Provider.Docaska.MENO, docaskar.getMeno());
        values.put(Provider.Docaska.KONTAKT, docaskar.getKontakt());

        if (docaskar.getId() == null) {
            handler.startInsert(0,null,Provider.CONTENT_URI_DOCASKAR, values);
        } else {
            Uri uri = Uri.withAppendedPath(Provider.CONTENT_URI_DOCASKAR, ""+docaskar.getId());
            handler.startUpdate(0,null, uri, values, null, null);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader;
        if (args != null) {
            String[] projection = args.getStringArray(getString(R.string.projection));
            String selection = args.getString(getString(R.string.selection));
            String[] selectionArgs = args.getStringArray(getString(R.string.selectionArgs));
            String sortOrder = args.getString(getString(R.string.sortOrder));
            loader = new CursorLoader(this,Provider.CONTENT_URI_ZVIERA,projection,selection,selectionArgs, sortOrder);
        } else {
            loader = new CursorLoader(this);
            loader.setUri(Provider.CONTENT_URI_ZVIERA);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        zvierataAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        zvierataAdapter.swapCursor(null);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (docaskar.getId() == null) {
            menu.findItem(R.id.vymazMenuItem).setEnabled(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.docaskari_menu, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(getString(R.string.docaskar), docaskar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.vymazMenuItem) {
            // potvrd vymazanie
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Vymazať zviera?")
                    .setMessage("Naozaj chcete vymazať dočaskára s menom "+docaskar.getMeno()+"?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            vymaz();
                            finish();
                        }
                    }).setNegativeButton("Zrušiť", null).show();
            return true;
        } else if (item.getItemId() == R.id.ulozMenuItem) {
            // potvrd vymazanie
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Uložiť zmeny?")
                    .setMessage("Naozaj chcete uložiť vykonané zmeny?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ulozDocaskara();
                            finish();
                        }
                    }).setNegativeButton("Zrušiť", null).show();
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void vymaz() {
        AsyncQueryHandler handler = new AsyncQueryHandler(getContentResolver()) {
            //nada
        };

        if (docaskar.getId() != null) {
            Uri uri = Uri.withAppendedPath(Provider.CONTENT_URI_DOCASKAR, ""+docaskar.getId());
            handler.startDelete(0, null, uri, null, null);
        }
        finish();
    }

}
