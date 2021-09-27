package GraphTraversal;

import java.io.File;
import java.util.*;


public class DirectedGraph {
    private List<Town> townList;
    private HashMap<String, Integer> townNamesIndex;

    DirectedGraph() {
        townList = new ArrayList<Town>();
        townNamesIndex = new HashMap<String, Integer>();
    }

    public String parseGraphFile(String filename) throws Exception {
        File file = new File(filename);
        Scanner routeScanner = new Scanner(file);
        String routeString;
        StringBuilder inputString = new StringBuilder("");

        String fromTown;
        String toTown;
        Integer distance;

        routeScanner.useDelimiter(", ");
        while (routeScanner.hasNext()) {
            routeString = routeScanner.next();

            fromTown = routeString.substring(0, 1).toUpperCase();
            toTown = routeString.substring(1, 2).toUpperCase();

            if (fromTown.equals(toTown))
                throw new Exception("The from and to town in a route can't be the same.");

            try {
                distance = Integer.parseInt(routeString.substring(2));
            }
            catch (Exception exception) {
                throw new Exception("There was an error parsing the distance. Most likely it is not a number or the route file is in the wrong format.");
            }

            if (distance <= 0)
                throw new Exception("The distance must be an integer greater than 0.");

            createNewRoute(fromTown.toString(), toTown.toString(), distance);

            inputString.append("Route from ");
            inputString.append(fromTown);
            inputString.append(" to ");
            inputString.append(toTown);
            inputString.append(" is a distance of ");
            inputString.append(distance);
            inputString.append("\n");
        }

        return inputString.toString();
    }

    public Integer getTotalRouteDistance(ArrayList<String> routeTownList) {
        Integer totalRouteDistance = 0;
        Integer currentRouteDistance;
        String fromTown;
        String toTown;

        // Start from the first town in the list.
        fromTown = routeTownList.get(0);

        for (int i = 1; i < routeTownList.size(); i++) {
            toTown = routeTownList.get(i);
            currentRouteDistance = townList.get(townNamesIndex.get(fromTown)).getRouteDistance(toTown);

            // If zero was returned from getRouteDistance, the route doesn't exist.
            if (currentRouteDistance == 0)
                return 0;

            totalRouteDistance += currentRouteDistance;

            fromTown = toTown;
        }

        return totalRouteDistance;
    }

    public Integer getTripsWithStopsUpTo(String fromTown, String toTown, Integer maxStops) {
        ArrayList<String> currentRoutes = new ArrayList<String>();
        Integer tripsPossible = 0;

        fromTown.toUpperCase();
        toTown.toUpperCase();

        currentRoutes.addAll(townList.get(townNamesIndex.get(fromTown)).getRouteHashMap().keySet());

        if (maxStops > 0) {
            if (currentRoutes.contains(toTown))
                tripsPossible++;

            for (int i = 0; i < currentRoutes.size(); i++) {
                tripsPossible += getTripsWithStopsUpTo(currentRoutes.get(i), toTown, maxStops - 1);
            }
        }

        return tripsPossible;
    }

    public Integer getTripsWithExactStops(String fromTown, String toTown, Integer stops) {
        ArrayList<String> currentRoutes = new ArrayList<String>();
        Integer tripsPossible = 0;

        fromTown.toUpperCase();
        toTown.toUpperCase();

        currentRoutes.addAll(townList.get(townNamesIndex.get(fromTown)).getRouteHashMap().keySet());

        if (stops >= 0) {
            if (currentRoutes.contains(toTown) && (stops == 0))
                tripsPossible++;

            for (int i = 0; i < currentRoutes.size(); i++) {
                tripsPossible += getTripsWithExactStops(currentRoutes.get(i), toTown, stops - 1);
            }
        }

        return tripsPossible;
    }

    public Integer getShortestRouteLength(String fromTown, String toTown) {
        HashMap<String, Integer> townDistances = new HashMap<String, Integer>();
        ArrayList<String> townsNotVisited = new ArrayList<String>();
        HashMap<String, Integer> currentTownRoutes = new HashMap<String, Integer>();

        fromTown.toUpperCase();
        toTown.toUpperCase();

        String shortestRouteTown = fromTown;
        Integer shortestRouteDistance = 0;

        for (int i = 0; i < townList.size(); i++) {
            townDistances.put(townList.get(i).getTownName(), Integer.MAX_VALUE);
            townsNotVisited.add(townList.get(i).getTownName());
        }

        townDistances.put(fromTown, 0);
        shortestRouteTown = fromTown;

        // Account for instances when the end town is the same as the start.
        if (fromTown.equals(toTown)) {
            townDistances.put(fromTown + " END", Integer.MAX_VALUE);
            townsNotVisited.add(fromTown + " END");
        }

        while (!townsNotVisited.isEmpty()) {
            shortestRouteDistance = Integer.MAX_VALUE;

            for (String town : townsNotVisited) {
                if ((townDistances.get(town) < shortestRouteDistance) && (townDistances.get(town) != Integer.MAX_VALUE)) {
                    shortestRouteDistance = townDistances.get(town);
                    shortestRouteTown = town;
                }
            }

            townsNotVisited.remove(shortestRouteTown);

            // Account for instances when the end town is the same as the start.
            if (shortestRouteTown.equals(fromTown + " END"))
                // If the shortest route leads back to the start town, break here to avoid an endless loop.
                break;
            else
                currentTownRoutes = townList.get(townNamesIndex.get(shortestRouteTown)).getRouteHashMap();

            for (String town : currentTownRoutes.keySet()) {
                // Account for instances when the end town is the same as the start.
                if (fromTown.equals(toTown) && town.equals(fromTown)) {
                    if ((currentTownRoutes.get(town) + shortestRouteDistance) < townDistances.get(town + " END"))
                        townDistances.replace(town + " END", currentTownRoutes.get(town) + shortestRouteDistance);
                }
                else {
                    if ((currentTownRoutes.get(town) + shortestRouteDistance) < townDistances.get(town))
                        townDistances.replace(town, currentTownRoutes.get(town) + shortestRouteDistance);
                }
            }
        }

        if (fromTown.equals(toTown))
            return townDistances.get(toTown + " END");
        else
            return townDistances.get(toTown);
    }

    public Integer getNumberOfRoutesLessThan(ArrayList<String> townsVisited, String toTown, Integer maxDistance) {
        HashMap<String, Integer> currentTownRoutes = new HashMap<String, Integer>();
        Integer routesPossible = 0;

        toTown.toUpperCase();

        // Get the routes leaving the most recently visited town.
        currentTownRoutes = townList.get(townNamesIndex.get(townsVisited.get(townsVisited.size() - 1))).getRouteHashMap();

        for (String town : currentTownRoutes.keySet()) {
            if (town.equals(toTown) && ((maxDistance - currentTownRoutes.get(town)) > 0))
                routesPossible++;

            if (maxDistance > 0) {
                townsVisited.add(town);
                routesPossible += getNumberOfRoutesLessThan(townsVisited, toTown, maxDistance - currentTownRoutes.get(town));
            }
            else
                townsVisited.remove(town);
        }

        return routesPossible;
    }

    private void createNewRoute(String fromTown, String toTown, Integer distance) throws Exception {
        if (!townNamesIndex.containsKey(fromTown)) {
            townList.add(new Town(fromTown));
            townNamesIndex.put(fromTown, townList.size() - 1);
        }

        if (!townList.get(townNamesIndex.get(fromTown)).addRoute(toTown, distance))
            throw new Exception("A town cannot have more than one route to another town.");
    }
}
