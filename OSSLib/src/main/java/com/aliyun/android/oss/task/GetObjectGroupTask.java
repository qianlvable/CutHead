/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.task;

/**
 * 对于Object Group的Get操作，和普通Object是一致的，所以本类继承于{@link GetObjectTask}，未做任何变化。
 * 需要注意的是，如果Object Group创建成功后，其中所属的某些object被修改（引起了ETag值的变化），则Get操
 * 作会返回错误码：FilePartStale
 * 
 * @author Harttle
 */
public class GetObjectGroupTask extends GetObjectTask {

    /**
     * @param bucketName object group所属bucket名
     * @param objectKey object group的key值
     */
    public GetObjectGroupTask(String bucketName, String objectKey) {
        super(bucketName, objectKey);
    }

}
