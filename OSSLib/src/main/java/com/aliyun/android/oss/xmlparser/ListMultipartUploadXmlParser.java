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

import com.aliyun.android.oss.model.ListMultipartUploadsXmlObject;
import com.aliyun.android.oss.model.MultipartUploadSummary;
import com.aliyun.android.util.Helper;

/**
 * @author ruici
 */
public class ListMultipartUploadXmlParser extends AbstractXmlParser {

    public ListMultipartUploadsXmlObject parse(InputStream in)
            throws XmlPullParserException, IOException {
        try {
            this.prepare(in);

            return read();
        } finally {
            in.close();
        }
    }

    /**
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private ListMultipartUploadsXmlObject read() throws XmlPullParserException,
            IOException {

        ListMultipartUploadsXmlObject xml = new ListMultipartUploadsXmlObject();
        parser.require(XmlPullParser.START_TAG, ns,
                "ListMultipartUploadsResult");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            if (name.equals("Bucket")) {
                xml.setBucketName(readTextByTagName(parser, name));
            } else if (name.equals("KeyMarker")) {
                xml.setKeyMarker(readTextByTagName(parser, name));
            } else if (name.equals("UploadIdMarker")) {
                xml.setUploadIdMarker(readTextByTagName(parser, name));
            } else if (name.equals("NextKeyMarker")) {
                xml.setNextKeyMarker(readTextByTagName(parser, name));
            } else if (name.equals("NextUploadIdMarker")) {
                xml.setNextUploadIdMarker(readTextByTagName(parser, name));
            } else if (name.equals("Prefix")) {
                xml.setPrefix(readTextByTagName(parser, name));
            } else if (name.equals("Delimiter")) {
                xml.setDelimiter(readTextByTagName(parser, name));
            } else if (name.equals("MaxUploads")) {
                xml.setMaxUploads(readTextByTagName(parser, name));
            } else if (name.equals("IsTruncated")) {
                xml.setTruncated(readTextByTagName(parser, name));
            } else if (name.equals("Upload")) {
                MultipartUploadSummary upload = readUpload();
                xml.getUploads().add(upload);
            } else {
                skip(parser);
            }
        }
        return xml;
    }

    /**
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private MultipartUploadSummary readUpload() throws XmlPullParserException,
            IOException {
        String key = null;
        String uploadId = null;
        String initiated = null;

        parser.require(XmlPullParser.START_TAG, ns,
                "Upload");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            if (name.equals("Key")) {
                key = readTextByTagName(parser, name);
            } else if (name.equals("UploadId")) {
                uploadId = readTextByTagName(parser, name);
            } else if (name.equals("Initiated")) {
                initiated = readTextByTagName(parser, name);
            } else {
                skip(parser);
            }
        }
        MultipartUploadSummary res = new MultipartUploadSummary(key, uploadId,
                null);
        try {
            res.setInitiated(Helper.getDateFromString(initiated));
        } catch (ParseException e) {
            Log.e("DateParserError", "Parsing date " + initiated
                    + " error.");
        }
        return res;
    }
}
