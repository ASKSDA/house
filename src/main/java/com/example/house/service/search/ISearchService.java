package com.example.house.service.search;

import com.example.house.base.ServiceMultiResult;
import com.example.house.form.RentSearch;

public interface ISearchService {
    void index(Long houseId);//索引目标房源，将mysql中的房源数据索引到es里面
    void remove(Long houseId);//移除房源索引
    ServiceMultiResult<Long> query(RentSearch rentSearch);

}
