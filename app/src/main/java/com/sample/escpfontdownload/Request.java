package com.sample.escpfontdownload;

public class Request{
    public String Text=null;
    public byte[] Data=null;
    public String ExpectedResponse=null;
    public char[] ExpectedChars=null;

    public int ExtendedTimeout=1;

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

    public Request(byte[] data, char[] expectedData)
    {
        this.Data=data;
        this.ExpectedChars=expectedData;

    }

    public Request(byte[] data, String expectedResponse, int extendedTimeout)
    {
        this.Data=data;
        this.ExpectedResponse=expectedResponse;
        this.ExtendedTimeout = extendedTimeout;
    }

    public Request(byte[] data, char[] expectedData, int extendedTimeout)
    {
        this.Data=data;
        this.ExpectedChars=expectedData;
        this.ExtendedTimeout = extendedTimeout;
    }

    public Request(String text, String expectedResponse, int extendedTimeout)
    {
        this.Text=text;
        this.ExpectedResponse=expectedResponse;
        this.ExtendedTimeout = extendedTimeout;
    }
}
