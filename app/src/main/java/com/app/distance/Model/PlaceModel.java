package com.app.distance.Model;

import java.util.List;

public class PlaceModel {


    private String id;
    private String busString;
    private String bus_no;

    private List<Bus>bus;

    public List<Bus> getBus() {
        return bus;
    }

    public void setBus(List<Bus> bus) {
        this.bus = bus;
    }

    public String getBus_no() {
        return bus_no;
    }

    public void setBus_no(String bus_no) {
        this.bus_no = bus_no;
    }

    public List<Waypoints> getWay_points() {
        return way_points;
    }

    public void setWay_points(List<Waypoints> way_points) {
        this.way_points = way_points;
    }

    private List<Waypoints> way_points;

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    private Source source;
    private Destination destination;

    public PlaceModel(String route_no, List<Waypoints> way_points) {

        this.busString = route_no;
        this.way_points = way_points;
    }

    public PlaceModel() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusString() {
        return busString;
    }

    public void setBusString(String busString) {
        this.busString = busString;
    }

    public PlaceModel(String id, String route_no) {
        this.id = id;
        this.busString = route_no;
    }

    public class Source{
        public Source(){}
        private String source;
        private String sLattitude;
        private String sLongitude;

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getsLattitude() {
            return sLattitude;
        }

        public void setsLattitude(String sLattitude) {
            this.sLattitude = sLattitude;
        }

        public String getsLongitude() {
            return sLongitude;
        }

        public void setsLongitude(String sLongitude) {
            this.sLongitude = sLongitude;
        }

        public Source(String source, String sLattitude, String sLongitude) {
            this.source = source;
            this.sLattitude = sLattitude;
            this.sLongitude = sLongitude;
        }
    }

    public class Destination{
    public Destination(){}
    private String destination;
    private String dLattitude;
    private String dLongitude;

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getdLattitude() {
        return dLattitude;
    }

    public void setdLattitude(String dLattitude) {
        this.dLattitude = dLattitude;
    }

    public String getdLongitude() {
        return dLongitude;
    }

    public void setdLongitude(String dLongitude) {
        this.dLongitude = dLongitude;
    }

    public Destination(String destination, String dLattitude, String dLongitude) {
        this.destination = destination;
        this.dLattitude = dLattitude;
        this.dLongitude = dLongitude;
    }
}

    public  class Waypoints{
        private String waypoint;
        private String wLattitude;
        private String wLongitude;

        public String getWaypoint() {
            return waypoint;
        }

        public void setWaypoint(String waypoint) {
            this.waypoint = waypoint;
        }

        public String getwLattitude() {
            return wLattitude;
        }

        public void setwLattitude(String wLattitude) {
            this.wLattitude = wLattitude;
        }

        public String getwLongitude() {
            return wLongitude;
        }

        public void setwLongitude(String wLongitude) {
            this.wLongitude = wLongitude;
        }

        public Waypoints() {

        }
    }
    public class Bus{
        public Bus(){}
        private int id;
        private String uuid;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }
    }
}
