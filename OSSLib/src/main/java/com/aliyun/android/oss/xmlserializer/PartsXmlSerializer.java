/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.xmlserializer;

import java.io.StringWriter;
import java.util.List;

import com.aliyun.android.oss.model.Part;
import com.aliyun.android.util.Helper;

/**
 * PartList的XML serializer
 * 
 * @author Michael
 */
public class PartsXmlSerializer extends AbstractXmlSerializer {
    /**
     * 构造新实例
     */
    public PartsXmlSerializer(String root) {
        super(root);
    }

    /**
     * 将part List存储为xml
     */
    public String serialize(List<Part> partsList) {
        StringWriter writer = new StringWriter();
        try {
            serializer.setOutput(writer);
//            serializer.startDocument("UTF-8", true);
            serializer.startTag("", root);

            for (Part part: partsList) {
                serializer.startTag("", "Part");
                if (part.getPartNumber() != null) {
                    addTextTag("PartNumber", part.getPartNumber().toString());
                }
                if (!Helper.isEmptyString(part.getPartName())) {
                    addTextTag("PartName", part.getPartName());
                }
                if (!Helper.isEmptyString(part.getEtag())) {
                    addTextTag("ETag", part.getEtag());
                }
                serializer.endTag("", "Part");
            }

            serializer.endTag("", root);
            serializer.endDocument();
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
