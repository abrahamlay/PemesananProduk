package abrahamlay.kknp2015.warungsosro;

/**
 * Created by Abraham on 14/07/2015.
 */
public class Barang {
    private int idBarang;
    private String jenis;
    private String nama_barang;
    private int stok_agen;
    public Barang(){}

    public Barang(int id, String jenis,String name,int stok){
        this.idBarang = id;
        this.jenis=jenis;
        this.nama_barang = name;
        this.stok_agen=stok;
    }

    public void setIdBarang(int id){
        this.idBarang = id;
    }

    public void setNamaBarang(String name){
        this.nama_barang = name;
    }

    public int getIdBarang(){
        return this.idBarang;
    }

    public String getNamaBarang(){
        return this.nama_barang;
    }

    public int getStokAgen(){
        return this.stok_agen;
    }

}
