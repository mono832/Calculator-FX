package com.example.calculatorfx;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

//A versatile class that acts as history, and memory
public class memoryControls
{
    //memory and history for tab
    Button history=new Button("history");
    Button memory=new Button("memory");

    private final HBox tab=new HBox();

    //memory & history panes
    private final VBox memoryPane=new VBox(2);
    private final VBox historyPane=new VBox(2);
    private final ScrollPane memoryScroll=new ScrollPane(historyPane);
    private final BorderPane pane=new BorderPane();

    /**
     * no arg constructor for making things not traversable, and disabling history
     */
    memoryControls()
    {
        history.setDisable(true);
        history.setFocusTraversable(false);
        memory.setFocusTraversable(false);

        memoryScroll.setFocusTraversable(false);
        memoryPane.setFocusTraversable(false);
        historyPane.setFocusTraversable(false);
        pane.setFocusTraversable(false);
    }

    /**
     * Makes the UI. I never would have guessed
     * @return a borderPane of the UI used
     */
    public BorderPane createUI()
    {
        tab.getChildren().clear();
        tab.getChildren().addAll(history,memory);
        pane.setTop(tab);
        pane.setCenter(memoryScroll);

        memoryPane.setPrefWidth(181);
        historyPane.setPrefWidth(181);
        memoryScroll.setPrefSize(200,300);
        memoryPane.setStyle("-fx-background-color:grey");
        historyPane.setStyle("-fx-background-color:grey");

        memory.setOnAction(event -> {
            memory.setDisable(true);
            history.setDisable(false);
            memoryScroll.setContent(memoryPane);
        });
        history.setOnAction(event -> {
            history.setDisable(true);
            memory.setDisable(false);
            memoryScroll.setContent(historyPane);
        });

        return pane;
    }

    /**
     * Updates history
     * @param input the answer stored
     * @param operationType the operation type
     * @param equationText the equation holder and what gets the input when is button is pressed
     */
    public void updateHistory(String input, String operationType, TextField equationText)
    {
        Button storedString=new Button(operationType+"\n"+equationText.getText()+"\n"+input);
        storedString.setFocusTraversable(false);
        storedString.setId(input);

        historyPane.getChildren().add(storedString);

        storedString.setOnAction(event ->
        {
            equationText.setText(equationText.getText().replace("¦",storedString.getId()+"¦"));
            equationText.positionCaret(equationText.getText().indexOf("¦")+1);
        });
    }

    /**
     * A special Trig history updater
     @param input the answer stored
      * @param operationType the operation type
     * @param rDG a string of deg rad or grad
     * @param equationText the equation holder and what gets the input when is button is pressed
     */
    public void updateHistoryTrig(String input, String operationType,String rDG, TextField equationText)
    {
        Button storedString=new Button(operationType+" "+rDG+"\n"+equationText.getText()+"\n"+input);
        storedString.setFocusTraversable(false);
        storedString.setId(input);

        historyPane.getChildren().add(storedString);

        storedString.setOnAction(event ->
        {
            equationText.setText(equationText.getText().replace("¦",storedString.getId()+"¦"));
            equationText.positionCaret(equationText.getText().indexOf("¦")+1);
        });
    }

    /**
     * A special history updater for conversions
     * @param input the answer stored
     * @param from what's converted from
     * @param to what's converted to
     * @param equationText the equation holder and what gets the input when is button is pressed
     */
    public void updateHistoryFromTo(String input, String from,String to, TextField equationText)
    {
        Button storedString=new Button(from+" ⮞ "+to+"\n"+equationText.getText()+"\n"+input);
        storedString.setFocusTraversable(false);
        storedString.setId(input);

        historyPane.getChildren().add(storedString);

        storedString.setOnAction(event ->
        {
            equationText.setText(equationText.getText().replace("¦",storedString.getId()+"¦"));
            equationText.positionCaret(equationText.getText().indexOf("¦")+1);
        });
    }

    /**
     * Updates memory
     * @param input a string of equationText yes its redundant but were not evaluating now, are we?
     * @param equationText what gets the input when its button is pressed
     */
    public void updateMemory(String input,TextField equationText)
    {
        Button storedString=new Button(input);
        storedString.setFocusTraversable(false);
        storedString.setId(equationText.getText());

        memoryPane.getChildren().add(storedString);

        storedString.setOnAction(event -> {
            equationText.setText(equationText.getText().replace("¦",storedString.getId()+"¦"));
            equationText.positionCaret(equationText.getText().indexOf("¦")+1);
        });
    }

}
