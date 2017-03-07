package abrahamlay.kknp2015.warungsosro;

/**
 * Created by Abraham on 03/07/2015.
 */
public class Warung {
    public String idWarung;
    //public String Nama_Distributor;
    public String Nama_Agen;
    public String Nama_Warung;
    public String Alamat;
    public String Telp;
    public String Kota;
    public String Jenis_Warung;
    public String Status_Warung;



    public Warung(String idWarung,
                  String Nm_Agen,
                  String Nm_Warung,
                  String Status_wrg,
                  String jenis_wrg,
                  String alamat,
                  String telp,
                  String kota){
        this.idWarung=idWarung;
        this.Nama_Agen=Nm_Agen;
        this.Nama_Warung = Nm_Warung;
        this.Status_Warung=Status_wrg;
        this.Jenis_Warung=jenis_wrg;
        this.Alamat=alamat;
        this.Telp=telp;
        this.Kota=kota;
    }
public Warung(){}
}
