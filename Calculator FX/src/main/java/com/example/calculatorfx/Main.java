package com.example.calculatorfx;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
//import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/*
Created by,     MK
Description,    A scientific Calculator, inspired by the MS calculator, written in java FX,
                as a capstone project and, as a passion project for the first program I ever made.

*You have my permission to use, study, and probe my code as much as you want. Just give credit where its due*
Do not redistribute on any other website

Not my work credit (please don't kill me, I gave credit)
GraalVM for their javascript evaluator
js https://mvnrepository.com/artifact/org.graalvm.js/js
js-scriptengine https://mvnrepository.com/artifact/org.graalvm.js/js-scriptengine
org.apache.commons for their Gamma function
commons-math3 https://mvnrepository.com/artifact/org.apache.commons/commons-math3
and of course Maven, Oracle, and Jetbrains for obvious reasons
 */
public class Main extends Application
{
    //the guide Stage. It's here so I know when its open
    Stage guideStage=new Stage();

    //needed for making UI and storing info
    MemoryControls memory=new MemoryControls();

    //the answer, the equation, the hint
    Text answerText=new Text("");
    TextField equationText=new TextField("¦");
    Text helpText=new Text("Default");

    //changes scenes and determines what form of evaluation happens
    ComboBox<String> operation=new ComboBox<>();
    ComboBox<String> fromThis=new ComboBox<>();
    ComboBox<String> toThis=new ComboBox<>();

    //arrow buttons
    Button left=new Button("⮜");
    Button right=new Button("⮞");

    //number buttons
    Button b0=new Button("0");
    Button b1=new Button("1");
    Button b2=new Button("2");
    Button b3=new Button("3");
    Button b4=new Button("4");
    Button b5=new Button("5");
    Button b6=new Button("6");
    Button b7=new Button("7");
    Button b8=new Button("8");
    Button b9=new Button("9");

    //condition buttons? expression? whatever there called
    Button pie=new Button("π");
    Button euler=new Button("e");
    Button plus =new Button("+");
    Button minus =new Button("-");
    Button divide =new Button("/");
    Button multiply =new Button("x");
    Button modulo =new Button("%");
    Button point=new Button(".");
    Button equal=new Button("=");
    Button remove =new Button("⌫");
    Button clear=new Button("C");
    Button parL=new Button("(");
    Button parR=new Button(")");

    //the rest of the usable keyboard
    Button bA=new Button("a");
    Button bB=new Button("b");
    Button bC=new Button("c");
    Button bD=new Button("d");
    Button bF=new Button("f");
    Button backQuote=new Button("`");
    Button bG=new Button("g");
    Button bH=new Button("h");
    Button bI=new Button("i");
    Button bJ=new Button("j");
    Button bK=new Button("k");
    Button bL=new Button("l");
    Button bM=new Button("m");
    Button bN=new Button("n");
    Button bO=new Button("o");
    Button bP=new Button("p");
    Button bQ=new Button("q");
    Button bR=new Button("r");
    Button bS=new Button("s");
    Button bT=new Button("t");
    Button bU=new Button("u");
    Button bV=new Button("v");
    Button bW=new Button("w");
    Button bX=new Button("x");
    Button bY=new Button("y");
    Button bZ=new Button("z");
    Button bracketL=new Button("{");
    Button bracketR=new Button("}");
    Button backSlash=new Button("\\");
    Button semiColon=new Button(";");
    Button quote=new Button("'");
    Button comma=new Button(",");
    Button space=new Button("˽");

    //Deg, Rad, or Grad
    Button rDG=new Button("Degrees");

    //Stuff that goes in the menu
    MenuBar mainMenu=new MenuBar();
    Menu helpMenu=new Menu("Help");
    MenuItem helpItem=new MenuItem("Guid");
    MenuItem copyItem=new MenuItem("Copy");
    MenuItem pasteItem=new MenuItem("Paste");
    MenuItem cutItem=new MenuItem("Cut");
    MenuItem saveItem=new MenuItem("Save to memory");

    //keyboard condition checking
    //keyboardType holds the keyboard type, capSwitch checks if capslock is on, capitalized checks if capitalized
    String keyboardType="math";
    boolean capSwitch=false;
    boolean capitalized=false;

    @Override
    public void start(Stage primaryStage)
    {
        //Don't cheat my system. Makes sure capslock is off
        Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_CAPS_LOCK, false);

        //removes focus of things, so that left and right keyboard arrows can be used
        removeFocus();

        //set available functions
        operation.getItems().addAll("Default","Common","Trig","Geometry",
                "Standard deviation calc", "Quadratic calc","Cubic calc","int, hex, binary, octal converter",
                "Ascii converter", "Data converter 2¹⁰", "Data converter 10³", "Temp converter",
                "Time converter", "Angle converter","Frequency converter","Discount");
        operation.setValue("Default");

        //The main menu and its items
        helpItem.setAccelerator(KeyCombination.keyCombination("Ctrl+H"));
        copyItem.setAccelerator(KeyCombination.keyCombination("Ctrl+C"));
        pasteItem.setAccelerator(KeyCombination.keyCombination("Ctrl+V"));
        cutItem.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
        saveItem.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        helpMenu.getItems().addAll(helpItem,copyItem,pasteItem,cutItem,saveItem);
        mainMenu.getMenus().addAll(helpMenu);

        //set the stage and make it pretty setting image was PANE-ful (very funny)
        primaryStage.setScene(defaultUI());
        primaryStage.setResizable(false);
        primaryStage.setTitle("Calculator FX");
        primaryStage.getIcons().add(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icon.png"))));
        primaryStage.show();

        //Input based actions call some method or lambda expression (This is the part where it becomes hard to
        //  comment and order things since they have no order)
        helpItem.setOnAction(event -> createGuideUI());
        copyItem.setOnAction(event -> clipboardAndMemory("copy",equationText.getText()));
        pasteItem.setOnAction(event -> clipboardAndMemory("paste",equationText.getText()));
        cutItem.setOnAction(event -> clipboardAndMemory("cut",equationText.getText()));
        saveItem.setOnAction(event -> clipboardAndMemory("save",equationText.getText()));
        operation.setOnAction(event -> operations(primaryStage));
        left.setOnAction(event ->moveIndex('<'));
        right.setOnAction(event -> moveIndex('>'));
        b0.setOnAction(event -> pressInputKey("button",b0.getText()));
        b1.setOnAction(event -> pressInputKey("button",b1.getText()));
        b2.setOnAction(event -> pressInputKey("button",b2.getText()));
        b3.setOnAction(event -> pressInputKey("button",b3.getText()));
        b4.setOnAction(event -> pressInputKey("button",b4.getText()));
        b5.setOnAction(event -> pressInputKey("button",b5.getText()));
        b6.setOnAction(event -> pressInputKey("button",b6.getText()));
        b7.setOnAction(event -> pressInputKey("button",b7.getText()));
        b8.setOnAction(event -> pressInputKey("button",b8.getText()));
        b9.setOnAction(event -> pressInputKey("button",b9.getText()));
        pie.setOnAction(event -> pressInputKey("button","π"));
        euler.setOnAction(event -> pressInputKey("button",euler.getText()));
        plus.setOnAction(event -> pressInputKey("button",plus.getText()));
        minus.setOnAction(event -> pressInputKey("button",minus.getText()));
        divide.setOnAction(event -> pressInputKey("button",divide.getText()));
        multiply.setOnAction(event -> pressInputKey("button","*"));
        modulo.setOnAction(event -> pressInputKey("button","%"));
        point.setOnAction(event -> pressInputKey("button",point.getText()));
        remove.setOnAction(event ->pressRemove());
        clear.setOnAction(event ->pressClear());
        parL.setOnAction(event -> pressInputKey("button","("));
        parR.setOnAction(event -> pressInputKey("button",")"));
        equal.setOnAction(event ->pressEqual());
        comma.setOnAction(actionEvent -> pressInputKey("button",comma.getText()));
        backQuote.setOnAction(actionEvent -> pressInputKey("button",backQuote.getText()));
        bracketL.setOnAction(actionEvent -> pressInputKey("button",bracketL.getText()));
        bracketR.setOnAction(actionEvent -> pressInputKey("button",bracketR.getText()));
        backSlash.setOnAction(actionEvent -> pressInputKey("button",backSlash.getText()));
        semiColon.setOnAction(actionEvent -> pressInputKey("button",semiColon.getText()));
        quote.setOnAction(actionEvent -> pressInputKey("button",quote.getText()));
        space.setOnAction(actionEvent -> pressInputKey("button"," "));
        bA.setOnAction(actionEvent -> pressInputKey("button",bA.getText()));
        bB.setOnAction(actionEvent -> pressInputKey("button",bB.getText()));
        bC.setOnAction(actionEvent -> pressInputKey("button",bC.getText()));
        bD.setOnAction(actionEvent -> pressInputKey("button",bD.getText()));
        bF.setOnAction(actionEvent -> pressInputKey("button",bF.getText()));
        bG.setOnAction(actionEvent -> pressInputKey("button",bG.getText()));
        bH.setOnAction(actionEvent -> pressInputKey("button",bH.getText()));
        bI.setOnAction(actionEvent -> pressInputKey("button",bI.getText()));
        bJ.setOnAction(actionEvent -> pressInputKey("button",bJ.getText()));
        bK.setOnAction(actionEvent -> pressInputKey("button",bK.getText()));
        bL.setOnAction(actionEvent -> pressInputKey("button",bL.getText()));
        bM.setOnAction(actionEvent -> pressInputKey("button",bM.getText()));
        bN.setOnAction(actionEvent -> pressInputKey("button",bN.getText()));
        bO.setOnAction(actionEvent -> pressInputKey("button",bO.getText()));
        bP.setOnAction(actionEvent -> pressInputKey("button",bP.getText()));
        bQ.setOnAction(actionEvent -> pressInputKey("button",bQ.getText()));
        bR.setOnAction(actionEvent -> pressInputKey("button",bR.getText()));
        bS.setOnAction(actionEvent -> pressInputKey("button",bS.getText()));
        bT.setOnAction(actionEvent -> pressInputKey("button",bT.getText()));
        bU.setOnAction(actionEvent -> pressInputKey("button",bU.getText()));
        bV.setOnAction(actionEvent -> pressInputKey("button",bV.getText()));
        bW.setOnAction(actionEvent -> pressInputKey("button",bW.getText()));
        bX.setOnAction(actionEvent -> pressInputKey("button",bX.getText()));
        bY.setOnAction(actionEvent -> pressInputKey("button",bY.getText()));
        bZ.setOnAction(actionEvent -> pressInputKey("button",bZ.getText()));
        primaryStage.setOnCloseRequest(event -> {
            //if I go down you go down with me
            guideStage.close();
        });
        rDG.setOnAction(event -> {
            switch (rDG.getText())
            {
                case "Degrees": rDG.setText("Radians");break;
                case "Radians": rDG.setText("GRadians");break;
                case "GRadians": rDG.setText("Degrees");break;
            }
        });
    }

    /**
     * enables buttons that could have disabled
     */
    public void enable()
    {
        plus.setDisable(false);
        multiply.setDisable(false);
        divide.setDisable(false);
        minus.setDisable(false);
        remove.setDisable(false);
        modulo.setDisable(false);
        comma.setDisable(false);
        b2.setDisable(false);
        b3.setDisable(false);
        b4.setDisable(false);
        b5.setDisable(false);
        b6.setDisable(false);
        b7.setDisable(false);
        b8.setDisable(false);
        b9.setDisable(false);
        pie.setDisable(false);
        euler.setDisable(false);
        bA.setDisable(false);
        bB.setDisable(false);
        bC.setDisable(false);
        bD.setDisable(false);
        bF.setDisable(false);
        parL.setDisable(false);
        parR.setDisable(false);
        point.setDisable(false);

        backQuote.setDisable(false);
        bQ.setDisable(false);
        bW.setDisable(false);
        bR.setDisable(false);
        bT.setDisable(false);
        bY.setDisable(false);
        bU.setDisable(false);
        bI.setDisable(false);
        bO.setDisable(false);
        bP.setDisable(false);
        bracketL.setDisable(false);
        bracketR.setDisable(false);
        backSlash.setDisable(false);
        bS.setDisable(false);
        bG.setDisable(false);
        bH.setDisable(false);
        bJ.setDisable(false);
        bK.setDisable(false);
        bL.setDisable(false);
        semiColon.setDisable(false);
        quote.setDisable(false);
        bZ.setDisable(false);
        bX.setDisable(false);
        bV.setDisable(false);
        bN.setDisable(false);
        bM.setDisable(false);
    }

    /**
     * Creates and opens the Guide UI
     */
    public void createGuideUI()
    {
        // if it's open don't open
        if(guideStage.isShowing())
            return;

        //the panes
        Text specialText=new Text("try");
        VBox startPane=new VBox();
        VBox textPane=new VBox(2);
        HBox specialTextLine=new HBox();
        ScrollPane pane=new ScrollPane(textPane);

        pane.setPrefSize(535,500);
        startPane.setAlignment(Pos.CENTER);

        //the mess of text to display
        startPane.getChildren().add(new Text("Hi!"));
        startPane.getChildren().add(new Text("Welcome to the calculator FX"));
        startPane.getChildren().add(new Text("A capstone and personal project ive wanted to make for years"));

        textPane.getChildren().add(startPane);
        textPane.getChildren().add(new Text("You can use the buttons on screen or your keyboard to type" +
                " what you need \n"+"\t(remember to use shift or caps when needed)"));

        specialTextLine.getChildren().add(new Text("If you try and break the program the program will "));
        specialText.getStyleClass().add("tryText");
        specialTextLine.getChildren().add(specialText);
        specialTextLine.getChildren().add(new Text(" and fix the problem. So try and be specific."));
        textPane.getChildren().add(specialTextLine);

        textPane.getChildren().add(new Text("The top right of the text field will show your result."));
        textPane.getChildren().add(new Text("The right of the text field will show you an extension with\n\t" +
                "2 tabs, history which will display some information of the evaluation\n\t" +
                " and will send the answer to where your cursor line is. And memory which\n\t" +
                "stores what is in the text field for later use."));
        textPane.getChildren().add(new Text("The bottom right of the text field will show you a hint of the" +
                "\n\tsyntax or formula used... Or if the text is copied, pasted, cut, or saved to memory."));

        specialText=new Text("Here are the Calculators and their general description");
        specialText.getStyleClass().add("descriptionText");
        textPane.getChildren().add(specialText);
        textPane.getChildren().add(new Text("\t-Default page contains all the basic calculator components"));
        textPane.getChildren().add(new Text("\t-Common page contains common mathematical functions"));
        textPane.getChildren().add(new Text("\t\t-Fact! & Gamma can find the factorial of any number or " +
                "decimal after evaluating."));
        textPane.getChildren().add(new Text("\t\t-Square² squares the function."));
        textPane.getChildren().add(new Text("\t\t-Cube³ cubes the function."));
        textPane.getChildren().add(new Text("\t\t-Custom power ^ʸ will accept 2 inputs separated by" +
                " \",\" and raises the first to the power of the second."));
        textPane.getChildren().add(new Text("\t\t-e^ᵡ is euler to the power of the input."));
        textPane.getChildren().add(new Text("\t\t-Square root √ returns the square root of the input."));
        textPane.getChildren().add(new Text("\t\t-Cube root ∛ returns the cube root of the function."));
        textPane.getChildren().add(new Text("\t\t-Custom root ʸ√ will accept 2 inputs separated by" +
                " \",\" and roots the second by the first input."));
        textPane.getChildren().add(new Text("\t\t-Abs || returns the absolute of the input."));
        textPane.getChildren().add(new Text("\t\t-Floor ⌊⌋ returns the floor of the input."));
        textPane.getChildren().add(new Text("\t\t-Ceil ⌈⌉ returns the ceiling of the input."));
        textPane.getChildren().add(new Text("\t\t-log returns the log of the input."));
        textPane.getChildren().add(new Text("\t\t-ln returns the natural log of the input."));
        textPane.getChildren().add(new Text("\t\t-Exp will accept 2 inputs separated by " +
                "\",\" and is xEy x is the number multiplied or\n\t\t divided by " +
                "y(number of times 10 is added. Y MUST be whole.)"));
        textPane.getChildren().add(new Text("\t\t-nPr will accept 2 inputs separated by " +
                "\",\" and is n!/(n-r)! if n,r>=0 and are whole."));
        textPane.getChildren().add(new Text("\t\t-nCr will accept 2 inputs separated by " +
                "\",\" and is n!/[r! (n-r)!] if n,r>=0 and are whole."));
        textPane.getChildren().add(new Text("\t\t-decimal ⮞ fraction turns decimals to fractions."));
        textPane.getChildren().add(new Text("\t\tAll of these will evaluate first then preform their task."));

        textPane.getChildren().add(new Text("\t-Trig page contains common trig functions, their inverse and" +
                " their hyp"));
        textPane.getChildren().add(new Text("\t\tIm not explaining them they are obvious."));
        textPane.getChildren().add(new Text("\t\tAll of them will evaluate first then preform their task."));

        textPane.getChildren().add(new Text("\t-Geometry page contains common geometry functions"));
        textPane.getChildren().add(new Text("\t\t-Area of square is input squared."));
        textPane.getChildren().add(new Text("\t\t-Area of triangle will accept 2 inputs separated by" +
                " \",\" and is (a*b)/2"));
        textPane.getChildren().add(new Text("\t\t-Area of rectangle will accept 2 inputs separated by" +
                " \",\" and is just a*b"));
        textPane.getChildren().add(new Text("\t\t-Area of trapezoid will accept 3 inputs separated by" +
                " \",\" and is ((b₁+b₂)/2)*h"));
        textPane.getChildren().add(new Text("\t\t-Area of circle is just πr² r=only input."));
        textPane.getChildren().add(new Text("\t\t-Circumference of circle is just 2πr r=only input."));
        textPane.getChildren().add(new Text("\t\t-Volume of cube is input cubed."));
        textPane.getChildren().add(new Text("\t\t-Volume of rectangular solid will accept 3 inputs separated by" +
                " \",\" and is l*w*h"));
        textPane.getChildren().add(new Text("\t\t-Volume of cylinder will accept 2 inputs separated by" +
                " \",\" and is πr²h"));
        textPane.getChildren().add(new Text("\t\t-Slope M will accept 4 inputs separated by" +
                " \",\" and is (y₂-y₁)/(x₂-x₁)"));
        textPane.getChildren().add(new Text("\t\t-Slope intercept form B is the same as as slope M but" +
                " finds y=mx+b B specifically."));
        textPane.getChildren().add(new Text("\t\tAll of these will evaluate first then preform their task."));

        textPane.getChildren().add(new Text("\t-Standard deviation page will accept many inputs separated by" +
                " \",\" and will find the variance."));
        textPane.getChildren().add(new Text("\t\tThe inputs will evaluate first then preform its task."));
        textPane.getChildren().add(new Text("\t-Quadratic page will accept 3 inputs separated by" +
                " \",\" and will find the quadratic"));
        textPane.getChildren().add(new Text("\t\tThe formula is very easy to find."));
        textPane.getChildren().add(new Text("\t\tThe \"a\" variable cannot be 0"));
        textPane.getChildren().add(new Text("\t\tThe inputs will evaluate first then preform its task."));
        textPane.getChildren().add(new Text("\t\tSince this function can return 2 answers it cannot be saved " +
                "to memory."));
        textPane.getChildren().add(new Text("\t-Cubic page will accept 4 inputs separated by" +
                " \",\" and will try to find the cubic"));
        textPane.getChildren().add(new Text("\t\tThe formula is very difficult to find and explain."));
        textPane.getChildren().add(new Text("\t\tDue to the above the formula can only find x1"));
        textPane.getChildren().add(new Text("\t\tThe Inputs will evaluate first then preform its task."));
        textPane.getChildren().add(new Text("\t\tTo keep the function on par with quadratic it cannot be saved " +
                "to memory."));

        textPane.getChildren().add(new Text("\t-Converters will convert between the stated data types"));
        textPane.getChildren().add(new Text("\t\tThe inputs will not evaluate."));

        textPane.getChildren().add(new Text("\t-Discount will accept 2 inputs separated by \",\" and" +
                " is a money based calculator for calculating sales discounts"));
        textPane.getChildren().add(new Text("\t\tThe inputs will not evaluate."));
        textPane.getChildren().add(new Text("\t\tDiscount total shows you the discounted price based on the original price and percent off."));
        textPane.getChildren().add(new Text("\t\tDiscount % shows you the percent off based on the original price, and discounted price."));


        textPane.getChildren().add(new Text("There as secrets to find. Find them all."));

        specialText=new Text("For the sneaky code readers");
        specialText.getStyleClass().add("tryText");
        textPane.getChildren().add(specialText);
        textPane.getChildren().add(new Text("If you wish to add your own functions here's a very basic how to"));
        textPane.getChildren().add(new Text("\t1) add name of function to operation in start()"));
        textPane.getChildren().add(new Text("\t2) make UI method or find an appropriate existing UI and " +
                "make desired changes"));
        textPane.getChildren().add(new Text("\t3) send the UI and info to operations() and link to switches"));
        textPane.getChildren().add(new Text("\t4) create method with function in Evaluator.java"));
        textPane.getChildren().add(new Text("\t5) hook up function name to inputSyntaxCheck() in Evaluator.java"));
        textPane.getChildren().add(new Text("\t6) hook up method in pressEqual()\n"));


        textPane.setPadding(new Insets(5,5,5,5));

        Scene scene=new Scene(pane);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        scene.setOnKeyPressed(event ->{
            if(event.getCode()==KeyCode.ESCAPE)
            {
                //close guide if escape is pressed and the stage is selected
                guideStage.close();
            }
        });
        guideStage.setScene(scene);
        guideStage.setTitle("FX Guide");
        guideStage.getIcons().add(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/guide icon.png"))));
        guideStage.show();
    }

    /**
     * Creates the Default UI
     * @return UI Scene
     */
    public Scene defaultUI()
    {
        //the panes (Extra painful edition)
        VBox textPane=new VBox(5);
        HBox arrows=new HBox(2);
        HBox prePane=new HBox(5);
        StackPane answerPane=new StackPane();
        StackPane helPane=new StackPane();
        BorderPane comboHelpPane=new BorderPane();
        BorderPane calcPane=new BorderPane();
        BorderPane pane=new BorderPane();
        GridPane buttonPane=new GridPane();

        equationText.setAlignment(Pos.CENTER_RIGHT);
        equationText.setDisable(true);
        equationText.setOpacity(1);     //make text not look disabled

        setCssID();

        answerPane.setAlignment(Pos.CENTER_RIGHT);
        answerPane.getChildren().add(answerText);

        helPane.setAlignment(Pos.CENTER_RIGHT);
        helPane.getChildren().add(helpText);

        comboHelpPane.setLeft(operation);
        comboHelpPane.setRight(helPane);

        textPane.getChildren().add(answerPane);
        textPane.getChildren().add(equationText);
        textPane.getChildren().add(comboHelpPane);

        arrows.getChildren().addAll(left,right);
        arrows.setAlignment(Pos.CENTER);

        buttonPane.setHgap(2);
        buttonPane.setVgap(2);

        buttonPane.add(clear,1,0);
        buttonPane.add(remove,2,0);
        buttonPane.add(arrows,3,0);

        buttonPane.add(parL,1,1);
        buttonPane.add(parR,2,1);

        buttonPane.add(pie,0,0);
        buttonPane.add(euler,0,1);
        buttonPane.add(point,2,5);
        buttonPane.add(modulo,0,5);
        buttonPane.add(plus,3,4);
        buttonPane.add(minus,3,3);
        buttonPane.add(divide,3,1);
        buttonPane.add(multiply,3,2);
        buttonPane.add(equal,3,5);

        buttonPane.add(b0,1,5);
        buttonPane.add(b1,0,4);
        buttonPane.add(b2,1,4);
        buttonPane.add(b3,2,4);
        buttonPane.add(b4,0,3);
        buttonPane.add(b5,1,3);
        buttonPane.add(b6,2,3);
        buttonPane.add(b7,0,2);
        buttonPane.add(b8,1,2);
        buttonPane.add(b9,2,2);

        buttonPane.setPadding(new Insets( 5,0,0,0));

        //the main UI maker
        calcPane.setTop(textPane);
        calcPane.setBottom(buttonPane);
        prePane.getChildren().addAll(calcPane,memory.createUI());
        prePane.setPadding(new Insets(5,5,5,5));
        pane.setTop(mainMenu);
        pane.setCenter(prePane);

        Scene scene=new Scene(pane);
        scene.setOnMouseClicked(event -> changeFocus());
        scene.setOnKeyPressed(event -> keyboardControls(event,"press"));
        scene.setOnKeyReleased(event -> keyboardControls(event,"release"));
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        return scene;
    }

    /**
     * Creates Common UI
     * @return UI Scene
     */
    public Scene commonUI()
    {
        //the panes (Extra painful edition)
        VBox textPane=new VBox(5);
        HBox arrows=new HBox(2);
        HBox prePane=new HBox(5);
        StackPane answerPane=new StackPane();
        StackPane helPane=new StackPane();
        BorderPane comboHelpPane=new BorderPane();
        BorderPane calcPane=new BorderPane();
        BorderPane pane=new BorderPane();
        GridPane buttonPane=new GridPane();

        StackPane fHPane=new StackPane();
        StackPane sHPane=new StackPane();
        GridPane groupedText=new GridPane();
        Text firstHalf=new Text("!(");
        Text secondHalf=new Text(")");

        fromThis= new ComboBox<>();
        fromThis.setFocusTraversable(false);
        fromThis.getItems().addAll("Fact! & Gamma","Square²","Cube³","Custom power ^ʸ","e^ᵡ","Square root √"
                ,"Cube root ∛","Custom root ʸ√","Abs ||","Floor ⌊⌋","Ceil ⌈⌉","log","ln"
                ,"Exp","nPr","nCr","decimal ⮞ fraction");

        fromThis.getSelectionModel().selectFirst();
        comma.setDisable(true);

        equationText.setAlignment(Pos.CENTER_RIGHT);
        equationText.setDisable(true);
        equationText.setOpacity(1);     //make text not look disabled
        firstHalf.getStyleClass().add("specialText");
        secondHalf.getStyleClass().add("specialText");

        setCssID();

        answerPane.setAlignment(Pos.CENTER_RIGHT);
        answerPane.getChildren().add(answerText);

        helPane.setAlignment(Pos.CENTER_RIGHT);
        helPane.getChildren().add(helpText);

        comboHelpPane.setLeft(operation);
        comboHelpPane.setRight(helPane);

        fHPane.setAlignment(Pos.CENTER);
        fHPane.getChildren().add(firstHalf);
        sHPane.setAlignment(Pos.CENTER);
        sHPane.getChildren().add(secondHalf);

        groupedText.add(answerPane,1,0);
        groupedText.add(fHPane,0,1);
        groupedText.add(equationText,1,1);
        groupedText.add(sHPane,2,1);
        groupedText.add(comboHelpPane,1,2);
        groupedText.setHgap(2);
        groupedText.setVgap(5);

        textPane.getChildren().add(groupedText);
        textPane.getChildren().add(fromThis);

        arrows.getChildren().addAll(left,right);
        arrows.setAlignment(Pos.CENTER);

        buttonPane.setHgap(2);
        buttonPane.setVgap(2);

        buttonPane.add(comma,0,0);
        buttonPane.add(clear,1,1);
        buttonPane.add(remove,2,1);
        buttonPane.add(arrows,3,1);

        buttonPane.add(parL,1,2);
        buttonPane.add(parR,2,2);

        buttonPane.add(pie,0,1);
        buttonPane.add(euler,0,2);
        buttonPane.add(point,2,6);
        buttonPane.add(modulo,0,6);
        buttonPane.add(plus,3,5);
        buttonPane.add(minus,3,4);
        buttonPane.add(divide,3,2);
        buttonPane.add(multiply,3,3);
        buttonPane.add(equal,3,6);

        buttonPane.add(b0,1,6);
        buttonPane.add(b1,0,5);
        buttonPane.add(b2,1,5);
        buttonPane.add(b3,2,5);
        buttonPane.add(b4,0,4);
        buttonPane.add(b5,1,4);
        buttonPane.add(b6,2,4);
        buttonPane.add(b7,0,3);
        buttonPane.add(b8,1,3);
        buttonPane.add(b9,2,3);

        buttonPane.setPadding(new Insets( 5,0,0,0));

        //the main UI maker
        calcPane.setTop(textPane);
        calcPane.setBottom(buttonPane);
        prePane.getChildren().addAll(calcPane,memory.createUI());
        prePane.setPadding(new Insets(5,5,5,5));
        pane.setTop(mainMenu);
        pane.setCenter(prePane);

        Scene scene=new Scene(pane);

        //change UI and values based on the value of fromThis actions
        fromThis.setOnAction(event -> {
            comma.setDisable(true);
            keyboardType = "math";
            switch (fromThis.getValue()) {
                case "Fact! & Gamma" -> {
                    firstHalf.setText("!(");
                    secondHalf.setText(")");
                    helpText.setText("!(x)");
                }
                case "Square²" -> {
                    firstHalf.setText("(");
                    secondHalf.setText(")²");
                    helpText.setText("x²");
                }
                case "Cube³" -> {
                    firstHalf.setText("(");
                    secondHalf.setText(")³");
                    helpText.setText("x³");
                }
                case "Custom power ^ʸ" -> {
                    keyboardType = "custom^";
                    firstHalf.setText("(");
                    secondHalf.setText(")^ʸ");
                    helpText.setText("x^ʸ");
                }
                case "e^ᵡ" -> {
                    firstHalf.setText("e^(");
                    secondHalf.setText(")");
                    helpText.setText("e^ᵡ");
                }
                case "Square root √" -> {
                    firstHalf.setText("√(");
                    secondHalf.setText(")");
                    helpText.setText("√x");
                }
                case "Cube root ∛" -> {
                    firstHalf.setText("∛(");
                    secondHalf.setText(")");
                    helpText.setText("∛x");
                }
                case "Custom root ʸ√" -> {
                    keyboardType = "custom,";
                    firstHalf.setText("ʸ√(");
                    secondHalf.setText(")");
                    helpText.setText("(y,x)= ʸ√x");
                    comma.setDisable(false);
                }
                case "Abs ||" -> {
                    firstHalf.setText("|");
                    secondHalf.setText("|");
                    helpText.setText("|x|");
                }
                case "Ceil ⌈⌉" -> {
                    firstHalf.setText("⌈");
                    secondHalf.setText("⌉");
                    helpText.setText("⌈x⌉");
                }
                case "Floor ⌊⌋" -> {
                    firstHalf.setText("⌊");
                    secondHalf.setText("⌋");
                    helpText.setText("⌊x⌋");
                }
                case "log" -> {
                    firstHalf.setText("log(");
                    secondHalf.setText(")");
                    helpText.setText("log(x)");
                }
                case "ln" -> {
                    firstHalf.setText("ln(");
                    secondHalf.setText(")");
                    helpText.setText("ln(x)");
                }
                case "Exp" -> {
                    keyboardType = "custom,";
                    firstHalf.setText("(");
                    secondHalf.setText(")");
                    helpText.setText("(x,y)= x EXP y");
                    comma.setDisable(false);
                }
                case "nPr" -> {
                    keyboardType = "custom,";
                    firstHalf.setText("(");
                    secondHalf.setText(")");
                    helpText.setText("(x,y)= x nPr y");
                    comma.setDisable(false);
                }
                case "nCr" -> {
                    keyboardType = "custom,";
                    firstHalf.setText("(");
                    secondHalf.setText(")");
                    helpText.setText("(x,y)= x nCr y");
                    comma.setDisable(false);
                }
                case "decimal ⮞ fraction"->{
                    firstHalf.setText("(");
                    secondHalf.setText(")");
                    helpText.setText("0.5 ⮞ 1/2");
                }
            }
            capitalizeButtons();
        });
        scene.setOnMouseClicked(event -> changeFocus());
        scene.setOnKeyPressed(event -> keyboardControls(event,"press"));
        scene.setOnKeyReleased(event -> keyboardControls(event,"release"));
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        return scene;
    }

    /**
     * Creates Trig UI
     * @return UI Scene
     */
    public Scene trigUI()
    {
        //the panes (Extra painful edition)
        VBox textPane=new VBox(5);
        HBox arrows=new HBox(2);
        HBox comboDegPane=new HBox(2);
        HBox prePane=new HBox(5);
        StackPane answerPane=new StackPane();
        StackPane helPane=new StackPane();
        BorderPane comboHelpPane=new BorderPane();
        BorderPane calcPane=new BorderPane();
        BorderPane pane=new BorderPane();
        GridPane buttonPane=new GridPane();

        StackPane fHPane=new StackPane();
        StackPane sHPane=new StackPane();
        GridPane groupedText=new GridPane();
        Text firstHalf=new Text("Sin(");
        Text secondHalf=new Text(")");

        fromThis= new ComboBox<>();
        fromThis.setFocusTraversable(false);
        fromThis.getItems().addAll("Sin","Sin¯¹","Sinh","Sinh¯¹","Csc","Csc¯¹","Csch","Csch¯¹","Cos",
                "Cos¯¹","Cosh", "Cosh¯¹","Sec","Sec¯¹","Sech","Sech¯¹","Tan","Tan¯¹","Tanh","Tanh¯¹","Cot",
                "Cot¯¹","Coth","Coth¯¹");
        fromThis.getSelectionModel().selectFirst();

        equationText.setAlignment(Pos.CENTER_RIGHT);
        equationText.setDisable(true);
        equationText.setOpacity(1);     //make text not look disabled
        firstHalf.getStyleClass().add("specialText");
        secondHalf.getStyleClass().add("specialText");

        setCssID();

        answerPane.setAlignment(Pos.CENTER_RIGHT);
        answerPane.getChildren().add(answerText);

        helPane.setAlignment(Pos.CENTER_RIGHT);
        helPane.getChildren().add(helpText);

        comboHelpPane.setLeft(operation);
        comboHelpPane.setRight(helPane);

        fHPane.setAlignment(Pos.CENTER);
        fHPane.getChildren().add(firstHalf);
        sHPane.setAlignment(Pos.CENTER);
        sHPane.getChildren().add(secondHalf);

        groupedText.add(answerPane,1,0);
        groupedText.add(fHPane,0,1);
        groupedText.add(equationText,1,1);
        groupedText.add(sHPane,2,1);
        groupedText.add(comboHelpPane,1,2);
        groupedText.setHgap(2);
        groupedText.setVgap(5);

        comboDegPane.getChildren().addAll(rDG,fromThis);
        textPane.getChildren().add(groupedText);
        textPane.getChildren().add(comboDegPane);

        arrows.getChildren().addAll(left,right);
        arrows.setAlignment(Pos.CENTER);

        buttonPane.setHgap(2);
        buttonPane.setVgap(2);

        buttonPane.add(clear,1,0);
        buttonPane.add(remove,2,0);
        buttonPane.add(arrows,3,0);

        buttonPane.add(parL,1,1);
        buttonPane.add(parR,2,1);

        buttonPane.add(pie,0,0);
        buttonPane.add(euler,0,1);
        buttonPane.add(point,2,5);
        buttonPane.add(modulo,0,5);
        buttonPane.add(plus,3,4);
        buttonPane.add(minus,3,3);
        buttonPane.add(divide,3,1);
        buttonPane.add(multiply,3,2);
        buttonPane.add(equal,3,5);

        buttonPane.add(b0,1,5);
        buttonPane.add(b1,0,4);
        buttonPane.add(b2,1,4);
        buttonPane.add(b3,2,4);
        buttonPane.add(b4,0,3);
        buttonPane.add(b5,1,3);
        buttonPane.add(b6,2,3);
        buttonPane.add(b7,0,2);
        buttonPane.add(b8,1,2);
        buttonPane.add(b9,2,2);

        buttonPane.setPadding(new Insets( 5,0,0,0));

        //the main UI maker
        calcPane.setTop(textPane);
        calcPane.setBottom(buttonPane);
        prePane.getChildren().addAll(calcPane,memory.createUI());
        prePane.setPadding(new Insets(5,5,5,5));
        pane.setTop(mainMenu);
        pane.setCenter(prePane);

        Scene scene=new Scene(pane);

        //change UI and values based on the value of fromThis actions
        fromThis.setOnAction(event ->
        {
            switch (fromThis.getValue()) {
                case "Sin" -> {
                    firstHalf.setText("Sin(");
                    helpText.setText("Sin(x)");
                }
                case "Sin¯¹" -> {
                    firstHalf.setText("Sin¯¹(");
                    helpText.setText("Sin¯¹(x)");
                }
                case "Sinh" -> {
                    firstHalf.setText("Sinh(");
                    helpText.setText("Sinh(x)");
                }
                case "Sinh¯¹" -> {
                    firstHalf.setText("Sinh¯¹(");
                    helpText.setText("Sinh¯¹(x)");
                }
                case "Csc" -> {
                    firstHalf.setText("Csc(");
                    helpText.setText("Csc(x)");
                }
                case "Csc¯¹" -> {
                    firstHalf.setText("Csc¯¹(");
                    helpText.setText("Csc¯¹(x)");
                }
                case "Csch" -> {
                    firstHalf.setText("Csch(");
                    helpText.setText("Csch(x)");
                }
                case "Csch¯¹" -> {
                    firstHalf.setText("Csch¯¹(");
                    helpText.setText("Csch¯¹(x)");
                }
                case "Cos" -> {
                    firstHalf.setText("Cos(");
                    helpText.setText("Cos(x)");
                }
                case "Cos¯¹" -> {
                    firstHalf.setText("Cos¯¹(");
                    helpText.setText("Cos¯¹(x)");
                }
                case "Cosh" -> {
                    firstHalf.setText("Cosh(");
                    helpText.setText("Cosh(x)");
                }
                case "Cosh¯¹" -> {
                    firstHalf.setText("Cosh¯¹(");
                    helpText.setText("Cosh¯¹(x)");
                }
                case "Sec" -> {
                    firstHalf.setText("Sec(");
                    helpText.setText("Sec(x)");
                }
                case "Sec¯¹" -> {
                    firstHalf.setText("Sec¯¹(");
                    helpText.setText("Sec¯¹(x)");
                }
                case "Sech" -> {
                    firstHalf.setText("Sech(");
                    helpText.setText("Sech(x)");
                }
                case "Sech¯¹" -> {
                    firstHalf.setText("Sech¯¹(");
                    helpText.setText("Sech¯¹(x)");
                }
                case "Tan" -> {
                    firstHalf.setText("Tan(");
                    helpText.setText("Tan(x)");
                }
                case "Tan¯¹" -> {
                    firstHalf.setText("Tan¯¹(");
                    helpText.setText("Tan¯¹(x)");
                }
                case "Tanh" -> {
                    firstHalf.setText("Tanh(");
                    helpText.setText("Tanh(x)");
                }
                case "Tanh¯¹" -> {
                    firstHalf.setText("Tanh¯¹(");
                    helpText.setText("Tanh¯¹(x)");
                }
                case "Cot"->{
                    firstHalf.setText("Cot(");
                    helpText.setText("Cot(x)");
                }
                case "Cot¯¹"->{
                    firstHalf.setText("Cot¯¹(");
                    helpText.setText("Cot¯¹(x)");
                }
                case "Coth"->{
                    firstHalf.setText("Coth(");
                    helpText.setText("Coth(x)");
                }
                case "Coth¯¹"->{
                    firstHalf.setText("Coth¯¹(");
                    helpText.setText("Coth¯¹(x)");
                }
            }
            capitalizeButtons();
        });
        scene.setOnMouseClicked(event -> changeFocus());
        scene.setOnKeyPressed(event -> keyboardControls(event,"press"));
        scene.setOnKeyReleased(event -> keyboardControls(event,"release"));
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        return scene;
    }

    /**
     * Creates Geometry UI
     * @return UI Scene
     */
    public Scene geometryUI()
    {
        //the panes
        VBox textPane=new VBox(5);
        HBox arrows=new HBox(2);
        HBox prePane=new HBox(5);
        StackPane answerPane=new StackPane();
        StackPane helPane=new StackPane();
        BorderPane comboHelpPane=new BorderPane();
        BorderPane calcPane=new BorderPane();
        BorderPane pane=new BorderPane();
        GridPane buttonPane=new GridPane();

        equationText.setAlignment(Pos.CENTER_RIGHT);
        equationText.setDisable(true);
        equationText.setOpacity(1);     //make text not look disabled

        fromThis= new ComboBox<>();
        fromThis.setFocusTraversable(false);
        fromThis.getItems().addAll("Area of square","Area of triangle","Area of rectangle",
                "Area of trapezoid","Area of circle", "Circumference of circle",
                "Volume of cube","Volume of rectangular solid","Volume of cylinder","Slope M",
                "Slope intercept form B");
        fromThis.getSelectionModel().selectFirst();
        comma.setDisable(true);

        setCssID();

        answerPane.setAlignment(Pos.CENTER_RIGHT);
        answerPane.getChildren().add(answerText);

        helPane.setAlignment(Pos.CENTER_RIGHT);
        helPane.getChildren().add(helpText);

        comboHelpPane.setLeft(operation);
        comboHelpPane.setRight(helPane);

        textPane.getChildren().add(answerPane);
        textPane.getChildren().add(equationText);
        textPane.getChildren().add(comboHelpPane);
        textPane.getChildren().add(fromThis);

        arrows.getChildren().addAll(left,right);
        arrows.setAlignment(Pos.CENTER);

        buttonPane.setHgap(2);
        buttonPane.setVgap(2);

        buttonPane.add(comma,0,0);
        buttonPane.add(clear,1,1);
        buttonPane.add(remove,2,1);
        buttonPane.add(arrows,3,1);

        buttonPane.add(parL,1,2);
        buttonPane.add(parR,2,2);

        buttonPane.add(pie,0,1);
        buttonPane.add(euler,0,2);
        buttonPane.add(point,2,6);
        buttonPane.add(modulo,0,6);
        buttonPane.add(plus,3,5);
        buttonPane.add(minus,3,4);
        buttonPane.add(divide,3,2);
        buttonPane.add(multiply,3,3);
        buttonPane.add(equal,3,6);

        buttonPane.add(b0,1,6);
        buttonPane.add(b1,0,5);
        buttonPane.add(b2,1,5);
        buttonPane.add(b3,2,5);
        buttonPane.add(b4,0,4);
        buttonPane.add(b5,1,4);
        buttonPane.add(b6,2,4);
        buttonPane.add(b7,0,3);
        buttonPane.add(b8,1,3);
        buttonPane.add(b9,2,3);

        buttonPane.setPadding(new Insets( 5,0,0,0));

        //the main UI maker
        calcPane.setTop(textPane);
        calcPane.setBottom(buttonPane);
        prePane.getChildren().addAll(calcPane,memory.createUI());
        prePane.setPadding(new Insets(5,5,5,5));
        pane.setTop(mainMenu);
        pane.setCenter(prePane);

        Scene scene=new Scene(pane);

        //change UI and values based on the value of fromThis actions
        fromThis.setOnAction(event -> {
            comma.setDisable(true);
            keyboardType = "math";
            switch (fromThis.getValue())
            {
                case "Area of square" -> {
                    helpText.setText("s²");
                }
                case "Area of triangle", "Area of rectangle" -> {
                    keyboardType = "custom,";
                    helpText.setText("a,b");
                    comma.setDisable(false);
                }
                case "Area of trapezoid" -> {
                    keyboardType = "custom,";
                    helpText.setText("b₁,b₂,h");
                    comma.setDisable(false);
                }
                case "Area of circle" -> {
                    helpText.setText("πr²");
                }
                case "Circumference of circle" -> {
                    helpText.setText("2πr");
                }
                case "Volume of cube" -> {
                    helpText.setText("s³");
                }
                case "Volume of rectangular solid" -> {
                    keyboardType = "custom,";
                    helpText.setText("l,w,h");
                    comma.setDisable(false);
                }
                case "Volume of cylinder" -> {
                    keyboardType = "custom,";
                    helpText.setText("r,h");
                    comma.setDisable(false);
                }
                case "Slope M", "Slope intercept form B" -> {
                    keyboardType = "custom,";
                    helpText.setText("y₂,y₁,x₂,x₁");
                    comma.setDisable(false);
                }
            }
            capitalizeButtons();
        });
        scene.setOnMouseClicked(event -> changeFocus());
        scene.setOnKeyPressed(event -> keyboardControls(event,"press"));
        scene.setOnKeyReleased(event -> keyboardControls(event,"release"));
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        return scene;
    }

    /**
     * Creates the comma UI for anything that needs the default and comma
     * @return UI Scene
     */
    public Scene commaUI()
    {
        //the panes
        VBox textPane=new VBox(5);
        HBox arrows=new HBox(2);
        HBox prePane=new HBox(5);
        StackPane answerPane=new StackPane();
        StackPane helPane=new StackPane();
        BorderPane comboHelpPane=new BorderPane();
        BorderPane calcPane=new BorderPane();
        BorderPane pane=new BorderPane();
        GridPane buttonPane=new GridPane();

        equationText.setAlignment(Pos.CENTER_RIGHT);
        equationText.setDisable(true);
        equationText.setOpacity(1);     //make text not look disabled

        setCssID();

        answerPane.setAlignment(Pos.CENTER_RIGHT);
        answerPane.getChildren().add(answerText);

        helPane.setAlignment(Pos.CENTER_RIGHT);
        helPane.getChildren().add(helpText);

        comboHelpPane.setLeft(operation);
        comboHelpPane.setRight(helPane);

        textPane.getChildren().add(answerPane);
        textPane.getChildren().add(equationText);
        textPane.getChildren().add(comboHelpPane);

        arrows.getChildren().addAll(left,right);
        arrows.setAlignment(Pos.CENTER);

        buttonPane.setHgap(2);
        buttonPane.setVgap(2);

        buttonPane.add(comma,0,0);
        buttonPane.add(clear,1,1);
        buttonPane.add(remove,2,1);
        buttonPane.add(arrows,3,1);

        buttonPane.add(parL,1,2);
        buttonPane.add(parR,2,2);

        buttonPane.add(pie,0,1);
        buttonPane.add(euler,0,2);
        buttonPane.add(point,2,6);
        buttonPane.add(modulo,0,6);
        buttonPane.add(plus,3,5);
        buttonPane.add(minus,3,4);
        buttonPane.add(divide,3,2);
        buttonPane.add(multiply,3,3);
        buttonPane.add(equal,3,6);

        buttonPane.add(b0,1,6);
        buttonPane.add(b1,0,5);
        buttonPane.add(b2,1,5);
        buttonPane.add(b3,2,5);
        buttonPane.add(b4,0,4);
        buttonPane.add(b5,1,4);
        buttonPane.add(b6,2,4);
        buttonPane.add(b7,0,3);
        buttonPane.add(b8,1,3);
        buttonPane.add(b9,2,3);

        buttonPane.setPadding(new Insets( 5,0,0,0));

        //the main UI maker
        calcPane.setTop(textPane);
        calcPane.setBottom(buttonPane);
        prePane.getChildren().addAll(calcPane,memory.createUI());
        prePane.setPadding(new Insets(5,5,5,5));
        pane.setTop(mainMenu);
        pane.setCenter(prePane);

        Scene scene=new Scene(pane);
        scene.setOnMouseClicked(event -> changeFocus());
        scene.setOnKeyPressed(event -> keyboardControls(event,"press"));
        scene.setOnKeyReleased(event -> keyboardControls(event,"release"));
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        return scene;
    }

    /**
     * Creates the programmer converter UI
     * @return UI Scene
     */
    public Scene programmerUI()
    {
        //the panes
        VBox textPane=new VBox(5);
        HBox arrows=new HBox(2);
        HBox prePane=new HBox(5);
        HBox fromToPane=new HBox(5);
        StackPane answerPane=new StackPane();
        StackPane helPane=new StackPane();
        BorderPane comboHelpPane=new BorderPane();
        BorderPane calcPane=new BorderPane();
        BorderPane pane=new BorderPane();
        GridPane buttonPane=new GridPane();

        //the default UI look as 'Dec' will always be the first active
        fromThis= new ComboBox<>();
        toThis= new ComboBox<>();
        fromThis.setFocusTraversable(false);
        toThis.setFocusTraversable(false);
        fromThis.getItems().addAll("Dec","Bin","Hex","Oct");
        toThis.getItems().addAll("Dec","Bin","Hex","Oct");
        fromThis.getSelectionModel().selectFirst();
        toThis.getSelectionModel().select(1);
        pie.setDisable(true);
        parL.setDisable(true);
        parR.setDisable(true);
        divide.setDisable(true);
        multiply.setDisable(true);
        plus.setDisable(true);
        modulo.setDisable(true);
        point.setDisable(true);
        bA.setDisable(true);
        bB.setDisable(true);
        bC.setDisable(true);
        bD.setDisable(true);
        euler.setDisable(true);
        bF.setDisable(true);

        equationText.setAlignment(Pos.CENTER_RIGHT);
        equationText.setDisable(true);
        equationText.setOpacity(1);     //make text not look disabled

        setCssID();

        answerPane.setAlignment(Pos.CENTER_RIGHT);
        answerPane.getChildren().add(answerText);

        helPane.setAlignment(Pos.CENTER_RIGHT);
        helPane.getChildren().add(helpText);

        comboHelpPane.setLeft(operation);
        comboHelpPane.setRight(helPane);

        fromToPane.getChildren().add(new Text("from"));
        fromToPane.getChildren().add(fromThis);
        fromToPane.getChildren().add(new Text("to"));
        fromToPane.getChildren().add(toThis);

        textPane.getChildren().add(answerPane);
        textPane.getChildren().add(equationText);
        textPane.getChildren().add(comboHelpPane);
        textPane.getChildren().add(fromToPane);

        arrows.getChildren().addAll(left,right);
        arrows.setAlignment(Pos.CENTER);

        buttonPane.setHgap(2);
        buttonPane.setVgap(2);

        buttonPane.add(bA,0,0);
        buttonPane.add(bB,0,1);
        buttonPane.add(bC,0,2);
        buttonPane.add(bD,0,3);
        buttonPane.add(bF,0,5);

        buttonPane.add(clear,2,0);
        buttonPane.add(remove,3,0);
        buttonPane.add(arrows,4,0);

        buttonPane.add(parL,2,1);
        buttonPane.add(parR,3,1);

        buttonPane.add(pie,1,1);
        buttonPane.add(euler,0,4);
        buttonPane.add(point,3,5);
        buttonPane.add(modulo,1,5);
        buttonPane.add(plus,4,4);
        buttonPane.add(minus,4,3);
        buttonPane.add(divide,4,1);
        buttonPane.add(multiply,4,2);
        buttonPane.add(equal,4,5);

        buttonPane.add(b0,2,5);
        buttonPane.add(b1,1,4);
        buttonPane.add(b2,2,4);
        buttonPane.add(b3,3,4);
        buttonPane.add(b4,1,3);
        buttonPane.add(b5,2,3);
        buttonPane.add(b6,3,3);
        buttonPane.add(b7,1,2);
        buttonPane.add(b8,2,2);
        buttonPane.add(b9,3,2);

        buttonPane.setPadding(new Insets( 5,0,0,0));

        //the main UI maker
        calcPane.setTop(textPane);
        calcPane.setBottom(buttonPane);
        prePane.getChildren().addAll(calcPane,memory.createUI());
        prePane.setPadding(new Insets(5,5,5,5));
        pane.setTop(mainMenu);
        pane.setCenter(prePane);

        Scene scene=new Scene(pane);

        //change UI and values based on the value of fromThis actions
        fromThis.setOnAction(event -> {
            switch (fromThis.getValue()) {
                case "Dec" -> {
                    keyboardType = "dec";
                    bA.setDisable(true);
                    bB.setDisable(true);
                    bC.setDisable(true);
                    bD.setDisable(true);
                    euler.setDisable(true);
                    bF.setDisable(true);
                    b2.setDisable(false);
                    b3.setDisable(false);
                    b4.setDisable(false);
                    b5.setDisable(false);
                    b6.setDisable(false);
                    b7.setDisable(false);
                    b8.setDisable(false);
                    b9.setDisable(false);
                    minus.setDisable(false);
                }
                case "Bin" -> {
                    keyboardType = "binary";
                    bA.setDisable(true);
                    bB.setDisable(true);
                    bC.setDisable(true);
                    bD.setDisable(true);
                    euler.setDisable(true);
                    bF.setDisable(true);
                    b2.setDisable(true);
                    b3.setDisable(true);
                    b4.setDisable(true);
                    b5.setDisable(true);
                    b6.setDisable(true);
                    b7.setDisable(true);
                    b8.setDisable(true);
                    b9.setDisable(true);
                    minus.setDisable(true);
                }
                case "Hex" -> {
                    keyboardType = "hex";
                    bA.setDisable(false);
                    bB.setDisable(false);
                    bC.setDisable(false);
                    bD.setDisable(false);
                    euler.setDisable(false);
                    bF.setDisable(false);
                    b2.setDisable(false);
                    b3.setDisable(false);
                    b4.setDisable(false);
                    b5.setDisable(false);
                    b6.setDisable(false);
                    b7.setDisable(false);
                    b8.setDisable(false);
                    b9.setDisable(false);
                    minus.setDisable(true);
                }
                case "Oct" -> {
                    keyboardType = "octal";
                    bA.setDisable(true);
                    bB.setDisable(true);
                    bC.setDisable(true);
                    bD.setDisable(true);
                    euler.setDisable(true);
                    bF.setDisable(true);
                    b8.setDisable(true);
                    b9.setDisable(true);
                    minus.setDisable(true);
                }
            }
            capitalizeButtons();
        });

        scene.setOnMouseClicked(event -> changeFocus());
        scene.setOnKeyPressed(event -> keyboardControls(event,"press"));
        scene.setOnKeyReleased(event -> keyboardControls(event,"release"));
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        return scene;
    }

    /**
     * Creates ascii UI aka an entire keyboard
     * @return UI Scene
     */
    public Scene asciiUI()
    {
        VBox textPane=new VBox(5);
        VBox buttonPane=new VBox(2);
        HBox arrows=new HBox(2);
        HBox row1=new HBox(2);
        HBox row2=new HBox(2);
        HBox row3=new HBox(2);
        HBox row4=new HBox(2);
        HBox row5=new HBox(2);
        HBox prePane=new HBox(5);
        StackPane answerPane=new StackPane();
        StackPane helPane=new StackPane();
        BorderPane comboHelpPane=new BorderPane();
        BorderPane calcPane=new BorderPane();
        BorderPane pane=new BorderPane();

        fromThis= new ComboBox<>();
        fromThis.setFocusTraversable(false);
        fromThis.getItems().addAll("To ascii","From ascii","To ascii combined");
        fromThis.getSelectionModel().selectFirst();

        equationText.setAlignment(Pos.CENTER_RIGHT);
        equationText.setDisable(true);
        equationText.setOpacity(1);     //make text not look disabled

        //Makes these more keyboard like
        clear.setText("clear");
        equal.setText("Enter");
        remove.setText("\uD83D\uDD19");

        setCssID();

        answerPane.setAlignment(Pos.CENTER_RIGHT);
        answerPane.getChildren().add(answerText);

        helPane.setAlignment(Pos.CENTER_RIGHT);
        helPane.getChildren().add(helpText);

        comboHelpPane.setLeft(operation);
        comboHelpPane.setRight(helPane);

        textPane.getChildren().add(answerPane);
        textPane.getChildren().add(equationText);
        textPane.getChildren().add(comboHelpPane);
        textPane.getChildren().add(fromThis);

        arrows.getChildren().addAll(left,right);
        arrows.setAlignment(Pos.CENTER);

        row1.getChildren().add(backQuote);
        row1.getChildren().add(b1);
        row1.getChildren().add(b2);
        row1.getChildren().add(b3);
        row1.getChildren().add(b4);
        row1.getChildren().add(b5);
        row1.getChildren().add(b6);
        row1.getChildren().add(b7);
        row1.getChildren().add(b8);
        row1.getChildren().add(b9);
        row1.getChildren().add(b0);
        row1.getChildren().add(minus);
        row1.getChildren().add(plus);
        row1.getChildren().add(remove);

        row2.getChildren().add(bQ);
        row2.getChildren().add(bW);
        row2.getChildren().add(euler);
        row2.getChildren().add(bR);
        row2.getChildren().add(bT);
        row2.getChildren().add(bY);
        row2.getChildren().add(bU);
        row2.getChildren().add(bI);
        row2.getChildren().add(bO);
        row2.getChildren().add(bP);
        row2.getChildren().add(bracketL);
        row2.getChildren().add(bracketR);
        row2.getChildren().add(backSlash);
        row2.setPadding(new Insets( 0,0,0,90));

        row3.getChildren().add(bA);
        row3.getChildren().add(bS);
        row3.getChildren().add(bD);
        row3.getChildren().add(bF);
        row3.getChildren().add(bG);
        row3.getChildren().add(bH);
        row3.getChildren().add(bJ);
        row3.getChildren().add(bK);
        row3.getChildren().add(bL);
        row3.getChildren().add(semiColon);
        row3.getChildren().add(quote);
        row3.getChildren().add(equal);
        row3.setPadding(new Insets( 0,0,0,110));

        row4.getChildren().add(bZ);
        row4.getChildren().add(bX);
        row4.getChildren().add(bC);
        row4.getChildren().add(bV);
        row4.getChildren().add(bB);
        row4.getChildren().add(bN);
        row4.getChildren().add(bM);
        row4.getChildren().add(comma);
        row4.getChildren().add(point);
        row4.getChildren().add(divide);
        row4.setPadding(new Insets( 0,0,0,150));

        row5.getChildren().addAll(clear,space,arrows);
        row5.setPadding(new Insets( 0,0,0,182));

        buttonPane.getChildren().addAll(row1,row2,row3,row4,row5);

        buttonPane.setPadding(new Insets( 5,0,0,0));

        //the main UI maker
        calcPane.setTop(textPane);
        calcPane.setBottom(buttonPane);
        prePane.getChildren().addAll(calcPane,memory.createUI());
        prePane.setPadding(new Insets(5,5,5,5));
        pane.setTop(mainMenu);
        pane.setCenter(prePane);

        //change UI and values based on the value of fromThis actions
        fromThis.setOnAction(event -> {
            keyboardType = "all";
            enable();
            switch (fromThis.getValue())
            {
                case "To ascii":
                helpText.setText("hi ⮞ 104 105");
                    break;
                case "From ascii":
                    helpText.setText("104 105⮞ hi");
                    keyboardType="asciiNum";
                    backQuote.setDisable(true);
                    minus.setDisable(true);
                    plus.setDisable(true);
                    bQ.setDisable(true);
                    bW.setDisable(true);
                    euler.setDisable(true);
                    bR.setDisable(true);
                    bT.setDisable(true);
                    bY.setDisable(true);
                    bU.setDisable(true);
                    bI.setDisable(true);
                    bO.setDisable(true);
                    bP.setDisable(true);
                    bracketL.setDisable(true);
                    bracketR.setDisable(true);
                    backSlash.setDisable(true);
                    bA.setDisable(true);
                    bS.setDisable(true);
                    bD.setDisable(true);
                    bF.setDisable(true);
                    bG.setDisable(true);
                    bH.setDisable(true);
                    bJ.setDisable(true);
                    bK.setDisable(true);
                    bL.setDisable(true);
                    semiColon.setDisable(true);
                    quote.setDisable(true);
                    bZ.setDisable(true);
                    bX.setDisable(true);
                    bC.setDisable(true);
                    bV.setDisable(true);
                    bB.setDisable(true);
                    bN.setDisable(true);
                    bM.setDisable(true);
                    comma.setDisable(true);
                    point.setDisable(true);
                    divide.setDisable(true);
                    break;
                case "To ascii combined":
                    helpText.setText("hi ⮞ 209 one way only");
                    break;
            }
            capitalizeButtons();
        });

        Scene scene=new Scene(pane);
        scene.setOnMouseClicked(event -> changeFocus());
        scene.setOnKeyPressed(event -> keyboardControls(event,"press"));
        scene.setOnKeyReleased(event -> keyboardControls(event,"release"));
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        return scene;
    }

    /**
     * Creates the data converter UI
     * @return UI Scene
     */
    public Scene converterUI()
    {
        VBox textPane=new VBox(5);
        HBox arrows=new HBox(2);
        HBox prePane=new HBox(5);
        HBox fromToPane=new HBox(5);
        StackPane answerPane=new StackPane();
        StackPane helPane=new StackPane();
        BorderPane comboHelpPane=new BorderPane();
        BorderPane calcPane=new BorderPane();
        BorderPane pane=new BorderPane();
        GridPane buttonPane=new GridPane();

        //from and to will get their input based on what operations current value is
        fromThis= new ComboBox<>();
        toThis= new ComboBox<>();
        fromThis.setFocusTraversable(false);
        toThis.setFocusTraversable(false);
        minus.setDisable(true);
        switch (operation.getValue())
        {
            case "Data converter 2¹⁰": case "Data converter 10³":
                fromThis.getItems().addAll("Bit","Byte","Kilobyte","Megabyte","Gigabyte"
                        ,"Terabyte","Petabyte","Exabyte","Zettabyte","Yottabyte");
                toThis.getItems().addAll("Bit","Byte","Kilobyte","Megabyte","Gigabyte"
                        ,"Terabyte","Petabyte","Exabyte","Zettabyte","Yottabyte");
                break;
            case "Temp converter":
                fromThis.getItems().addAll("Celsius","Fahrenheit","Kelvin");
                toThis.getItems().addAll("Celsius","Fahrenheit","Kelvin");
                minus.setDisable(false);
                break;
            case "Time converter":
                fromThis.getItems().addAll("Seconds","Minutes","Hours","Days","Weeks","Months","Years");
                toThis.getItems().addAll("Seconds","Minutes","Hours","Days","Weeks","Months","Years");
                break;
            case "Angle converter":
                fromThis.getItems().addAll("Degree","Radian","Gradian");
                toThis.getItems().addAll("Degree","Radian","Gradian");
                minus.setDisable(false);
                break;
            case "Frequency converter":
                fromThis.getItems().addAll("Hertz","Kilohertz","Megahertz","Gigahertz");
                toThis.getItems().addAll("Hertz","Kilohertz","Megahertz","Gigahertz");
                break;
        }
        fromThis.getSelectionModel().selectFirst();
        toThis.getSelectionModel().select(1);

        equationText.setAlignment(Pos.CENTER_RIGHT);
        equationText.setDisable(true);
        equationText.setOpacity(1);     //make text not look disabled

        setCssID();

        answerPane.setAlignment(Pos.CENTER_RIGHT);
        answerPane.getChildren().add(answerText);

        helPane.setAlignment(Pos.CENTER_RIGHT);
        helPane.getChildren().add(helpText);

        comboHelpPane.setLeft(operation);
        comboHelpPane.setRight(helPane);

        fromToPane.getChildren().add(new Text("from"));
        fromToPane.getChildren().add(fromThis);
        fromToPane.getChildren().add(new Text("to"));
        fromToPane.getChildren().add(toThis);

        textPane.getChildren().add(answerPane);
        textPane.getChildren().add(equationText);
        textPane.getChildren().add(comboHelpPane);
        textPane.getChildren().add(fromToPane);

        arrows.getChildren().addAll(left,right);
        arrows.setAlignment(Pos.CENTER);

        buttonPane.setHgap(2);
        buttonPane.setVgap(2);

        buttonPane.add(minus,0,0);
        buttonPane.add(clear,0,1);
        buttonPane.add(remove,1,1);
        buttonPane.add(arrows,2,1);

        buttonPane.add(point,0,5);
        buttonPane.add(equal,2,5);

        buttonPane.add(b0,1,5);
        buttonPane.add(b1,0,4);
        buttonPane.add(b2,1,4);
        buttonPane.add(b3,2,4);
        buttonPane.add(b4,0,3);
        buttonPane.add(b5,1,3);
        buttonPane.add(b6,2,3);
        buttonPane.add(b7,0,2);
        buttonPane.add(b8,1,2);
        buttonPane.add(b9,2,2);

        buttonPane.setPadding(new Insets( 5,0,0,0));

        //the main UI maker
        calcPane.setTop(textPane);
        calcPane.setBottom(buttonPane);
        prePane.getChildren().addAll(calcPane,memory.createUI());
        prePane.setPadding(new Insets(5,5,5,5));
        pane.setTop(mainMenu);
        pane.setCenter(prePane);

        Scene scene=new Scene(pane);
        scene.setOnMouseClicked(event -> changeFocus());
        scene.setOnKeyPressed(event -> keyboardControls(event,"press"));
        scene.setOnKeyReleased(event -> keyboardControls(event,"release"));
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        return scene;
    }

    /**
     * Creates the percent calculating UI
     * @return UI Scene
     */
    public Scene percentUI()
    {
        VBox textPane=new VBox(5);
        HBox arrows=new HBox(2);
        HBox prePane=new HBox(5);
        StackPane answerPane=new StackPane();
        StackPane helPane=new StackPane();
        BorderPane comboHelpPane=new BorderPane();
        BorderPane calcPane=new BorderPane();
        BorderPane pane=new BorderPane();
        GridPane buttonPane=new GridPane();

        fromThis= new ComboBox<>();
        fromThis.setFocusTraversable(false);

        fromThis.getItems().addAll("Discount total","Discount %");

        fromThis.getSelectionModel().selectFirst();

        equationText.setAlignment(Pos.CENTER_RIGHT);
        equationText.setDisable(true);
        equationText.setOpacity(1);     //make text not look disabled

        setCssID();

        answerPane.setAlignment(Pos.CENTER_RIGHT);
        answerPane.getChildren().add(answerText);

        helPane.setAlignment(Pos.CENTER_RIGHT);
        helPane.getChildren().add(helpText);

        comboHelpPane.setLeft(operation);
        comboHelpPane.setRight(helPane);

        textPane.getChildren().add(answerPane);
        textPane.getChildren().add(equationText);
        textPane.getChildren().add(comboHelpPane);
        textPane.getChildren().add(fromThis);

        arrows.getChildren().addAll(left,right);
        arrows.setAlignment(Pos.CENTER);

        buttonPane.setHgap(2);
        buttonPane.setVgap(2);

        buttonPane.add(comma,0,0);
        buttonPane.add(clear,0,1);
        buttonPane.add(remove,1,1);
        buttonPane.add(arrows,2,1);

        buttonPane.add(point,0,5);
        buttonPane.add(equal,2,5);

        buttonPane.add(b0,1,5);
        buttonPane.add(b1,0,4);
        buttonPane.add(b2,1,4);
        buttonPane.add(b3,2,4);
        buttonPane.add(b4,0,3);
        buttonPane.add(b5,1,3);
        buttonPane.add(b6,2,3);
        buttonPane.add(b7,0,2);
        buttonPane.add(b8,1,2);
        buttonPane.add(b9,2,2);

        buttonPane.setPadding(new Insets( 5,0,0,0));

        //the main UI maker
        calcPane.setTop(textPane);
        calcPane.setBottom(buttonPane);
        prePane.getChildren().addAll(calcPane,memory.createUI());
        prePane.setPadding(new Insets(5,5,5,5));
        pane.setTop(mainMenu);
        pane.setCenter(prePane);

        fromThis.setOnAction(event ->{
            switch (fromThis.getValue()) {
                case "Discount total":
                    helpText.setText("$29.99 , 33% ⮞ 20.09");
                    break;
                case "Discount %":
                    helpText.setText("$29.99 , 20.09 ⮞ 33%");
                    break;
            }
            capitalizeButtons();
        });

        Scene scene=new Scene(pane);
        scene.setOnMouseClicked(event -> changeFocus());
        scene.setOnKeyPressed(event -> keyboardControls(event,"press"));
        scene.setOnKeyReleased(event -> keyboardControls(event,"release"));
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        return scene;
    }

    /**
     * Changes the Scene
     * @param primaryStage the stage used to change UI
     */
    void operations(Stage primaryStage)
    {
        //Some important things to keep things tidy
        enable();
        clear.setText("C");
        equal.setText("=");
        plus.setText("+");
        remove.setText("⌫");
        answerText.setText("");
        pressClear();

        //changes the UI and sets values
        switch (operation.getValue()) {
            case "Default" -> {
                keyboardType = "math";
                helpText.setText("Default");
                primaryStage.setScene(defaultUI());
            }
            case "Common" -> {
                keyboardType = "math";
                helpText.setText("!(x)");
                primaryStage.setScene(commonUI());
            }
            case "Trig"->{
                keyboardType = "math";
                helpText.setText("Sin(x)");
                primaryStage.setScene(trigUI());
            }
            case "Geometry"->{
                keyboardType = "math";
                helpText.setText("s²");
                primaryStage.setScene(geometryUI());
            }
            case "Standard deviation calc"->{
                keyboardType = "custom,";
                helpText.setText("2,2.64,-2,-5,-2.7 ⮞ 2.9030...");
                primaryStage.setScene(commaUI());
            }
            case "Quadratic calc"->{
                keyboardType = "custom,";
                helpText.setText("a,b,c");
                answerText.setText(answerText.getText()+"\n");
                primaryStage.setScene(commaUI());
            }
            case "Cubic calc"->{
                keyboardType = "custom,";
                helpText.setText("a,b,c,d first output only");
                primaryStage.setScene(commaUI());
            }
            case "int, hex, binary, octal converter"->{
                keyboardType = "dec";
                helpText.setText("");
                primaryStage.setScene(programmerUI());
            }
            case "Ascii converter"->{
                keyboardType = "all";
                helpText.setText("hi ⮞ 104 105");
                primaryStage.setScene(asciiUI());
            }
            case "Data converter 2¹⁰", "Data converter 10³","Time converter",
                "Frequency converter"->{
                keyboardType = "num";
                helpText.setText("");
                primaryStage.setScene(converterUI());
            }
            case "Temp converter","Angle converter"->{
                keyboardType = "temp";
                helpText.setText("");
                primaryStage.setScene(converterUI());
            }
            case "Discount"->{
                keyboardType = "basic,";
                helpText.setText("$29.99 , 33% ⮞ 20.09");
                primaryStage.setScene(percentUI());
            }
        }
        capitalizeButtons();
    }

    /**
     * fetches helpText post clipboardAndMemory
     * @return helpText copy
     */
    String operations()
    {
        switch (operation.getValue())
        {
            case "Default":
                return "Default";
            case "Common":
                switch (fromThis.getValue()) {
                    case "Fact! & Gamma":
                        return "!(x)";
                    case "Square²":
                        return "x²";
                    case "Cube³":
                        return "x³";
                    case "Custom power ^ʸ":
                        return "x^ʸ";
                    case "e^ᵡ":
                        return "e^ᵡ";
                    case "Square root √":
                        return "√x";
                    case "Cube root ∛":
                        return "∛x";
                    case "Custom root ʸ√":
                        return "y,x";
                    case "Abs ||":
                        return "|x|";
                    case "Ceil ⌈⌉":
                        return "⌈x⌉";
                    case "Floor ⌊⌋":
                        return "⌊x⌋";
                    case "log":
                        return "log(x)";
                    case "ln":
                        return "ln(x)";
                    case "Exp":
                        return "(x,y)= x EXP y";
                    case "nPr":
                        return "(x,y)= x nPr y";
                    case "nCr":
                        return "(x,y)= x nCr y";
                    case "decimal ⮞ fraction":
                        return "0.5 ⮞ 1/2";
                }
            case "Trig":
                switch (fromThis.getValue())
                {
                    case "Sin":
                        return "Sin(x)";
                    case "Sin¯¹":
                        return "Sin¯¹(x)";
                    case "Sinh":
                        return "Sinh(x)";
                    case "Sinh¯¹":
                        return "Sinh¯¹(x)";
                    case "Csc":
                        return "Csc(x)";
                    case "Csc¯¹":
                        return "Csc¯¹(x)";
                    case "Csch":
                        return "Csch(x)";
                    case "Csch¯¹":
                        return "Csch¯¹(x)";
                    case "Cos":
                        return "Cos(x)";
                    case "Cos¯¹":
                        return "Cos¯¹(x)";
                    case "Cosh":
                        return "Cosh(x)";
                    case "Cosh¯¹":
                        return "Cosh¯¹(x)";
                    case "Sec":
                        return "Sec(x)";
                    case "Sec¯¹":
                        return "Sec¯¹(x)";
                    case "Sech":
                        return "Sech(x)";
                    case "Sech¯¹":
                        return "Sech¯¹(x)";
                    case "Tan":
                        return "Tan(x)";
                    case "Tan¯¹":
                        return "Tan¯¹(x)";
                    case "Tanh":
                        return "Tanh(x)";
                    case "Tanh¯¹":
                        return "Tanh¯¹(x)";
                    case "Cot":
                        return "Cot(x)";
                    case "Cot¯¹":
                        return "Cot¯¹(x)";
                    case "Coth":
                        return "Coth(x)";
                    case "Coth¯¹":
                        return "Coth¯¹(x)";
                }
            case "Geometry":
                switch (fromThis.getValue())
                {
                    case "Area of square":
                        return"s²";
                    case "Area of triangle": case "Area of rectangle":
                        return"a,b";
                    case "Area of trapezoid":
                        return"b₁,b₂,h";
                    case "Area of circle":
                        return"πr²";
                    case "Circumference of circle":
                        return"2πr";
                    case "Volume of cube":
                        return"s³";
                    case "Volume of rectangular solid":
                        return"l,w,h";
                    case "Volume of cylinder":
                        return"r,h";
                    case "Slope M": case "Slope intercept form B":
                        return "y₂,y₁,x₂,x₁";
                }
            case "Standard deviation calc":
                return "2,2.64,-2,-5,-2.7 ⮞ 2.9030...";
            case "Quadratic calc":
                return "a,b,c";
            case"Cubic calc":
                return "a,b,c,d first output only";
            case "int, hex, binary, octal converter": case "Data converter 2¹⁰":
            case "Data converter 10³": case "Temp converter": case "Time converter":
            case "Angle converter": case "Frequency converter":
                return "";
            case "Ascii converter":
                switch (fromThis.getValue())
                {
                    case "To ascii":
                        return "hi ⮞ 104 105";
                    case "From ascii":
                        return "104 105⮞ hi";
                    case "To ascii combined":
                        return "hi ⮞ 209 one way only";
                }
            case "Discount":
                switch (fromThis.getValue())
                {
                    case "Discount total":
                        return "$29.99 , 33% ⮞ 20.09";
                    case "Discount %":
                        return "$29.99 , 20.09 ⮞ 33%";
                }
        }
        return "Error";
    }

    /**
     * Set the id of nodes that can then be changed with their proper css style
     */
    void setCssID()
    {
        mainMenu.setId("menu");
        operation.setId("operations");
        rDG.setId("rDG");
        equationText.setId("textFieldNormal");
        left.setId("specialSizeColor");
        right.setId("specialSizeColor");
        pie.setId("color");
        euler.setId("color");
        clear.setId("color");
        remove.setId("color");
        parR.setId("color");
        parL.setId("color");
        divide.setId("color");
        multiply.setId("color");
        minus.setId("color");
        plus.setId("color");
        equal.setId("color");
        point.setId("color");
        modulo.setId("color");
        b0.setId("normal");
        b1.setId("normal");
        b2.setId("normal");
        b3.setId("normal");
        b4.setId("normal");
        b5.setId("normal");
        b6.setId("normal");
        b7.setId("normal");
        b8.setId("normal");
        b9.setId("normal");
        switch (operation.getValue()) {
            case "Common":
                equationText.setId("textFieldSmall");
                comma.setId("specialComma");
                break;
            case "Trig":
                equationText.setId("textFieldSmall");
                break;
            case "Geometry":
            case "Standard deviation calc":
            case "Quadratic calc":
            case "Cubic calc":
                equationText.setId("textFieldNormal");
                comma.setId("specialComma");
                break;
            case "int, hex, binary, octal converter":
                bA.setId("color");
                bB.setId("color");
                bC.setId("color");
                bD.setId("color");
                bF.setId("color");
                break;
            case "Data converter 2¹⁰":
            case "Data converter 10³":
            case "Temp converter":
            case "Time converter":
            case "Angle converter":
            case "Frequency converter":
            case "Discount":
                minus.setId("converterMinus");
                comma.setId("converterMinus");
                point.setId("converterColor");
                left.setId("converterArrows");
                right.setId("converterArrows");
                b0.setId("converter");
                b1.setId("converter");
                b2.setId("converter");
                b3.setId("converter");
                b4.setId("converter");
                b5.setId("converter");
                b6.setId("converter");
                b7.setId("converter");
                b8.setId("converter");
                b9.setId("converter");
                clear.setId("converterColor");
                remove.setId("converterColor");
                equal.setId("converterColor");
                break;
            case "Ascii converter":
                left.setId("asciiArrows");
                right.setId("asciiArrows");
                b0.setId("ascii");
                backQuote.setId("ascii");
                b1.setId("ascii");
                b2.setId("asciiB2");
                b3.setId("ascii");
                b4.setId("ascii");
                b5.setId("asciiB57");
                b6.setId("ascii");
                b7.setId("asciiB57");
                b8.setId("ascii");
                b9.setId("ascii");
                b0.setId("ascii");
                minus.setId("ascii");
                plus.setId("ascii");
                bQ.setId("ascii");
                bW.setId("asciiBWM");
                euler.setId("ascii");
                bR.setId("ascii");
                bT.setId("ascii");
                bY.setId("ascii");
                bU.setId("ascii");
                bI.setId("ascii");
                bO.setId("ascii");
                bP.setId("ascii");
                bracketL.setId("ascii");
                bracketR.setId("ascii");
                backSlash.setId("asciiBackSlash");
                bA.setId("ascii");
                bS.setId("ascii");
                bD.setId("ascii");
                bF.setId("ascii");
                bG.setId("ascii");
                bH.setId("ascii");
                bJ.setId("ascii");
                bK.setId("ascii");
                bL.setId("ascii");
                semiColon.setId("ascii");
                quote.setId("ascii");
                bZ.setId("ascii");
                bX.setId("ascii");
                bC.setId("ascii");
                bV.setId("ascii");
                bB.setId("ascii");
                bN.setId("ascii");
                bM.setId("asciiBWM");
                comma.setId("ascii");
                point.setId("ascii");
                divide.setId("ascii");
                equal.setId("asciiEqual");
                remove.setId("asciiRemove");
                clear.setId("asciiClear");
                space.setId("asciiSpace");
                break;
        }
    }

    /**
     * Moves cursor line right or left
     * @param direction the direction to move the cursor line
     */
    void moveIndex(char direction)
    {
        if(direction=='<')
        {
            //left
            String temp=equationText.getText();
            System.out.println("Left\t\t\t"+temp);
            System.out.println("Size of string "+temp.length());
            System.out.println("index of ¦ "+temp.indexOf("¦"));

            if(temp.indexOf("¦")>0)
            {
                int i=temp.indexOf("¦");
                temp=temp.replace("¦","");
                StringBuilder tempBuilder=new StringBuilder(temp);
                tempBuilder.insert(i-1,"¦");
                temp=tempBuilder.toString();
                System.out.println("Done with left "+temp);
                equationText.setText(temp);
                i=temp.indexOf("¦");

                equationText.positionCaret(i+1); //IMPORTANT
                System.out.println("Caret "+equationText.getCaretPosition());
            }
            else
            {
                System.out.println("left failed");
            }
        }
        else if(direction=='>')
        {
            //right
            String temp=equationText.getText();
            System.out.println("Right\t\t\t"+temp);
            System.out.println("Size of string "+temp.length());
            System.out.println("index of ¦ "+temp.indexOf("¦"));

            if(temp.indexOf("¦")+1!=temp.length())
            {
                int i=temp.indexOf("¦");
                temp=temp.replace("¦","");
                StringBuilder tempBuilder=new StringBuilder(temp);
                tempBuilder.insert(i+1,"¦");
                temp=tempBuilder.toString();
                System.out.println("Done with right "+temp);
                equationText.setText(temp);
                i=temp.indexOf("¦");

                equationText.positionCaret(i+1); //IMPORTANT
                System.out.println("Caret "+equationText.getCaretPosition());
            }
            else
            {
                System.out.println("right failed");
            }

        }
    }

    /**
     * Removes focus from all Main clickable nodes (so cursor line can move)
     */
    void removeFocus()
    {
        operation.setFocusTraversable(false);
        mainMenu.setFocusTraversable(false);
        fromThis.setFocusTraversable(false);
        toThis.setFocusTraversable(false);
        left.setFocusTraversable(false);
        right.setFocusTraversable(false);
        b0.setFocusTraversable(false);
        b1.setFocusTraversable(false);
        b2.setFocusTraversable(false);
        b3.setFocusTraversable(false);
        b4.setFocusTraversable(false);
        b5.setFocusTraversable(false);
        b6.setFocusTraversable(false);
        b7.setFocusTraversable(false);
        b8.setFocusTraversable(false);
        b9.setFocusTraversable(false);
        pie.setFocusTraversable(false);
        euler.setFocusTraversable(false);
        plus.setFocusTraversable(false);
        minus.setFocusTraversable(false);
        divide.setFocusTraversable(false);
        multiply.setFocusTraversable(false);
        modulo.setFocusTraversable(false);
        point.setFocusTraversable(false);
        equal.setFocusTraversable(false);
        parL.setFocusTraversable(false);
        parR.setFocusTraversable(false);
        remove.setFocusTraversable(false);
        clear.setFocusTraversable(false);
        rDG.setFocusTraversable(false);
        bA.setFocusTraversable(false);
        bB.setFocusTraversable(false);
        bC.setFocusTraversable(false);
        bD.setFocusTraversable(false);
        bF.setFocusTraversable(false);
        backQuote.setFocusTraversable(false);
        bG.setFocusTraversable(false);
        bH.setFocusTraversable(false);
        bI.setFocusTraversable(false);
        bJ.setFocusTraversable(false);
        bK.setFocusTraversable(false);
        bL.setFocusTraversable(false);
        bM.setFocusTraversable(false);
        bN.setFocusTraversable(false);
        bO.setFocusTraversable(false);
        bP.setFocusTraversable(false);
        bQ.setFocusTraversable(false);
        bR.setFocusTraversable(false);
        bS.setFocusTraversable(false);
        bT.setFocusTraversable(false);
        bU.setFocusTraversable(false);
        bV.setFocusTraversable(false);
        bW.setFocusTraversable(false);
        bX.setFocusTraversable(false);
        bY.setFocusTraversable(false);
        bZ.setFocusTraversable(false);
        bracketL.setFocusTraversable(false);
        bracketR.setFocusTraversable(false);
        backSlash.setFocusTraversable(false);
        semiColon.setFocusTraversable(false);
        quote.setFocusTraversable(false);
        comma.setFocusTraversable(false);
        space.setFocusTraversable(false);
    }

    /**
     * Makes sure the focus stays on answerText every mouse click (so cursor line can move)
     */
    void changeFocus()
    {
        answerText.requestFocus();
    }

    /**
     * Capitalizes buttons based on the keyboard type
     */
    void capitalizeButtons()
    {
        if(capitalized)
        {
            switch (keyboardType)
            {
                case"all":
                    b1.setText("!");
                    b2.setText("@");
                    b3.setText("#");
                    b4.setText("$");
                    b5.setText("%");
                    b6.setText("^");
                    b7.setText("&");
                    b8.setText("*");
                    b9.setText("(");
                    b0.setText(")");
                    minus.setText("_");
                    plus.setText("+");

                    bA.setText("A");
                    bB.setText("B");
                    bC.setText("C");
                    bD.setText("D");
                    euler.setText("E");
                    bF.setText("F");
                    backQuote.setText("~");
                    bG.setText("G");
                    bH.setText("H");
                    bI.setText("I");
                    bJ.setText("J");
                    bK.setText("K");
                    bL.setText("L");
                    bM.setText("M");
                    bN.setText("N");
                    bO.setText("O");
                    bP.setText("P");
                    bQ.setText("Q");
                    bR.setText("R");
                    bS.setText("S");
                    bT.setText("T");
                    bU.setText("U");
                    bV.setText("V");
                    bW.setText("W");
                    bX.setText("X");
                    bY.setText("Y");
                    bZ.setText("Z");
                    bracketL.setText("{");
                    bracketR.setText("}");
                    backSlash.setText("|");
                    semiColon.setText(":");
                    quote.setText("\"");
                    comma.setText("<");
                    point.setText(">");
                    divide.setText("?");
                    break;
                case "math", "dec", "num", "temp", "asciiNum", "basic,":
                    b1.setText("1");
                    b2.setText("2");
                    b3.setText("3");
                    b4.setText("4");
                    b5.setText("5");
                    b6.setText("6");
                    b7.setText("7");
                    b8.setText("8");
                    b9.setText("9");
                    b0.setText("0");
                    bA.setText("a");
                    bB.setText("b");
                    bC.setText("c");
                    bD.setText("d");
                    euler.setText("e");
                    bF.setText("f");
                    minus.setText("-");
                    plus.setText("+");
                    point.setText(".");
                    divide.setText("/");
                    comma.setText(",");
                    break;
                case "custom^":
                    b6.setText("^");
                    break;

            }
        }
        else
        {
            //if not capitalized
            b1.setText("1");
            b2.setText("2");
            b3.setText("3");
            b4.setText("4");
            b5.setText("5");
            b6.setText("6");
            b7.setText("7");
            b8.setText("8");
            b9.setText("9");
            b0.setText("0");
            minus.setText("-");
            if(keyboardType.equals("all"))
                plus.setText("=");

            bA.setText("a");
            bB.setText("b");
            bC.setText("c");
            bD.setText("d");
            euler.setText("e");
            bF.setText("f");
            backQuote.setText("`");
            bG.setText("g");
            bH.setText("h");
            bI.setText("i");
            bJ.setText("j");
            bK.setText("k");
            bL.setText("l");
            bM.setText("m");
            bN.setText("n");
            bO.setText("o");
            bP.setText("p");
            bQ.setText("q");
            bR.setText("r");
            bS.setText("s");
            bT.setText("t");
            bU.setText("u");
            bV.setText("v");
            bW.setText("w");
            bX.setText("x");
            bY.setText("y");
            bZ.setText("z");
            bracketL.setText("[");
            bracketR.setText("]");
            backSlash.setText("\\");
            semiColon.setText(";");
            quote.setText("'");
            comma.setText(",");
            point.setText(".");
            divide.setText("/");
        }
    }

    /**
     * removes one character behind the cursor line
     */
    void pressRemove()
    {
        String integrator=equationText.getText();

        if(integrator.indexOf("¦")>0)
        {
            System.out.println("removed, "+integrator.charAt(integrator.indexOf("¦")-1)+"¦");
            integrator=integrator.replace(integrator.charAt(integrator.indexOf("¦")-1)+"¦","¦");
            equationText.setText(integrator);
            equationText.positionCaret(integrator.indexOf("¦")+1);
        }
    }

    /**
     * Clears the equationText
     */
    void pressClear()
    {
        equationText.setText("¦");
    }

    /**
     * Calls the correct method in the Evaluator class and sorts everything
     */
    void pressEqual ()
    {
        Evaluator evaluator=new Evaluator();
        String memoryStoreType="";
        evaluator.inputSyntaxCheck(equationText,operation.getValue(),fromThis.getValue());

        //there's always time for stupidity... Until there isn't
        if(equationText.getLength()==0)
        {
            //nothing
        }
        else if(equationText.getText().equals("2+2") && operation.getValue().equals("Default"))
        {
            answerText.setText("|><=>  *Fish*... But also 4");
            memory.updateHistory("4",operation.getValue(),equationText);
        }
        else if(equationText.getText().equals("69") && operation.getValue().equals("Default"))
        {
            answerText.setText("get your head out of your pants");
            memory.updateHistory("69",operation.getValue(),equationText);
        }
        else if(equationText.getText().equals("is this the krusty krab?") && fromThis.getValue().equals("To ascii combined"))
        {
            answerText.setText("no, this is Patrick. also 2278");
            memory.updateHistory("2278",operation.getValue(),equationText);
        }
        else if(equationText.getText().equals("who is MK?") && fromThis.getValue().equals("To ascii combined"))
        {
            answerText.setText("Who's asking?. also 833");
            memory.updateHistory("833",operation.getValue(),equationText);
        }
        else if(equationText.getText().equals("sellout") && fromThis.getValue().equals("To ascii combined"))
        {
            answerText.setText("\"Make sure to like, and subscribe. Hit that bell\" -every clickbait youtuber. also 776");
            memory.updateHistory("776",operation.getValue(),equationText);
        }
        else if(equationText.getText().equals("launch4j") && fromThis.getValue().equals("To ascii combined"))
        {
            answerText.setText("launch4j > jpackage. also 793");
            memory.updateHistory("793",operation.getValue(),equationText);
        }
        else
        {
            switch (operation.getValue())
            {
                case "Default":
                    evaluator.evaluate(equationText,answerText);
                    memoryStoreType="default";
                    break;
                case "Common":
                    evaluator.common(equationText,answerText,fromThis.getValue());
                    memoryStoreType="from";
                    break;
                case "Trig":
                    evaluator.trig(equationText,answerText,fromThis.getValue(),rDG.getText());
                    memoryStoreType="trig";
                    break;
                case"Geometry":
                    evaluator.geometry(equationText,answerText,fromThis.getValue());
                    memoryStoreType="from";
                    break;
                case "Standard deviation calc":
                    evaluator.standardDeviation(equationText,answerText);
                    memoryStoreType="default";
                    break;
                case "Quadratic calc":
                    evaluator.quadratic(equationText,answerText);
                    memoryStoreType="none";
                    break;
                case"Cubic calc":
                    evaluator.cubic(equationText,answerText);
                    memoryStoreType="none";
                    break;
                case "int, hex, binary, octal converter":
                    evaluator.programmerCalc(equationText,answerText,fromThis.getValue(),toThis.getValue());
                    memoryStoreType="from to";
                    break;
                case "Ascii converter":
                    evaluator.asciiConverter(equationText,answerText,fromThis.getValue());
                    memoryStoreType="from";
                    break;
                case "Data converter 2¹⁰":
                    evaluator.dataConverterBase2(equationText,answerText,fromThis.getValue(),toThis.getValue());
                    memoryStoreType="from to";
                    break;
                case "Data converter 10³":
                    evaluator.dataConverterBase10(equationText,answerText,fromThis.getValue(),toThis.getValue());
                    memoryStoreType="from to";
                    break;
                case "Temp converter":
                    evaluator.tempConverter(equationText,answerText,fromThis.getValue(),toThis.getValue());
                    memoryStoreType="from to";
                    break;
                case "Time converter":
                    evaluator.timeConverter(equationText,answerText,fromThis.getValue(),toThis.getValue());
                    memoryStoreType="from to";
                    break;
                case"Angle converter":
                    evaluator.angleConverter(equationText,answerText,fromThis.getValue(),toThis.getValue());
                    memoryStoreType="from to";
                    break;
                case "Frequency converter":
                    evaluator.frequencyConverter(equationText,answerText,fromThis.getValue(),toThis.getValue());
                    memoryStoreType="from to";
                    break;
                case "Discount":
                    evaluator.percentageAndTax(equationText,answerText,fromThis.getValue());
                    memoryStoreType="default";
                    break;

            }

            //Ignores adding to memory if any of these results show up
            if((answerText.getText().equals("Infinity")|| answerText.getText().equals("-Infinity"))
                    && operation.getValue().equals("Default"))
                answerText.setText("Thou shan't not divide'ith by zero'ith");
            else if(answerText.getText().equals("ERROR!") || answerText.getText().equals("NaN")
                    ||answerText.getText().equals("Infinity")||answerText.getText().equals("-Infinity")
                    ||answerText.getText().equals("")){}
            else
            {
                if(memoryStoreType.equals("default"))
                    memory.updateHistory(answerText.getText(),operation.getValue(),equationText);
                else if(memoryStoreType.equals("from"))
                    memory.updateHistory(answerText.getText(),fromThis.getValue(),equationText);
                else if(memoryStoreType.equals("trig"))
                    memory.updateHistoryTrig(answerText.getText(),fromThis.getValue(),rDG.getText(),equationText);
                else if(memoryStoreType.equals("from to"))
                    memory.updateHistoryFromTo(answerText.getText(),fromThis.getValue(),toThis.getValue(),equationText);
            }
        }

        //reapply cursor line
        equationText.appendText("¦");
        equationText.positionCaret(equationText.getText().indexOf("¦")+1); //still important
    }

    /**
     * adds the appropriate character to equationText
     * @param type if pressed by button or keyboard
     * @param command what to enter
     * math all math + (c clear, r remove, p π, e euler)
     * custom, all math and , + (c clear, r remove, p π, e euler)
     * custom^ all math and ^ + (c clear, r remove, p π, e euler)
     * basic, basic numbers and ,. +(c clear, r remove)
     * dec 0-9 and minus + (c clear, r remove)
     * binary 0-1 + (c clear, r remove)
     * hex 0-9 and a-f + (r remove)
     * octal 0-7 + (c clear, r remove)
     * all everything except for tab
     * num 0-9 and . + (c clear, r remove)
     * temp 0-9 - and . + (c clear, r remove)
     * asciiNum 0-9 and space (c clear, r remove)
     */
    void pressInputKey(String type,String command)
    {
        String integrator=equationText.getText();

        if(type.equals("button"))
        {
            integrator=integrator.replace("¦",command+"¦");
        }
        else
        {
            //I hope you like spaghetti
            switch (command)
            {
                case "esc":
                    System.out.println(guideStage.isShowing());
                    if(guideStage.isShowing())
                        guideStage.close();
                    else
                    {
                        pressClear();
                        simulatePress(clear);
                        return;
                    }
                    break;
                case "`":
                    if(keyboardType.equals("all"))
                    {
                        if(!capitalized)
                            integrator=integrator.replace("¦","`¦");
                        else
                            integrator=integrator.replace("¦","~¦");
                        simulatePress(backQuote);
                    }
                    break;
                case "1":
                    if(keyboardType.equals("all") || keyboardType.equals("math")
                            || keyboardType.equals("dec")|| keyboardType.equals("binary")
                            || keyboardType.equals("octal")|| keyboardType.equals("hex")
                            || keyboardType.equals("custom^") || keyboardType.equals("custom,")
                            || keyboardType.equals("num")|| keyboardType.equals("temp")
                            || keyboardType.equals("asciiNum")|| keyboardType.equals("basic,"))
                    {
                        if(!capitalized)
                        {
                            integrator=integrator.replace("¦","1¦");
                            simulatePress(b1);
                        }
                        else if (capitalized && keyboardType.equals("all"))
                        {
                            integrator=integrator.replace("¦","!¦");
                            simulatePress(b1);
                        }
                    }
                    break;
                case "2":
                    if(keyboardType.equals("all") || keyboardType.equals("math") || keyboardType.equals("dec")
                            || keyboardType.equals("octal") || keyboardType.equals("hex")
                            || keyboardType.equals("custom^") || keyboardType.equals("custom,")
                            || keyboardType.equals("num")|| keyboardType.equals("temp")
                            || keyboardType.equals("asciiNum")|| keyboardType.equals("basic,"))
                    {
                        if(!capitalized)
                        {
                            integrator=integrator.replace("¦","2¦");
                            simulatePress(b2);
                        }
                        else if (capitalized && keyboardType.equals("all"))
                        {
                            integrator=integrator.replace("¦","@¦");
                            simulatePress(b2);
                        }
                    }
                    break;
                case "3":
                    if(keyboardType.equals("all") || keyboardType.equals("math") || keyboardType.equals("dec")
                            || keyboardType.equals("octal") || keyboardType.equals("hex")
                            || keyboardType.equals("custom^") || keyboardType.equals("custom,")
                            || keyboardType.equals("num")|| keyboardType.equals("temp")
                            || keyboardType.equals("asciiNum")|| keyboardType.equals("basic,"))
                    {
                        if(!capitalized)
                        {
                            integrator=integrator.replace("¦","3¦");
                            simulatePress(b3);
                        }
                        else if (capitalized && keyboardType.equals("all"))
                        {
                            integrator=integrator.replace("¦","#¦");
                            simulatePress(b3);
                        }
                    }
                    break;
                case "4":
                    if(keyboardType.equals("all") || keyboardType.equals("math") || keyboardType.equals("dec")
                            || keyboardType.equals("octal") || keyboardType.equals("hex")
                            || keyboardType.equals("custom^") || keyboardType.equals("custom,")
                            || keyboardType.equals("num")|| keyboardType.equals("temp")
                            || keyboardType.equals("asciiNum")|| keyboardType.equals("basic,"))
                    {
                        if(!capitalized)
                        {
                            integrator=integrator.replace("¦","4¦");
                            simulatePress(b4);
                        }
                        else if (capitalized && keyboardType.equals("all"))
                        {
                            integrator=integrator.replace("¦","$¦");
                            simulatePress(b4);
                        }
                    }
                    break;
                case "5":
                    if(keyboardType.equals("all") || keyboardType.equals("math") || keyboardType.equals("dec")
                            || keyboardType.equals("octal") || keyboardType.equals("hex")
                            || keyboardType.equals("custom^") || keyboardType.equals("custom,")
                            || keyboardType.equals("num")|| keyboardType.equals("temp")
                            || keyboardType.equals("asciiNum")|| keyboardType.equals("basic,"))
                    {
                        if(!capitalized)
                        {
                            integrator=integrator.replace("¦","5¦");
                            simulatePress(b5);
                        }
                        else if(capitalized && (keyboardType.equals("all") || keyboardType.equals("math")
                                || keyboardType.equals("custom^")|| keyboardType.equals("custom,")))
                        {
                            integrator=integrator.replace("¦","%¦");
                            if(keyboardType.equals("all"))
                                simulatePress(b5);
                            else
                                simulatePress(modulo);
                        }
                    }
                    break;
                case "6":
                    if(keyboardType.equals("all") || keyboardType.equals("math") || keyboardType.equals("dec")
                            || keyboardType.equals("octal") || keyboardType.equals("hex")
                            || keyboardType.equals("custom^") || keyboardType.equals("custom,")
                            || keyboardType.equals("num")|| keyboardType.equals("temp")
                            || keyboardType.equals("asciiNum")|| keyboardType.equals("basic,"))
                    {
                        if(!capitalized)
                        {
                            integrator=integrator.replace("¦","6¦");
                            simulatePress(b6);
                        }
                        else if (capitalized && (keyboardType.equals("all") || keyboardType.equals("custom^")))
                        {
                            integrator=integrator.replace("¦","^¦");
                            simulatePress(b6);
                        }
                    }
                    break;
                case "7":
                    if(keyboardType.equals("all") || keyboardType.equals("math") || keyboardType.equals("dec")
                            || keyboardType.equals("octal") || keyboardType.equals("hex")
                            || keyboardType.equals("custom^") || keyboardType.equals("custom,")
                            || keyboardType.equals("num")|| keyboardType.equals("temp")
                            || keyboardType.equals("asciiNum")|| keyboardType.equals("basic,"))
                    {
                        if(!capitalized)
                        {
                            integrator=integrator.replace("¦","7¦");
                            simulatePress(b7);
                        }
                        else if (capitalized && keyboardType.equals("all"))
                        {
                            integrator=integrator.replace("¦","&¦");
                            simulatePress(b7);
                        }
                    }
                    break;
                case "8":
                    if(keyboardType.equals("all") || keyboardType.equals("math") || keyboardType.equals("dec")
                            || keyboardType.equals("hex")|| keyboardType.equals("num")
                            || keyboardType.equals("custom^") || keyboardType.equals("custom,")
                            || keyboardType.equals("temp") || keyboardType.equals("asciiNum")
                            || keyboardType.equals("basic,"))
                    {
                        if(!capitalized)
                        {
                            integrator=integrator.replace("¦","8¦");
                            simulatePress(b8);
                        }
                        else if(capitalized && !(keyboardType.equals("hex")|| keyboardType.equals("num")
                                || keyboardType.equals("dec")|| keyboardType.equals("temp")))
                        {
                            integrator=integrator.replace("¦","*¦");
                            if(keyboardType.equals("all"))
                                simulatePress(b8);
                            else
                                simulatePress(multiply);
                        }
                    }
                    break;
                case "9":
                    if(keyboardType.equals("all") || keyboardType.equals("math") || keyboardType.equals("dec")
                            || keyboardType.equals("hex")|| keyboardType.equals("num")
                            || keyboardType.equals("custom^") || keyboardType.equals("custom,")
                            || keyboardType.equals("temp") || keyboardType.equals("asciiNum")
                            || keyboardType.equals("basic,"))
                    {
                        if(!capitalized)
                        {
                            integrator=integrator.replace("¦","9¦");
                            simulatePress(b9);
                        }
                        else if(capitalized && !(keyboardType.equals("hex")|| keyboardType.equals("num")
                                || keyboardType.equals("dec")|| keyboardType.equals("temp")))
                        {
                            integrator=integrator.replace("¦","(¦");
                            if(keyboardType.equals("all"))
                                simulatePress(b9);
                            else
                                simulatePress(parL);
                        }
                    }
                    break;
                case "0":
                    if(keyboardType.equals("all") || keyboardType.equals("math")
                            || keyboardType.equals("dec")|| keyboardType.equals("binary")
                            || keyboardType.equals("octal")|| keyboardType.equals("hex")
                            || keyboardType.equals("custom^") || keyboardType.equals("custom,")
                            || keyboardType.equals("num")|| keyboardType.equals("temp")
                            || keyboardType.equals("asciiNum")|| keyboardType.equals("basic,"))
                    {
                        if(!capitalized)
                        {
                            integrator=integrator.replace("¦","0¦");
                            simulatePress(b0);
                        }
                        else if (capitalized && (keyboardType.equals("all") || keyboardType.equals("math")
                                || keyboardType.equals("custom,") || keyboardType.equals("custom^")))
                        {
                            integrator=integrator.replace("¦",")¦");
                            if(keyboardType.equals("all"))
                                simulatePress(b0);
                            else
                                simulatePress(parR);
                        }
                    }
                    break;
                case "-":
                    if(keyboardType.equals("all") || keyboardType.equals("math") || keyboardType.equals("dec")
                            || keyboardType.equals("custom^") || keyboardType.equals("custom,")
                            || keyboardType.equals("temp"))
                    {
                        if(!capitalized)
                        {
                            integrator=integrator.replace("¦","-¦");
                            simulatePress(minus);
                        }
                        else if (capitalized && keyboardType.equals("all"))
                        {
                            integrator=integrator.replace("¦","_¦");
                            simulatePress(minus);
                        }
                    }
                    break;
                case "=":
                    if(keyboardType.equals("all") || keyboardType.equals("math")|| keyboardType.equals("num")
                            || keyboardType.equals("dec")|| keyboardType.equals("binary")
                            || keyboardType.equals("octal")|| keyboardType.equals("hex")
                            || keyboardType.equals("custom^") || keyboardType.equals("custom,")
                            || keyboardType.equals("temp"))
                    {
                        if(!capitalized)
                        {
                            if(!keyboardType.equals("all"))
                            {
                                pressEqual();
                                simulatePress(equal);
                                return;
                            }
                            else
                            {
                                integrator=integrator.replace("¦","=¦");
                                simulatePress(plus);
                            }
                        }
                        else if (capitalized && (keyboardType.equals("all") || keyboardType.equals("math")
                                || keyboardType.equals("custom^") || keyboardType.equals("custom,")))
                        {
                            integrator=integrator.replace("¦","+¦");
                            simulatePress(plus);
                        }
                    }
                    break;
                case "remove":
                    pressRemove();
                    simulatePress(remove);
                    break;
                case "tab":
                    //tab doesn't work well with text fields. I think it's a java problem
                    break;
                case "q":
                    if(keyboardType.equals("all"))
                    {
                        if(!capitalized)
                            integrator=integrator.replace("¦","q¦");
                        else
                            integrator=integrator.replace("¦","Q¦");
                        simulatePress(bQ);
                    }
                    break;
                case "w":
                    if(keyboardType.equals("all"))
                    {
                        if(!capitalized)
                            integrator=integrator.replace("¦","w¦");
                        else
                            integrator=integrator.replace("¦","W¦");
                        simulatePress(bW);
                    }
                    break;
                case "e":
                    if(keyboardType.equals("all") || keyboardType.equals("math") || keyboardType.equals("hex")
                            || keyboardType.equals("custom^") || keyboardType.equals("custom,"))
                    {
                        if(!capitalized)
                        {
                            integrator=integrator.replace("¦","e¦");
                            simulatePress(euler);
                        }
                        else if (capitalized && keyboardType.equals("all"))
                        {
                            integrator=integrator.replace("¦","E¦");
                            simulatePress(euler);
                        }
                    }
                    break;
                case "r":
                    if(keyboardType.equals("all")|| keyboardType.equals("math")
                            || keyboardType.equals("custom,")|| keyboardType.equals("custom^")
                            || keyboardType.equals("dec")|| keyboardType.equals("hex")
                            || keyboardType.equals("binary")|| keyboardType.equals("octal")
                            || keyboardType.equals("num")|| keyboardType.equals("temp")
                            || keyboardType.equals("asciiNum")|| keyboardType.equals("basic,"))
                    {
                        if(!capitalized)
                        {
                            if(!keyboardType.equals("all"))
                            {
                                pressRemove();
                                simulatePress(remove);
                                return;
                            }
                            else
                            {
                                integrator=integrator.replace("¦","r¦");
                                simulatePress(bR);
                            }
                        }
                        else if(capitalized && keyboardType.equals("all"))
                        {
                            integrator=integrator.replace("¦","R¦");
                            simulatePress(bR);
                        }
                    }
                    break;
                case "t":
                    if(keyboardType.equals("all"))
                    {
                        if(!capitalized)
                            integrator=integrator.replace("¦","t¦");
                        else
                            integrator=integrator.replace("¦","T¦");
                        simulatePress(bT);
                    }
                    break;
                case "y":
                    if(keyboardType.equals("all"))
                    {
                        if(!capitalized)
                            integrator=integrator.replace("¦","y¦");
                        else
                            integrator=integrator.replace("¦","Y¦");
                        simulatePress(bY);
                    }
                    break;
                case "u":
                    if(keyboardType.equals("all"))
                    {
                        if(!capitalized)
                            integrator=integrator.replace("¦","u¦");
                        else
                            integrator=integrator.replace("¦","U¦");
                        simulatePress(bU);
                    }
                    break;
                case "i":
                    if(keyboardType.equals("all"))
                    {
                        if(!capitalized)
                            integrator=integrator.replace("¦","i¦");
                        else
                            integrator=integrator.replace("¦","I¦");
                        simulatePress(bI);
                    }
                    break;
                case "o":
                    if(keyboardType.equals("all"))
                    {
                        if(!capitalized)
                            integrator=integrator.replace("¦","o¦");
                        else
                            integrator=integrator.replace("¦","O¦");
                        simulatePress(bO);
                    }
                    break;
                case "p":
                    if(keyboardType.equals("all")|| keyboardType.equals("math")
                            || keyboardType.equals("custom^") || keyboardType.equals("custom,"))
                    {
                        if(!capitalized)
                        {
                            if(!keyboardType.equals("all"))
                            {
                                integrator=integrator.replace("¦","π¦");
                                simulatePress(pie);
                            }
                            else
                            {
                                integrator=integrator.replace("¦","p¦");
                                simulatePress(bP);
                            }
                        }
                        else if(capitalized && keyboardType.equals("all"))
                        {
                            integrator=integrator.replace("¦","P¦");
                            simulatePress(bP);
                        }
                    }
                    break;
                case "[":
                    if(keyboardType.equals("all"))
                    {
                        if(!capitalized)
                            integrator=integrator.replace("¦","[¦");
                        else
                            integrator=integrator.replace("¦","{¦");
                        simulatePress(bracketL);
                    }
                    break;
                case "]":
                    if(keyboardType.equals("all"))
                    {
                        if(!capitalized)
                            integrator=integrator.replace("¦","]¦");
                        else
                            integrator=integrator.replace("¦","}¦");
                        simulatePress(bracketR);
                    }
                    break;
                case "\\":
                    if(keyboardType.equals("all"))
                    {
                        if(!capitalized)
                            integrator=integrator.replace("¦","\\¦");
                        else
                            integrator=integrator.replace("¦","|¦");
                        simulatePress(backSlash);
                    }
                    break;
                case "a":
                    if(keyboardType.equals("all") ||keyboardType.equals("hex"))
                    {
                        if(!capitalized )
                        {
                            integrator=integrator.replace("¦","a¦");
                            simulatePress(bA);
                        }
                        else if(capitalized && !keyboardType.equals("hex"))
                        {
                            integrator=integrator.replace("¦","A¦");
                            simulatePress(bA);
                        }
                    }
                    break;
                case "s":
                    if(keyboardType.equals("all"))
                    {
                        if(!capitalized)
                            integrator=integrator.replace("¦","s¦");
                        else
                            integrator=integrator.replace("¦","S¦");
                        simulatePress(bS);
                    }
                    break;
                case "d":
                    if(keyboardType.equals("all") ||keyboardType.equals("hex"))
                    {
                        if(!capitalized)
                        {
                            integrator=integrator.replace("¦","d¦");
                            simulatePress(bD);
                        }
                        else if(capitalized && !keyboardType.equals("hex"))
                        {
                            integrator=integrator.replace("¦","D¦");
                            simulatePress(bD);
                        }
                    }
                    break;
                case "f":
                    if(keyboardType.equals("all") ||keyboardType.equals("hex"))
                    {
                        if(!capitalized)
                        {
                            integrator=integrator.replace("¦","f¦");
                            simulatePress(bF);
                        }
                        else if(capitalized && !keyboardType.equals("hex"))
                        {
                            integrator=integrator.replace("¦","F¦");
                            simulatePress(bF);
                        }
                    }
                    break;
                case "g":
                    if(keyboardType.equals("all"))
                    {
                        if(!capitalized)
                            integrator=integrator.replace("¦","g¦");
                        else
                            integrator=integrator.replace("¦","G¦");
                        simulatePress(bG);
                    }
                    break;
                case "h":
                    if(keyboardType.equals("all"))
                    {
                        if(!capitalized)
                            integrator=integrator.replace("¦","h¦");
                        else
                            integrator=integrator.replace("¦","H¦");
                        simulatePress(bH);
                    }
                    break;
                case "j":
                    if(keyboardType.equals("all"))
                    {
                        if(!capitalized)
                            integrator=integrator.replace("¦","j¦");
                        else
                            integrator=integrator.replace("¦","J¦");
                        simulatePress(bJ);
                    }
                    break;
                case "k":
                    if(keyboardType.equals("all"))
                    {
                        if(!capitalized)
                            integrator=integrator.replace("¦","k¦");
                        else
                            integrator=integrator.replace("¦","K¦");
                        simulatePress(bK);
                    }
                    break;
                case "l":
                    if(keyboardType.equals("all"))
                    {
                        if(!capitalized)
                            integrator=integrator.replace("¦","l¦");
                        else
                            integrator=integrator.replace("¦","L¦");
                        simulatePress(bL);
                    }
                    break;
                case ";":
                    if(keyboardType.equals("all"))
                    {
                        if(!capitalized)
                            integrator=integrator.replace("¦",";¦");
                        else
                            integrator=integrator.replace("¦",":¦");
                        simulatePress(semiColon);
                    }
                    break;
                case "'":
                    if(keyboardType.equals("all"))
                    {
                        if(!capitalized)
                            integrator=integrator.replace("¦","'¦");
                        else
                            integrator=integrator.replace("¦","\"¦");
                        simulatePress(quote);
                    }
                    break;
                case "z":
                    if(keyboardType.equals("all"))
                    {
                        if(!capitalized)
                            integrator=integrator.replace("¦","z¦");
                        else
                            integrator=integrator.replace("¦","Z¦");
                        simulatePress(bZ);
                    }
                    break;
                case "x":
                    if(keyboardType.equals("all"))
                    {
                        if(!capitalized)
                            integrator=integrator.replace("¦","x¦");
                        else
                            integrator=integrator.replace("¦","X¦");
                        simulatePress(bX);
                    }
                    break;
                case "c":
                    if(keyboardType.equals("all") ||keyboardType.equals("math")
                            || keyboardType.equals("custom^") || keyboardType.equals("custom,")
                            || keyboardType.equals("dec")||keyboardType.equals("binary")
                            ||keyboardType.equals("hex")||keyboardType.equals("octal")
                            ||keyboardType.equals("num")|| keyboardType.equals("temp")
                            || keyboardType.equals("asciiNum")|| keyboardType.equals("basic,"))
                    {
                        if(!capitalized)
                        {
                            if(!(keyboardType.equals("all") || keyboardType.equals("hex")))
                            {
                                pressClear();
                                simulatePress(clear);
                                return;
                            }
                            else
                            {
                                integrator=integrator.replace("¦","c¦");
                                simulatePress(bC);
                            }
                        }
                        else if(capitalized && keyboardType.equals("all"))
                        {
                            integrator=integrator.replace("¦","C¦");
                            simulatePress(bC);
                        }
                    }
                    break;
                case "v":
                    if(keyboardType.equals("all"))
                    {
                        if(!capitalized)
                            integrator=integrator.replace("¦","v¦");
                        else
                            integrator=integrator.replace("¦","V¦");
                        simulatePress(bV);
                    }
                    break;
                case "b":
                    if(keyboardType.equals("all") ||keyboardType.equals("hex"))
                    {
                        if(!capitalized)
                        {
                            integrator=integrator.replace("¦","b¦");
                            simulatePress(bB);
                        }
                        else if(capitalized && !keyboardType.equals("hex"))
                        {
                            integrator=integrator.replace("¦","B¦");
                            simulatePress(bB);
                        }
                    }
                    break;
                case "n":
                    if(keyboardType.equals("all"))
                    {
                        if(!capitalized)
                            integrator=integrator.replace("¦","n¦");
                        else
                            integrator=integrator.replace("¦","N¦");
                        simulatePress(bN);
                    }
                    break;
                case "m":
                    if(keyboardType.equals("all"))
                    {
                        if(!capitalized)
                            integrator=integrator.replace("¦","m¦");
                        else
                            integrator=integrator.replace("¦","M¦");
                        simulatePress(bM);
                    }
                    break;
                case ",":
                    if(keyboardType.equals("all") || keyboardType.equals("custom,")
                            || keyboardType.equals("basic,"))
                    {
                        if(!capitalized)
                        {
                            integrator=integrator.replace("¦",",¦");
                            simulatePress(comma);
                        }
                        else if(capitalized && keyboardType.equals("all"))
                        {
                            integrator=integrator.replace("¦","<¦");
                            simulatePress(comma);
                        }
                    }
                    break;
                case ".":
                    if(keyboardType.equals("all") || keyboardType.equals("math")
                            || keyboardType.equals("custom^") || keyboardType.equals("custom,")
                            || keyboardType.equals("num")|| keyboardType.equals("temp")
                            || keyboardType.equals("basic,"))
                    {
                        if(!capitalized)
                        {
                            integrator=integrator.replace("¦",".¦");
                            simulatePress(point);
                        }
                        else if (capitalized && keyboardType.equals("all"))
                        {
                            integrator=integrator.replace("¦",">¦");
                            simulatePress(point);
                        }
                    }
                    break;
                case "/":
                    if(keyboardType.equals("all") || keyboardType.equals("math")
                            || keyboardType.equals("custom^") || keyboardType.equals("custom,"))
                    {
                        if(!capitalized)
                        {
                            integrator=integrator.replace("¦","/¦");
                            simulatePress(divide);
                        }
                        else if (capitalized && keyboardType.equals("all"))
                        {
                            integrator=integrator.replace("¦","?¦");
                            simulatePress(divide);
                        }
                    }
                    break;
                case "space":
                    if(keyboardType.equals("all") || keyboardType.equals("asciiNum"))
                    {
                        integrator=integrator.replace("¦"," ¦");
                        simulatePress(space);
                    }
                    break;
            }
        }

        //actually sets the text
        equationText.setText(integrator);
        equationText.positionCaret(integrator.indexOf("¦")+1);
    }

    /**
     * Controls keyboard input
     * @param key the key pressed
     * @param shiftCheck checks is shift was released or pressed
     */
    void keyboardControls(javafx.scene.input.KeyEvent key,String shiftCheck)
    {
        //because shift is a nuisance and must get special treatment
        if(shiftCheck.equals("release"))
        {
            //Capslock is very broken in general
            if ( key.getCode() == KeyCode.CAPS ) {
                if(!capSwitch)
                {
                    capSwitch=true;
                    System.out.println("Capslock state: on");
                }
                else
                {
                    capSwitch=false;
                    System.out.println("Capslock state: off ");
                }
            }

            //an XOR for capSwitch and shift
            if(key.isShiftDown()^capSwitch)
            {
                capitalized=true;
                capitalizeButtons();
            }
            else
            {
                capitalized=false;
                capitalizeButtons();
            }
            return;
        }

        //an XOR for capSwitch and shift yes, I told you shift makes things difficult
        if(key.isShiftDown()^capSwitch)
        {
            capitalized=true;
            capitalizeButtons();
        }
        else
        {
            capitalized=false;
            capitalizeButtons();
        }

        System.out.println(key.getCode());

        //sends character pressed to pressInputKey
        switch (key.getCode())
        {
            case ESCAPE:
                pressInputKey("key","esc");
                break;
            case BACK_QUOTE:
                pressInputKey("key","`");
                break;
            case DIGIT1:
                pressInputKey("key","1");
                break;
            case DIGIT2:
                pressInputKey("key","2");
                break;
            case DIGIT3:
                pressInputKey("key","3");
                break;
            case DIGIT4:
                pressInputKey("key","4");
                break;
            case DIGIT5:
                pressInputKey("key","5");
                break;
            case DIGIT6:
                pressInputKey("key","6");
                break;
            case DIGIT7:
                pressInputKey("key","7");
                break;
            case DIGIT8:
                pressInputKey("key","8");
                break;
            case DIGIT9:
                pressInputKey("key","9");
                break;
            case DIGIT0:
                pressInputKey("key","0");
                break;
            case MINUS:
                pressInputKey("key","-");
                break;
            case EQUALS:
                pressInputKey("key","=");
                break;
            case BACK_SPACE:
                pressRemove();
                simulatePress(remove);
                break;
            case TAB:
                pressInputKey("key","tab");
                break;
            case Q:
                pressInputKey("key","q");
                break;
            case W:
                pressInputKey("key","w");
                break;
            case E:
                pressInputKey("key","e");
                break;
            case R:
                pressInputKey("key","r");
                break;
            case T:
                pressInputKey("key","t");
                break;
            case Y:
                pressInputKey("key","y");
                break;
            case U:
                pressInputKey("key","u");
                break;
            case I:
                pressInputKey("key","i");
                break;
            case O:
                pressInputKey("key","o");
                break;
            case P:
                pressInputKey("key","p");
                break;
            case OPEN_BRACKET:
                pressInputKey("key","[");
                break;
            case CLOSE_BRACKET:
                pressInputKey("key","]");
                break;
            case BACK_SLASH:
                pressInputKey("key","\\");
                break;
            case A:
                pressInputKey("key","a");
                break;
            case S:
                if(!key.isControlDown())
                    pressInputKey("key","s");
                break;
            case D:
                pressInputKey("key","d");
                break;
            case F:
                pressInputKey("key","f");
                break;
            case G:
                pressInputKey("key","g");
                break;
            case H:
                if(!key.isControlDown())
                    pressInputKey("key","h");
                break;
            case J:
                pressInputKey("key","j");
                break;
            case K:
                pressInputKey("key","k");
                break;
            case L:
                pressInputKey("key","l");
                break;
            case SEMICOLON:
                pressInputKey("key",";");
                break;
            case QUOTE:
                pressInputKey("key","'");
                break;
            case ENTER:
                pressEqual();
                simulatePress(equal);
                break;
            case Z:
                pressInputKey("key","z");
                break;
            case X:
                if(!key.isControlDown())
                    pressInputKey("key","x");
                break;
            case C:
                if(!key.isControlDown())
                    pressInputKey("key","c");
                break;
            case V:
                if(!key.isControlDown())
                    pressInputKey("key","v");
                break;
            case B:
                pressInputKey("key","b");
                break;
            case N:
                pressInputKey("key","n");
                break;
            case M:
                pressInputKey("key","m");
                break;
            case COMMA:
                pressInputKey("key",",");
                break;
            case PERIOD:
                pressInputKey("key",".");
                break;
            case SLASH:
                pressInputKey("key","/");
                break;
            case SPACE:
                pressInputKey("key","space");
                break;
            case RIGHT:
                moveIndex('>');
                simulatePress(right);
                break;
            case LEFT:
                moveIndex('<');
                simulatePress(left);
                break;
        }
    }

    /**
     * Simulates the pressing of a button, simple, sweet, and works for everything
     * @param button the node being pressed
     */
    void simulatePress(Button button)
    {
        button.arm();
        PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
        pause.setOnFinished(evt -> {
            button.disarm();
        });
        pause.play();
    }

    /**
     * Cut, copy, paste, and save to memory
     * @param command what to do
     * @param inject a copy of equationText
     */
    void clipboardAndMemory(String command, String inject )
    {
        System.out.println("Ding clip");
        inject=inject.replace("¦","");
        ClipboardContent content = new ClipboardContent();

        if(inject.equals("") && !command.equals("paste"))
            return;

        switch (command) {
            case "copy" -> {
                System.out.println("COPY");
                content.putString(inject);
                Clipboard.getSystemClipboard().setContent(content);
                helpText.setText("Copied to clipboard");
                Thread ts = new Thread(() -> {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        Platform.runLater(() -> {
                            helpText.setText(operations());
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                ts.start();
            }
            case "paste" -> {
                System.out.println("PASTE");
                String temp = Clipboard.getSystemClipboard().getString();
                String toReplace = equationText.getText();
                temp = temp.replace("¦", "");
                toReplace = toReplace.replace("¦", temp + "¦");
                equationText.setText(toReplace);
                equationText.positionCaret(toReplace.indexOf("¦") + 1);
                helpText.setText("Pasted from clipboard");
                Thread ts = new Thread(() -> {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        Platform.runLater(() -> {
                            helpText.setText(operations());
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                ts.start();
            }
            case "cut" -> {
                System.out.println("CUT");
                content.putString(inject);
                Clipboard.getSystemClipboard().setContent(content);
                pressClear();
                helpText.setText("Cut to clipboard");
                Thread ts = new Thread(() -> {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        Platform.runLater(() -> {
                            helpText.setText(operations());
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                ts.start();
            }
            case "save" -> {
                System.out.println("SAVE TO MEMORY");
                memory.updateMemory(inject, equationText);
                helpText.setText("Saved to memory");
                Thread ts = new Thread(() -> {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        Platform.runLater(() -> {
                            helpText.setText(operations());
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                ts.start();
            }
        }
    }

    //just calls start
    public static void main(String[] args)
    {
        launch(args);
    }
}
