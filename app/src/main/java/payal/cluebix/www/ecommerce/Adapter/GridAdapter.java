package payal.cluebix.www.ecommerce.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.R;
import payal.cluebix.www.ecommerce.Datas.sample_myProduct;

/**
 * Created by speed on 26-Mar-18.
 */

public class GridAdapter extends BaseAdapter {
    Context mctx;
    ArrayList<sample_myProduct> product;

    public GridAdapter(Context mctx, ArrayList<sample_myProduct> name){
        this.mctx=mctx;
        this.product=name;
    }

    @Override
    public int getCount() {
        return product.size();
    }

    @Override
    public Object getItem(int i) {
        return product.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View grid;
        LayoutInflater inflater = (LayoutInflater) mctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {

            grid = new View(mctx);
            grid = inflater.inflate(R.layout.grid_adapter, null);
            TextView textView = (TextView) grid.findViewById(R.id.text2);
            ImageView imageView = (ImageView)grid.findViewById(R.id.image2);
            textView.setText(product.get(i).getProduct_name());
            String image_names=product.get(i).getProduct_images_String();
            List<String> items = Arrays.asList(image_names.split(","));
            String first_image_url= Base_url.IMAGE_DIRECTORY_NAME+""+items.get(0);
           // imageView.setImageBitmap(product.get(i).getimage());
            Picasso.with(mctx)
                    .load(first_image_url)
                    .placeholder(R.drawable.loading) // optional
                    .error(R.drawable.error_load)         // optional
                    .into(imageView);

        } else {
            grid = (View) view;
        }

        return grid;
    }


    public void notifyData(ArrayList<sample_myProduct> myList) {
        this.product = myList;
        notifyDataSetChanged();
    }

}
