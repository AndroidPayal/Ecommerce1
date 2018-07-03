package payal.cluebix.www.ecommerce.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.ArrayList;

import payal.cluebix.www.ecommerce.R;


/**
 * Created by speed on 07-Feb-18.
 */

public class New_product_adapter extends RecyclerView.Adapter<New_product_adapter.ProductViewHolder> {


    private Context mCtx;
    private ArrayList<Bitmap> productList;
    ClickListener clickListener;

    public New_product_adapter(Context mCtx, ArrayList<Bitmap> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_new_product_adapter, null,false);
        ViewGroup.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        holder.image.setImageBitmap(productList.get(position));
    }


    public void setClickListener(ClickListener clickListener){
        this.clickListener=clickListener;
    }

    public void notifyData(ArrayList<Bitmap> myList) {
        this.productList = myList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        ImageView image;

        public ProductViewHolder(View itemView) {
            super(itemView);

            image=(ImageView)itemView.findViewById(R.id.image1);
            image.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
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
        public void onLongClick(View view,int position);
    }

}
