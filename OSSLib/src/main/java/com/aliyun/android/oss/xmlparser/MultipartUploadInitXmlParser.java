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

/**
 * @author Ruici
 */
public class MultipartUploadInitXmlParser extends AbstractXmlParser {

    public String parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            prepare(in);
            return read();
        } finally {
            in.close();
        }
    }

    private String read() throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "InitiateMultipartUploadResult");
        
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            if (name.equals("UploadId")) {
                return readTextByTagName(parser, "UploadId");
            } else {
                skip(parser);
            }
        }           
        return null;
    }
}
