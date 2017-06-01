package ru.spbu.arts.game.unknown;

import org.dyn4j.dynamics.Body;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllWorldGameObjects {

    private Map<String, GameObject> nameToGameObject = new HashMap<>();
    private Map<Body, GameObject> bodyToGameObject = new HashMap<>();
    private ArrayList<GameObject> allWorldGameObjects = new ArrayList<>();


    //TODO make all names be constants
    public void addObject(GameObject gameObject, String name) {
        //записать в map и в list

        allWorldGameObjects.add(gameObject);
        bodyToGameObject.put(gameObject.body, gameObject);
        if (name != null)
            nameToGameObject.put(name, gameObject);
    }

    //body -> GameObject
    public GameObject getObjectByBody(Body body) {
        return  bodyToGameObject.get(body);
    }

    public GameObject getByName(String name) {
        return nameToGameObject.get(name);
    }

    public ArrayList<GameObject> getList(){
        return allWorldGameObjects;
    }
}
