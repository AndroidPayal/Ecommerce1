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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import payal.cluebix.www.ecommerce.Adapter.Quotation_item_adap;
import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.Datas.quotation2;
import payal.cluebix.www.ecommerce.Handlers.RquestHandler;

public class Quotation_items_list extends AppCompatActivity implements Quotation_item_adap.ClickListener {

    Quotation_item_adap adapter;
    RecyclerView recyclerView;
    ArrayList<quotation2> product_item;

    String url=Base_url.Get_an_quotation_detail;/*/quote_number/user_id*/
    String quote_no,user_id,screen;
    TextView pdfcontent,t_Quantity,t_price,quotation_id,created_Date,expiry_date;
    int quant=0,invoice_items=0;Float total_price=0.0f;
    ArrayList<String> qutation_table_item=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        product_item = new ArrayList<>();

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

        get_old_Element();

        t_Quantity.setText("Total Items Ordered: "+invoice_items);//quant
        t_price.setText("Grand Total: "+total_price);
     /*   created_Date.setText("created date");
        expiry_date.setText("expiry date");*/
        quotation_id.setText("Quotation Code:"+quote_no);


        adapter=new Quotation_item_adap(getApplicationContext(),product_item);
        adapter.setClickListener(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,linearLayoutManager.getOrientation()));
        recyclerView.setAdapter(adapter);

    }

    private void get_old_Element() {

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url+quote_no+"/"+user_id, new Response.Listener<String>(){
            @Override
            public void onResponse(final String response) {
                Log.d("quotescreen1","quotaion detail="+response+"\n quote url="+url+quote_no+"/"+user_id);

                /*
                *  {
        "id": "129",
        "quote_id": "Q-5",
        "shopping_cart_id": "196",
        "product_id": "9",
        "product_name": "For_Demo",
        "price": "200.00",
        "qty": "1",
        "description": "this is very good layer wall paper to display this time, this is very good",
        "brand": "Cluebix Software",

        "stock_status": "0",
        "user_id": "39",
        "amount": null,
        "percent": null,
        "created_date": null,
        "expiry_date": null,
        "sample": "1",
        "sample_price": "0.00",
        "manufacturing": "0",
        "quantity": "1"
    }*/
                JSONObject post_data;
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++) {
                        post_data = jsonArray.getJSONObject(i);
                        Log.d("quotescreen1","quotaion in="+post_data.getString("id"));

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
                        String created_date1=post_data.getString("created_date");
                        String expiry_date1=post_data.getString("expiry_date");
                        String sample=post_data.getString("sample");
                        String sample_price=post_data.getString("sample_price");

                        product_item.add(new quotation2(id, quote_id,shopping_cart_id,user_id,product_id
                                ,product_name,price,qty,description,brand
                                ,sample,sample_price));//,product_images

                        quant=quant+Integer.parseInt(qty);
                        invoice_items++;
                        if (sample.equals("1")) {
                            total_price = total_price + (Float.parseFloat(price) * Integer.parseInt(qty))
                                        +Float.parseFloat(sample_price);
                        }else{
                            total_price = total_price + (Float.parseFloat(price) * Integer.parseInt(qty));
                        }

                        created_Date.setText("Gen:"+created_date1);
                        expiry_date.setText("Exp:"+expiry_date1);

                        pdfcontent.setText(pdfcontent.getText()+"Product "+i+"_ID : "+product_id+"\nName : "+product_name
                                +"\nPrice : "+price
                                +"\nQuantity ordered : "+qty
                                +"\nProduct Description : "+description
                                +"\nBrand : "+brand
                                +"\n\n---------------------------\n");

/*String id, String quote_id, String shopping_cart_id
            , String user_id, String product_id, String product_name, String price,String qty,String description
    ,String brand, String product_images*/
/*
*{
        "id": "10",
        "quote_id": "Q-5",
        "shopping_cart_id": "15",
        "product_id": "3",
        "product_name": "classy look wallpaper",
        "price": "253.00",
        "qty": "1",
        "description": "this is very good layer wall paper to display this time",
        "brand": "Unique Wallpaper",
        "user_id": "1",
        "amount": null,
        "percent": null
    }*/
                    }
                    t_Quantity.setText("Total Items Ordered: "+invoice_items);//quant
                    t_price.setText("Grand Total: "+total_price);

                    adapter.notifyData(product_item);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("quotescreen1","volley error="+error+"");
                Toast.makeText(Quotation_items_list.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        });
        RquestHandler.getInstance(Quotation_items_list.this).addToRequestQueue(stringRequest);

    }

    @Override
    public void itemClicked(View view, int position) {

        quotation2 a=product_item.get(position);

        Intent i=new Intent(this,ProductDetail.class);
        i.putExtra("selected_prod_id",a.getProduct_id());
        i.putExtra("ParentScreen","2");
        startActivity(i);

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







/*


    */


    /**
     * Background task to generate pdf from users content
     * @author androidsrc.net
     *
     *//*

    private class PdfGenerationTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            PdfDocument document = new PdfDocument();

            // repaint the user's text into the page
            View content = findViewById(R.id.pdf_content);

            // crate a page description
            int pageNumber = 1;
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(content.getWidth(),
                    content.getHeight() + 20, pageNumber).create();

            // create a new page from the PageInfo
            PdfDocument.Page page = document.startPage(pageInfo);

            content.draw(page.getCanvas());

            // do final processing of the page
            document.finishPage(page);

            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmmss");
            String pdfName = Base_url.pdf_name
                    + sdf.format(Calendar.getInstance().getTime()) + ".pdf";

            File outputFile = new File(Base_url.pdf_saved_path, pdfName);

            try {
                outputFile.createNewFile();
                OutputStream out = new FileOutputStream(outputFile);
                document.writeTo(out);
                document.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return outputFile.getPath();
        }

        @Override
        protected void onPostExecute(String filePath) {
            if (filePath != null) {

                Toast.makeText(getApplicationContext(),
                        "Pdf saved at " + Base_url.pdf_saved_path, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Error in Pdf creation" + Base_url.pdf_saved_path, Toast.LENGTH_SHORT)
                        .show();
            }

        }

    }
*/



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("backPress",screen);
        if (screen.equals("1")){
            Intent i=new Intent(Quotation_items_list.this,CenterActivity.class);
            i.putExtra("cartTransition","dash");
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }



}

