/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.xmlparser;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.aliyun.android.oss.model.ObjectMetaData;
import com.aliyun.android.util.Helper;

/**
 * 解析Copy Ojbect操作返回的xml
 * 返回一个OSSObject对象，表示服务器返回的新的Object信息
 * @author ruici
 */
public class CopyObjectXmlParser extends AbstractXmlParser {

    public ObjectMetaData parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            prepare(in);
            return read();
        } finally {
            in.close();
        }
    }

    /** * @return 
     * @throws IOException 
     * @throws XmlPullParserException */
    private ObjectMetaData read() throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "CopyObjectResult");
        String lastModified = null;
        String eTag = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            if (name.equals("LastModified")) {
                lastModified = readTextByTagName(parser, "LastModified");
            } else if (name.equals("ETag")) {
                eTag = readTextByTagName(parser, "ETag");
            } else {
                skip(parser);
            }
        }
        
        Date d = null;
        try {
            d = Helper.getDateFromString(lastModified);
        } catch (ParseException e) {
            Log.e("DateParserError", "Parsing date " + lastModified + " error.");
        }
        ObjectMetaData md = new ObjectMetaData();
        md.setLastModified(d);
        md.seteTag(eTag);
        
        return md;
    }
}
