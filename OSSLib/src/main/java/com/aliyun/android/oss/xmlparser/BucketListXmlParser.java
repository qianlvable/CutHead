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
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.aliyun.android.oss.model.Bucket;
import com.aliyun.android.oss.model.User;
import com.aliyun.android.util.Helper;

/**
 * @author ruici
 */
public class BucketListXmlParser extends AbstractXmlParser {

    public BucketListXmlParser() {
        super();
    }

    public List<Bucket> parse(InputStream in) throws IOException,
            XmlPullParserException {
        try {
            prepare(in);
            return readBuckets();
        } finally {
            in.close();
        }
    }

    private List<Bucket> readBuckets() throws XmlPullParserException,
            IOException {
        List<Bucket> buckets = null;
        User owner = null;
        parser.require(XmlPullParser.START_TAG, ns, "ListAllMyBucketsResult");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            if (name.equals("Buckets")) {
                buckets = readBuckets(parser);
                break;
            } else if (name.equals("Owner")) {
                owner = readOwner(parser);
            } else {
                skip(parser);
            }
        }

        if (owner != null) {
            for (Bucket bucket: buckets) {
                bucket.setOwner(owner);
            }
        }

        return buckets;
    }
    
    private List<Bucket> readBuckets(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        List<Bucket> buckets = new ArrayList<Bucket>();

        parser.require(XmlPullParser.START_TAG, ns, "Buckets");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            Log.d("Name", name);
            if (name.equals("Bucket")) {
                Bucket bucket = readBucket(parser);
                buckets.add(bucket);
            } else {
                skip(parser);
            }
        }
        return buckets;
    }

    /**
     * * @param parser /** * @return
     * 
     * @throws IOException
     * @throws XmlPullParserException
     */
    private Bucket readBucket(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Bucket");
        String bucketName = null;
        String dateString = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            if (name.equals("Name")) {
                bucketName = readTextByTagName(parser, "Name");
            } else if (name.equals("CreateDate")) {
                dateString = readTextByTagName(parser, "CreateDate");
            } else {
                skip(parser);
            }
        }

        Bucket bucket = new Bucket(bucketName);
        if (dateString != null) {
            try {
                Date date = Helper.getDateFromString(dateString);
                bucket.setCreationDate(date);
            } catch (ParseException e) {
                Log.e("Date Parsing", "parsing date " + dateString + " error!");
            }
        }

        return bucket;
    }
}
