package payal.cluebix.www.ecommerce.Handlers;

import android.os.Environment;
import android.util.Log;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by speed on 09-Apr-18.
 */

public class Metodos {

    public Boolean write(String fname, String fcontent) {
        try {
  /*
            String fpath = "/sdcard/"  ;
            File file = new File(fpath, ""+fname + ".pdf");

            if (!file.exists()) {
                file.createNewFile();
            }*/

        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "pdfdemo");
        if (!pdfFolder.exists()) {
            pdfFolder.createNewFile();//f.getParentFile().mkdirs()
            Log.i("quotescreen", "Pdf Directory created");
        }

        //Create time stamp

        File myFile = new File(pdfFolder + "/eCo" + ".pdf");

        OutputStream output = new FileOutputStream(myFile);
            Log.i("quotescreen", pdfFolder + "/eCo" + ".pdf");


            Font bfBold12 = new Font(Font.TIMES_ROMAN, 12, Font.BOLD);
            Font bf12 = new Font(Font.TIMES_ROMAN, 12);

            Document document = new Document();

            PdfWriter.getInstance(document,
                   output);//output
            document.open();

            document.add(new Paragraph("Sigueme en Twitter!"));

            document.add(new Paragraph("@DavidHackro"));
//            document.close();

            return true;
        } catch (IOException e) {
            Log.d("quotescreen1",e+"");
            return false;
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            Log.d("quotescreen2",e+"");
            return false;
        }
    }

}
