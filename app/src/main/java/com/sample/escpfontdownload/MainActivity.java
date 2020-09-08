package com.sample.escpfontdownload;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        copyAssets();
        //readFontHeader();

        Font font =new Font(this, "DOS864.FON");
        utils.Log(font.fontInfo.toString());

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice("00:06:66:03:84:C9");

        Command command=new Command(this, device);
        command.writeFont("DOS864.FON", font.fontInfo.getName(), font.fontInfo.getCharName(), font.fontInfo.getDescription());
        command.close();
    }

    void readFontHeader(){
        Font font =new Font(this, "DOS864.FON");
        utils.Log(font.fontInfo.toString());
    }

    private void copyAssets() {
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            utils.Log("Failed to get asset file list." + e.getMessage());
        }
        if (files != null) for (String filename : files) {
            if(filename.equals("images") || filename.equals("webkit"))
                continue;
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);
                if(in!=null) {
                    File outFile = new File(getExternalFilesDir(null), filename);
                    out = new FileOutputStream(outFile);
                    copyFile(in, out);
                }
            } catch(IOException e) {
                utils.Log("Failed to copy asset file: " + filename + " " + e.getMessage());
            }
            finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
            }
        }
    }
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
}