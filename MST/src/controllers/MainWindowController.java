/*
 * Copyright (C) 2019 brahim
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mst.Circle;
import mst.Graph;
import mst.GraphPane;
import mst.Line;

/**
 * FXML Controller class
 *
 * @author brahim
 */
public class MainWindowController implements Initializable {
    
    @FXML private BorderPane root;
    @FXML private Label file_name;
    @FXML private Label creator;
    @FXML private Label Nodes_number;
    @FXML private Label edges_number;
    @FXML private Label terminals_number;
    @FXML private Label status;
    @FXML private Label totalCost;
    @FXML private CheckMenuItem showLineValues;
    
    @FXML private Label kmbTime;
    @FXML private Label degigneTime;
    
    private GraphPane graphPane;
    private ObservableList<Node> linesValues;
    private double scaleFactor = 0.5;
    
    private Graph graph = null;
    
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        graphPane = new GraphPane(15);
        root.setCenter(graphPane);
        Event e = new Event(EventType.ROOT);
        graphPane.layoutBoundsProperty().addListener((observable) -> {
            resize(e);
        });
        //graphPane.layoutBoundsProperty().addListener();
        /*graphPane.widthProperty().addListener((event) -> {
            ((Event)event).consume();
            Platform.runLater(() -> {resize();});
        });
        graphPane.heightProperty().addListener((event) -> {
            ((Event)event).consume();
            Platform.runLater(() -> {resize();});
        });*/
    }
    
    private void resize(Event e) {
        e.consume();
        if ( graph != null ) {
            Platform.runLater(() -> { this.draw(graph, graphPane);System.gc(); });
        } else {
            graphPane.drawBorder();
        }
    }
    
    @FXML
    void close(ActionEvent event) {
        System.exit(0);
    }
    
    @FXML void openFile(ActionEvent event) throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("STP files (*.stp)", "*.stp"));
        fileChooser.setTitle("Choose Data File");
        File file = fileChooser.showOpenDialog(null);
        if ( file != null && file.exists() ) {
            changeStatusMsg("Loading Data from file ...");
            try {
                graph = new Graph(file);
            } catch (Exception ex) {
                graph = null;
                (new Alert(Alert.AlertType.ERROR, "File Syntax Error !")).show();
            }
            this.setInformations(graph);
            if ( graph != null ) {
                changeStatusMsg("Loading Graph ...");
                Platform.runLater(() -> {this.draw(graph, graphPane);});
            } else {
                changeStatusMsg("No file selected");
                graphPane.getChildren().clear();
            }
        } else {
            //(new Alert(Alert.AlertType.ERROR, "File Not Correct")).show();
        }
    }
    
    @FXML
    void selectLinesValues(ActionEvent event) {
        if ( graph != null ) {
            if ( showLineValues.isSelected() ) {
                graphPane.addComponents(linesValues);
            } else {
                graphPane.removeComponents(linesValues);
            }
        }
    }
    
    @FXML
    void kmb(ActionEvent event) {
        System.out.println("----------------------------------------------------");
        if ( graph != null ) {
            long start = System.currentTimeMillis();
            graph = graph.kmb();
            long end = System.currentTimeMillis();
            Platform.runLater(() -> {kmbTime.setText(String.valueOf((end - start) / 1000F)+" s");});
            System.out.println("KMB -> "+(end - start) / 1000F + " seconds");
            draw(graph, graphPane); 
        }
        System.out.println("----------------------------------------------------");
    }
    
    @FXML
    void completeGraph(ActionEvent event) {
        if ( graph != null ) {
            graph = Graph.completeGraph(graph);
            draw(graph, graphPane); 
        }
    }

    @FXML
    void kruskal(ActionEvent event) {
        if ( graph != null ) {
            graph = Graph.createNewGraph(graph, Graph.kruskal(graph), null);
            draw(graph, graphPane); 
        }
    }
    
    @FXML
    void about(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent aboutRoot = FXMLLoader.load(getClass().getResource("/views/About.fxml"));
        Scene scene = new Scene(aboutRoot, 400, 275);
        stage.setTitle("About");
        stage.getIcons().add(new Image("/icons/icons8-navigate-480.png"));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    
    private double updateScaleFactor(int size) {
        double factor;
        if (size < 100) {
            factor = 1.2;
        } else if (size < 200) {
            factor = 1;
        } else if (size < 500) {
            factor = 0.9;
        } else if (size < 1000) {
            factor = 0.8;
        } else if (size < 5000) {
            factor = 0.7;
        } else if (size < 10000) {
            factor = 0.6;
        } else if (size < 25000) {
            factor = 0.5;
        } else {
            factor = 0.4;
        }
        return factor;
    }
    
    private void draw(Graph graph, GraphPane graphPane) {
        changeStatusMsg("Clear Graph ...");
        Platform.runLater(() -> {
            long start = System.currentTimeMillis();
            scaleFactor = updateScaleFactor(graph.getNodes_number());
            ObservableList<Node> graphComponents = FXCollections.observableArrayList();
            linesValues = FXCollections.observableArrayList();
            int [][] coordinates = graph.getGraphCoordinate((int) graphPane.getWidth(), (int) graphPane.getHeight(), this.graphPane.MARGIN);
            int [][] arets = graph.getGraph();
            changeStatusMsg("Loading Nodes ...");
            int totalCosts = 0;
            for (int[] aret : arets) {
                totalCosts += aret[2];
                int [] n1 = graph.getNodeCoordinate(aret[0]);
                int [] n2 = graph.getNodeCoordinate(aret[1]);
                Line l = new Line(n1[0], n1[1], n2[0], n2[1], Color.BLUE, StrokeLineCap.BUTT, 1);
                graphComponents.add(l);
            }
            this.totalCost.setText(String.valueOf(totalCosts));
            changeStatusMsg("Loading Edges ...");
            for (int[] coordinate : coordinates) {
                Circle c = new Circle(coordinate[0], coordinate[1], coordinate[2], 4*scaleFactor, Color.BISQUE, Color.BROWN, 1*scaleFactor);              
                graphComponents.add(c);
            }
            changeStatusMsg("Loading Terminals ...");
            for (int terminal : graph.getTerminals()) {
                int coordinate[] = graph.getNodeCoordinate(terminal);
                Circle c = new Circle(terminal, coordinate[0], coordinate[1], 5*scaleFactor, Color.AQUAMARINE, Color.DARKCYAN, 1*scaleFactor);
                graphComponents.add(c);
            }
            for (int[] aret : arets) {
                int coordinate0[] = graph.getNodeCoordinate(aret[0]);
                int coordinate1[] = graph.getNodeCoordinate(aret[1]);
                int x = (coordinate0[0]+coordinate1[0])/2;
                int y = (coordinate0[1]+coordinate1[1])/2;
                if ( Math.abs(coordinate0[0]-coordinate1[0]) > 15 ) {
                    Text t = new Text(x-4, y-3, String.valueOf(aret[2]));
                    t.setFont(new Font(10+((scaleFactor-1)*10)));
                    linesValues.add(t);
                } else if ( Math.abs(coordinate0[1]-coordinate1[1]) > 15) {
                    Text t = new Text(x+3, y+4, String.valueOf(aret[2]));
                    t.setFont(new Font(10+((scaleFactor-1)*10)));
                    linesValues.add(t);
                }
            }
            
            changeStatusMsg("Designing Graph ...");
            this.graphPane.drawGraph(graphComponents);
            if ( showLineValues.isSelected() ) {
                this.graphPane.addComponents(linesValues);
            }
            changeStatusMsg("All Done");
            
            long end = System.currentTimeMillis();
            System.out.println("Graph time -> "+(end - start) / 1000F + " seconds");
            Platform.runLater(() -> {degigneTime.setText(String.valueOf((end - start) / 1000F)+" s");});
            System.gc();
        });
    }
    
    private void setInformations(Graph graph) {
        if ( graph == null ) {
            this.file_name.setText("- - -");
            this.creator.setText("- - -");
            this.Nodes_number.setText("- - -");
            this.edges_number.setText("- - -");
            this.terminals_number.setText("- - -");
            this.totalCost.setText("- - -");
            this.kmbTime.setText("- - -");
        } else {
            this.file_name.setText(graph.getName());
            this.creator.setText(graph.getCreator());
            this.Nodes_number.setText(String.valueOf(graph.getNodes_number()));
            this.edges_number.setText(String.valueOf(graph.getEdges_number()));
            this.terminals_number.setText(String.valueOf(graph.getTerminals_number()));
            this.kmbTime.setText("- - -");
        }
    }
    
    private void changeStatusMsg(String msg) {
        Platform.runLater(() -> { status.setText(msg); });
    }
    
    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {}
    }
}