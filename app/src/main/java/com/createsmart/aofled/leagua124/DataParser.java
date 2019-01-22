package com.createsmart.aofled.leagua124;



public class DataParser {

    public static String newsDate (String datetime) {
        int i= Integer.parseInt(datetime.substring(5,7));
        datetime=datetime.substring(8,10)+" "+ getMans(Integer.parseInt(datetime.substring(5,7)));
        return datetime;

    }


    public static String getMans (int i){
        String mans;
        switch(i) {
            case 1:
                mans="января";
                break;
            case 2:
                mans="Февраля";
                break;
            case 3:
                mans="марта";
                break;
            case 4:
                mans="апреля";
                break;
            case 5:
                mans="мая";
                break;
            case 6:
                mans="июня";
                break;
            case 7:
                mans="июля";
                break;
            case 8:
                mans="августа";
                break;
            case 9:
                mans="сентября";
                break;
            case 10:
                mans="октября";
                break;
            case 11:
                mans="ноября";
                break;
            case 12:
                mans="декабря";
                break;

            default:
                mans="";
                break;
        }
        return mans;
    }





}
