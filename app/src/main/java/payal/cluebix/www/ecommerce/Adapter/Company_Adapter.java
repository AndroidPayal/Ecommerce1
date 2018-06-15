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

import java.util.ArrayList;

import payal.cluebix.www.ecommerce.Datas.company_data;
import payal.cluebix.www.ecommerce.Datas.unit_color_data;
import payal.cluebix.www.ecommerce.R;

/**
 * Created by speed on 02-Jun-18.
 */

public class Company_Adapter extends RecyclerView.Adapter<Company_Adapter.ProductViewHolder> {

    private Context mCtx;
    private ArrayList<company_data> productList;
    Company_Adapter.ClickListener clickListener;

    public Company_Adapter(Context mCtx, ArrayList<company_data> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public Company_Adapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.unit_adapter, null);
        return new Company_Adapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Company_Adapter.ProductViewHolder holder, final int position) {
        company_data a= productList.get(position);

        holder.s_no.setText(position+"");
        holder.name.setText(a.getName1());
//        holder.date.setText(a.getCreatedAt());

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

    public void setClickListener(Company_Adapter.ClickListener clickListener){
        this.clickListener=clickListener;
    }

    public void notifyData(ArrayList<company_data> myList) {
        this.productList = myList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }




    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView s_no,name,options,date;

        public ProductViewHolder(View itemView) {
            super(itemView);
            s_no=(TextView)itemView.findViewById(R.id.unit_Sno);
            name=(TextView)itemView.findViewById(R.id.unit_name);
            date=(TextView)itemView.findViewById(R.id.com_date);
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
