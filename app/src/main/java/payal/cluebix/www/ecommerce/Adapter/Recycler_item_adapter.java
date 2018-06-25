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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class Recycler_item_adapter extends RecyclerView.Adapter<Recycler_item_adapter.ProductViewHolder> {

    private Context mCtx;
    private List<data_dashboard> productList;
    ClickListener clickListener;
    Slider_adapter sliderPagerAdapter;
    List<String> slider_image;
    ArrayList<String> searchArray=new ArrayList<>();

    private TextView[] dots;

    public Recycler_item_adapter(Context mCtx, List<data_dashboard> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.recycler_item_design, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        data_dashboard a=productList.get(position);
        slider_image=a.getProduct_images_Array();

        holder.textprize.setText(a.getPrice());
        holder.Product_name.setText(a.getProduct_name());

        if(!a.getManufacturing().equals("0"))
        holder.text_manufact.setText("Manufacturing");
        if(!a.getSample().equals("0"))
        holder.text_sample.setText("Sample Available");

        if(a.getCart_disable()==1){
            holder.add_to_cart.setText("Added to Cart");
            holder.add_to_cart.setClickable(false);
        }

        init(slider_image, holder);
        addBottomDots( holder,0);

        long elapsedDays = 0;

        String date="2018-05-29";//productList.get(position).getCreated_date();
        SimpleDateFormat format1=new SimpleDateFormat("yyyy-mm-dd");//2018-04-02
        try {
            Date dt1=format1.parse(date);
            String dt = format1.format(Calendar.getInstance().getTime());
            Date dt2=format1.parse(dt);

            long different = dt2.getTime() - dt1.getTime();
            long daysInMilli =  24* 60* 60*1000;
             elapsedDays = different / daysInMilli;


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
        TextView Product_name,view_detail,add_to_cart;
     //   ProgressBar progress;

        public ProductViewHolder(View itemView) {
            super(itemView);

            vp_slider =itemView.findViewById(R.id.vp_slider);
            ll_dots = itemView.findViewById(R.id.ll_dots);
            textprize=itemView.findViewById(R.id.text_prize);
            Product_name=itemView.findViewById(R.id.product_name);
            view_detail=itemView.findViewById(R.id.viewDetail);
            add_to_cart=itemView.findViewById(R.id.add_to_cart);
            new_tag=itemView.findViewById(R.id.new_tag);
          //  progress=itemView.findViewById(R.id.progress);
            text_manufact=itemView.findViewById(R.id.manufacture);
            text_sample=itemView.findViewById(R.id.sample);

            view_detail.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            add_to_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener!=null){
                        clickListener.Add_to_cart(view,getPosition());
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
             void itemClicked(View view, int position);
             void onLongClick(View view, int position);
             void Add_to_cart(View view, int position);
        }

    private void init(List<String> slider_image,final ProductViewHolder holder) {

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

    e.printStackTrace();
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


    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        searchArray.clear();
        if (charText.length() == 0) {
            for(int i=0;i<productList.size();i++)
            searchArray.add(productList.get(i).getProduct_name());
        } else {
          /*  for (AnimalNames wp : arraylist) {
                if (wp.getAnimalName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    animalNamesList.add(wp);
                }
            }*/
          for(data_dashboard dd : productList){
              if(dd.getProduct_name().toLowerCase(Locale.getDefault()).contains(charText)){
                  searchArray.add(dd.getProduct_name());
              }
          }
        }
        notifyDataSetChanged();
    }


}

