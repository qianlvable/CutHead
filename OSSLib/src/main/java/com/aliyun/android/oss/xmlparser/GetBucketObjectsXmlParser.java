/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.xmlparser;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.aliyun.android.oss.model.GetBucketXmlObject;
import com.aliyun.android.oss.model.OSSObjectSummary;
import com.aliyun.android.oss.model.User;
import com.aliyun.android.util.Helper;

/**
 * @author ruici
 */
public class GetBucketObjectsXmlParser extends AbstractXmlParser {

    public GetBucketXmlObject parse(InputStream in) throws IOException,
            XmlPullParserException {
        try {
            prepare(in);
            return read();
        } finally {
            in.close();
        }
    }

    /**
     * * @return
     * 
     * @throws IOException
     * @throws XmlPullParserException
     */
    private GetBucketXmlObject read() throws XmlPullParserException,
            IOException {

        GetBucketXmlObject xml = new GetBucketXmlObject();

        parser.require(XmlPullParser.START_TAG, ns, "ListBucketResult");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            if (name.equals("Name")) {
                xml.setBucketName(readTextByTagName(parser, "Name"));
            } else if (name.equals("Marker")) {
                xml.setMarker(readTextByTagName(parser, "Marker"));
            } else if (name.equals("MaxKeys")) {
                xml.setMaxkeys(readTextByTagName(parser, "MaxKeys"));
            } else if (name.equals("Delimiter")) {
                xml.setDelimiter(readTextByTagName(parser, "Delimiter"));
            } else if (name.equals("NextMarker")) {
                xml.setNextMarker(readTextByTagName(parser, "NextMarker"));
            } else if (name.equals("Contents")) {
                OSSObjectSummary obj = readFileObject(parser,
                        xml.getBucketName());
                xml.getItems().add(obj);
            } else if (name.equals("CommonPrefixes")) {
                xml.getItems()
                        .addAll(readPrefixes(parser, xml.getBucketName()));
            } else if (name.equals("Prefix")) {
                String prefix = readTextByTagName(parser, "Prefix");
                if (!Helper.isEmptyString(prefix)) {
                    OSSObjectSummary obj = new OSSObjectSummary(
                            xml.getBucketName(), prefix);
                    xml.getItems().add(obj);
                }
            } else {
                skip(parser);
            }
        }
        return xml;
    }

    private List<OSSObjectSummary> readPrefixes(XmlPullParser parser,
            String bucketName) throws XmlPullParserException, IOException {

        List<OSSObjectSummary> list = new ArrayList<OSSObjectSummary>();
        parser.require(XmlPullParser.START_TAG, ns, "CommonPrefixes");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            if (name.equals("Prefix")) {
                String prefix = readTextByTagName(parser, "Prefix");
                list.add(new OSSObjectSummary(bucketName, prefix));
            } else {
                skip(parser);
            }
        }
        return list;
    }

    /**
     * * @param parser /** * @return
     * 
     * @throws IOException
     * @throws XmlPullParserException
     */
    private OSSObjectSummary readFileObject(XmlPullParser parser,
            String bucketName) throws XmlPullParserException, IOException {

        String key = null;
        String lastModified = null;
        String eTag = null;
        String type = null;
        String size = null;
        String storageClass = null;
        User owner = null;

        parser.require(XmlPullParser.START_TAG, ns, "Contents");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            if (name.equals("Key")) {
                key = readTextByTagName(parser, "Key");
            } else if (name.equals("LastModified")) {
                lastModified = readTextByTagName(parser, "LastModified");
            } else if (name.equals("ETag")) {
                eTag = readTextByTagName(parser, "ETag");
            } else if (name.equals("Type")) {
                type = readTextByTagName(parser, "Type");
            } else if (name.equals("Size")) {
                size = readTextByTagName(parser, "Size");
            } else if (name.equals("StorageClass")) {
                storageClass = readTextByTagName(parser, "StorageClass");
            } else if (name.equals("Owner")) {
                owner = readOwner(parser);
            } else {
                skip(parser);
            }
        }
        int i;
        if (size == null || size == "") {
            i = -1;
        } else {
            i = Integer.parseInt(size);
        }
        OSSObjectSummary obj = new OSSObjectSummary(bucketName, key, null,
                eTag, type, i, storageClass, owner);
        try {
            obj.setLastModified(Helper.getDateFromString(lastModified));
        } catch (ParseException e) {
            Log.e("DateParserError", "Parsing date " + lastModified + " error.");
        }

        return obj;
    }

}
