package sk.upjs.ics.utulkacik.activity;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import sk.upjs.ics.utulkacik.sql.Provider;
import sk.upjs.ics.utulkacik.R;
import sk.upjs.ics.utulkacik.Zviera;

public class DetailZvierataActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Zviera zviera;
    private FloatingActionButton editFAB;
    private TextView idTextView;
    private TextView menoTextView;
    private TextView rokTextView;
    private TextView pohlavieTextView;
    private TextView popisTextView;
    private TextView druhTextView;
    private TextView rasaTextView;
    private TextView miestoTextView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_zvierata);

        Bundle bundle = getIntent().getExtras();

        if (savedInstanceState != null) {
            zviera = (Zviera) savedInstanceState.getSerializable(getString(R.string.zviera));
        } else if ((bundle != null) && (zviera == null)) {
            zviera = (Zviera) bundle.getSerializable(getString(R.string.zviera));
        } else {
            zviera = new Zviera();
        }

        idTextView = (TextView) findViewById(R.id.idTextView);
        menoTextView = (TextView) findViewById(R.id.menoTextView);
        rokTextView = (TextView) findViewById(R.id.rokTextView);
        pohlavieTextView = (TextView) findViewById(R.id.pohlavieTextView);
        popisTextView = (TextView) findViewById(R.id.popisTextView);
        druhTextView = (TextView) findViewById(R.id.druhTextView);
        rasaTextView = (TextView) findViewById(R.id.rasaTextView);
        miestoTextView = (TextView) findViewById(R.id.miestoTextView);
        imageView = (ImageView) findViewById(R.id.imageView);

        getLoaderManager().initLoader(0, null, this);

        editFAB = (FloatingActionButton) findViewById(R.id.editFAB);
        editFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (zviera == null) {
                    zviera = new Zviera();
                }
                Intent i = new Intent(getApplicationContext(), ZvieraActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(getString(R.string.zviera), zviera);
                i.putExtras(bundle);
                startActivityForResult(i, 1);
            }
        });

        initInfo();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (((requestCode == 1) || (requestCode == 0)) && (resultCode == RESULT_OK)) {
            Zviera zvieraResult = (Zviera) data.getSerializableExtra(getString(R.string.zviera));
            if (zvieraResult != null) {
                zviera = zvieraResult;
            }
        }
        initInfo();
    }

    private void initInfo() {
        idTextView.setText(zviera.getId().toString());
        menoTextView.setText(zviera.getMeno());
        rokTextView.setText(zviera.getRok().toString());
        pohlavieTextView.setText(zviera.getPohlavie());
        popisTextView.setText(zviera.getPopis());
        druhTextView.setText(zviera.getDruh());
        rasaTextView.setText(zviera.getRasa());
        getLoaderManager().restartLoader(0,null, this);
        Picasso.with(this).load(zviera.getFotka()).into(imageView);
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
        //cursor = data;
        if (zviera != null) {
            int position = 1;
            while(data.moveToNext()){
                if (data.getInt(data.getColumnIndex(Provider.Docaska._ID))==zviera.getDocaskar()){
                    position = data.getPosition();
                }
            }
            data.moveToPosition(position);
            miestoTextView.setText(data.getString(data.getColumnIndex(Provider.Docaska.MENO)));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // nada
    }

    public void otvorGaleriu(View view) {
        Intent i = new Intent(getApplicationContext(), GaleriaActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.zviera), zviera);
        i.putExtras(bundle);
        startActivityForResult(i, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.zviera_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.vymazMenuItem) {
            // potvrd vymazanie
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Vymazať zviera?")
                    .setMessage("Naozaj chcete vymazať zviera s menom " + zviera.getMeno() + "?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            vymaz();
                            finish();
                        }
                    }).setNegativeButton("Zrušiť", null).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void vymaz() {
        AsyncQueryHandler handler = new AsyncQueryHandler(getContentResolver()) {
            //nada
        };

        if (zviera.getId() != null) {
            Uri uri = Uri.withAppendedPath(Provider.CONTENT_URI_ZVIERA, "" + zviera.getId());
            handler.startDelete(0, null, uri, null, null);
        }
        finish();
    }


}
