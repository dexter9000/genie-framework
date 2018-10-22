package com.genie.core.utils;

import org.springframework.data.domain.Pageable;

public class PageWrapper {

    private Pageable pageable;

    private Integer pageNum;

    private Integer pageSize;

    private boolean isPage=false;

    public PageWrapper(){

    }

    public PageWrapper(Pageable page){
        this.pageable = page;
        setPageWrapperInfo(page);
    }


    private final void setPageWrapperInfo (Pageable pageable){
        if(null != pageable){
            Integer pageNum = pageable.getPageNumber();
            Integer pageSize = pageable.getPageSize();
            if (null != pageNum && null != pageSize) {
                isPage = true;
                this.pageNum=pageNum+1;
                this.pageSize=pageSize;
            }
        }
    }

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isPage() {
        return isPage;
    }

    public void setPage(boolean page) {
        isPage = page;
    }
}
