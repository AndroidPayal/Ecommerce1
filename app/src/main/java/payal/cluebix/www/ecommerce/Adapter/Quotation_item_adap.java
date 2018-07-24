package payal.cluebix.www.ecommerce.Adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.Datas.quotation2;
import payal.cluebix.www.ecommerce.R;

public class Quotation_item_adap extends RecyclerView.Adapter<Quotation_item_adap.ProductViewHolder> {
    private Context mCtx;
    private ArrayList<quotation2> productList;

    public Quotation_item_adap(Context mCtx, ArrayList<quotation2> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public Quotation_item_adap.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.invoice_adapter_layout, null, false);
        ViewGroup.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);

        return new Quotation_item_adap.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Quotation_item_adap.ProductViewHolder holder, int position) {
        final quotation2 a = productList.get(position);

        holder.invoice_sr_number.setText((position + 1) + "");


        holder.q_name.setText(a.getProduct_name());
        //holder.q_brand.setText("Brand: "+a.getBrand());
        holder.q_desc.setText("Description: " + a.getDescription());
        holder.q_qty.setText(a.getQty());
        holder.invoice_price.setText("Product price:" + a.getPrice());

        if (a.getSample().equals("1")) {
            holder.invoice_sample.setText("Sample:" + a.getSample_price());
            holder.q_prise.setText("Rs. " + (Float.parseFloat(a.getPrice()) * Integer.parseInt(a.getQty()) +
                    Float.parseFloat(a.getSample_price())));
        } else {
            holder.invoice_sample.setText("");
            holder.q_prise.setText("Rs. " + (Float.parseFloat(a.getPrice()) * Integer.parseInt(a.getQty())));
        }
        Log.d("quotation_item_adap__", "qty=" + a.getQty() + "\nsrNo:" + (position + 1));

        holder.image_Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mCtx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tel://" + ""+a.getMobile())));
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel://" + "" + a.getMobile()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (ActivityCompat.checkSelfPermission(mCtx, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Toast.makeText(mCtx, "Call Permission not granted", Toast.LENGTH_SHORT).show();
                    return;
                }
                mCtx.startActivity(intent);


            }
        });

    }


    public void notifyData(ArrayList<quotation2> myList) {
        this.productList = myList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView q_name,q_desc,q_prise,q_qty,invoice_sample,invoice_sr_number,invoice_price;
        ImageView image_Call;

        public ProductViewHolder(View itemView) {
            super(itemView);
            invoice_sr_number=(TextView)itemView.findViewById(R.id.invoice_sr_number);
            q_name=(TextView)itemView.findViewById(R.id.q_name);
       //     q_brand=(TextView)itemView.findViewById(R.id.q_brand);
            q_desc=(TextView)itemView.findViewById(R.id.q_desc);
            q_prise=(TextView)itemView.findViewById(R.id.q_prise);
            //q_image=(ImageView)itemView.findViewById(R.id.q_image);
            q_qty=(TextView)itemView.findViewById(R.id.q_qty);
            invoice_sample=(TextView)itemView.findViewById(R.id.invoice_sample);
            invoice_price=(TextView)itemView.findViewById(R.id.invoice_price);
            image_Call=(ImageView)itemView.findViewById(R.id.image_call);
           // q_image.setOnClickListener(this);

           /* image_Call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCtx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tel://" + ""+)));

                }
            });*/
        }


    }


}
