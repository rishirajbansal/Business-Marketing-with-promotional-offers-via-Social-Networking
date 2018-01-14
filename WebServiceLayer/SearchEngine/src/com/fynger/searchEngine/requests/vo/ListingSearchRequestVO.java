
package com.fynger.searchEngine.requests.vo;


public class ListingSearchRequestVO extends BaseRequestVO {

    private String keyword;

    private String location;

    private double latitude;

    private double longitude;

    
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
    	this.keyword = "\""+keyword+"\"";
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString(){
        String object = "";
        //Implement this method to print all the attributes on log for debugging purposes

        return object;
    }

 
}