package GraphTraversal;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class GraphTraversalController {
    DirectedGraph directedGraph;

    @FXML
    public void initialize() {
        directedGraph = new DirectedGraph();
    }

    @FXML
    private TextArea inputTextArea;
    @FXML
    private TextArea outputTextArea;

    @FXML
    private void menuParseRouteFileButtonClicked(ActionEvent actionEvent) {
        FileDialog fileDialog = new FileDialog(new Frame(), "Select a route file to parse.");

        fileDialog.setVisible(true);

        try {
            inputTextArea.setText(directedGraph.parseGraphFile(fileDialog.getDirectory() + fileDialog.getFile()));
        }
        catch (Exception exception) {
            JOptionPane.showMessageDialog(new Frame(), exception);
        }

        outputTextArea.setText(createOutputString());
    }

    @FXML
    private void menuExitButtonClicked(ActionEvent actionEvent) {
        javafx.application.Platform.exit();
    }

    private String createOutputString() {
        ArrayList<ArrayList<String>> routeDistanceStrings = new ArrayList<ArrayList<String>>();
        StringBuilder outputString = new StringBuilder("");
        Integer totalRouteDistance;

        routeDistanceStrings.add(new ArrayList<String>(Arrays.asList("A", "B", "C")));
        routeDistanceStrings.add(new ArrayList<String>(Arrays.asList("A", "D")));
        routeDistanceStrings.add(new ArrayList<String>(Arrays.asList("A", "D", "C")));
        routeDistanceStrings.add(new ArrayList<String>(Arrays.asList("A", "E", "B", "C", "D")));
        routeDistanceStrings.add(new ArrayList<String>(Arrays.asList("A", "E", "D")));

        for (int i = 0; i < routeDistanceStrings.size(); i++) {
            outputString.append("Distance of route ");

            for (int j = 0; j < routeDistanceStrings.get(i).size(); j++) {
                outputString.append(routeDistanceStrings.get(i).get(j));

                if (j != routeDistanceStrings.get(i).size() - 1)
                    outputString.append("-");
                else
                    outputString.append(": ");
            }

            totalRouteDistance = directedGraph.getTotalRouteDistance(routeDistanceStrings.get(i));

            if (totalRouteDistance != 0)
                outputString.append(totalRouteDistance);
            else
                outputString.append("NO SUCH ROUTE");

            outputString.append("\n");
        }

        outputString.append("Trips from C-C with a max of 3 stops: ");
        outputString.append(directedGraph.getTripsWithStopsUpTo("C", "C", 3));
        outputString.append("\n");
        outputString.append("Trips from A-C with exactly 4 stops: ");
        outputString.append(directedGraph.getTripsWithExactStops("A", "C", 4));
        outputString.append("\n");

        outputString.append("Shortest distance between A-C: ");
        outputString.append(directedGraph.getShortestRouteLength("A", "C"));
        outputString.append("\n");

        outputString.append("Shortest distance between B-B: ");
        outputString.append(directedGraph.getShortestRouteLength("B", "B"));
        outputString.append("\n");

        outputString.append("Trips from C-C with distance less than 30: ");
        outputString.append(directedGraph.getNumberOfRoutesLessThan(new ArrayList<String>(Arrays.asList("C")), "C", 30));
        outputString.append("\n");

        return outputString.toString();
    }
}
