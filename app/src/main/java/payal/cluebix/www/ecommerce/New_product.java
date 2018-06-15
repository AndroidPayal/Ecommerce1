package payal.cluebix.www.ecommerce;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import payal.cluebix.www.ecommerce.Adapter.New_product_adapter;
import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.Handlers.AndroidMultiPartEntity;
import payal.cluebix.www.ecommerce.Handlers.SessionManager;

public class New_product extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText e_name,e_desc,e_prize,e_color;
    Button b_submit;
    ImageButton b_add_image;
    RecyclerView recyclerView;
    Spinner spin1,spin2,spin3,spin4,spin5,spin6;
    EditText edit_spin1,edit_spin2,edit_spin3;
    TextView back_add_product;

    ArrayList<Bitmap> imageArray=new ArrayList<>();
    ArrayList<String> imageNameList=new ArrayList<>();
    ArrayList<String> imagePath=new ArrayList<>();

    String url1=Base_url.Add_product_url;

    long totalSize = 0;

    ArrayAdapter<Integer> adapter;

     String p_name;
     String p_desc;
     String p_prize;
     String p_color,f_min="0",f_max="0",f_cost="0";
     String s_min="0",s_max="0",s_cost="0",t_min="0",t_max="30",t_cost="0";

     ProgressBar prog;
     TextView textProg;
     TextView textOk;
     TextView textResp;
     Dialog dialog;

    SessionManager session;
    String Uname,Uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature( Window.FEATURE_NO_TITLE );

        setContentView(R.layout.activity_new_product);

        session=new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        Uname = user.get(SessionManager.KEY_NAME);
        Uid = user.get(SessionManager.KEY_ID);
        Log.d("session","name_userId="+Uid+"\nemail_user_name="+Uname);

        e_name=(EditText)findViewById(R.id.productName);
        e_desc=(EditText)findViewById(R.id.productDescription);
        e_prize=(EditText)findViewById(R.id.prize);
        e_color=(EditText)findViewById(R.id.e_color);
        b_submit=(Button)findViewById(R.id.button_add_item);
        back_add_product=(TextView)findViewById(R.id.back_add_product);

        b_add_image=(ImageButton) findViewById(R.id.add_image);
        recyclerView=(RecyclerView)findViewById(R.id.list_selected_images);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

        spin1=(Spinner)findViewById(R.id.spin1);
        spin2=(Spinner)findViewById(R.id.spin2);
        spin3=(Spinner)findViewById(R.id.spin3);
        spin4=(Spinner)findViewById(R.id.spin4);
        spin5=(Spinner)findViewById(R.id.spin5);
        //spin6=(Spinner)findViewById(R.id.spin6);
        edit_spin1=(EditText) findViewById(R.id.edit_spin1);
        edit_spin2=(EditText) findViewById(R.id.edit_spin2);
        edit_spin3=(EditText) findViewById(R.id.edit_spin3);


        Integer[] items = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items);
        spin1.setAdapter(adapter);
        spin2.setAdapter(adapter);

        items = new Integer[]{11,12,13,14,15,16,17,18,19,20};
        adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items);
        spin3.setAdapter(adapter);
        spin4.setAdapter(adapter);

        items = new Integer[]{21,22,23,24,25,26,27,28,29,30};
        adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items);
        spin5.setAdapter(adapter);
        //spin6.setAdapter(adapter);

        spin1.setOnItemSelectedListener(this);
        b_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(New_product.this, AlbumSelectActivity.class);
                intent.putExtra(Constants.INTENT_EXTRA_LIMIT, Base_url.numberOfImagesToSelect);
                startActivityForResult(intent, Constants.REQUEST_CODE);
            }
        });


        dialog = new Dialog(New_product.this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.progress_dialog);

        Window window = dialog.getWindow();
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        dialog.setTitle("Add Product Request..");
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
         prog=(ProgressBar) dialog.findViewById(R.id.progBar);
         textProg=(TextView)dialog.findViewById(R.id.textProgress);
         textOk=(TextView)dialog.findViewById(R.id.text_ok);
         textResp=(TextView)dialog.findViewById(R.id.textResponse);
         textOk.setVisibility(View.INVISIBLE);
        textOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        b_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 p_name=e_name.getText().toString().trim();
                 p_desc=e_desc.getText().toString().trim();
                 p_prize=e_prize.getText().toString().trim();
                 p_color=e_color.getText().toString().trim();
                f_cost=edit_spin1.getText().toString().trim();
                s_cost=edit_spin2.getText().toString().trim();
                t_cost=edit_spin3.getText().toString().trim();

                if (p_name.isEmpty()||p_desc.isEmpty()||p_prize.isEmpty()||p_color.isEmpty()||imageArray.isEmpty()){
                    Toast.makeText(New_product.this, "Fill All Mandetory Fields and select images", Toast.LENGTH_SHORT).show();
                    e_name.setError("Fill detail");e_desc.setError("Fill detail");e_prize.setError("Fill detail");e_color.setError("Fill detail");
                }
                else {

                    new UploadFileToServer().execute();

                }
            }
        });

        back_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(New_product.this,My_products.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            recyclerView.setVisibility(View.VISIBLE);
            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);

            for (int i = 0; i < images.size(); i++) {
                File f = new File(images.get(i).path);
                String filename=images.get(i).path.substring(images.get(i).path.lastIndexOf("/")+1);
                imagePath.add(images.get(i).path);
                imageNameList.add(filename);

                if (f.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                    imageArray.add(myBitmap);

                }
            }

            if(!imageArray.isEmpty()) {
                New_product_adapter adapter = new New_product_adapter(getApplicationContext(), imageArray);
                recyclerView.setAdapter(adapter);
            }
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //Toast.makeText(this, "selected: "+i, Toast.LENGTH_SHORT).show();
        int id=view.getId();
        switch(id){
            case R.id.spin1:f_min=spin1.getSelectedItem().toString();break;
            case R.id.spin2:f_max=spin1.getSelectedItem().toString();break;

            case R.id.spin3:s_min=spin1.getSelectedItem().toString();break;
            case R.id.spin4:s_max=spin1.getSelectedItem().toString();break;

            case R.id.spin5:t_min=spin1.getSelectedItem().toString();break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    /**
     * Uploading the file to server
     * */
    class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            prog.setProgress(0);
            dialog.show();
            super.onPreExecute();
        }
        @Override
        protected void onProgressUpdate(Integer... progress) {
            prog.setProgress(progress[0]);
            textProg.setText(String.valueOf(progress[0]) + "%");
        }
        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }
        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url1+Uid);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });
                if (imagePath.size()>Base_url.numberOfImagesToSelect){
                    for (int i=0;i<Base_url.numberOfImagesToSelect;i++) {
                        File sourceFile1 = new File(imagePath.get(i));
                        entity.addPart("product_images[]", new FileBody(sourceFile1));
                    }
                }else {
                    for (int i = 0; i < imagePath.size(); i++) {
                        File sourceFile1 = new File(imagePath.get(i));
                        entity.addPart("product_images[]", new FileBody(sourceFile1));
                    }
                }

                entity.addPart("product_name",new StringBody(p_name));
                entity.addPart("brand", new StringBody("Product Brand"));
                entity.addPart("description", new StringBody(p_desc));
                entity.addPart("price", new StringBody(p_prize));
                entity.addPart("color", new StringBody(p_color));

                entity.addPart("first_min", new StringBody(f_min));
                entity.addPart("first_max", new StringBody(f_max));
                entity.addPart("first_price", new StringBody(f_cost));
                entity.addPart("second_min", new StringBody(s_min));
                entity.addPart("second_max", new StringBody(s_max));
                entity.addPart("second_price", new StringBody(s_cost));
                entity.addPart("third_min", new StringBody(t_min));
                entity.addPart("third_max", new StringBody(t_max));
                entity.addPart("third_price", new StringBody(t_cost));


              /*   `product_name` varchar(100) NOT NULL,
  `brand` varchar(100) NOT NULL,
  `description` varchar(500) NOT NULL,
  `product_code` varchar(255) NOT NULL,
  `color` varchar(255) NOT NULL,
  `price` decimal(7,2) NOT NULL,
  `first_min` varchar(255) NOT NULL,
  `first_max` varchar(255) NOT NULL,
  `first_price` decimal(7,2) NOT NULL,
  `second_min` varchar(255) NOT NULL,
  `second_max` varchar(255) NOT NULL,
  `second_price` decimal(7,2) NOT NULL,
  `third_min` varchar(255) NOT NULL,
  `third_max` varchar(255) NOT NULL,
  `third_price` decimal(7,2) NOT NULL,
  `product_images` varchar(1000) NOT NULL,
  `created_date` date NOT NULL*/


                totalSize = entity.getContentLength();
                Log.d("Tag___","total data size="+totalSize+"");
                httppost.setEntity(entity);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("TAG__", "Response from server: " + result);
            textResp.setText("Status : "+result);
            textOk.setVisibility(View.VISIBLE);
            prog.setVisibility(View.INVISIBLE);
            super.onPostExecute(result);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        MenuInflater inf=getMenuInflater();
        inf.inflate(R.menu.menu_myproduct, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.my_product) {
            Intent intent=new Intent(New_product.this,My_products.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
