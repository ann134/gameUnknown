package pac2Graphics;

import java.util.List;

public class AllWorldGameObjects {

    private List<GameObject> allWorldGameObjects;

    public AllWorldGameObjects(List<GameObject> list){
        allWorldGameObjects = list;
    }

    public List<GameObject> getList(){
        return allWorldGameObjects;
    }
}
