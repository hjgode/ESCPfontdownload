package com.sample.escpfontdownload;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Font {
    ByteBuffer buffer;
    public FontInfo fontInfo=new FontInfo();

    public Font(Context context, String filename){
         read(context, filename);
        Log.d("###TAG", fontInfo.toString());
    }

    FontInfo read(Context context, String filename){
        byte[] header=new byte[4+3+1+5+1+8+1+1+1+1+8+20];

        File file=new File(context.getExternalFilesDir(null), filename);
        if(!file.exists()){
            return null;
        }
        try {
            FileInputStream inputStreamReader = new FileInputStream(file);// context.getExternalFilesDir(null),filename);
            int cnt=inputStreamReader.read(header,0,header.length);
            if(cnt!=header.length)
                return null;
            buffer=ByteBuffer.wrap(header);
            fontInfo.notUsed1 = buffer.getInt();
            fontInfo.version= readBytes(3);  //1.0
            fontInfo.notUsedC1=buffer.get();
            fontInfo.name= readBytes(5);
            fontInfo.charName=buffer.get();
            fontInfo.notUsed2=buffer.getLong();
            fontInfo.notUsedC2=buffer.get();
            fontInfo.notUsedC3=buffer.get();
            fontInfo.notUsedC4=buffer.get();
            fontInfo.userVersion=buffer.get();
            fontInfo.date= readBytes(8);
            fontInfo.description= readBytes(20);

        }catch(Exception ex){

        }
        return fontInfo;
    }

    byte[] readBytes(int count){
        byte[] bytes=new byte[count];
        for(int i=0; i<count; i++)
            bytes[i]=buffer.get();
        String s= new String(bytes, StandardCharsets.US_ASCII);
        return bytes;
    }

    class FontInfo {
         int notUsed1;   //4
        //[MarshalAs(UnmanagedType.ByValArray, SizeConst = 3)]
         byte[] version;
         byte notUsedC1;
        //[MarshalAs(UnmanagedType.ByValArray, SizeConst = 5)]
         byte[] name;
         byte charName;
         long notUsed2;  //8
         byte notUsedC2;
         byte notUsedC3;
         byte notUsedC4;
         byte userVersion;
        //[MarshalAs(UnmanagedType.ByValArray, SizeConst = 8)]
         byte[] date;
        //[MarshalAs(UnmanagedType.ByValArray, SizeConst = 20)]
         byte[] description;

        public String getVersion(){
            return new String(version, StandardCharsets.US_ASCII);
        }
        public String getName(){
            return new String(name, StandardCharsets.US_ASCII);
        }
        public String getCharName(){
            return new String(new byte[]{charName}, StandardCharsets.US_ASCII);
        }
        public String getDescription(){
            return new String(description, StandardCharsets.US_ASCII);
        }
        public String getDate(){
            return new String(date, StandardCharsets.US_ASCII);
        }

        @Override
        public String toString(){
            String s=getName()+", "+getCharName()+", "+getDate()+", "+getDescription()+", "+getVersion();
            return s;
        }
    }

}
