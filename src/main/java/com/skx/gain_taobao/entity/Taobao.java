package com.skx.gain_taobao.entity;

import java.util.List;

public class Taobao {

    private String title;
    private List<String> masterImages;
    private String desUrl;
    private List<String> desImages;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getMasterImages() {
        return masterImages;
    }

    public void setMasterImages(List<String> masterImages) {
        this.masterImages = masterImages;
    }

    public List<String> getDesImages() {
        return desImages;
    }

    public void setDesImages(List<String> desImages) {
        this.desImages = desImages;
    }

    public String getDesUrl() {
        return desUrl;
    }

    public void setDesUrl(String desUrl) {
        this.desUrl = desUrl;
    }
}
