package sk.upjs.ics.utulkacik;

import java.io.Serializable;
import java.sql.Date;

public class Zviera implements Serializable {

    private Integer id;
    private String meno;
    private Integer rok;
    private String pohlavie;
    private String popis;
    private String druh;
    private String rasa;
    private Integer docaskar;
    private String fotka;

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

    public Integer getRok() {
        return rok;
    }

    public void setRok(Integer rok) {
        this.rok = rok;
    }

    public String getPohlavie() {
        return pohlavie;
    }

    public void setPohlavie(String pohlavie) {
        this.pohlavie = pohlavie;
    }

    public String getPopis() {
        return popis;
    }

    public void setPopis(String popis) {
        this.popis = popis;
    }

    public String getDruh() {
        return druh;
    }

    public void setDruh(String druh) {
        this.druh = druh;
    }

    public String getRasa() {
        return rasa;
    }

    public void setRasa(String rasa) {
        this.rasa = rasa;
    }

    public Integer getDocaskar() {
        return docaskar;
    }

    public void setDocaskar(Integer docaskar) {
        this.docaskar = docaskar;
    }

    public String getFotka() {
        return fotka;
    }

    public void setFotka(String fotka) {
        this.fotka = fotka;
    }
}
