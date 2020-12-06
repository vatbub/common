package com.github.vatbub.common.view.reporting;

/*-
 * #%L
 * FOKProjects Common
 * %%
 * Copyright (C) 2016 - 2017 Frederik Kammel
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

/**
 * Various utilities to interact with AWS S3
 */
public class AWSS3Utils {
    private AWSS3Utils() {
        throw new IllegalStateException("Class may not be instantiated");
    }

    public static boolean keyExists(S3Client s3Client, String bucketName, String key) {
        if (!doesBucketExist(s3Client, bucketName)) {
            return false;
        }

        // bucket exists, try to retrieve the object
        try {
            s3Client.getObject(GetObjectRequest.builder().bucket(bucketName).key(key).build());
            // worked
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean doesBucketExist(S3Client s3Client, String bucketName) {
        try {
            /*
             * If a bucket exists, but isn't owned by you, trying to list its
             * objects returns a 403 AccessDenied error response from Amazon S3.
             *
             * Notice that we supply the bucket name in the request and specify
             * that we want 0 keys returned since we don't actually care about the data.
             */
            s3Client.listObjects(ListObjectsRequest.builder().bucket(bucketName).maxKeys(0).build());
        } catch (NoSuchBucketException ase) {
            return false;
        } catch (S3Exception e) {
            // Ain't no  permission?
            return e.statusCode() == 403;
        }

        // bucket exists and we have the permission to write in it
        return true;
    }
}
