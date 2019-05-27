package com.hdstat.douyu.service.impl;

import com.hdstat.douyu.service.HiveService;
import org.springframework.stereotype.Service;

@Service("hiveService")
public class HiveServiceImpl implements HiveService {
    public String getHiveData() {
        return "321";
    }
}
