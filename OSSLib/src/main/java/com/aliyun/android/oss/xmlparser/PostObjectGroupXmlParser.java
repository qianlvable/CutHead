/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved. Use of this source code
 * is governed by a BSD-style license that can be found in the LICENSE file.
 */
package com.aliyun.android.oss.xmlparser;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.aliyun.android.oss.model.OSSObjectSummary;

/**
 * @author Ruici
 */
public class PostObjectGroupXmlParser extends AbstractXmlParser {

    public OSSObjectSummary parse(InputStream in)
            throws XmlPullParserException, IOException {
        try {
            this.prepare(in);
            return read();
        } finally {
            in.close();
        }
    }

    private OSSObjectSummary read() throws XmlPullParserException, IOException {
        String bucket = null;
        String key = null;
        String size = null;
        String eTag = null;
        parser.require(XmlPullParser.START_TAG, ns, "CompleteFileGroup");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            if (name.equals("Bucket")) {
                bucket = readTextByTagName(parser, name);
            } else if (name.equals("Key")) {
                key = readTextByTagName(parser, name);
            } else if (name.equals("Size")) {
                size = readTextByTagName(parser, name);
            } else if (name.equals("ETag")) {
                eTag = readTextByTagName(parser, name);
            } else {
                skip(parser);
            }
        }

        OSSObjectSummary obj = new OSSObjectSummary(bucket, key);
        obj.seteTag(eTag);
        obj.setSize(Long.parseLong(size));

        return obj;
    }
}
