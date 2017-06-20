package sk.upjs.ics.utulkacik.sql;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

public class UtulkacikContentProvider extends ContentProvider {

    private UtulkacikOpenHelper openHelper;
    private static final UriMatcher uriMatcher;

    private static final int ZVIERA = 1;
    private static final int DOCASKAR = 2;
    private static final int ZVIERA_ITEM = 3;
    private static final int DOCASKAR_ITEM = 4;
    private static final int FOTKA = 5;
    private static final int FOTKA_ITEM = 6;


    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Provider.AUTHORITY, Provider.Zvieratko.ZVIERA_TABLE_NAME , ZVIERA);
        uriMatcher.addURI(Provider.AUTHORITY, Provider.Docaska.DOCASKAR_TABLE_NAME , DOCASKAR);
        uriMatcher.addURI(Provider.AUTHORITY, Provider.Zvieratko.ZVIERA_TABLE_NAME + "/#" , ZVIERA_ITEM);
        uriMatcher.addURI(Provider.AUTHORITY, Provider.Docaska.DOCASKAR_TABLE_NAME + "/#", DOCASKAR_ITEM);
        uriMatcher.addURI(Provider.AUTHORITY, Provider.Fotka.FOTKA_TABLE_NAME, FOTKA);
        uriMatcher.addURI(Provider.AUTHORITY, Provider.Fotka.FOTKA_TABLE_NAME + "/#", FOTKA_ITEM);
    }

    public UtulkacikContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int pocet = 0;
        SQLiteDatabase db = this.openHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ZVIERA_ITEM:
                pocet = db.delete(Provider.Zvieratko.ZVIERA_TABLE_NAME,
                        "_id = " + uri.getPathSegments().get(1)
                                + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : ""),
                        selectionArgs);
                break;
            case DOCASKAR_ITEM:
                pocet = db.delete(Provider.Docaska.DOCASKAR_TABLE_NAME,
                        "_id = " + uri.getPathSegments().get(1)
                                + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : ""),
                        selectionArgs);
                break;
            case FOTKA_ITEM:
                pocet = db.delete(Provider.Fotka.FOTKA_TABLE_NAME,
                        "_id = " + uri.getPathSegments().get(1)
                                + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : ""),
                        selectionArgs);
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return pocet;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri _uri = null;
        SQLiteDatabase db = this.openHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ZVIERA:
                long id1 = db.insert(Provider.Zvieratko.ZVIERA_TABLE_NAME, null, values);
                _uri = ContentUris.withAppendedId(Provider.CONTENT_URI_ZVIERA, id1);
                break;
            case DOCASKAR:
                long id2 = db.insert(Provider.Docaska.DOCASKAR_TABLE_NAME, null, values);
                _uri = ContentUris.withAppendedId(Provider.CONTENT_URI_DOCASKAR, id2);
                break;
            case FOTKA:
                long id3 = db.insert(Provider.Fotka.FOTKA_TABLE_NAME, null, values);
                _uri = ContentUris.withAppendedId(Provider.CONTENT_URI_FOTKA, id3);
                break;
        }
        getContext().getContentResolver().notifyChange(_uri, null);
        return _uri;
    }

    @Override
    public boolean onCreate() {
        openHelper = new UtulkacikOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        SQLiteDatabase db = this.openHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ZVIERA:
                cursor = db.query(Provider.Zvieratko.ZVIERA_TABLE_NAME,
                        projection, selection, selectionArgs, null, null, null);
                cursor.setNotificationUri(getContext().getContentResolver(), Provider.CONTENT_URI_ZVIERA);
                break;
            case DOCASKAR:
                cursor = db.query(Provider.Docaska.DOCASKAR_TABLE_NAME,
                        projection, selection, selectionArgs, null, null, null);
                cursor.setNotificationUri(getContext().getContentResolver(), Provider.CONTENT_URI_DOCASKAR);
                break;
            case FOTKA:
                cursor = db.query(Provider.Fotka.FOTKA_TABLE_NAME,
                        projection, selection, selectionArgs, null, null, null);
                cursor.setNotificationUri(getContext().getContentResolver(), Provider.CONTENT_URI_FOTKA);
                break;
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int pocet = 0;
        SQLiteDatabase db = this.openHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ZVIERA_ITEM:
                pocet = db.update(Provider.Zvieratko.ZVIERA_TABLE_NAME, values,
                        "_id = " + uri.getPathSegments().get(1)
                                + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : ""),
                        selectionArgs);
                break;
            case DOCASKAR_ITEM:
                pocet = db.update(Provider.Docaska.DOCASKAR_TABLE_NAME, values,
                        "_id = " + uri.getPathSegments().get(1)
                                + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : ""),
                        selectionArgs);
                break;
            case FOTKA_ITEM:
                pocet = db.update(Provider.Fotka.FOTKA_TABLE_NAME, values,
                        "_id = " + uri.getPathSegments().get(1)
                                + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : ""),
                        selectionArgs);
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return pocet;
    }
}
