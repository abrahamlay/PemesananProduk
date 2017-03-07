package abrahamlay.kknp2015.warungsosro;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Node;

import java.util.ArrayList;

/**
 * Created by Abraham on 03/07/2015.
 */
public class TicketAdapter extends ArrayAdapter {

    public Context context;
    public ArrayList<Ticket> listTiket = null;


    public TicketAdapter(Context context, ArrayList<Ticket> listtiket) {
        super(context, R.layout.item_ticket,listtiket);
        this.context=context;
        this.listTiket=listtiket;
    }

    // Clean all elements of the recycler
    public void clear() {
        listTiket.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(ArrayList<Ticket> list) {
        listTiket.addAll(list);
        notifyDataSetChanged();
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_ticket, parent, false);

        notifyDataSetChanged();

        TextView namaWarung= (TextView) rowView.findViewById(R.id.namaWarung);
        TextView Waktu_Pesan = (TextView) rowView.findViewById(R.id.waktuPesan);
        TextView namaBarang = (TextView) rowView.findViewById(R.id.namaBarang);
        TextView jmlBarang = (TextView) rowView.findViewById(R.id.jmlBrg);
        TextView Status = (TextView) rowView.findViewById(R.id.status);
        LinearLayout bgstatus=(LinearLayout) rowView.findViewById(R.id.bgStatus);

        if(listTiket.get(position).Status.equals("Delivered"))
        {bgstatus.setBackgroundColor(getContext().getResources().getColor(R.color.bg_delivered)); }
        else if(listTiket.get(position).Status.equals("Pending"))
        {bgstatus.setBackgroundColor(getContext().getResources().getColor(R.color.bg_pending)); }
        else if(listTiket.get(position).Status.equals("Forwarded"))
        {bgstatus.setBackgroundColor(getContext().getResources().getColor(R.color.bg_forwarded)); }
        else if(listTiket.get(position).Status.equals("Diverted"))
        {bgstatus.setBackgroundColor(getContext().getResources().getColor(R.color.bg_diverted)); }
        else if(listTiket.get(position).Status.equals("Canceled"))
        {bgstatus.setBackgroundColor(getContext().getResources().getColor(R.color.bg_error)); }
        else if(listTiket.get(position).Status.equals("Approved"))
        {bgstatus.setBackgroundColor(getContext().getResources().getColor(R.color.bg_approved)); }

        namaWarung.setText(listTiket.get(position).Nama_Warung);
        Waktu_Pesan.setText(listTiket.get(position).Waktu_Request);
        Status.setText(listTiket.get(position).Status);
        namaBarang.setText(listTiket.get(position).Nama_barang);
        jmlBarang.setText(listTiket.get(position).Jumlah_Pesan+" Kratt");


        return rowView;
    }


}
