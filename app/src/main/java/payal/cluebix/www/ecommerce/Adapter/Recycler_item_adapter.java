package payal.cluebix.www.ecommerce.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.Datas.data_dashboard;
import payal.cluebix.www.ecommerce.R;


/**
 * Created by speed on 10-March-18.
 */

public class Recycler_item_adapter extends RecyclerView.Adapter<Recycler_item_adapter.ProductViewHolder> implements Filterable{

    private Context mCtx;
    private List<data_dashboard> productList;
    private List<data_dashboard> productList_Copy;
    private List<String> productList_names=new ArrayList<>();

    ClickListener clickListener;
    Slider_adapter sliderPagerAdapter;
    List<String> slider_image;
    private TextView[] dots;

    public Recycler_item_adapter(Context mCtx, List<data_dashboard> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
        productList_Copy= new ArrayList<>(productList);



        //productList_Copy.addAll(productList);
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.dashboard_items_, null,false);
        ViewGroup.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        data_dashboard a=productList.get(position);

        slider_image=a.getProduct_images_Array();

        holder.textprize.setText(a.getPrice());
//        Log.d("ProductPrice",a.getProduct_name()+" price= "+a.getPrice());
        holder.Product_name.setText(a.getProduct_name());

        if(!a.getManufacturing().equals("0"))
            holder.text_manufact.setText("Manufacturing");
        else{
            holder.text_manufact.setText("In Stock : "+a.getQty());
        }

        Log.d("samplevalue","ITEM ADAPTER sample status="+a.getSample());
        if(!a.getSample().trim().equals("0"))
            holder.text_sample.setVisibility(View.VISIBLE);

        Log.d("cartval",a.getCart_disable()+" name="+a.getProduct_name());
        if(a.getCart_disable()==1){
            holder.view_detail.setText("Added to Cart");
            //holder.add_to_cart.setClickable(false);
        }

        holder.product_code.setText("Product Code:"+a.getProduct_code());

        init(slider_image, holder);
        addBottomDots( holder,0);

        long elapsedDays = 0;

        String date="2018-05-29";//productList.get(position).getCreated_date();
        SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");//2018-04-02
        try {
            Date dt1=format1.parse(date);
            String dt = format1.format(Calendar.getInstance().getTime());
            Date dt2=format1.parse(dt);

            long different = dt2.getTime() - dt1.getTime();
            long daysInMilli =  24* 60* 60*1000;
             elapsedDays = different / daysInMilli;

            Log.d("dates1","dt1="+dt1
                    +"\ndt2="+dt2+"\n elapsed="+elapsedDays
            );


        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (elapsedDays<= Base_url.new_lable_days) {
            Log.d("dashboardscreen","newLableVal=yes\ndays:"+elapsedDays+"\nmax="+Base_url.new_lable_days);
            holder.new_tag.bringToFront();
        }else{
            holder.new_tag.setVisibility(View.GONE);
        }
    }

    public void setClickListener(ClickListener clickListener){
        this.clickListener=clickListener;
    }

    public void notifyData(List<data_dashboard> myList) {
        this.productList = myList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }



    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        ViewPager vp_slider;
        LinearLayout ll_dots;
        TextView textprize,new_tag, text_manufact,text_sample;
        TextView Product_name,view_detail,product_code;
     //   ProgressBar progress;

