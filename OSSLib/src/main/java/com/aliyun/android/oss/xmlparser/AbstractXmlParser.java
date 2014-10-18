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

import com.aliyun.android.oss.model.User;

import android.util.Xml;

/** 
 * 
 * @author ruici 
 * 
 */
public abstract class AbstractXmlParser {

    protected static final String ns = null;
    protected XmlPullParser parser;
    
    public AbstractXmlParser() {
        parser = Xml.newPullParser();
    }
    
    /**
     * 仅在parser方法调用时被调用
     * @param in
    /** * @throws XmlPullParserException
     * @throws IOException 
     */
    protected void prepare(InputStream in) throws XmlPullParserException, IOException {
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(in, null);
        parser.nextTag();
    }
    
    /**
     * 递归跳过当前Tag* @param parser
    /** * @throws XmlPullParserException
    /** * @throws IOException
     */
    protected void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    /**
     * 获取给定标签内的文本内容* @param parser
    /** * @param tagName
    /** * @return
    /** * @throws XmlPullParserException
    /** * @throws IOException
     */
    protected String readTextByTagName(XmlPullParser parser, String tagName) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, tagName);
        String result = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, tagName);
        return result;
    }
    
    /**
     * 获取parser扫描到的文本节点的内容* @param parser
    /** * @return
    /** * @throws XmlPullParserException
    /** * @throws IOException
     */
    protected String readText(XmlPullParser parser) throws XmlPullParserException, IOException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        
        return result;
    }
    
    /**
     * * 获取owner标签的内容，转换为user对象@param parser /** * @return
     * 
     * @throws IOException
     * @throws XmlPullParserException
     */
    protected User readOwner(XmlPullParser parser) throws XmlPullParserException,
            IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Owner");
        String id = null;
        String displayName = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            if (name.equals("ID")) {
                id = readTextByTagName(parser, "ID");
            } else if (name.equals("DisplayName")) {
                displayName = readTextByTagName(parser, "DisplayName");
            } else {
                skip(parser);
            }
        }

        return new User(id, displayName);
    }
}

