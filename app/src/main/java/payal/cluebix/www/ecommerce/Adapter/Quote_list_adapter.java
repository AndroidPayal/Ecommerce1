package payal.cluebix.www.ecommerce.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import payal.cluebix.www.ecommerce.Datas.quotation1;

import payal.cluebix.www.ecommerce.R;

/**
 * Created by speed on 12-Apr-18.
 */

public class Quote_list_adapter extends RecyclerView.Adapter<Quote_list_adapter.ProductViewHolder> {
    private Context mCtx;
    private ArrayList<quotation1> productList;
        Quote_list_adapter.ClickListener clickListener;

    public Quote_list_adapter(Context mCtx, ArrayList<quotation1> productList) {
            this.mCtx = mCtx;
            this.productList = productList;
            }

    @Override
    public Quote_list_adapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.activity_quote_list_new, null);
            return new Quote_list_adapter.ProductViewHolder(view);
            }

    @Override
    public void onBindViewHolder(Quote_list_adapter.ProductViewHolder holder, int position) {
            quotation1 a= productList.get(position);

            holder.t_userid.setText("Expire:"+a.getExpiry_date());
            holder.t_quoteid.setText("Quotation ID:"+a.getQuote_number());
            holder.t_date.setText(""+a.getCreated_date());
            }

    public void setClickListener(Quote_list_adapter.ClickListener clickListener){
            this.clickListener=clickListener;
            }

    public void notifyData(ArrayList<quotation1> myList) {
            this.productList = myList;
            notifyDataSetChanged();
            }

    @Override
    public int getItemCount() {
            return productList.size();
            }



class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView t_userid,t_quoteid,t_date;

    public ProductViewHolder(View itemView) {
        super(itemView);
        t_userid=(TextView)itemView.findViewById(R.id.userid);
        t_quoteid=(TextView)itemView.findViewById(R.id.quoteid);
        t_date=(TextView)itemView.findViewById(R.id.date);

        itemView.setOnClickListener(this);

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
