package com.sample.escpfontdownload;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Environment;
import android.os.MemoryFile;
import android.util.Log;
import android.util.Xml;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.logging.StreamHandler;

public class Command {
    Context _context;
    BluetoothSocket _socket=null;
    BluetoothDevice _device;
    private InputStream _inStream;
    private OutputStream _outStream;
    final UUID sppUuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    Printer.PrintMode printMode= Printer.PrintMode.Unknown;
    public static BufferedOutputStream outputStreamWriter=null;

    public Command(Context context, BluetoothDevice device){
        _context=context;
        _device=device;
        try {
            _socket = device.createRfcommSocketToServiceRecord(sppUuid);
        } catch (IOException e) {
            //Error
        }
        try {
            _socket.connect();
        } catch (IOException connEx) {
            try {
                _socket.close();
            } catch (IOException closeException) {
                //Error
            }
        }

        if (_socket != null && _socket.isConnected()) {
            //Socket is connected, now we can obtain our IO streams
//....
            try {
                _inStream = _socket.getInputStream();
                _outStream =  _socket.getOutputStream();

                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "output.txt");

                if (file.exists ())
                    file.delete ();

                FileOutputStream fileOutputStream=new FileOutputStream(file);
                outputStreamWriter=new BufferedOutputStream(fileOutputStream);

            } catch (IOException e) {
                //Error
                utils.Log("EXCEPTION in assigning in/out stream: "+ e.getMessage());
            }
        }
    }

    public void close(){
        try {
            if(outputStreamWriter!=null){
                outputStreamWriter.flush();
                outputStreamWriter.close();
            }
            if(_outStream!=null) {
                _outStream.flush();
                _outStream.close();
            }
            if(_inStream!=null) {
                _inStream.close();
            }
            if(_socket!=null)
                _socket.close();
        }catch(Exception ex){

        }
    }


    String read(final int timeout){
        utils.Log("read: ..."+timeout);
        byte[] buffer = new byte[1024];  // buffer (our data)
        int bytesCount=0; // amount of read bytes
        String s="";
        int timecount=0;

        while (timecount < timeout) {
            try {
                //reading data from input stream
                if(_inStream.available()>0) {
                    bytesCount = _inStream.read(buffer);
                    if (buffer != null && bytesCount > 0) {
                        //Parse received bytes
                        break;
                    }
                }else {
                    Thread.sleep(1000);
                    timecount += 1000;
                }
            } catch (InterruptedException ex) {
                //Error
                utils.Log("read: InterruptedException: "+ ex.getMessage());
            } catch (IOException ex) {
                //Error
                utils.Log("read: IOException: "+ ex.getMessage());
            }
        }
        utils.Log("read: "+timecount);
        try{
            if(bytesCount==0){
                utils.Log("read: no bytes received");
                s="";
            }else {
                s = new String(buffer, "US-ASCII");
                utils.Log("read: bytes received: "+s);
            }
        }catch (Exception ex){
            utils.Log("read: exception: "+ ex.getMessage());
        }
        return s;
    }

    void write(byte[] bytes) {
        try {
            _outStream.write(bytes);
            outputStreamWriter.write(bytes);
        } catch (IOException e) {
            //Error
        }
    }

    void write(String text){
        try {
            _outStream.write(text.getBytes("UTF-8"));
            _outStream.write(text.getBytes("UTF-8"));
        } catch (IOException e) {
            //Error
        }

    }

    /***
     * Write a font file to connected output stream
     * @param filename
     */
    public void writeFont(String filename, String shortName, String name, String description){
        File file=new File(_context.getExternalFilesDir(null), filename);
        if(!file.exists()){
            return;
        }
        EasyPrintMode();
        byte[] bytes= toBytes(("{SC,N1 "+shortName+",N5 "+ name +",D "+description+"}").toCharArray());
        this.Execute(new Request(bytes, ""));
        SendFile(file, new char[]{0x7B, 0x41, 0xE5, 0x58, 0x7D} /*"{A.X}"*/);// or is it: SendFile(file, "{A?X}");

        //End file download
/*        ASCII
        {Ea.}
        Hex
        7B 45 61 18 7D
*/
        this.Execute(new Request(new  byte[]{0x7b,0x45, 0x61, 0x2E, 0x7D}, new char[]{'{','D','@',0x08,'}'})); // "{W.\u0012*}{D@\u0008}"));
        // after all we should read: {W.*}{D@.}, "7B 57 12 2A 7D 7B 44 40 08 7D"
        LinePrintMode();
    }

    void writeImage(String filename, String shortName, String name, String description){
        File file=new File(filename);
        if(!file.exists()){
            return;
        }
        this.EasyPrintMode();
        long fileTime=  file.lastModified();// new FileInfo(resource.SourcePath).LastWriteTime.Date.ToString("MM/dd/yyyy");
        Date date=new Date(fileTime);
        String datetime=String.format("MM/dd/yyyy", date);
        this.Execute(new Request(String.format("{SG,N1 "+shortName+ ",N5 " +name +",D " +description +  ",CD " + datetime+" }"), ""));
        this.SendFile(file, "\\{W.\\*\\}\\{R.z\\}".toCharArray());
        LinePrintMode();
    }

    byte[] toBytes(char[] chars) {
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
        byte[] bytes = Arrays.copyOfRange(byteBuffer.array(),
                byteBuffer.position(), byteBuffer.limit());
        Arrays.fill(byteBuffer.array(), (byte) 0); // clear sensitive data
        return bytes;
    }

    void EasyPrintMode(){
        if (this.printMode == Printer.PrintMode.EasyPrint)
            return;
        byte[] chars=new byte[]{0x1b,'E', 'Z'};
        this.Execute(new Request(chars, ""));
        this.printMode = Printer.PrintMode.EasyPrint;
    }

    void LinePrintMode()
    {
        if (this.printMode == Printer.PrintMode.LinePrint)
            return;
        byte[] chars=new byte[]{0x1b,'E', 'Z','{','L','P','}'};
        this.Execute(new Request(chars, ""));
        this.printMode = Printer.PrintMode.LinePrint;
    }

    void Execute(Request request){
        utils.Log("Execute...");
        if(this._outStream!=null){
            if(request.Text!=null && request.Text.length()>0) {
                this.write(request.Text);
                utils.Log("Execute: wrote "+request.Text);
            }
            if(request.Data!=null && request.Data.length>0) {
                this.write(request.Data);
                utils.Log("Execute: wrote "+request.Data.length +" bytes");
            }
            //is there any expected response?
            if(request.ExpectedResponse!=null && request.ExpectedResponse.length()>0) {
                utils.Log("Execute: looking for "+request.ExpectedResponse);
                String answer = read(request.ExtendedTimeout);
                if (answer.length() > 0) {
                    if (answer.equals(request.ExpectedResponse)) {
                        //response matches expectation
                        utils.Log("Execute: " + request.ExpectedResponse + " received");
                    }else{
                        utils.Log("Execute: "+request.ExpectedResponse +" NOT received: "+answer);
                    }
                }
            }else if(request.ExpectedChars!=null){
                utils.Log("Execute: looking for chars: "+ new String(request.ExpectedChars));
                String answer = read(request.ExtendedTimeout);
                if (answer.length() > 0) {
                    if (answer.equals(request.ExpectedResponse)) {
                        //response matches expectation
                        utils.Log("Execute: " + request.ExpectedResponse + " received");
                    }else{
                        utils.Log("Execute: "+request.ExpectedResponse +" NOT received: "+answer);
                    }
                }

            }else{
                utils.Log("Execute: no response requested");
            }
        }else{
            utils.Log("Execute..."+"outstream is null");
        }
    }

    /*
    Download Fonts
    The process of downloading fonts has multiple parts:

    Send font header information.

    Send one block of font data.

    The printer sends the Printer Acknowledge command.

    Repeat Steps 2 and 3 until all font data has been sent.

    End the font download.

    The printer sends Printer Acknowledgement and Flash Done commands.

    1. Send font header information.
    Command

    {SC,N1 sfName,N5 ftName,D ftDesc}

    N1, N5, and D are the names for the parameter values. Use this table to understand the variable fields.

    Field
     Description

    SC
     Font header information command

    sfName
     1 byte short font name

    ftName
     5 bytes of font name

    ftDesc
     1 to 20 bytes of font description




    Example

    {SC,N1 f,N5 ARABE, D 224 CH E-MAP D ARABIC}

       Note: You must include the comma (,) as the field separator in the command.

    2. Send one block of font data.
    Command

    {Seq;Length;Data;CRC}

    Use this table to understand the variable fields.

    Field
     Field Length
    (byte)

    Description

    Seq
     1
     Sequence number, starts from 0x20

    Length
     2
     Data length of this block

    Data
     Variable
     Font data

    CRC
     2
     Data length + data


    Example

    {20;00 10;7C 6A 00 ..... 00 0C 00;1A E5}

     3. The printer sends the Printer Acknowledge command.
    Format
     Command

    ASCII
     {A?X}

    Hex
     7B 41 E5 58 7D


    4. Repeat Steps 2 and 3 until all font data has been sent.
    5. End the font download.
    Format
     Command

    ASCII
     {Ea.}

    Hex
     7B 45 61 18 7D


    6. The printer sends Printer Acknowledgement and Flash Done commands.
    Format
     Command

    ASCII
     {W.*}{D@.}

    Hex
     7B 57 12 2A 7D 7B 44 40 08 7D


    ESC/P Programmer's Reference Manual (P/N 937-010-004)
    © 2007-2011 Intermec Technologies Corporation. All rights reserved.

     */
    void SendFile(File file, char[] blockresponse){
        int seqNum = 32; //sequence number, starts at 0x20
        FileInputStream fileInputStream=null;
        try {
            fileInputStream=new FileInputStream(file);
        }catch (FileNotFoundException ex){
            utils.Log("SendFile: "+ex.getMessage());
        }
        if(file==null) {
            utils.Log("SendFile: "+"file is null");
            return;
        }
        while(true){
            byte[] numArray=new byte[8000];
            try {
                int numRead = fileInputStream.read(numArray, 0, 4096);
                utils.Log("SendFile: "+"read "+numRead+" bytes");
                if(numRead>0) {
                /*
                byte[] bytes = new byte[2 + numArray.Length];
                bytes[0] = (byte) (numArray.Length % 256);
                bytes[1] = (byte) (numArray.Length / 256);
                BlockCopy( src, srcOffset, dst, dstOffset, count)
                System.Buffer.BlockCopy((Array) numArray, 0, (Array) bytes, 2, numArray.Length);
                ushort checksum = this.crcEsc.ComputeChecksum(bytes);
                byte[] data = new byte[bytes.Length + 5];
                data[0] = (byte) 123;
                data[1] = (byte) num;
                System.Buffer.BlockCopy((Array) bytes, 0, (Array) data, 2, bytes.Length);
                data[data.Length - 3] = (byte) ((uint) checksum % 256U);
                data[data.Length - 2] = (byte) ((uint) checksum / 256U);
                data[data.Length - 1] = (byte) 125;
                this.Command.Execute(new Request(data, blockResponse, 8000));
                */

                utils.Log("====== numArray =======");
                utils.Log(utils.dumpHexString(numArray, 0, 32));
                    byte[] bytes = new byte[numRead + 2];
                    bytes[0] = (byte) (numRead % 256); // will be 00
                    bytes[1] = (byte) (numRead / 256); // will be 10  => 0x1000 = 4096
                    //BlockCopy((Array) numArray, 0, (Array) bytes, 2, numArray.Length);
                    //copy src from start to dest at start for number of bytes
                    System.arraycopy(numArray, 0, bytes, 2, numRead);

                    utils.Log("====== bytes =======");
                    utils.Log(utils.dumpHexString(bytes, 0, 32));

                    //bytes is now prepended with num bytes in front
                    int chksum = Crc16x.crc16(bytes); //chksum includes two bytes for size at beginning

                    byte[] data = new byte[bytes.length + 5 ];
                    data[0] = (byte) 123; // this is a {
                    data[1] = (byte) seqNum; // magic number?, no, just a blank (0x20, 32), a sequence number that is constant?!
                    //Do we need to send a semicolon between the fields and data?
                    //like {seqNum;size;data;crc}

                    //copy source array bytes starting from 0 to data starting at 2
                    System.arraycopy(bytes, 0, data, 2, bytes.length);

                    utils.Log("====== data =======");
                    utils.Log(utils.dumpHexString(data, 0, 32));

                    //add chksum at end and then a }
                    data[data.length-3]=(byte)(chksum % 256);
                    data[data.length-2]=(byte)(chksum / 256);
                    data[data.length-1]=(byte)125;  // this is a }
                    utils.Log("SendFile: "+"checksum= "+utils.dumpHexString(new byte[]{data[data.length-3]}) +
                            utils.dumpHexString(new byte[]{data[data.length-2]}));
                    this.Execute(new Request(data, blockresponse, 8000));
                }else{
                    break;
                }

            }catch (Exception ex){
                utils.Log("sendFile EXCEPTION: "+ex.getMessage());
            }
        }
        try{
            fileInputStream.close();
        }catch (Exception ex){

        }
        byte[] chars=new byte[]{'{', 'E','a','}'};
        this.Execute(new Request(chars, "{D@.}", 6000));
    }

}

