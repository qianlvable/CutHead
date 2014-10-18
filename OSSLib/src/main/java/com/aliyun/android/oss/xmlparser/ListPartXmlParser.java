/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved. Use of this source code
 * is governed by a BSD-style license that can be found in the LICENSE file.
 */
package com.aliyun.android.oss.xmlparser;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.aliyun.android.oss.model.ListPartXmlObject;
import com.aliyun.android.oss.model.Part;
import com.aliyun.android.util.Helper;

/**
 * @author ruici
 */
public class ListPartXmlParser extends AbstractXmlParser {

    public ListPartXmlObject parse(InputStream in)
            throws XmlPullParserException, IOException {
        try {
            this.prepare(in);

            return read();
        } finally {
            in.close();
        }
    }

    private ListPartXmlObject read() throws XmlPullParserException, IOException {
        ListPartXmlObject xml = new ListPartXmlObject();

        parser.require(XmlPullParser.START_TAG, ns, "ListPartsResult");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            if (name.equals("Bucket")) {
                xml.setBucketName(readTextByTagName(parser, name));
            } else if (name.equals("Key")) {
                xml.setKey(readTextByTagName(parser, name));
            } else if (name.equals("UploadId")) {
                xml.setUploadId(readTextByTagName(parser, name));
            } else if (name.equals("NextPartNumberMarker")) {
                xml.setNextPartNumberMarker(readTextByTagName(parser, name));
            } else if (name.equals("MaxParts")) {
                xml.setMaxParts(readTextByTagName(parser, name));
            } else if (name.equals("IsTruncated")) {
                xml.setTruncated(readTextByTagName(parser, name));
            } else if (name.equals("Part")) {
                Part part = readPart();
                xml.getParts().add(part);
            } else {
                skip(parser);
            }
        }
        return xml;
    }

    private Part readPart() throws XmlPullParserException, IOException {
        String partNumber = null;
        String lastModified = null;
        String eTag = null;
        String size = null;

        parser.require(XmlPullParser.START_TAG, ns, "Part");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            if (name.equals("PartNumber")) {
                partNumber = readTextByTagName(parser, name);
            } else if (name.equals("LastModified")) {
                lastModified = readTextByTagName(parser, name);
            } else if (name.equals("ETag")) {
                eTag = readTextByTagName(parser, name);
            } else if (name.equals("Size")) {
                size = readTextByTagName(parser, name);
            } else {
                skip(parser);
            }
        }

        Part part = new Part(eTag, Integer.valueOf(partNumber),
                Integer.parseInt(size));
        try {
            part.setLastModified(Helper.getDateFromString(lastModified));
        } catch (ParseException e) {
            Log.e("DateParserError", "Parsing date " + lastModified + " error.");
        }
        
        return part;
    }
}
