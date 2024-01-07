package com.example.Gabean;

public class BusRouteInfo {
    private String stId;
    private String busRouteId;
    private String ord;

    public BusRouteInfo(String stId, String busRouteId, String ord) {
        this.stId = stId;
        this.busRouteId = busRouteId;
        this.ord = ord;
    }

    public String getStId() {
        return stId;
    }

    public void setStId(String stId) {
        this.stId = stId;
    }

    public String getBusRouteId() {
        return busRouteId;
    }

    public void setBusRouteId(String busRouteId) {
        this.busRouteId = busRouteId;
    }

    public String getOrd() {
        return ord;
    }

    public void setOrd(String ord) {
        this.ord = ord;
    }
}
