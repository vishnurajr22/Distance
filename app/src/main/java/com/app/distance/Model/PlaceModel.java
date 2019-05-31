package com.app.distance.Model;

public class PlaceModel {


    private String source;
    private String sLattitude;
    private String sLongitude;
    private String destination;
    private String dLattitude;
    private String dLongitude;
    private String waypoint;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    private String uuid;


    public PlaceModel(){}

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setsLattitude(String sLattitude) {
        this.sLattitude = sLattitude;
    }

    public void setsLongitude(String sLongitude) {
        this.sLongitude = sLongitude;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setdLattitude(String dLattitude) {
        this.dLattitude = dLattitude;
    }

    public void setdLongitude(String dLongitude) {
        this.dLongitude = dLongitude;
    }

    public void setWaypoint(String waypoint) {
        this.waypoint = waypoint;
    }

    public void setwLattitude(String wLattitude) {
        this.wLattitude = wLattitude;
    }

    public void setwLongitude(String wLongitude) {
        this.wLongitude = wLongitude;
    }

    public String getSource() {
        return source;
    }

    public String getsLattitude() {
        return sLattitude;
    }

    public String getsLongitude() {
        return sLongitude;
    }

    public String getDestination() {
        return destination;
    }

    public String getdLattitude() {
        return dLattitude;
    }

    public String getdLongitude() {
        return dLongitude;
    }

    public String getWaypoint() {
        return waypoint;
    }

    public String getwLattitude() {
        return wLattitude;
    }

    public String getwLongitude() {
        return wLongitude;
    }

    public PlaceModel(String id,String source, String sLattitude, String sLongitude, String destination, String dLattitude, String dLongitude, String waypoint, String wLattitude, String wLongitude,String uuid) {
        this.id=id;
        this.source = source;
        this.sLattitude = sLattitude;
        this.sLongitude = sLongitude;
        this.destination = destination;
        this.dLattitude = dLattitude;
        this.dLongitude = dLongitude;
        this.waypoint = waypoint;
        this.wLattitude = wLattitude;
        this.wLongitude = wLongitude;
        this.uuid=uuid;
    }

    private String wLattitude;
    private String wLongitude;

}
