package payal.cluebix.www.ecommerce.Adapter;

import android.app.LauncherActivity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import payal.cluebix.www.ecommerce.Datas.GuestItem;
import payal.cluebix.www.ecommerce.R;



import java.util.List;

/**
 * Created by UNMESH on 21-03-2018.
 */

public class GuestAdapter extends RecyclerView.Adapter<GuestAdapter.ViewHolder> {

    private Context context;
//    private List<ListItemQuote> listItems;
    private  List<GuestItem> listItems;

    public GuestAdapter(Context context, List listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.guest_adapter_layout,parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GuestAdapter.ViewHolder holder, int position) {

//        ListItemQuote item = listItems.get(position);

        GuestItem item = listItems.get(position);

        holder.productName.setText(item.getProductName());
        holder.productCode.setText(item.getProductCode());
        holder.productPrize.setText(item.getProductPrize());

//        holder.name.setText(item.getName());
//        holder.description.setText(item.getDescription());
//        holder.rating.setText(item.getRating());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView productName;
        public TextView productCode;
        public TextView productPrize;



        public ViewHolder(View itemView) {

            super(itemView);

            productName = (TextView) itemView.findViewById(R.id.retail_product_name);
            productCode = (TextView) itemView.findViewById(R.id.retail_product_code);
            productPrize = (TextView) itemView.findViewById(R.id.retail_product_prize);

        }
    }
}
