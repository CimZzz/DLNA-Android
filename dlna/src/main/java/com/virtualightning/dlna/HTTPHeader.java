package com.virtualightning.dlna;

import com.virtualightning.dlna.tools.HeaderReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by CimZzz on 17/5/31.<br>
 * Project Name : Hunban.com Education<br>
 * Since : Education_0.0.1<br>
 * Description:<br>
 * Description
 */
public class HTTPHeader {
    String method;
    String methodPath;
    Long contentLength;
    long rangeStart;
    long rangeEnd;
    HashMap<String,String> otherHeaders;

    HTTPHeader() {
        rangeStart = rangeEnd = -1L;
    }

    static HTTPHeader analyzeParams(InputStream inputStream) throws IOException {
        return analyzeParams(inputStream,null,false);
    }

    static HTTPHeader analyzeParams(InputStream inputStream,boolean otherUpper) throws IOException {
        return analyzeParams(inputStream,null,otherUpper);
    }

    static HTTPHeader analyzeParams(InputStream inputStream, String needMethod , boolean otherUpper) throws IOException {
        HeaderReader reader = new HeaderReader(inputStream);
        HTTPHeader params = new HTTPHeader();
        params.otherHeaders = new HashMap<>();
        String line;
        String splitArray[] = new String[2];
        /*Read方法行*/
        line = reader.readLine();
        if(line == null)
            return null;
        String methodLine[] = line.split(" ");
        params.method = methodLine[0];
        params.methodPath = methodLine[1];
        if(needMethod != null && !needMethod.equals(params.method))
            return null;
        while ((line = reader.readLine()) != null && !line.equals("")) {
            int colonIndex = line.indexOf(':');
            splitArray[0] = line.substring(0,colonIndex);
            splitArray[1] = line.substring(colonIndex + 1,line.length());

            switch (splitArray[0]) {
                case "Content-Length":
                    params.contentLength = Long.parseLong(splitArray[1].trim());
                    break;
                case "Range":
                    String rangeStr = splitArray[1].substring(splitArray[1].indexOf('=')+1,splitArray[1].length());
                    String rangeArr[] = rangeStr.split("-");
                    params.rangeStart = Long.parseLong(rangeArr[0]);
                    if(rangeArr.length == 1)
                        params.rangeEnd = -1L;
                    else params.rangeEnd = Long.parseLong(rangeArr[1]);
                    break;

            }
            if(otherUpper)
                params.otherHeaders.put(splitArray[0].toUpperCase(),splitArray[1].trim());
            else params.otherHeaders.put(splitArray[0],splitArray[1].trim());
        }

        return params;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return methodPath;
    }

    public String getHeader(String name) {
        return otherHeaders.get(name);
    }
}
