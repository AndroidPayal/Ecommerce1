package payal.cluebix.www.ecommerce.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
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

    public Slider_adapter(Activity activity, List<String> image_arraylist) {
        this.activity = activity;
        this.image_arraylist = image_arraylist;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.activity_slider_adapter, container, false);
        ImageView im_slider = (ImageView) view.findViewById(R.id.im_slider);
        Log.e("dashboard_imageUrl", Base_url.IMAGE_DIRECTORY_NAME+image_arraylist.get(position));
        Picasso.with(activity.getApplicationContext())
                .load(Base_url.IMAGE_DIRECTORY_NAME+image_arraylist.get(position))
                .placeholder(R.drawable.loading) // optional
                .error(R.drawable.error_load)         // optional
                .into(im_slider);

        container.addView(view);

        im_slider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity.getApplicationContext(), "slider clicked_"+position, Toast.LENGTH_SHORT).show();

                image_preview(position);

            }
        });
        return view;
    }

    private void image_preview(int position) {

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
        //image.setImageBitmap(imge);

      /*   image.setOnTouchImageViewListener(new OnTouchImageViewListener() {

           @Override
            public void onMove() {
                PointF point = image.getScrollPosition();
                RectF rect = image.getZoomedRect();
                float currentZoom = image.getCurrentZoom();
                boolean isZoomed = image.isZoomed();
                scrollPositionTextView.setText("x: " + df.format(point.x) + " y: " + df.format(point.y));
                zoomedRectTextView.setText("left: " + df.format(rect.left) + " top: " + df.format(rect.top)
                        + "\nright: " + df.format(rect.right) + " bottom: " + df.format(rect.bottom));
                currentZoomTextView.setText("getCurrentZoom(): " + currentZoom + " isZoomed(): " + isZoomed);
            }
        });*/

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
