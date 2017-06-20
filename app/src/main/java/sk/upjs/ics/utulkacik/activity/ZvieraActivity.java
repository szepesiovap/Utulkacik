package sk.upjs.ics.utulkacik.activity;

import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.Calendar;

import sk.upjs.ics.utulkacik.DocaskaSpinnerAdapter;
import sk.upjs.ics.utulkacik.sql.Provider;
import sk.upjs.ics.utulkacik.R;
import sk.upjs.ics.utulkacik.Zviera;

public class ZvieraActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private Zviera zviera;
    private EditText menoEditText;
    private RadioGroup pohlavieRadioGroup;
    private RadioGroup druhRadioGroup;
    private EditText rasaEditText;
    private EditText popisEditText;
    private Spinner rokSpinner;
    private Spinner docaskaSpinner;
    private Cursor cursor;
    private DocaskaSpinnerAdapter docaskaSpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zviera);

        popisEditText = (EditText) findViewById(R.id.popisEditText);
        menoEditText = (EditText) findViewById(R.id.menoEditText);
        pohlavieRadioGroup = (RadioGroup) findViewById(R.id.pohlavieRadioGroup);
        druhRadioGroup = (RadioGroup) findViewById(R.id.druhRadioGroup);
        rasaEditText = (EditText) findViewById(R.id.rasaEditText);
        rokSpinner = (Spinner) findViewById(R.id.rokSpinner);
        docaskaSpinner = (Spinner) findViewById(R.id.docaskaSpinner);

        //na vyber poslednych 100 rokov
        Integer[] roky = new Integer[100];
        int aktulanyRok = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i < 100; i++) {
            roky[i] = aktulanyRok - i;
        }
        ArrayAdapter<Integer> rokAdapter = new ArrayAdapter<Integer>(this,
                R.layout.spinner_item, roky);
        rokSpinner.setAdapter(rokAdapter);

        //umiestnenie
        String[] from = {Provider.Docaska.MENO};
        int[] to = {android.R.id.text1};
        docaskaSpinnerAdapter = new DocaskaSpinnerAdapter(this, R.layout.spinner_item, null, from, to);
        docaskaSpinner.setAdapter(docaskaSpinnerAdapter);

        Bundle bundle = getIntent().getExtras();
        if (savedInstanceState != null) {
            zviera = (Zviera) savedInstanceState.getSerializable(getString(R.string.zviera));
        } else if ((bundle != null) && (zviera == null)) {
            zviera = (Zviera) bundle.getSerializable(getString(R.string.zviera));
        } else {
            zviera = new Zviera();
        }

        if (zviera.getId() != null) {
            menoEditText.setText(zviera.getMeno());
            rokSpinner.setSelection(rokAdapter.getPosition(zviera.getRok()));
            if (zviera.getPohlavie().equals(getString(R.string.samec))) {
                pohlavieRadioGroup.check(R.id.samecRadioButton);
            } else {
                pohlavieRadioGroup.check(R.id.samicaRadioButton);
            }
            if (zviera.getDruh().equals(getString(R.string.pes))) {
                druhRadioGroup.check(R.id.pesRadioButton);
            } else if (zviera.getDruh().equals(getString(R.string.macka))) {
                druhRadioGroup.check(R.id.mackaRadioButton);
            } else {
                druhRadioGroup.check(R.id.ineZvieraRadioButton);
            }
            rasaEditText.setText(zviera.getRasa());
            popisEditText.setText(zviera.getPopis());
        }
        //nacitanie docaskarov
        getLoaderManager().initLoader(0, null, this);
    }


    public void ulozZvieratko() {
        //meno
        zviera.setMeno(menoEditText.getText().toString());
        //rok narodenia
        zviera.setRok((int) rokSpinner.getSelectedItem());
        //pohlavie
        if (pohlavieRadioGroup.getCheckedRadioButtonId() == R.id.samecRadioButton) {
            zviera.setPohlavie(getString(R.string.samec));
        } else {
            zviera.setPohlavie(getString(R.string.samica));
        }
        //druh
        if (druhRadioGroup.getCheckedRadioButtonId() == R.id.pesRadioButton) {
            zviera.setDruh(getString(R.string.pes));
        } else if (druhRadioGroup.getCheckedRadioButtonId() == R.id.mackaRadioButton) {
            zviera.setDruh(getString(R.string.macka));
        } else {
            zviera.setDruh(getString(R.string.ine));
        }
        //rasa
        zviera.setRasa(rasaEditText.getText().toString());
        //popis
        zviera.setPopis(popisEditText.getText().toString());
        //umiestnenie
        cursor.moveToPosition(docaskaSpinner.getSelectedItemPosition());
        int id = cursor.getInt(cursor.getColumnIndex(Provider.Docaska._ID));
        zviera.setDocaskar(id);

        ContentValues values = new ContentValues();
        values.put(Provider.Zvieratko.MENO, zviera.getMeno());
        values.put(Provider.Zvieratko.POHLAVIE, zviera.getPohlavie());
        values.put(Provider.Zvieratko.DRUH_ZVIERATA, zviera.getDruh());
        values.put(Provider.Zvieratko.RASA, zviera.getRasa());
        values.put(Provider.Zvieratko.POPIS, zviera.getPopis());
        values.put(Provider.Zvieratko.ID_DOCASKAR, zviera.getDocaskar());
        values.put(Provider.Zvieratko.ROK_NAR, zviera.getRok());

        AsyncQueryHandler handler = new AsyncQueryHandler(getContentResolver()) {
            //nada
        };
        if (zviera.getId() == null) {
            handler.startInsert(0, null, Provider.CONTENT_URI_ZVIERA, values);
        } else {
            Uri uri = Uri.withAppendedPath(Provider.CONTENT_URI_ZVIERA, "" + zviera.getId());
            handler.startUpdate(0, null, uri, values, null, null);
        }

        Intent intent = new Intent();
        Bundle extras = new Bundle();
        extras.putSerializable(getString(R.string.zviera), zviera);
        intent.putExtras(extras);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(getString(R.string.zviera), zviera);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader;
        if (args != null) {
            String[] projection = args.getStringArray(getString(R.string.projection));
            String selection = args.getString(getString(R.string.selection));
            String[] selectionArgs = args.getStringArray(getString(R.string.selectionArgs));
            String sortOrder = args.getString(getString(R.string.sortOrder));
            loader = new CursorLoader(this, Provider.CONTENT_URI_DOCASKAR, projection, selection, selectionArgs, sortOrder);
        } else {
            loader = new CursorLoader(this);
            loader.setUri(Provider.CONTENT_URI_DOCASKAR);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursor = data;
        docaskaSpinnerAdapter.swapCursor(data);

        int position = 1;
        if (zviera.getId() == null) {
            position = 0;
        } else {
            while (data.moveToNext()) {
                if (cursor.getInt(cursor.getColumnIndex(Provider.Docaska._ID)) == zviera.getDocaskar()) {
                    position = cursor.getPosition();
                }
            }
        }
        docaskaSpinner.setSelection(position);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // nada
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.uloz_zviera_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ulozMenuItem) {
            // potvrd vymazanie
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Uložiť zmeny?")
                    .setMessage("Naozaj chcete uložiť vykonané zmeny?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ulozZvieratko();
                            finish();
                        }
                    }).setNegativeButton("Zrušiť", null).show();
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }

}
