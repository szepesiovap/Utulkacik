package sk.upjs.ics.utulkacik;

import java.io.Serializable;

public class Docaskar implements Serializable {
    private Integer id;
    private String meno;
    private String kontakt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMeno() {
        return meno;
    }

    public void setMeno(String meno) {
        this.meno = meno;
    }

    public String getKontakt() {
        return kontakt;
    }

    public void setKontakt(String kontakt) {
        this.kontakt = kontakt;
    }
}
