package com.example.house.service.search.impl;

import com.example.house.base.ServiceMultiResult;
import com.example.house.domain.House;
import com.example.house.domain.HouseDetail;
import com.example.house.domain.HouseTag;
import com.example.house.form.RentSearch;
import com.example.house.mapper.HouseDetailMapper;
import com.example.house.mapper.HouseMapper;
import com.example.house.mapper.HouseTagMapper;
import com.example.house.service.search.HouseIndexKey;
import com.example.house.service.search.HouseIndexMessage;
import com.example.house.service.search.HouseIndexTemplate;
import com.example.house.service.search.ISearchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.modelmapper.ModelMapper;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImpl implements ISearchService {
    private static final Logger logger = LoggerFactory.getLogger(ISearchService.class);

    private static final String INDEX_NAME = "house";  //es中索引的名字
    private static final String INDEX_TOPIC = "house"; //kafka中topic的名字
    private static final String INDEX_TYPE = "house";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HouseDetailMapper houseDetailMapper;

    @Autowired
    private HouseMapper houseMapper;

    @Autowired
    private HouseTagMapper houseTagMapper;

    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @KafkaListener(topics = INDEX_TOPIC)
    private void handleMessage(String content){
        try{
            HouseIndexMessage houseIndexMessage =
                    objectMapper.readValue(content,HouseIndexMessage.class);
            switch (houseIndexMessage.getOperation()){
                case HouseIndexMessage.INDEX:
                    this.createOrUpdateIndex(houseIndexMessage);
                    break;
                case HouseIndexMessage.REMOVE:
                    this.removeIndex(houseIndexMessage);
                    break;
                default:

                    break;
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void removeIndex(HouseIndexMessage houseIndexMessage) {
        Long houseId = houseIndexMessage.getHouseId();
        DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest(INDEX_NAME);
        deleteByQueryRequest.setQuery(
                QueryBuilders.termQuery(HouseIndexKey.HOUSE_ID, houseId)); //where houseId=id
        BulkByScrollResponse bulkByScrollResponse = null;
        try {
            bulkByScrollResponse =
                    restHighLevelClient.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long delete = bulkByScrollResponse.getDeleted();
        logger.debug("Delete total " + delete);

        if (delete <= 0) { //quartz
            logger.warn("Did not remove data from es for response: " + bulkByScrollResponse);
            // 重新加入消息队列
            this.remove(houseId, houseIndexMessage.getRetry() + 1);
        }
    }

    //主要为了处理kafka
    private void createOrUpdateIndex(HouseIndexMessage houseIndexMessage) {
        HouseIndexTemplate houseIndexTemplate = new HouseIndexTemplate();
        long houseId = houseIndexMessage.getHouseId();

        House house = houseMapper.findOne(houseId);
        if (house == null) {
            logger.error("Index house {} dose not exist!", houseId);
            this.index(houseId, houseIndexMessage.getRetry() + 1);
        }
        modelMapper.map(house, houseIndexTemplate);

        HouseDetail houseDetail = houseDetailMapper.findByHouseId(houseId);
        if (houseDetail == null) {
            //exception
        }
        modelMapper.map(houseDetail, houseIndexTemplate);

        List<HouseTag> houseTags = houseTagMapper.findAllByHouseId(houseId);
        if (houseTags != null && !houseTags.isEmpty()) {
            List<String> houseTagsString = new ArrayList<>();
            houseTags.forEach(houseTag -> houseTagsString.add(houseTag.getName()));
            houseIndexTemplate.setTags(houseTagsString);
        }
//        SearchRequestBuilder searchRequestBuilder = transportClient.prepareSearch(INDEX_NAME).setSearchType(INDEX_TYPE)
//                .setQuery(QueryBuilders.termQuery(HouseIndexKey.HOUSE_ID, houseId)); //ES6

        SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
        //SearchType searchType = SearchType.fromString(INDEX_TYPE);
        QueryBuilder queryBuilder = QueryBuilders.termQuery(HouseIndexKey.HOUSE_ID, houseId);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(queryBuilder);
        searchRequest.source(sourceBuilder);
        //SearchRequest.searchType(searchType);

        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long totalHit = searchResponse.getHits().getTotalHits().value;
        boolean success;
        if (totalHit == 0) {
            success = create(houseIndexTemplate);
        } else if (totalHit == 1) {
            String esId = searchResponse.getHits().getAt(0).getId();
            success = update(esId, houseIndexTemplate);
        } else {
            success = deleteAndCreate(totalHit, houseIndexTemplate);
        }
        if (success) {
            logger.debug("");
        }
    }

    private boolean create(HouseIndexTemplate houseIndexTemplate) {
        try {
            IndexRequest indexRequest = new IndexRequest(INDEX_NAME)
                    .source(objectMapper.writeValueAsBytes(houseIndexTemplate), XContentType.JSON);
            IndexResponse response =
                    restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            if (response.status() == RestStatus.CREATED)
                return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean update(String esId, HouseIndexTemplate houseIndexTemplate) {
        try {
            UpdateRequest updateRequest = new UpdateRequest(INDEX_NAME, esId) //esId等待更新文档的ID
                    .doc(objectMapper.writeValueAsBytes(houseIndexTemplate), XContentType.JSON);
            UpdateResponse updateResponse
                    = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);

            logger.debug("Create index with house: " + houseIndexTemplate.getHouseId());
            if (updateResponse.status() == RestStatus.OK)
                return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean deleteAndCreate(long totalHit, HouseIndexTemplate houseIndexTemplate) {
        DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest(INDEX_NAME);
        deleteByQueryRequest.setQuery(
                QueryBuilders.termQuery(HouseIndexKey.HOUSE_ID, houseIndexTemplate.getHouseId()));
        BulkByScrollResponse bulkByScrollResponse = null;
        try {
            bulkByScrollResponse =
                    restHighLevelClient.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long delete = bulkByScrollResponse.getDeleted();
        if (delete != totalHit)
            return false;
        else return create(houseIndexTemplate);
    }



    private void index(long houseId, int i) {
    }

    private void remove(Long houseId, int i) {
    }

    @Override
    public void index(Long houseId) {

    }

    @Override
    public void remove(Long houseId) {

    }

    @Override
    public ServiceMultiResult<Long> query(RentSearch rentSearch) {
        return null;
    }
}
