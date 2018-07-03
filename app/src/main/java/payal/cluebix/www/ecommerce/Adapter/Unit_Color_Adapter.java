package payal.cluebix.www.ecommerce.Adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lowagie.toolbox.plugins.PhotoAlbum;

import java.util.ArrayList;

import payal.cluebix.www.ecommerce.Datas.unit_color_data;
import payal.cluebix.www.ecommerce.R;

/**
 * Created by speed on 01-Jun-18.
 */

public class Unit_Color_Adapter extends RecyclerView.Adapter<Unit_Color_Adapter.ProductViewHolder>{
    private Context mCtx;
    private ArrayList<unit_color_data> productList;
    Unit_Color_Adapter.ClickListener clickListener;

    public Unit_Color_Adapter(Context mCtx, ArrayList<unit_color_data> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public Unit_Color_Adapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.unit_adapter, null,false);
        ViewGroup.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new Unit_Color_Adapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Unit_Color_Adapter.ProductViewHolder holder, final int position) {
        unit_color_data a= productList.get(position);

        holder.s_no.setText(position+"");
        holder.name.setText(a.getName());
/*        holder.t_userid.setText("Your ID:"+a.getUser_id());
        holder.t_quoteid.setText("Quotation ID:"+a.getQuote_number());
        holder.t_date.setText("Quotation Date:"+a.getCreated_date());*/

        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(mCtx,holder.options);
                //inflating menu from xml resource
                popup.inflate(R.menu.menu_unit);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.unit_options1_edit:
                                //handle menu1 click
                                Toast.makeText(mCtx, "edit at "+position, Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                popup.show();
            }
        });

    }

    public void setClickListener(Unit_Color_Adapter.ClickListener clickListener){
        this.clickListener=clickListener;
    }

    public void notifyData(ArrayList<unit_color_data> myList) {
        this.productList = myList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }




    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView s_no,name,options;

        public ProductViewHolder(View itemView) {
            super(itemView);
            s_no=(TextView)itemView.findViewById(R.id.unit_Sno);
            name=(TextView)itemView.findViewById(R.id.unit_name);
            options=(TextView)itemView.findViewById(R.id.unit_options);


        }

        @Override
        public void onClick(View view) {
            if (clickListener!=null){
                clickListener.itemClicked(view,getPosition());
            }
        }

    }

    public interface ClickListener{
        public void itemClicked(View view, int position);
    }


}
