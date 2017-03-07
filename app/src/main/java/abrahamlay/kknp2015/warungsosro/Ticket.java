package abrahamlay.kknp2015.warungsosro;

/**
 * Created by Abraham on 03/07/2015.
 */
public class Ticket {
    public String idTiket;
    public String Nama_Distributor;
    public String Nama_Agen;
    public String Nama_Warung;
    public String Alamat;
    public String Telp;
    public String Kota;
    public String Jumlah_Pesan;
    public String Nama_barang;


    public String Waktu_Request;
    public String Waktu_Terima;
    public String Status;



    public Ticket(String idTiket,
                  String Nm_Distributor,
                  String Nm_Agen ,
                  String Nm_Warung ,
                  String wkt_req ,
                  String wkt_terima,
                  String Status,
                  String alamat,
                  String telp,
                  String kota,String nm_brg,String jml_brg){
        this.idTiket=idTiket;
        this.Nama_Distributor=Nm_Distributor;
        this.Nama_Agen=Nm_Agen;
        this.Nama_Warung = Nm_Warung;
        this.Waktu_Request=wkt_req;
        this.Waktu_Terima=wkt_terima;
        this.Status=Status;
        this.Alamat=alamat;
        this.Telp=telp;
        this.Kota=kota;
        this.Jumlah_Pesan=jml_brg;
        this.Nama_barang=nm_brg;

    }
public Ticket(){}
}
