package com.tianji.learning.service;

import com.tianji.learning.domain.vo.SignResultVO;

public interface ISignRecordService {
    SignResultVO addSignRecords();

    Byte[] querySignRecords();

}
