package org.sia.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.sia.handler.RequestDataHandler;
import org.sia.mapper.AppCategoryMapper;
import org.sia.mapper.PackageRecordMapper;
import org.sia.model.AppCategory;
import org.sia.model.PackageRecord;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/10 16:34
 */
@Slf4j
@Service
public class PackageRecordService extends ServiceImpl<PackageRecordMapper, PackageRecord> {
    public void record(String category,String operation,Integer cost){
        // 积分扣除记录
        PackageRecord record = new PackageRecord();
        record.setUserId(RequestDataHandler.getUserId());
        record.setCategory(category);
        record.setOperation(operation);
        record.setCost(cost);
        record.setCreateTime(LocalDateTime.now());
        this.save(record);
    }
}
