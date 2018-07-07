package payal.cluebix.www.ecommerce.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.disklrucache.DiskLruCache;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.R;
import payal.cluebix.www.ecommerce.Datas.sample_Cart;

/**
 * Created by speed on 26-Mar-18.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ProductViewHolder> {
    private Context mCtx;
    private ArrayList<sample_Cart> productList=new ArrayList<>();
    CartAdapter.ClickListener clickListener;

    public CartAdapter(Context mCtx, ArrayList<sample_Cart> productList) {
        this.productList.clear();
        this.mCtx = mCtx;
        this.productList = productList;
        Log.d("cart_data","construcltor:"+productList+"");
    }

    @Override
    public CartAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("cart_data","oncreateViewHolder:"+productList+"");

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_cart_items, null,false);
        ViewGroup.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new CartAdapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartAdapter.ProductViewHolder holder, int position) {
        Log.d("cart_data","BindHolder:"+productList+"");

        sample_Cart a= productList.get(position);
       // holder.imagecart.setImageBitmap(a.getProduct_images().get(0));
        holder.name_text3.setText(a.getProduct_nam());
        holder.quantity.setText(a.getQty());
        holder.quant.setText("Qty:"+a.getQty());
        holder.price.setText("Price:"+a.getPrice());
        holder.desc.setText(a.getDescription());
        if (a.getManufacturing().equals("0"))
            holder.Avl.setText("Avl:"+a.getQuantity());
        else
            holder.Avl.setText("Owner");

        if (a.getSample().equals("1"))
            holder.sample.setText("Sample:"+a.getSamplePrice());
        else
            holder.sample.setText("");

        Picasso.with(mCtx)
                .load(Base_url.IMAGE_DIRECTORY_NAME+a.getProduct_images().get(0))
                .placeholder(R.drawable.loading) // optional
                .error(R.drawable.error_load)         // optional
                .into(holder.imagecart);
    }

    public void setClickListener(CartAdapter.ClickListener clickListener){
        this.clickListener=clickListener;
    }

    public void notifyData(ArrayList<sample_Cart> myList) {
        this.productList = myList;
        notifyDataSetChanged();
    }
    public void notifyitem(sample_Cart myList,int pos) {
        //this.productList.get(pos) = myList;
        this.productList.set(pos,myList);
        notifyItemChanged(pos);
    }
    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        ImageView imagecart,t_cart_remove;
        TextView name_text3,quant,price,desc,Avl,sample;
        EditText quantity;
        ImageButton plus,minus;
        public ProductViewHolder(View itemView) {
            super(itemView);

            Log.d("cart_data","holder class:"+productList+"");

            imagecart=(ImageView)itemView.findViewById(R.id.image_cart);
            name_text3=(TextView)itemView.findViewById(R.id.text3);
            quantity=(EditText)itemView.findViewById(R.id.quantity);
            plus=(ImageButton) itemView.findViewById(R.id.plus);
            minus=(ImageButton) itemView.findViewById(R.id.minus);
            t_cart_remove=(ImageView)itemView.findViewById(R.id.t_cart_remove);
            quant=(TextView)itemView.findViewById(R.id.quant);
            price=(TextView)itemView.findViewById(R.id.price_cart);
            desc=(TextView)itemView.findViewById(R.id.description_cart);
            Avl=(TextView)itemView.findViewById(R.id.available_cart);
            sample=(TextView)itemView.findViewById(R.id.sample_cart);

            t_cart_remove.setOnClickListener(this);
            imagecart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener!=null){
                        clickListener.imageClicked(view,getPosition());
                    }
                }
            });
            itemView.setOnLongClickListener(this);

            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener!=null){
                       /*
                        Log.d("cartscreen","new array="+array);
                        Log.d("cartscreen","old array="+productList);*/
                        clickListener.plusClicked(productList,view,getPosition());
                    }
                }
            });
            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener!=null){
                        clickListener.minusClicked(productList,view,getPosition());
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {
            if (clickListener!=null){
                clickListener.itemClicked(view,getPosition());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (clickListener!=null){
                clickListener.onLongClick(view,getPosition());
            }
            return true;
        }
    }

    public interface ClickListener{
        public void itemClicked(View view, int position);
        public void plusClicked(ArrayList<sample_Cart> array,View view, int position);
        public void minusClicked(ArrayList<sample_Cart> array,View view, int position);
        public void imageClicked(View view, int position);
        public void onLongClick(View view, int position);
    }

}
