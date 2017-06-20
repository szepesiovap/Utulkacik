package sk.upjs.ics.utulkacik;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class GaleriaGridViewAdapter extends BaseAdapter {
    private Context context;
    private final List<String> cesty;

    public GaleriaGridViewAdapter(Context context, List<String> cesty) {
        this.context = context;
        this.cesty = cesty;
    }

    @Override
    public int getCount() {
        return cesty.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View gridView;

        gridView = inflater.inflate(R.layout.fotka_item, null);
        ImageView imageView = (ImageView) gridView.findViewById(R.id.image);

        String cesta = cesty.get(position);
        System.out.println("CESTA " + cesta);
        Picasso.with(context).load(cesta).into(imageView);

        return gridView;
    }


}
