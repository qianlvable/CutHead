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

import com.aliyun.android.oss.model.AccessLevel;

/** 
 * 
 * @author ruici 
 * 
 */
public class GetBucketAclXmlParser extends AbstractXmlParser {

    public AccessLevel parse(InputStream in) throws XmlPullParserException, IOException {
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
    private AccessLevel read() throws XmlPullParserException, IOException {
        
        parser.require(XmlPullParser.START_TAG, ns, "AccessControlPolicy");
        
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            
            if (name.equals("AccessControlList")) {
                //do nothing
            } else if (name.equals("Grant")) {
                String grant = readTextByTagName(parser, "Grant");
                return AccessLevel.valueFromName(grant);
            } else {
                skip(parser);
            }
        }
        
        return null;
    }
}
