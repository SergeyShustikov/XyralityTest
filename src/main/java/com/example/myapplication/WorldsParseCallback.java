package com.example.myapplication;

import java.util.ArrayList;

/**
 * @autor Sergey Shustikov
 * (pandarium.shustikov@gmail.com)
 */
public interface WorldsParseCallback {

    void onComplete(ArrayList<World> worlds);

    void onError(Exception e);
}
