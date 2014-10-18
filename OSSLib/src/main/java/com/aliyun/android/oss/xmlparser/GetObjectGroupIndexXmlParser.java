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

import com.aliyun.android.oss.model.ObjectGroup;
import com.aliyun.android.oss.model.ObjectMetaData;
import com.aliyun.android.oss.model.Part;

/** 
 * 
 * @author luoruici 
 * 
 */
public class GetObjectGroupIndexXmlParser extends AbstractXmlParser {

    public ObjectGroup parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            this.prepare(in);
            return read();
        } finally {
            in.close();
        }
    }
    
    private ObjectGroup read() throws XmlPullParserException, IOException {
        String bucket = null;
        String key = null;
        String eTag = null;
        List<Part> parts = null;
        
        parser.require(XmlPullParser.START_TAG, ns, "FileGroup");
        
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            if (name.equals("Bucket")) {
                bucket = readTextByTagName(parser, name);
            } else if (name.equals("Key")) {
                key = readTextByTagName(parser, name);
            } else if (name.equals("ETag")) {
                eTag = readTextByTagName(parser, name);
            } else if (name.equals("FilePart")) {
                parts = readParts();
            } else {
                skip(parser);
            }
        }
        
        ObjectGroup obj = new ObjectGroup(key, bucket, parts);
        ObjectMetaData meta = new ObjectMetaData();
        meta.seteTag(eTag);
        obj.setMeta(meta);
        return obj;
    }
    
    private List<Part> readParts() throws XmlPullParserException, IOException {
        List<Part> parts = new ArrayList<Part>();
        parser.require(XmlPullParser.START_TAG, ns, "FilePart");
        
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            if (name.equals("Part")) {
                parts.add(readPart());
            } else {
                skip(parser);
            }
        }
        return parts;
    }

    /** * @return 
     * @throws IOException 
     * @throws XmlPullParserException */
    private Part readPart() throws XmlPullParserException, IOException {
        String number = null;
        String partName = null;
        String size = null;
        String eTag = null;
        parser.require(XmlPullParser.START_TAG, ns, "Part");
        
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            if (name.equals("PartNumber")) {
                number = readTextByTagName(parser, name);
            } else if (name.equals("PartName")) {
                partName = readTextByTagName(parser, name);;
            } else if (name.equals("PartSize")) {
                size = readTextByTagName(parser, name);
            } else if (name.equals("ETag")) {
                eTag = readTextByTagName(parser, name);
            } else {
                skip(parser);
            }
        }
        
        return new Part(eTag, Integer.valueOf(number), partName, Integer.parseInt(size));
    }
}

