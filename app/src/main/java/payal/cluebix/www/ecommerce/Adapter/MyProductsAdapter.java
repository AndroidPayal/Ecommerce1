package payal.cluebix.www.ecommerce.Adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.Datas.quotation1;

import payal.cluebix.www.ecommerce.Datas.sample_myProduct;
import payal.cluebix.www.ecommerce.My_products;
import payal.cluebix.www.ecommerce.Product_My_Detail;
import payal.cluebix.www.ecommerce.R;

/**
 * Created by speed on 12-Apr-18.
 */

            // perform search for Quote , Quote_list_adapter after every thing is done also search for quotation



public class MyProductsAdapter extends RecyclerView.Adapter<MyProductsAdapter.ViewHolder> {
    private Context mCtx;
    private ArrayList<sample_myProduct> productList;

    public MyProductsAdapter(Context mCtx, ArrayList<sample_myProduct> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public MyProductsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.grid_adapter, null,false);
        ViewGroup.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new MyProductsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyProductsAdapter.ViewHolder holder, int position) {
        sample_myProduct a= productList.get(position);




        String image_names=a.getProduct_images_String();

        List<String> items = Arrays.asList(image_names.split(","));
        String first_image_url= Base_url.IMAGE_DIRECTORY_NAME+""+items.get(0);
// imageView.setImageBitmap(product.get(i).getimage());
        Picasso.with(mCtx)
                .load(first_image_url)
                .placeholder(R.drawable.loading) // optional
                .error(R.drawable.error_load)         // optional
                .into(holder.productImage);

//
//        Picasso.with(mCtx)
//                .load(imageUrl[0]) // chceck and confirm ///////////// MUST NOT BE NEGLECTED
//                .placeholder(R.drawable.loading) // optional
//                .error(R.drawable.error_load)         // optional
//                .into(holder.productImage);


        holder.productName.setText(a.getProduct_name());


    }



    public void notifyData(ArrayList<sample_myProduct> myList) {
        this.productList = myList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{



        TextView productName;
        ImageView productImage;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            productName = (TextView) itemView.findViewById(R.id.text2);

            productImage = (ImageView) itemView.findViewById(R.id.image2);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();

            Intent intent=new Intent(mCtx,Product_My_Detail.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("current_product_id",productList.get(position).getProductId());

            mCtx.startActivity(intent);
        }

    }


}
