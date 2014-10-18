/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.task;

/**
 * Head Object Group操作和Head Object是一样的，具体细节参见{@link HeadObjectTask}
 * @author Harttle
 */
public class HeadObjectGroupTask extends HeadObjectTask {

    /**
     * @param bucketName
     * @param objectKey
     */
    public HeadObjectGroupTask(String bucketName, String objectKey) {

        super(bucketName, objectKey);
    }

}
