package org.sia.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.DataRedundancyType;
import com.aliyun.oss.model.StorageClass;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Setter
@Configuration
@ConfigurationProperties(prefix = "oss")
public class OssConfig {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucket;

    @Bean(destroyMethod = "shutdown")
    public OSS ossClient(){
        OSS oss = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 初始化bucket
        if (!oss.doesBucketExist(bucket)){
            CreateBucketRequest request = new CreateBucketRequest(bucket);
            request.setStorageClass(StorageClass.Standard);
            request.setDataRedundancyType(DataRedundancyType.LRS);
            request.setCannedACL(CannedAccessControlList.Private);
            oss.createBucket(request);
        }
        return oss;
    }


}
