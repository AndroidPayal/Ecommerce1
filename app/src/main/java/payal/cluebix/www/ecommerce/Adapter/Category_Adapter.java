package payal.cluebix.www.ecommerce.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import payal.cluebix.www.ecommerce.Datas.category_data;
import payal.cluebix.www.ecommerce.Datas.company_data;
import payal.cluebix.www.ecommerce.R;

/**
 * Created by speed on 02-Jun-18.
 */

public class Category_Adapter extends RecyclerView.Adapter<Category_Adapter.ProductViewHolder> {

    private Context mCtx;
    private ArrayList<category_data> productList;
    Category_Adapter.ClickListener clickListener;

    public Category_Adapter(Context mCtx, ArrayList<category_data> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public Category_Adapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_layout_category, null);
        return new Category_Adapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Category_Adapter.ProductViewHolder holder, final int position) {
        category_data a= productList.get(position);

        holder.s_no.setText(position+"");
        holder.name.setText(a.getName());
        holder.description.setText(a.getDescription());

        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        /*        PopupMenu popup = new PopupMenu(mCtx,holder.options);
                popup.inflate(R.menu.menu_unit);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.unit_options1_edit:
                                Toast.makeText(mCtx, "edit at "+position, Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.show();*/

            /*    AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                LayoutInflater inflater = LayoutInflater.from(mCtx);
                View dialogView = inflater.inflate( R.layout.popup_add_category_list,null);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();*/
            }
        });

    }

    public void setClickListener(Category_Adapter.ClickListener clickListener){
        this.clickListener=clickListener;
    }

    public void notifyData(ArrayList<category_data> myList) {
        this.productList = myList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }




    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView s_no,name,description;
        ImageView options;

        public ProductViewHolder(View itemView) {
            super(itemView);
            s_no=(TextView)itemView.findViewById(R.id.category_Sno);
            name=(TextView)itemView.findViewById(R.id.category_name);
            description=(TextView)itemView.findViewById(R.id.category_desc);
            options=(ImageView) itemView.findViewById(R.id.category_options);

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
