package sk.upjs.ics.utulkacik.sql;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


public interface Provider {


    public static final String AUTHORITY = "sk.upjs.ics.utulkacik";

    public static final Uri CONTENT_URI_ZVIERA =  new Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(AUTHORITY)
            .appendPath(Zvieratko.ZVIERA_TABLE_NAME)
            .build();

    public static final Uri CONTENT_URI_DOCASKAR =  new Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(AUTHORITY)
            .appendPath(Docaska.DOCASKAR_TABLE_NAME)
            .build();

    public static final Uri CONTENT_URI_FOTKA =  new Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(AUTHORITY)
            .appendPath(Fotka.FOTKA_TABLE_NAME)
            .build();

    public interface Docaska extends BaseColumns {
        //docaskari
        public static final String DOCASKAR_TABLE_NAME = "docaskar";
        public static final String MENO = "meno";
        public static final String KONTAKT = "kontakt";
    }

    public interface Zvieratko extends BaseColumns {
        //zvierata
        public static final String ZVIERA_TABLE_NAME = "zviera";
        public static final String MENO = "meno";
        public static final String ROK_NAR = "rok";
        public static final String POHLAVIE = "pohlavie";
        public static final String POPIS = "popis";
        public static final String DRUH_ZVIERATA = "druh";
        public static final String RASA = "rasa";
        public static final String FOTKA = "fotka";
        public static final String ID_DOCASKAR = "docaskar";

    }

    public interface Fotka extends BaseColumns {
        //fotky
        public static final String FOTKA_TABLE_NAME = "fotka";
        public static final String CESTA = "cesta";
        public static final String ZVIERA = "zviera";
    }

}
