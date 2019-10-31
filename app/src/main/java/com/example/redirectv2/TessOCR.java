package com.example.redirectv2;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;

public class TessOCR {

    private String datapath;
    private TessBaseAPI tess;
    Context context;
    public static String getDataPath()
    {
        StringBuilder b = new StringBuilder();
        b.append(Environment.getExternalStorageDirectory());
        b.append("/redirectocr/");
        b.append("tessdata/");
        b.append("eng.traineddata");
        return b.toString();
    }
    public static boolean hasTrainingData()
    {
        return new File(getDataPath()).exists();
    }

    //TODO: add store training data functin
    public static LinkedList<String> getDirectory()
    {
        LinkedList<String> filePaths = new LinkedList<>();
        File directory = new File(Environment.getExternalStorageDirectory().toString());
        File[] fList = directory.listFiles();

        for(File f : fList)
        {
            filePaths.add(f.getAbsolutePath());
        }

        return filePaths;

    }
    public TessOCR(Context con)
    {
        this.context = con;
        this.datapath = Environment.getExternalStorageDirectory() +"/redirectocr/";
        File dir = new File(datapath + "tessdata/");
        File fil = new File(datapath + "tessdata/" + "eng.traindata");
        if(!fil.exists())
        {
            Log.d("OCRLog", "in file does not exist" );
            dir.mkdirs();
            try {
                copyFile(this.context);
            }catch (IOException ex)
            {
                Log.e("OCRLog" , ex.getMessage() + ex.getStackTrace().toString());
            }
        }

        tess = new TessBaseAPI();
        String language = "eng";
        tess.init(datapath, language);

        tess.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO_ONLY);
    }

    public void StopRecognition()
    {
        tess.stop();
    }

    public String getOCRResult(Bitmap b)
    {
        tess.setImage(b);
        String result = tess.getUTF8Text();
        return result;
    }

    public void onDestroy()
    {
        if(tess != null)
        {
            tess.end();
        }
    }
    private void copyFile(Context c) throws IOException
    {
        AssetManager as = c.getAssets();

        InputStream in = as.open("eng.traineddata");
        OutputStream out = new FileOutputStream(datapath + "tessdata/");
        byte buffer[] = new byte[1024];
        int read = in.read(buffer);
        while(read != -1)
        {
            out.write(buffer, 0, read);
            read = in.read(buffer);
        }



    }
}
