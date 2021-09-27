package GraphTraversal;

import java.util.HashMap;

public class Town {
    private String townName;
    private HashMap<String, Integer> routes;

    public Town (String townName) {
        this.townName = townName;
        routes = new HashMap<String, Integer>();
    }

    public boolean addRoute(String routeTown, Integer distance) {
        if (routes.containsKey(routeTown))
            return false;

        routes.put(routeTown, distance);

        return true;
    }

    public String getTownName() {
        return townName;
    }

    public Integer getRouteDistance(String toTown) {
        if (routes.containsKey(toTown))
            return routes.get(toTown);
        else
            return 0;
    }

    public HashMap<String, Integer> getRouteHashMap() {
        return routes;
    }
}
