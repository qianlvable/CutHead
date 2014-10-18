/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.xmlserializer; 

import java.io.StringWriter;
import java.util.List;

/** 
 * 
 * @author Ruici 
 * 
 */
public class DeleteMultipleObjectXmlSerializer extends AbstractXmlSerializer {

    /** 
     * @param root 
     */
    public DeleteMultipleObjectXmlSerializer(String root) {
        super(root);
    }
    
    public String serialize(List<String> keyList, boolean quiet) {
        StringWriter writer = new StringWriter();
        try {
            serializer.setOutput(writer);
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", root);
            addTextTag("Quiet", String.valueOf(quiet));
            
            for (String key : keyList) {
                serializer.startTag("", "Object");
                addTextTag("Key", key);
                serializer.endTag("", "Object");
            }
            
            serializer.endTag("", root);
            serializer.endDocument();
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

