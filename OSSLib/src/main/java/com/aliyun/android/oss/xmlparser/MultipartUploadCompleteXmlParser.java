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

import com.aliyun.android.oss.model.OSSObject;
import com.aliyun.android.oss.model.ObjectMetaData;

/** 
 * 
 * @author Ruici 
 * 
 */
public class MultipartUploadCompleteXmlParser extends AbstractXmlParser {

    public OSSObject parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            prepare(in);
            return read();
        } finally {
            in.close();
        }
    }

    private OSSObject read() throws XmlPullParserException, IOException {
        String bucketName = null;
        String objectKey = null;
        String eTag = null;
       
        
        parser.require(XmlPullParser.START_TAG, ns, "CompleteMultipartUploadResult");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            
            if (name.equals("Bucket")) {
                bucketName = readTextByTagName(parser, "Bucket");
            } else if (name.equals("Key")) {
                objectKey = readTextByTagName(parser, "Key");
            } else if (name.equals("ETag")) {
                eTag = readTextByTagName(parser, "ETag");
            } else {
                skip(parser);
            }
               
        }
        OSSObject obj = new OSSObject(bucketName, objectKey);
        ObjectMetaData metadata = new ObjectMetaData();
        metadata.seteTag(eTag);
        obj.setObjectMetaData(metadata);
        
        return obj;
        
    }
}

