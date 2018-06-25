package payal.cluebix.www.ecommerce.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.Handlers.TouchImageView;
import payal.cluebix.www.ecommerce.R;

public class Slider_adapter extends PagerAdapter {

    private LayoutInflater layoutInflater;
    Activity activity;
    List<String> image_arraylist;
    ViewPager imageSlider;

    public Slider_adapter(Activity activity, List<String> image_arraylist) {
        this.activity = activity;
        this.image_arraylist = image_arraylist;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.activity_slider_adapter, container, false);


        ImageView im_slider = (ImageView) view.findViewById(R.id.im_slider);
        Picasso.with(activity.getApplicationContext())
                .load(Base_url.IMAGE_DIRECTORY_NAME+image_arraylist.get(position))
                .placeholder(R.drawable.loading) // optional
                .error(R.drawable.error_load)         // optional
                .into(im_slider);

        container.addView(view);
        Log.d("images","inflated image view for detail\nurl="+Base_url.IMAGE_DIRECTORY_NAME+image_arraylist.get(position));

        im_slider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Toast.makeText(activity.getApplicationContext(), "slider clicked_"+position, Toast.LENGTH_SHORT).show();

                image_preview(position);

            }
        });
        return view;
    }

    private void image_preview(int position) {
        Log.d("images","image dialog screen");
/*
        final Dialog nagDialog = new Dialog(activity,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        nagDialog.setCancelable(true);
        nagDialog.setContentView(R.layout.preview_image);

        TouchImageView image=nagDialog.findViewById(R.id.img);

        Picasso.with(activity.getApplicationContext())
                .load(Base_url.IMAGE_DIRECTORY_NAME+image_arraylist.get(position))
                .placeholder(R.drawable.loading) // optional
                .error(R.drawable.error_load)         // optional
                .into(image);

        Log.d("dialog1",""+Base_url.IMAGE_DIRECTORY_NAME+image_arraylist.get(position));
        nagDialog.show();
      */

        final Dialog nagDialog = new Dialog(activity,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        nagDialog.setCancelable(true);
        nagDialog.setContentView(R.layout.preview_mediator);

         imageSlider=nagDialog.findViewById(R.id.preview_viewPager);

        init();

        nagDialog.show();



    }

    @Override
    public int getCount() {
        return image_arraylist.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }


    private void init() {

        try {
            Log.d("images","calling Slide_adap_fullScreen");

            Slide_adap_fullScreen sliderPagerAdapter = new Slide_adap_fullScreen(activity, image_arraylist);
            imageSlider.setAdapter(sliderPagerAdapter);

            imageSlider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        }catch (Exception e){

            e.printStackTrace();
        }
    }


}
