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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.Datas.company_data;
import payal.cluebix.www.ecommerce.Datas.unit_color_data;
import payal.cluebix.www.ecommerce.Handlers.RquestHandler;
import payal.cluebix.www.ecommerce.R;

/**
 * Created by speed on 02-Jun-18.
 */

public class Company_Adapter extends RecyclerView.Adapter<Company_Adapter.ProductViewHolder> {

    private Context mCtx;
    private ArrayList<company_data> productList;
    Company_Adapter.ClickListener clickListener;
    private String url2= Base_url.UpdateCompany;

    public Company_Adapter(Context mCtx, ArrayList<company_data> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public Company_Adapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_layout_color, null,false);
        ViewGroup.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new Company_Adapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Company_Adapter.ProductViewHolder holder, final int position) {
        final company_data a= productList.get(position);

        holder.s_no.setText((position+1)+"");
        holder.name.setText(a.getName1());
        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                LayoutInflater inflater = LayoutInflater.from(mCtx);
                View dialogView = inflater.inflate( R.layout.popup_add_company_list,null);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                final EditText edit_name=(EditText)dialogView.findViewById(R.id.edit_company_name);
                Button submit=(Button)dialogView.findViewById(R.id.dialog_button_apply);
                Button cancel=(Button)dialogView.findViewById(R.id.dialog_button_cancel);

                edit_name.setText(a.getName1());

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

                        StringRequest stringRequest=new StringRequest(Request.Method.POST, url2+a.getId1(), new Response.Listener<String>(){
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
                                parameters.put("name",name1);
                                return parameters;
                            }
                        };
                        RquestHandler.getInstance(mCtx).addToRequestQueue(stringRequest);


                    }
                });

            }

        });


    }

    public void setClickListener(Company_Adapter.ClickListener clickListener){
        this.clickListener=clickListener;
    }

    public void notifyData(ArrayList<company_data> myList) {
        this.productList = myList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }




    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView s_no,name,date;
        ImageView options;

        public ProductViewHolder(View itemView) {
            super(itemView);
            s_no=(TextView)itemView.findViewById(R.id.unit_Sno);
            name=(TextView)itemView.findViewById(R.id.unit_name);
            date=(TextView)itemView.findViewById(R.id.com_date);
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
