package com.netease.homework.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {
    private static List<String> imageSuffixes = new ArrayList<>() {{
        add(".jpg");
        add(".png");
        add(".gif");
        add(".jpeg");
        add(".bmp");
    }};

    public static List<String> getImageSuffixes(){
        return imageSuffixes;
    }
    public static void addImageSuffixes(String suffix){
        imageSuffixes.add(suffix.toLowerCase());
    }
    public static void setImageSuffixes(List<String> imageSuffixes) {
        Utils.imageSuffixes = imageSuffixes;
    }

}
