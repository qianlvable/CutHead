package com.cuthead.models;

/**
 * Created by asus on 2014/7/14.
 */
public class HairStyle {
       /*static public  Map <String,String>map;
        static public String num [] = {"11","12","13","14","21","22","23","31","32","33","34","41","42"};
        static public String hair[] = {"板寸","圆寸","修刘海","剃光","离子烫","热塑烫","陶瓷烫","全头染","片染","挑染","焗色","干洗","干洗"};
        public HairStyle(){
            map = new HashMap();
            for(int i=0;i<13;i++){
                map.put(num[1],hair[1]);
            }
        }*/
    static public String getHair(String number){
        /*
        Map map= new HashMap<String,String>();
        String num [] = {"11","12","13","14","21","22","23","31","32","33","34","41","42"};
        String hair[] = {"板寸","圆寸","修刘海","剃光","离子烫","热塑烫","陶瓷烫","全头染","片染","挑染","焗色","水洗","干洗"};
        for(int i=0;i<13;i++){
            map.put(num[1],hair[1]);
        }*/
        String hairstyle;
        int i = Integer.parseInt(number);
        switch (i){
            case 11 :hairstyle = "板寸";break;
            case 12 :hairstyle = "圆寸";break;
            case 13 :hairstyle = "修刘海";break;
            case 14 :hairstyle = "剃光";break;
            case 21 :hairstyle = "离子烫";break;
            case 22 :hairstyle = "热塑烫";break;
            case 23 :hairstyle = "陶瓷烫";break;
            case 31 :hairstyle = "全头染";break;
            case 32 :hairstyle = "片染";break;
            case 33 :hairstyle = "挑染";break;
            case 34 :hairstyle = "焗色";break;
            case 41 :hairstyle = "水洗";break;
            case 42 :hairstyle = "干洗";break;
            default: hairstyle = "出错啦！";
        }
        return hairstyle;
    }
}
