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
public class AgenAdapter extends ArrayAdapter {

    public Context context;
    public ArrayList<Agen> listAgen = null;


    public AgenAdapter(Context context, ArrayList<Agen> listAgen) {
        super(context, R.layout.item_agen,listAgen);
        this.context=context;
        this.listAgen=listAgen;
    }

    // Clean all elements of the recycler
    public void clear() {
        listAgen.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(ArrayList<Agen> list) {
        listAgen.addAll(list);
        notifyDataSetChanged();
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_agen, parent, false);

        TextView namaAgen = (TextView) rowView.findViewById(R.id.namaAgen);
        TextView Alamat = (TextView) rowView.findViewById(R.id.alamat_agen);
        TextView Telp = (TextView) rowView.findViewById(R.id.telp);
        TextView Kota = (TextView) rowView.findViewById(R.id.kota);

        TextView Status = (TextView) rowView.findViewById(R.id.status);
        LinearLayout bgstatus=(LinearLayout) rowView.findViewById(R.id.bgStatus);

        if(listAgen.get(position).Status_Agen.equals("Active"))
        {bgstatus.setBackgroundColor(getContext().getResources().getColor(R.color.bg_active)); }
        else if(listAgen.get(position).Status_Agen.equals("Pending"))
        {bgstatus.setBackgroundColor(getContext().getResources().getColor(R.color.bg_pending)); }

        namaAgen.setText(listAgen.get(position).Nama_Agen);
        Status.setText(listAgen.get(position).Status_Agen);
        Alamat.setText(listAgen.get(position).Alamat);
        Telp.setText(listAgen.get(position).Telp);
        Kota.setText(listAgen.get(position).Kota);

        return rowView;
    }


}
