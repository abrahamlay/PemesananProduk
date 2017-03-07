package abrahamlay.kknp2015.warungsosro;

/**
 * Created by Abraham on 03/07/2015.
 */
public class Agen {
    public String idAgen;
    public String Nama_Distributor;
    public String Nama_Agen;
  //  public String Nama_Warung;
    public String Alamat;
    public String Telp;
    public String Kota;
    public String Jenis_Agen;
    public String Status_Agen;



    public Agen(String idAgen,
                String Nm_Distributor,
                String Nm_Agen,
//                String Nm_Warung,
                String Status_Agen,
                String jenis_Agen,
                String alamat,
                String telp,
                String kota){
        this.idAgen=idAgen;
        this.Nama_Distributor=Nm_Distributor;
        this.Nama_Agen=Nm_Agen;
       // this.Nama_Warung = Nm_Warung;
        this.Status_Agen=Status_Agen;
        this.Jenis_Agen=jenis_Agen;
        this.Alamat=alamat;
        this.Telp=telp;
        this.Kota=kota;
    }

    public void setIdAgen(String id){
        this.idAgen = id;
    }

    public void setNamaAgen(String name){
        this.Nama_Agen = name;
    }

    public String getIdAgen(){
        return this.idAgen;
    }

    public String getNamaAgen(){
        return this.Nama_Agen;
    }


public Agen(){}
}
