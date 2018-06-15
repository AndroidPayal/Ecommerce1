package payal.cluebix.www.ecommerce.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.Datas.quotation2;
import payal.cluebix.www.ecommerce.R;

public class Quotation_item_adap extends RecyclerView.Adapter<Quotation_item_adap.ProductViewHolder> {
    private Context mCtx;
    private ArrayList<quotation2> productList;
    Quotation_item_adap.ClickListener clickListener;

    public Quotation_item_adap(Context mCtx, ArrayList<quotation2> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public Quotation_item_adap.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_quote_item_list_adap, null);
        return new Quotation_item_adap.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Quotation_item_adap.ProductViewHolder holder, int position) {
        quotation2 a= productList.get(position);

        holder.q_name.setText(a.getProduct_name());
        holder.q_brand.setText("Brand: "+a.getBrand());
        holder.q_desc.setText("Description: "+a.getDescription());
        holder.q_prise.setText("Rs. "+a.getPrice());
        holder.q_qty.setText("Quantity Ordered: "+a.getQty());

/*
        Picasso.with(mCtx)
                .load(Base_url.IMAGE_DIRECTORY_NAME+a.getProduct_image_array().get(0))
                .placeholder(R.drawable.loading) // optional
                .error(R.drawable.error_load)         // optional
                .into(holder.q_image);
*/
    }

    public void setClickListener(Quotation_item_adap.ClickListener clickListener){
        this.clickListener=clickListener;
    }

    public void notifyData(ArrayList<quotation2> myList) {
        this.productList = myList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView q_name,q_brand,q_desc,q_prise,q_qty;
        ImageView q_image;

        public ProductViewHolder(View itemView) {
            super(itemView);
            q_name=(TextView)itemView.findViewById(R.id.q_name);
            q_brand=(TextView)itemView.findViewById(R.id.q_brand);
            q_desc=(TextView)itemView.findViewById(R.id.q_desc);
            q_prise=(TextView)itemView.findViewById(R.id.q_prise);
            q_image=(ImageView)itemView.findViewById(R.id.q_image);
            q_qty=(TextView)itemView.findViewById(R.id.q_qty);

            q_image.setOnClickListener(this);

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
