package payal.cluebix.www.ecommerce;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.pdf.PdfDocument;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.itextpdf.awt.geom.CubicCurve2D;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import payal.cluebix.www.ecommerce.Adapter.Quotation_item_adap;
import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.Datas.quotation2;
import payal.cluebix.www.ecommerce.Datas.sample_Cart;
import payal.cluebix.www.ecommerce.Handlers.RquestHandler;
import payal.cluebix.www.ecommerce.Handlers.SessionManager;
import payal.cluebix.www.ecommerce.Handlers.myDbClass;

public class Quotation_items_list extends AppCompatActivity {

    Quotation_item_adap adapter;
    RecyclerView recyclerView;
    ArrayList<quotation2> product_item;

    String url=Base_url.Get_an_quotation_detail;/*/quote_number/user_id*/
    String quote_no,user_id,screen;
    TextView pdfcontent,t_Quantity,t_price,quotation_id,created_Date,expiry_date;
    int quant=0,invoice_items=0;Float total_price=0.0f;
    ArrayList<String> qutation_table_item=new ArrayList<>();

    SessionManager session;
    String Uid;String Uname,Umail,Udate1,Udate2,Umob;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        product_item = new ArrayList<>();

        session=new SessionManager(this.getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        Uname = user.get(SessionManager.KEY_NAME);
        Uid = user.get(SessionManager.KEY_ID);
        Umail=user.get(SessionManager.KEY_email);
        Udate1=user.get(SessionManager.KEY_createDate);
        Udate2=user.get(SessionManager.KEY_LastModified);
        Umob=user.get(SessionManager.KEY_mobile);
        Log.d("sessionscreen","name_userId="+Uid+"\n_user_name="+Uname+"\nemail="+Umail
                +"\ndate1="+Udate1+"\ndate2="+Udate2+"\nmobile="+Umob);

        screen=getIntent().getStringExtra("screen");
        quote_no=getIntent().getStringExtra("quote_id");
        user_id=getIntent().getStringExtra("userid");

        t_Quantity=(TextView)findViewById(R.id.t_quantity);
        t_price=(TextView)findViewById(R.id.t_price);

        pdfcontent=(TextView)findViewById(R.id.pdf_content);
        recyclerView=(RecyclerView)findViewById(R.id.invoice_recycler);

        quotation_id=(TextView)findViewById(R.id.quotation_id);
        created_Date=(TextView)findViewById(R.id.quotation_createcd_date);
        expiry_date=(TextView)findViewById(R.id.quotation_expiry_date);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter=new Quotation_item_adap(getApplicationContext(),product_item);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,linearLayoutManager.getOrientation()));
        recyclerView.setAdapter(adapter);


        get_old_Element();

        t_Quantity.setText("Total Items Ordered: "+invoice_items);//quant
        t_price.setText("Grand Total: "+total_price);
     /*   created_Date.setText("created date");
        expiry_date.setText("expiry date");*/

        if (Umail !=null) {
            quotation_id.setText("Quotation Code:" + quote_no);
        }
        else {
            quotation_id.setVisibility(View.GONE);
            created_Date.setVisibility(View.GONE);
            expiry_date.setVisibility(View.GONE);
        }



    }

    private void get_old_Element() {

        if(Umail!=null){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url + quote_no + "/" + user_id, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                Log.d("quotescreen1", "quotaion detail=" + response + "\n quote url=" + url + quote_no + "/" + user_id);

/*{"success":"true","quotes":[{"id":"28","quote_id":"Q-16","shopping_cart_id":"565","product_id":"20","product_name":"this is test product","price":"345.00","qty":"2","description":"this is dummy description","brand":"my comp","stock_status":"0","user_id":"39","amount":"10.00","percent":"%","created_date":"2018-07-21","expiry_date":"2018-07-28","product_code":"959794","sample":"0","sample_price":"58.00","manufacturing":"0","quantity":"2","name":"payal","mobile":"8962607775"}]}
* */
/* new::==
{"success":"true","quotes":[{"id":"18","quote_id":"Q-11","shopping_cart_id":"553","product_id":"58","product_name":"Demo_200","price":"2000.00","qty":"0","description":"This is a single layer wallpaper in a glossy finish.","brand":"Cluebix Software","stock_status":"0","user_id":"39","amount":"5.00","percent":"%","created_date":"2018-07-20","expiry_date":"2018-07-27","product_code":"968387","sample":"1","sample_price":"500.00","manufacturing":"1","quantity":"0","name":"tester","mobile":"9021073372"}]}*/

                String created_date1="0000/00/00";
                String expiry_date1="0000/00/00";
                JSONObject post_data;
                try {
                    JSONObject obj=new JSONObject(response);
                    JSONArray jsonArray = obj.getJSONArray("quotes");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        post_data = jsonArray.getJSONObject(i);
                        Log.d("quotescreen1", "quotaion in=" + post_data.getString("id"));

                        String id = post_data.getString("id");
                        String quote_id = post_data.getString("quote_id");
                        String shopping_cart_id = post_data.getString("shopping_cart_id");
                        String product_id = post_data.getString("product_id");
                        String product_name = post_data.getString("product_name");
                        String price = post_data.getString("price");
                        String qty = post_data.getString("qty");
                        String description = post_data.getString("description");
                        String brand = post_data.getString("brand");
                        // String product_images = post_data.getString("product_images");
                        String user_id = post_data.getString("user_id");
                         created_date1 = post_data.getString("created_date");
                        expiry_date1 = post_data.getString("expiry_date");
                        String sample = post_data.getString("sample");
                        String sample_price = post_data.getString("sample_price");
                        String product_code=post_data.getString("product_code");
                        String mobile=post_data.getString("mobile");

                        product_item.add(new quotation2(id, quote_id, shopping_cart_id, user_id, product_id
                                , product_name, price, qty, description, brand
                                , sample, sample_price,product_code,mobile));//,product_images

                        quant = quant + Integer.parseInt(qty);
                        invoice_items++;
                        if (sample.equals("1")) {
                            total_price = total_price + (Float.parseFloat(price) * Integer.parseInt(qty))
                                    + Float.parseFloat(sample_price);
                        } else {
                            total_price = total_price + (Float.parseFloat(price) * Integer.parseInt(qty));
                        }

//                        created_Date.setText("Gen:" + created_date1);
//                        expiry_date.setText("Exp:" + expiry_date1);

                        pdfcontent.setText(pdfcontent.getText() + "Product " + i + "_ID : " + product_id + "\nName : " + product_name
                                + "\nPrice : " + price
                                + "\nQuantity ordered : " + qty
                                + "\nProduct Description : " + description
                                + "\nBrand : " + brand
                                + "\n\n---------------------------\n");

                    }

                    //code to change date format
                    String start_dt = created_date1;
                    DateFormat formatter = new SimpleDateFormat("yyyy-MM-DD");
                    Date date = null;
                    try {
                        date = (Date)formatter.parse(start_dt);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
                    String finalString = newFormat.format(date);

                         created_Date.setText("Gen:" + finalString);

                     start_dt = expiry_date1;
                     formatter = new SimpleDateFormat("yyyy-MM-DD");
                     date = null;
                    try {
                        date = (Date)formatter.parse(start_dt);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                     newFormat = new SimpleDateFormat("dd-MM-yyyy");
                     finalString = newFormat.format(date);

                        expiry_date.setText("Exp:" + finalString);

                    t_Quantity.setText("Total Items Ordered: " + invoice_items);//quant
                    t_price.setText("Grand Total: " + total_price);

                    adapter.notifyData(product_item);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("quotescreen1", "volley error=" + error + "");
                Toast.makeText(Quotation_items_list.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        });
        RquestHandler.getInstance(Quotation_items_list.this).addToRequestQueue(stringRequest);


        }else {


            ArrayList<quotation2> datas = new myDbClass(Quotation_items_list.this).fetchValuesForQuotation();//fetch quotation value

            for (int i = 0; i < datas.size(); i++) {
                quotation2 a=datas.get(i);
                quant = quant + Integer.parseInt(a.getQty());
                invoice_items++;
                if (a.getSample().equals("1")) {
                    total_price = total_price + (Float.parseFloat(a.getPrice()) * Integer.parseInt(a.getQty()))
                            + Float.parseFloat(a.getSample_price());
                } else {
                    total_price = total_price + (Float.parseFloat(a.getPrice()) * Integer.parseInt(a.getQty()));
                }


                pdfcontent.setText(pdfcontent.getText() + "Product " + i + "_ID : " + a.getProduct_id() + "\nName : " + a.getProduct_name()
                        + "\nPrice : " + a.getPrice()
                        + "\nQuantity ordered : " + a.getQty()
                        + "\nProduct Description : " + a.getDescription()
                        + "\nBrand : " + a.getBrand()
                        + "\n\n---------------------------\n");

                product_item.add(new quotation2("0", "0", "0", "0", a.getProduct_id()
                        , a.getProduct_name(), a.getPrice(), a.getQty(), a.getDescription(), a.getBrand()
                        , a.getSample(), a.getSample_price(),a.getProduct_code(),a.getMobile()));
            }

            t_Quantity.setText("Total Items Ordered: " + invoice_items);//quant
            t_price.setText("Grand Total: " + total_price);

            adapter.notifyData(product_item);


        }




    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        MenuInflater inf=getMenuInflater();
        inf.inflate(R.menu.menu_pdf, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_save_pdf) {
            create_save_pdf();
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean create_save_pdf() {

        /*File dir = new File(Base_url.pdf_saved_path);
            if(!dir.exists())
                dir.mkdirs();
                pdfCreate();*/
          //  new PdfGenerationTask().execute();

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("permission_log","Permission is granted");
                File dir = new File(Base_url.pdf_saved_path);
                if(!dir.exists())
                    dir.mkdirs();
                pdfCreate();
                return true;
            } else {
                Log.v("permission_log","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("permission_log","Permission automatically granted");
            File dir = new File(Base_url.pdf_saved_path);
            if(!dir.exists())
                dir.mkdirs();
            pdfCreate();
            return true;
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v("permission_log","Permission: "+permissions[0]+ "was "+grantResults[0]);
            File dir = new File(Base_url.pdf_saved_path);
            if(!dir.exists())
                dir.mkdirs();
            pdfCreate();
        }
    }

    public void pdfCreate(){
        /**
         * Creating Document
         */
        qutation_table_item.clear();
        qutation_table_item.add("Name");qutation_table_item.add("ProductId");qutation_table_item.add("Sample");
        qutation_table_item.add("Rate");
        qutation_table_item.add("Quantity");qutation_table_item.add("Total Price");
        Document document = new Document();
            // Location to save
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmmss");
            String pdfName = Base_url.pdf_name
                    + sdf.format(Calendar.getInstance().getTime()) + ".pdf";

            File outputFile = new File(Base_url.pdf_saved_path, pdfName);

            try {
                outputFile.createNewFile();
               /* OutputStream out = new FileOutputStream(outputFile);
                document.close();
                out.close();*/
            } catch (IOException e) {
                e.printStackTrace();
            }


            PdfWriter.getInstance(document, new FileOutputStream(Base_url.pdf_saved_path+"/"+pdfName));//pdfName

        // Open to write
        document.open();

            // Document Settings
        document.setPageSize(PageSize.A4);
        document.addCreationDate();
        document.addAuthor("Cluebix Software");
        document.addCreator("Management");

        BaseColor mColorAccent = new BaseColor(getResources().getColor(R.color.colorPrimaryDark));//0, 153, 204, 255);
        float mHeadingFontSize = 20.0f;
        float mValueFontSize = 26.0f;
            float mLowFontSize = 16.0f;

            BaseFont urName = null;
            try {
                urName = BaseFont.createFont("assets/fonts/GreatVibes-Regular.otf", "UTF-8", BaseFont.EMBEDDED);
            } catch (IOException e) {
                e.printStackTrace();//Lobster_1.3
            }
            BaseFont urName2 = null;
            try {
                urName2 = BaseFont.createFont("assets/fonts/Lato-Semibold.ttf", "UTF-8", BaseFont.EMBEDDED);
            } catch (IOException e) {
                e.printStackTrace();
            }

        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));



        // Title Order Details...
// Adding Title....
        Font mOrderDetailsTitleFont = new Font(urName, mHeadingFontSize, Font.NORMAL, BaseColor.BLACK);
// Creating Chunk
        Chunk mOrderDetailsTitleChunk = new Chunk("Multivendor Application Invoice", mOrderDetailsTitleFont);
// Creating Paragraph to add...
        Paragraph mOrderDetailsTitleParagraph = new Paragraph(mOrderDetailsTitleChunk);
// Setting Alignment for Heading
        mOrderDetailsTitleParagraph.setAlignment(Element.ALIGN_RIGHT);
// Finally Adding that Chunk
        document.add(mOrderDetailsTitleParagraph);


// Fields of Order Details...
// Adding Chunks for Title and value
            Font mOrderIdFont = new Font(urName, mValueFontSize, Font.NORMAL, mColorAccent);
            Chunk mOrderIdChunk = new Chunk("Order No : "+quote_no+"\n\n", mOrderIdFont);
            Paragraph mOrderIdParagraph = new Paragraph(mOrderIdChunk);
            mOrderIdParagraph.setAlignment(Element.ALIGN_RIGHT);

            document.add(mOrderIdParagraph);


            //document.add(new Paragraph("hello this is first"));
            document.add(new Chunk(lineSeparator));
            document.add(new Paragraph("\n"));
            Toast.makeText(this, "generated pdf at Phone/MultivendorApp/", Toast.LENGTH_SHORT).show();

            PdfPTable table = new PdfPTable(qutation_table_item.size());//setting table heading row
            PdfPCell cell;

        for(int aw =0; aw < qutation_table_item.size(); aw++){//*(product_item.size()
///generating table row for hrandings
            cell = new PdfPCell(new Phrase(""+qutation_table_item.get(aw), new Font(urName2, mLowFontSize, Font.NORMAL, mColorAccent)));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setFixedHeight(28.0f);
            table.addCell(cell);

            //table.addCell();
            Log.d("awval",aw+"  "+product_item.size()* qutation_table_item.size());
        }

        for(int aw2 =0; aw2 <product_item.size();aw2++){//*(product_item.size()
 //generating table rows for items
            cell = new PdfPCell(new Phrase(""+product_item.get(aw2).getProduct_name()));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(""+product_item.get(aw2).getProduct_id()));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            if (product_item.get(aw2).getSample().equals("1")) {
                cell = new PdfPCell(new Phrase("" + product_item.get(aw2).getSample_price()));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

            }else{
                cell = new PdfPCell(new Phrase("0.00"));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

            }

            cell = new PdfPCell(new Phrase(""+product_item.get(aw2).getPrice()));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(""+product_item.get(aw2).getQty()));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
/*
                cell = new PdfPCell(new Phrase(""+product_item.get(aw2).getPrice()+ product_item.get(aw2).getSample_price()));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);*/
            float t=0.0f;
            if (product_item.get(aw2).getSample().equals("1")) {
                 t = Float.parseFloat(product_item.get(aw2).getPrice()) * Integer.parseInt(product_item.get(aw2).getQty())
                            +Float.parseFloat(product_item.get(aw2).getSample_price());
            }
            else{
                t = Float.parseFloat(product_item.get(aw2).getPrice()) * Integer.parseInt(product_item.get(aw2).getQty());
            }
            cell = new PdfPCell(new Phrase(""+t));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        document.add(table);

            Chunk mOrderIdChunk2 = new Chunk("\nTax = 0.00\nGrand Total = "+total_price);
            Paragraph mOrderIdParagraph2 = new Paragraph(mOrderIdChunk2);
            mOrderIdParagraph2.setAlignment(Element.ALIGN_RIGHT);

            document.add(mOrderIdParagraph2);

            Paragraph mOrderIdParagraph3=(new Paragraph(new Chunk(
                    "Define Designers\n" +
                    "Shailendra Nagar,\n" +
                    "Raipur, Chhattisgarh 492001,\n" +
                    "India",mOrderIdFont)));
            mOrderIdParagraph3.setAlignment(Element.ALIGN_RIGHT);
            document.add(mOrderIdParagraph3);

            Log.d("Tag1212","outing");
            document.close();

        } catch (DocumentException e) {
            Log.d("Tag1212","error1"+e);
        } catch (FileNotFoundException e) {
            Log.d("Tag1212","error2"+e);

        }
    }





    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("backPress",screen);
        if (screen.equals("1")){
            Intent i = null;
            if(Umail !=null) {
                 i = new Intent(Quotation_items_list.this, CenterActivity.class);

            }else{
                i = new Intent(Quotation_items_list.this, GuestActivity.class);
                new myDbClass(Quotation_items_list.this).DeleteAll();
            }
            i.putExtra("cartTransition", "dash");
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }



}

