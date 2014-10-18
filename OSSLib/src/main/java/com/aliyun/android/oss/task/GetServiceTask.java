/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.task;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import com.aliyun.android.oss.OSSException;
import com.aliyun.android.oss.http.HttpMethod;
import com.aliyun.android.oss.http.OSSHttpTool;
import com.aliyun.android.oss.model.Bucket;
import com.aliyun.android.oss.xmlparser.BucketListXmlParser;
import com.aliyun.android.util.Helper;

/**
 * @author ruici
 */
public class GetServiceTask extends Task {

    public GetServiceTask() {
        super(HttpMethod.GET);
    }

    /* (non-Javadoc) * @see com.aliyun.android.oss.task.Task#checkArguments() */
    @Override
    protected void checkArguments() {
        // no argument to check
    }

    /**
     * 返回用户所有的Bucket*
     * @return List<Bucket>
     * @throws Exception
     */
    public List<Bucket> getResult() throws OSSException {
        try {
            HttpResponse r = this.execute();
            BucketListXmlParser parser = new BucketListXmlParser();
            return parser.parse(r.getEntity().getContent());
        } catch (OSSException osse) {
            throw osse;
        } catch (Exception e) {
            throw new OSSException(e);
        } finally {
            this.releaseHttpClient();
        }
    }

    /*
     * (non-Javadoc) * @see
     * com.aliyun.android.oss.task.Task#generateHttpRequest()
     */
    @Override
    protected HttpUriRequest generateHttpRequest() {
        HttpGet httpGet = new HttpGet(this.getOSSEndPoint() + "/");

        String dateStr = Helper.getGMTDate();
        String authorization = OSSHttpTool.generateAuthorization(accessId,
                accessKey, httpMethod.toString(), "", "", dateStr, "", "/");

        httpGet.setHeader(AUTHORIZATION, authorization);
        httpGet.setHeader(DATE, dateStr);
        //httpGet.setHeader(HOST, this.getOSSHost());

        return httpGet;
    }

}
