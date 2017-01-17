package common;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Various utilities to interact with AWS S3
 */
public class AWSS3Utils {
    public static boolean keyExists(AmazonS3 s3Client, String bucketName, String key) {
        if (!s3Client.doesBucketExist(bucketName)) {
            return false;
        }

        // bucket exists, try to retreive the object
        try {
            s3Client.getObjectMetadata(bucketName, key);
            // worked
            return true;
        } catch (AmazonServiceException e) {
            return false;
        }
    }

    public static void createFolder(AmazonS3 client, String bucketName, String folderName) {
        // create meta-data for your folder and set content-length to 0
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(0);
        // create empty content
        InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
        // create a PutObjectRequest passing the folder name suffixed by /
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
                folderName + "/", emptyContent, metadata);
        // send request to S3 to create folder
        client.putObject(putObjectRequest);
    }
}
