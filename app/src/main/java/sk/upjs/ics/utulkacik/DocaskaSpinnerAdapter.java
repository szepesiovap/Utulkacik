package sk.upjs.ics.utulkacik;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import sk.upjs.ics.utulkacik.sql.Provider;

public class DocaskaSpinnerAdapter extends SimpleCursorAdapter{


    public DocaskaSpinnerAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView text = (TextView) view.findViewById(R.id.textView);
        int menoCol = cursor.getColumnIndex(Provider.Docaska.MENO);
        String meno = cursor.getString(menoCol);
        text.setText(meno);
    }
}
