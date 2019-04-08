package com.akashapplications.technicalanna.Utils;

import android.graphics.Color;
import android.util.Log;

import com.akashapplications.technicalanna.Login;
import com.akashapplications.technicalanna.Models.SubjectExams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Dummy {
    public static String[] bannerImages = {
            "https://i.ytimg.com/vi/68BqRHFaYU0/maxresdefault.jpg",
            "https://i.ytimg.com/vi/h_USkgyJKqM/maxresdefault.jpg",
            "https://images.topperlearning.com/mimg/topper/news/a2c23853a01ef682ac33479d9459921d.jpg?v=0.0.3"
    };

    public static int generateRandomNumberBetween(int min, int max)
    {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }


    public static int generateRandomColor() {
        // This is the base color which will be mixed with the generated one
        int randomIndex = generateRandomNumberBetween(0,9);
        int colorArr[] = {
                Color.parseColor("#F44336"),
                Color.parseColor("#E91E63"),
                Color.parseColor("#9C27B0"),
                Color.parseColor("#673AB7"),
                Color.parseColor("#3F51B5"),
                Color.parseColor("#2196F3"),
                Color.parseColor("#009688"),
                Color.parseColor("#006064"),
                Color.parseColor("#795548"),
                Color.parseColor("#546E7A")
        };

        return colorArr[randomIndex];
    }

}
