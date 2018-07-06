package payal.cluebix.www.ecommerce.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.Handlers.TouchImageView;
import payal.cluebix.www.ecommerce.R;

/**
 * Created by speed on 16-Jun-18.
 */

public class Slide_adap_fullScreen extends PagerAdapter{
    private LayoutInflater layoutInflater;
    Activity activity;
    List<String> image_arraylist;

    public Slide_adap_fullScreen(Activity activity, List<String> image_arraylist) {
        this.activity = activity;
        this.image_arraylist = image_arraylist;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.preview_image, container, false);

        Log.d("images","inflated full screen");

        TouchImageView image=view.findViewById(R.id.img);

        Picasso.with(activity.getApplicationContext())
                .load(Base_url.IMAGE_DIRECTORY_NAME+image_arraylist.get(position))
                .placeholder(R.drawable.loading) // optional
                .error(R.drawable.error_load)         // optional
                .into(image);

        container.addView(view);


        /* final Dialog nagDialog = new Dialog(activity,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
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
        nagDialog.show();*/

        return view;
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



}
