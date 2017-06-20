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

import java.io.File;

import sk.upjs.ics.utulkacik.sql.Provider;


public class ZoznamZvieratCursorAdapter extends SimpleCursorAdapter {


    public ZoznamZvieratCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.zoznam_zvierat_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView text = (TextView) view.findViewById(R.id.text);
        ImageView image = (ImageView) view.findViewById(R.id.img);

        String meno = cursor.getString(cursor.getColumnIndex(Provider.Zvieratko.MENO));
        text.setText(meno);

        String cesta = cursor.getString(cursor.getColumnIndex(Provider.Zvieratko.FOTKA));
        if(cesta != null) {
            String[] token = cesta.split(":");
            if ((token.length > 1)
                    && (new File(token[1]).isFile())) {
                Picasso.with(context).load(cesta).into(image);
                return;
            }
        }
            int druhIdx = cursor.getColumnIndex(Provider.Zvieratko.DRUH_ZVIERATA);
            String druh = cursor.getString(druhIdx);
            if (druh.equals(context.getString(R.string.pes))) {
                image.setImageResource(R.drawable.dog);
            } else if (druh.equals(context.getString(R.string.macka))) {
                image.setImageResource(R.drawable.cat);
            }
        }
}
