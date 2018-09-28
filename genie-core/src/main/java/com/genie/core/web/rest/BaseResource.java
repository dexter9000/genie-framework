package com.genie.core.web.rest;

import com.genie.core.web.rest.util.HeaderUtil;
import com.genie.core.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 基础Resource
 * 提供基础的增删改查Response
 */
public abstract class BaseResource {

    private final static Logger log = LoggerFactory.getLogger(BaseResource.class);

    /**
     * 服务名，直接从配置文件中得到
     */
    @Value("${spring.application.name}")
    private String APPLICATION_NAME;

    /**
     * 过滤器，支持返回实体类型的转换
     */
    @Autowired(required = false)
    @Qualifier("entityFilters")
    private List<EntityFilter> entityFilters;

    /**
     * 实体名称，返回的Response header中显示对应内容
     */
    private String entityName;

    public BaseResource(String entityName) {
        this.entityName = entityName;

    }

    protected BaseResource resource(String entityName) {
        this.entityName = entityName;
        return this;
    }


    /**
     * 创建成功响应
     *
     * @return 响应结果，返回状态码：201
     */
    protected ResponseEntity createSuccess() {
        HttpHeaders headers = HeaderUtil.createEntityCreationAlert(APPLICATION_NAME, entityName, "");
        headers = HeaderUtil.interfaceVersion(headers);
        log.debug("REST response for create success!");

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    /**
     * 创建成功响应
     *
     * @param entityObj 实体响应结果
     * @return 响应结果实体对象，返回状态码：201
     */
    protected ResponseEntity createSuccess(Object entityObj) {
        HttpHeaders headers = HeaderUtil.createEntityCreationAlert(APPLICATION_NAME, entityName, "");
        headers = HeaderUtil.interfaceVersion(headers);
        log.debug("REST response for create success!");

        return new ResponseEntity(entityObj, headers, HttpStatus.CREATED);
    }

    /**
     * 删除成功响应
     *
     * @return 响应结果，返回状态码：200
     */
    protected ResponseEntity deleteSuccess() {
        HttpHeaders headers = HeaderUtil.createEntityDeletionAlert(APPLICATION_NAME, entityName, "");
        headers = HeaderUtil.interfaceVersion(headers);
        log.debug("REST response for delete success!");
        return ResponseEntity.ok().headers(headers).build();
    }

    /**
     * 更新成功响应
     *
     * @param entityObj 实体响应结果
     * @return 响应结果，返回状态码：200
     */
    protected ResponseEntity updateSuccess(Object entityObj) {
        HttpHeaders headers = HeaderUtil.createEntityUpdateAlert(APPLICATION_NAME, entityName, "");
        headers = HeaderUtil.interfaceVersion(headers);
        log.debug("REST response for update success : \n" + entityObj);

        return ResponseEntity.ok().headers(headers).body(entityObj);
    }

    /**
     * 分页查询返回结果
     *
     * @param page 分页查询结果
     * @return 返回分页查询结果，在Header中返回分页信息，消息体返回结果列表
     */
    protected ResponseEntity pagedResponse(Page page) {
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page);
        headers = HeaderUtil.interfaceVersion(headers);
        log.debug("REST response for paged response : \n" + page);
        List list = doEntityFilters(page.getContent());
        return ResponseEntity.ok().headers(headers).body(list);
    }

    /**
     * 分页查询返回结果
     *
     * @param baseUrl 请求url
     * @param page    分页查询结果
     * @return 返回分页查询结果，在Header中返回分页信息，消息体返回结果列表
     */
    protected ResponseEntity pagedResponse(String baseUrl, Page page) {
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, baseUrl);
        headers = HeaderUtil.interfaceVersion(headers);
        log.debug("REST response for paged response : \n" + page);
        List list = doEntityFilters(page.getContent());
        return ResponseEntity.ok().headers(headers).body(list);
    }

    /**
     * 查询列表返回结果
     *
     * @param list 查询结果
     * @return 返回列表查询结果
     */
    protected ResponseEntity listResponse(List list) {
        HttpHeaders headers = new HttpHeaders();
        headers = HeaderUtil.interfaceVersion(headers);
        list = doEntityFilters(list);

        log.debug("REST response for list response : \n" + list);
        return ResponseEntity.ok().headers(headers).body(list);
    }

    /**
     * 查询实体返回结果
     *
     * @param entity 查询结果
     * @return 返回实体查询结果，当查不到内容时返回状态码：404
     */
    protected ResponseEntity entityResponse(Object entity) {
        HttpHeaders headers = new HttpHeaders();
        headers = HeaderUtil.interfaceVersion(headers);

        if (entity == null) {
            log.warn("REST response for entity response not found!");
            return ResponseEntity.notFound().headers(headers).build();
        } else {
            log.debug("REST response for entity response : \n" + entity);
            entity = doEntityFilter(entity);
            return ResponseEntity.ok().headers(headers).body(entity);

        }
    }

    /**
     * 查询实体返回结果
     *
     * @param maybeResponse 查询结果
     * @return 返回实体查询结果，当查不到内容时返回状态码：404
     */
    protected ResponseEntity entityResponse(Optional maybeResponse) {
        HttpHeaders headers = new HttpHeaders();
        headers = HeaderUtil.interfaceVersion(headers);
        if (maybeResponse.isPresent()) {
            Object entity = doEntityFilter(maybeResponse.get());
            log.debug("REST response for entity response : \n" + entity);
            return ResponseEntity.ok().headers(headers).body(entity);
        } else {
            log.warn("REST response for entity response not found!");
            return ResponseEntity.notFound().headers(headers).build();
        }
    }

    /**
     * 成功响应，不包含返回对象
     *
     * @return 返回状态码：200
     */
    protected ResponseEntity successResponse() {
        HttpHeaders headers = new HttpHeaders();
        headers = HeaderUtil.interfaceVersion(headers);
        log.debug("REST response for success response!");
        return ResponseEntity.ok().headers(headers).build();
    }

    protected ResponseEntity badRequest(){
        HttpHeaders headers = new HttpHeaders();
        headers = HeaderUtil.interfaceVersion(headers);
        log.debug("REST response for badRequest response!");
        return ResponseEntity.badRequest().headers(headers).build();
    }

    /**
     * 实体对象过滤器
     *
     * @param obj 要转换的实体对象
     * @param <T> 实体类型
     * @return 转换结果
     */
    private <T> T doEntityFilter(T obj) {

        if (entityFilters == null) {
            return obj;
        }
        for (EntityFilter filter : entityFilters) {
            obj = filter.doEntityFilter(obj);
        }

        return obj;
    }

    /**
     * 实体对象过滤器
     *
     * @param objs 要转换的实体对象列表
     * @param <T>  实体类型
     * @return 转换结果
     */
    private <T> List<T> doEntityFilters(List<T> objs) {
        List<T> result = new ArrayList<>();
        for (T obj : objs) {
            result.add(doEntityFilter(obj));
        }
        return result;
    }
}
