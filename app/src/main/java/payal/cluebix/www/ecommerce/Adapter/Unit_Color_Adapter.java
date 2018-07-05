package payal.cluebix.www.ecommerce.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lowagie.toolbox.plugins.PhotoAlbum;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.Datas.unit_color_data;
import payal.cluebix.www.ecommerce.Handlers.RquestHandler;
import payal.cluebix.www.ecommerce.R;

/**
 * Created by speed on 01-Jun-18.
 */

public class Unit_Color_Adapter extends RecyclerView.Adapter<Unit_Color_Adapter.ProductViewHolder>{
    private Context mCtx;
    private ArrayList<unit_color_data> productList;
    Unit_Color_Adapter.ClickListener clickListener;
    String url2= Base_url.UpdateColor;


    public Unit_Color_Adapter(Context mCtx, ArrayList<unit_color_data> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public Unit_Color_Adapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_layout_color, null,false);
        ViewGroup.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new Unit_Color_Adapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Unit_Color_Adapter.ProductViewHolder holder, final int position) {
        final unit_color_data a= productList.get(position);

        holder.s_no.setText((position+1)+"");
        holder.name.setText(a.getName());


        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                LayoutInflater inflater = LayoutInflater.from(mCtx);
                View dialogView = inflater.inflate( R.layout.popup_add_color_list,null);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                final EditText edit_name=(EditText)dialogView.findViewById(R.id.edit_color_name);
                Button submit=(Button)dialogView.findViewById(R.id.dialog_button_apply);
                Button cancel=(Button)dialogView.findViewById(R.id.dialog_button_cancel);

                edit_name.setText(a.getName());

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String name1=edit_name.getText().toString().trim();

                        StringRequest stringRequest=new StringRequest(Request.Method.POST, url2+a.getId(), new Response.Listener<String>(){
                            @Override
                            public void onResponse(String response) {
                                Log.d("___",response);

                                JSONObject post_data;
                                try {
                                    holder.name.setText(name1);
                                    alertDialog.cancel();
                                    //scroll krne pr updated name nii dikhta until we restart the screen
                                    //iske liye productlist ko update krna pdega
                                    JSONObject jsonObject=new JSONObject(response);
                                    String success=jsonObject.getString("success");
                                    String message=jsonObject.getString("message");

                                    Toast.makeText(mCtx, ""+message, Toast.LENGTH_SHORT).show();
                                    alertDialog.cancel();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("dashboard_error_res",error+"");
                                Toast.makeText(mCtx, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String , String> parameters= new HashMap<String, String>();
                                parameters.put("color_name",name1);
                                return parameters;
                            }
                        };
                        RquestHandler.getInstance(mCtx).addToRequestQueue(stringRequest);


                    }
                });

                    }

        });

    }

    public void setClickListener(Unit_Color_Adapter.ClickListener clickListener){
        this.clickListener=clickListener;
    }

    public void notifyData(ArrayList<unit_color_data> myList) {
        this.productList = myList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }




    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView s_no,name;ImageView options;

        public ProductViewHolder(View itemView) {
            super(itemView);
            s_no=(TextView)itemView.findViewById(R.id.unit_Sno);
            name=(TextView)itemView.findViewById(R.id.unit_name);
            options=(ImageView) itemView.findViewById(R.id.unit_options);
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
