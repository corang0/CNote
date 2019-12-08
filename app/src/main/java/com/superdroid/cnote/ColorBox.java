package com.superdroid.cnote;

import android.content.Context;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class ColorBox {
    private ArrayList<Integer> colors;
    private ArrayList<Integer> lightColors;

    public ColorBox(Context context) {
        colors = new ArrayList<>();
        colors.add(ContextCompat.getColor(context, R.color.color0));
        colors.add(ContextCompat.getColor(context, R.color.color1));
        colors.add(ContextCompat.getColor(context, R.color.color2));
        colors.add(ContextCompat.getColor(context, R.color.color3));
        colors.add(ContextCompat.getColor(context, R.color.color4));
        colors.add(ContextCompat.getColor(context, R.color.color5));
        colors.add(ContextCompat.getColor(context, R.color.color6));
        colors.add(ContextCompat.getColor(context, R.color.color7));
        colors.add(ContextCompat.getColor(context, R.color.color8));

        lightColors = new ArrayList<>();
        lightColors.add(ContextCompat.getColor(context, R.color.colorLight0));
        lightColors.add(ContextCompat.getColor(context, R.color.colorLight1));
        lightColors.add(ContextCompat.getColor(context, R.color.colorLight2));
        lightColors.add(ContextCompat.getColor(context, R.color.colorLight3));
        lightColors.add(ContextCompat.getColor(context, R.color.colorLight4));
        lightColors.add(ContextCompat.getColor(context, R.color.colorLight5));
        lightColors.add(ContextCompat.getColor(context, R.color.colorLight6));
        lightColors.add(ContextCompat.getColor(context, R.color.colorLight7));
        lightColors.add(ContextCompat.getColor(context, R.color.colorLight8));
    }

    public int getColor(int i) {
        return colors.get(i);
    }

    public int getLightColor(int i) {
        return lightColors.get(i);
    }
}
