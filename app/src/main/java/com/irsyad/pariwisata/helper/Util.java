package com.irsyad.pariwisata.helper;

import android.location.Location;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Util {
    public double hitungJarak(double startPointLat, double startPointLong, double endPointLat, double endPointLong){
        Location startPoint=new Location("locationA");
        startPoint.setLatitude(startPointLat);
        startPoint.setLongitude(startPointLong);

        Location endPoint=new Location("locationB");
        endPoint.setLatitude(endPointLat);
        endPoint.setLongitude(endPointLong);

        double distance=startPoint.distanceTo(endPoint);
        return distance;
    }

    public String formatJarak(double startPointLat, double startPointLong, double endPointLat, double endPointLong){
        Location startPoint=new Location("locationA");
        startPoint.setLatitude(startPointLat);
        startPoint.setLongitude(startPointLong);

        Location endPoint=new Location("locationB");
        endPoint.setLatitude(endPointLat);
        endPoint.setLongitude(endPointLong);

        double distance=startPoint.distanceTo(endPoint);
        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.CEILING);

        //jika jarak > 1000, jadikan km dengan dibagi 1000
        if (distance >1000){
            distance = distance/1000;
            return  df.format(distance) + "km";
        }else{
            return  df.format(distance) + "m";
        }

    }

}
