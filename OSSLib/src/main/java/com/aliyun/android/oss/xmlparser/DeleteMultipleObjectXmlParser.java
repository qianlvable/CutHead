/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.xmlparser; 

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

/** 
 * 
 * @author luoruici 
 * 
 */
public class DeleteMultipleObjectXmlParser extends AbstractXmlParser {
    
    public List<String> parse(InputStream in) throws IOException{
       
        try {
            this.prepare(in);
            return read();
        } catch (XmlPullParserException e) {
            return new ArrayList<String>();
        } finally {
            in.close();
        }
    }

    private List<String> read() throws XmlPullParserException, IOException {
        List<String> deletes = new ArrayList<String>();
        
        parser.require(XmlPullParser.START_TAG, ns, "DeleteResult");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            if (name.equals("Deleted")) {
                String key = readKey();
                Log.d("key", key);
                if (key != null) {
                    deletes.add(key);
                }
            } else {
                skip(parser);
            }
        }
        return deletes;
    }

    private String readKey() throws XmlPullParserException, IOException {
        String key = null;
        parser.require(XmlPullParser.START_TAG, ns, "Deleted");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            if (name.equals("Key")) {
                key = readTextByTagName(parser, name);
            } else {
                skip(parser);
            }
        }
        return key;
    }
}

