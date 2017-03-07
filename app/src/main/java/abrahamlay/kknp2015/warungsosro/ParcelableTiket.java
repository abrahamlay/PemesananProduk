package abrahamlay.kknp2015.warungsosro;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Abraham on 09/07/2015.
 */
public class ParcelableTiket implements Parcelable {
    private Ticket tiket;

    public ParcelableTiket(Ticket dataTiket) {
        super();
        this.tiket = dataTiket;
    }

    public Ticket getTiket() {
        return tiket;
    }

    private ParcelableTiket(Parcel in) {
        tiket = new Ticket();
        tiket.idTiket = in.readString();
        tiket.Nama_Distributor = in.readString();
        tiket.Nama_Agen = in.readString();
        tiket.Nama_Warung = in.readString();
        tiket.Alamat = in.readString();
        tiket.Telp = in.readString();
        tiket.Kota = in.readString();
        tiket.Waktu_Request = in.readString();
        tiket.Waktu_Terima = in.readString();
        tiket.Status = in.readString();
        tiket.Nama_barang = in.readString();
        tiket.Jumlah_Pesan = in.readString();


    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tiket.idTiket);
        dest.writeString(tiket.Nama_Distributor);
        dest.writeString(tiket.Nama_Agen);
        dest.writeString(tiket.Nama_Warung);

        dest.writeString(tiket.Alamat);
        dest.writeString(tiket.Telp);
        dest.writeString(tiket.Kota);
        dest.writeString(tiket.Waktu_Request);
        dest.writeString(tiket.Waktu_Terima);
        dest.writeString(tiket.Status);
        dest.writeString(tiket.Nama_barang);
        dest.writeString(tiket.Jumlah_Pesan);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ParcelableTiket createFromParcel(Parcel in) {
            return new ParcelableTiket(in);
        }

        public ParcelableTiket[] newArray(int size) {
            return new ParcelableTiket[size];
        }
    };

}