package com.example.Gabean;

public class BusInfo {
    private String stNm; // 정류장 이름
    private String rtNm; // 노선 이름
    private String arrmsg1; // 첫 번째 버스 도착 메시지
    private String arrmsg2; // 두 번째 버스 도착 메시지

    // 기본 생성자
    public BusInfo() {
    }

    // 모든 필드를 포함하는 생성자
    public BusInfo(String stNm, String rtNm, String arrmsg1, String arrmsg2) {
        this.stNm = stNm;
        this.rtNm = rtNm;
        this.arrmsg1 = arrmsg1;
        this.arrmsg2 = arrmsg2;
    }

    // 정류장 이름에 대한 getter
    public String getStNm() {
        return stNm;
    }

    // 정류장 이름에 대한 setter
    public void setStNm(String stNm) {
        this.stNm = stNm;
    }

    // 노선 이름에 대한 getter
    public String getRtNm() {
        return rtNm;
    }

    // 노선 이름에 대한 setter
    public void setRtNm(String rtNm) {
        this.rtNm = rtNm;
    }

    // 첫 번째 버스 도착 메시지에 대한 getter
    public String getArrmsg1() {
        return arrmsg1;
    }

    // 첫 번째 버스 도착 메시지에 대한 setter
    public void setArrmsg1(String arrmsg1) {
        this.arrmsg1 = arrmsg1;
    }

    // 두 번째 버스 도착 메시지에 대한 getter
    public String getArrmsg2() {
        return arrmsg2;
    }

    // 두 번째 버스 도착 메시지에 대한 setter
    public void setArrmsg2(String arrmsg2) {
        this.arrmsg2 = arrmsg2;
    }
}
