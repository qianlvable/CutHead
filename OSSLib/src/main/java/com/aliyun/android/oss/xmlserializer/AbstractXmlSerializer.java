/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.xmlserializer;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

/**
 * 存储对象为XML
 * 
 * @author Michael
 */
public abstract class AbstractXmlSerializer {
    /**
     * xml Serializer
     */
    protected XmlSerializer serializer;

    /**
     * root名称
     */
    protected String root;

    /**
     * 构造新实例
     */
    public AbstractXmlSerializer(String root) {
        serializer = Xml.newSerializer();
        this.root = root;
    }

    /**
     * 添加一个<tag>test</tag>
     */
    protected void addTextTag(String tag, String text) throws IOException {
        serializer.startTag("", tag);
        serializer.text(text);
        serializer.endTag("", tag);
    }
}
