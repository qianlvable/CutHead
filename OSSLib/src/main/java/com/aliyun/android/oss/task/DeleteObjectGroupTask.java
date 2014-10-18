/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.task;

/**
 * Delete Object Group操作和Delete Object是一样的，具体细节参见{@link DeleteObjectTask}
 * @author Harttle
 */
public class DeleteObjectGroupTask extends DeleteObjectTask {

    /**
     * @param bucketName
     * @param objectKey
     */
    public DeleteObjectGroupTask(String bucketName, String objectKey) {

        super(bucketName, objectKey);

    }

}
