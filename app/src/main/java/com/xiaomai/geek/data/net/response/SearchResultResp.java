package com.xiaomai.geek.data.net.response;

import com.xiaomai.geek.data.module.Repo;

import java.util.ArrayList;

/**
 * Created by XiaoMai on 2017/3/14 14:42.
 */

public class SearchResultResp {

    private int total_count;

    private boolean incomplete_results;

    private ArrayList<Repo> items;

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public boolean isIncomplete_results() {
        return incomplete_results;
    }

    public void setIncomplete_results(boolean incomplete_results) {
        this.incomplete_results = incomplete_results;
    }

    public ArrayList<Repo> getItems() {
        return items;
    }

    public void setItems(ArrayList<Repo> items) {
        this.items = items;
    }

}
