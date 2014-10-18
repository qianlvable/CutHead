/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.xmlparser;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.aliyun.android.oss.model.HttpResponseError;

import android.util.Xml;

public class HttpResponseErrorParser {

    private static final String ns = null;
    
    public HttpResponseError parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readError(parser);
        } finally {
            in.close();
        }
    }
    
    private HttpResponseError readError(XmlPullParser parser) throws XmlPullParserException, IOException {
        HttpResponseError error = new HttpResponseError();
        
        parser.require(XmlPullParser.START_TAG, ns, "Error");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            
            if (name.equals("Code")) {
                error.setCode(readTextByTagName(parser, name));
            } else if (name.equals("Message")) {
                error.setMessage(readTextByTagName(parser, name));
            } else if (name.equals("RequestId")) {
                error.setRequestId(readTextByTagName(parser, name));
            } else if (name.equals("HostId")) {
                error.setHostId(readTextByTagName(parser, name));
            } else {
                skip(parser);
            }
        }
        return error;
    }
    
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private String readTextByTagName(XmlPullParser parser, String tagName) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, tagName);
        String result = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, tagName);
        return result;
    }
    
    private String readText(XmlPullParser parser) throws XmlPullParserException, IOException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        
        return result;
    }
}
