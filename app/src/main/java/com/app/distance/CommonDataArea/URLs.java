package com.app.distance.CommonDataArea;

public class URLs {

    private static final String ROOT_URL = "http://192.168.25.197/myweb/Api.php?apicall=";

    public static final String URL_REGISTER = ROOT_URL + "signup";
    public static final String URL_LOGIN= ROOT_URL + "login";

    private static final String ROOT_URL2 = "http://192.168.25.197/myweb/PlacesApi.php?apicall=";
    public static final String REQ_DATA = ROOT_URL2 + "req_data";
    public static final String BUS_REQ = ROOT_URL2 + "bus_list";
    public static final String UPDATE_BUS_DETAILS = ROOT_URL2 + "update_data";
    public static final String INSERT_Places = ROOT_URL2 + "add_place";
    public static final String SET_BUS = ROOT_URL2 + "add_bus";


}
