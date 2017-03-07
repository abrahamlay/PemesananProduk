package abrahamlay.kknp2015.warungsosro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Abraham on 03/07/2015.
 */
public class WarungAdapter extends ArrayAdapter {

    public Context context;
    public ArrayList<Warung> listWarung = null;


    public WarungAdapter(Context context, ArrayList<Warung> listWarung) {
        super(context, R.layout.item_warung,listWarung);
        this.context=context;
        this.listWarung=listWarung;
    }

    // Clean all elements of the recycler
    public void clear() {
        listWarung.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(ArrayList<Warung> list) {
        listWarung.addAll(list);
        notifyDataSetChanged();
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_warung, parent, false);

        TextView namaWarung = (TextView) rowView.findViewById(R.id.namaWarung);
        TextView namaPengelola = (TextView) rowView.findViewById(R.id.pengelola);
        TextView Telp = (TextView) rowView.findViewById(R.id.telp_warung);
        TextView Kota = (TextView) rowView.findViewById(R.id.kota_warung);

        TextView Status = (TextView) rowView.findViewById(R.id.status);
        LinearLayout bgstatus=(LinearLayout) rowView.findViewById(R.id.bgStatus);

        if(listWarung.get(position).Status_Warung.equals("Verified"))
        {bgstatus.setBackgroundColor(getContext().getResources().getColor(R.color.bg_verified)); }
        else if(listWarung.get(position).Status_Warung.equals("Pending"))
        {bgstatus.setBackgroundColor(getContext().getResources().getColor(R.color.bg_pending)); }

        namaWarung.setText(listWarung.get(position).Nama_Warung);
        Status.setText(listWarung.get(position).Status_Warung);
        namaPengelola.setText("Nama Pengelola");
        Telp.setText(listWarung.get(position).Telp);
        Kota.setText(listWarung.get(position).Kota);

        return rowView;
    }


}
