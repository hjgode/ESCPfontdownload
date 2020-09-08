package com.sample.escpfontdownload;

public class Request{
    public String Text=null;
    public byte[] Data=null;
    public String ExpectedResponse=null;
    public int ExtendedTimeout=0;

    public Request(String text, String expectedResponse)
    {
        this.Text = text;
        this.ExpectedResponse = expectedResponse;
    }

    public Request(byte[] data, String expectedResponse)
    {
        this.Data = data;
        this.ExpectedResponse = expectedResponse;
    }

    public Request(byte[] data, String expectedResponse, int extendedTimeout)
    {
        this.Data=data;
        this.ExpectedResponse=expectedResponse;
        this.ExtendedTimeout = extendedTimeout;
    }

    public Request(String text, String expectedResponse, int extendedTimeout)
    {
        this.Text=text;
        this.ExpectedResponse=expectedResponse;
        this.ExtendedTimeout = extendedTimeout;
    }
}
