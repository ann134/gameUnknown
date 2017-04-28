package ru.spbu.arts.game.unknown;

import org.dyn4j.dynamics.Body;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllWorldGameObjects {

    private Map<Body, GameObject> bodyToGameObject = new HashMap<>();
    private ArrayList<GameObject> allWorldGameObjects = new ArrayList<>();

    /*public AllWorldGameObjects(List<GameObject> list){
        *//*allWorldGameObjects = list;*//*
    }*/

    public void addObject(GameObject gameObject) {
        //записать в map и в list

        allWorldGameObjects.add(gameObject);
        bodyToGameObject.put(gameObject.body, gameObject);

    }

    //body -> GameObject
    public GameObject getObjectByBody(Body body) {
        return  bodyToGameObject.get(body);
    }


    public ArrayList<GameObject> getList(){
        return allWorldGameObjects;
    }
}