        public ProductViewHolder(View itemView) {
            super(itemView);
         /*   for(int i=0;i<productList.size();i++ ){
                mStringFilterList.add(productList.get(i).getProduct_name());
            }*/

            vp_slider =itemView.findViewById(R.id.vp_slider);
            ll_dots = itemView.findViewById(R.id.ll_dots);
            textprize=itemView.findViewById(R.id.text_prize);
            Product_name=itemView.findViewById(R.id.product_name);
            view_detail=itemView.findViewById(R.id.viewDetail);
            product_code=itemView.findViewById(R.id.product_code);
         //   add_to_cart=itemView.findViewById(R.id.add_to_cart);
            new_tag=itemView.findViewById(R.id.new_tag);
          //  progress=itemView.findViewById(R.id.progress);
            text_manufact=itemView.findViewById(R.id.manufacture);
            text_sample=itemView.findViewById(R.id.sample);

            view_detail.setOnClickListener(this);
            text_sample.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener!=null){
                        clickListener.sampleClicked(view,getPosition());
                    }
                }
            });
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
             void itemClicked(View view, int position);
             void onLongClick(View view, int position);
            void sampleClicked(View view, int position);
            void Add_to_cart(View view, int position);
        }

    private void init(List<String> slider_image,final ProductViewHolder holder) {

        Log.d("images","slider images="+slider_image);
    try {
        sliderPagerAdapter = new Slider_adapter((Activity) mCtx, slider_image);
        holder.vp_slider.setAdapter(sliderPagerAdapter);

        holder.vp_slider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                try {
                    addBottomDots(holder, position);
                }catch (Exception e){e.printStackTrace();}

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }catch (Exception e){

    Log.e("images Error",""+e);
}
    }


    private void addBottomDots(ProductViewHolder holder,int currentPage) {
        try {
                dots = new TextView[slider_image.size()];

                holder.ll_dots.removeAllViews();
                holder.ll_dots.bringToFront();
                for (int i = 0; i < dots.length; i++) {
                    dots[i] = new TextView(mCtx);
                    dots[i].setText(Html.fromHtml("&#8226;"));
                    dots[i].setTextSize(35);
                    dots[i].setTextColor(Color.parseColor("#000000"));
                    holder.ll_dots.addView(dots[i]);
                }

                if (dots.length > 0)
                    dots[currentPage].setTextColor(Color.parseColor("#FFFFFF"));
            }catch (Exception e){e.printStackTrace();}
    }



    @Override
    public Filter getFilter() {



                                                // OLD filter code




//        return new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                String charString = constraint.toString().trim();
//                if (charString.isEmpty()) {
//                    productList = productList_Copy;
//                }else{
//                    List<data_dashboard> filteredList = new ArrayList<>();
//                        for(data_dashboard row:productList_Copy){
//                            if (row.getProduct_name().toLowerCase().contains(charString.toLowerCase()) ) {
//                                //||row.pr().contains(charSequence)
//                                filteredList.add(row);
//                            }
//                        }
//                        productList=filteredList;
//                }
//                FilterResults filterResults = new FilterResults();
//                filterResults.values = productList;
//                return null;
//            }
//
//            @SuppressWarnings("unchecked")
//            @Override
//            protected void publishResults(CharSequence constraint, FilterResults results) {
//                try {
//                    Log.d("resultVal",results.values+"");
//                    productList = (ArrayList<data_dashboard>) results.values;
//                    if (results.count > 0) {
//                        notifyDataSetChanged();
//                    } else {
//                        Toast.makeText(mCtx, "no data", Toast.LENGTH_SHORT).show();
//                    }
//                }catch (NullPointerException e){
//                    Log.e("nullPointerExce",e+"");
//                }
//
//            }
//        };



        return exampleFilter;


    }


    Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<data_dashboard> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0)
            {
                filteredList.addAll(productList_Copy);

                Log.d("Adapter Filter Response"," for empty char"+productList_Copy);
            }

            else{

                Log.d("Adapter Filter Response"," entered value : "+constraint);
                String filterCharSequence = constraint.toString().toLowerCase().trim();

                for(data_dashboard item : productList_Copy)
                {

                    if(item.getProduct_name().toLowerCase().contains(filterCharSequence))
                    {
                        filteredList.add(item);
                    }
                }

            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            productList.clear();

            productList.addAll((List) results.values);

//            notifyData(productList);

            notifyDataSetChanged();
        }
    };



//    public void filter(String charText) {
//        charText = charText.toLowerCase(Locale.getDefault());
//        ArrayList<data_dashboard> listcpy=new ArrayList<>();
//        listcpy.addAll(productList);
//
//        Log.d("filter123","start size=productList:"
//                +productList.size()
//            +"\nproductcpy:"+productList_Copy.size()
//                +"\nnamelist"+productList_names.size()
//                +"\ncopylist:"+listcpy.size());
//
//        productList.clear();
//
//        if (charText.length() == 0) {
//            productList.addAll(productList_Copy);
//        }
//        else{
//            for (int i=0;i<listcpy.size();i++ ) {
//                if (productList_names.get(i).toLowerCase(Locale.getDefault()).contains(charText)) {
//                    productList.add(listcpy.get(i));
//                }
//            }
//        }
//        Log.d("filter123","end size=productList:"+productList.size()
//                +"\nproductcpy:"+productList_Copy.size()+"\nnamelist"+productList_names.size()
//                +"\ncopylist:"+listcpy.size());
//        notifyDataSetChanged();
//    }
}

