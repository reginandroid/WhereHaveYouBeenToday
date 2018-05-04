package app.com.regiko.wherehaveyoubeentoday;

/**
 * Created by Ковтун on 14.02.2018.
 */

public class PointItem {
    private String dateTime;
    private float lat;
    private float logt;

    public PointItem() {
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public void setLogt(float logt) {
        this.logt = logt;
    }


    public String getDateTime() {

        return dateTime;
    }

    public float getLat() {
        return  lat;
    }

    public float getLogt() {
        return logt;
    }
}
