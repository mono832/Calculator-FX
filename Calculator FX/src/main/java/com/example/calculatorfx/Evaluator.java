package com.example.calculatorfx;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.apache.commons.math3.special.Gamma;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.math.BigInteger;

//For evaluating things... I know right. What a shocker
public class Evaluator
{
    /**
     * Evaluates the basic mathematical string
     * @param equationText the textField that holds the function
     * @param answerText the text that gets the result
     */
    public void evaluate(TextField equationText, Text answerText)
    {
        equationText.setText(syntaxCheck(equationText.getText()));
        String correctedString=mathSyntaxCorrector(equationText.getText());

        //Using the graal.js dependency js & js-sriptengine
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("graal.js");
        try {
            System.out.println("evaluate "+engine.eval(correctedString));
            answerText.setText(engine.eval(correctedString).toString());
            double answerNum=Double.parseDouble(answerText.getText());

            answerText.setText(reScaleDouble(answerNum));

        } catch (Exception e) {
            e.printStackTrace();
            answerText.setText("ERROR!");
        }
    }

    /**
     * A copy of evaluate that will return an evaluated String. Or an error string
     * @param equationText a String copy of equationText
     * @return a evaluated String or error message
     */
    public String getEvaluate(String equationText)
    {
        equationText=syntaxCheck(equationText);
        String correctedString=mathSyntaxCorrector(equationText);
        String answer;

        //Using the graal.js dependency js & js-sriptengine
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("graal.js");
        try {
            System.out.println("get evaluate "+engine.eval(correctedString));
            answer=engine.eval(correctedString).toString();

            double answerNum=Double.parseDouble(answer);
            return reScaleDouble(answerNum);

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR!";
        }
    }

    /**
     * Evaluates the common mathematical functions
     * @param equationText the textField being changed and drawn from
     * @param answerText gets the final answer or error once it's been rescaled
     * @param fromThis determines what Common function is triggered
     */
    public void common(TextField equationText, Text answerText, String fromThis)
    {
        //formulas found from across multiple sources, and some using basic mathematics
        try
        {
            double answerNum;
            switch (fromThis) {
                case "Fact! & Gamma" -> {
                    evaluate(equationText, answerText);
                    answerNum = Double.parseDouble(answerText.getText());

                    //Using the apache-commons-math3 dependency
                    answerNum=Gamma.gamma(answerNum+1);
                    answerText.setText(reScaleDouble(answerNum));
                }
                case "Square²" -> {
                    evaluate(equationText, answerText);
                    answerNum = Double.parseDouble(answerText.getText());
                    answerNum = Math.pow(answerNum, 2);
                    answerText.setText(reScaleDouble(answerNum));
                }
                case "Cube³" -> {
                    evaluate(equationText, answerText);
                    answerNum = Double.parseDouble(answerText.getText());
                    answerNum = Math.pow(answerNum, 3);
                    answerText.setText(reScaleDouble(answerNum));
                }
                case "Custom power ^ʸ" -> {
                    String[] splitter = equationText.getText().split("\\^");
                    equationText.setText(syntaxCheck(splitter[0]) + "^" + syntaxCheck(splitter[1]));
                    splitter[0] = getEvaluate(splitter[0]);
                    splitter[1] = getEvaluate(splitter[1]);
                    answerNum = Math.pow(Double.parseDouble(splitter[0]), Double.parseDouble(splitter[1]));
                    if (Double.isNaN(answerNum)) {
                        //works with i
                        splitter[0] = splitter[0].replace("-", "");
                        answerNum = Math.pow(Double.parseDouble(splitter[0]), Double.parseDouble(splitter[1]));
                        answerText.setText(reScaleDouble(answerNum) + " i");
                        return;
                    }
                    answerText.setText(reScaleDouble(answerNum));
                }
                case "e^ᵡ" -> {
                    evaluate(equationText, answerText);
                    answerNum = Double.parseDouble(answerText.getText());
                    answerNum = Math.exp(answerNum);
                    answerText.setText(reScaleDouble(answerNum));
                }
                case "Square root √" -> {
                    evaluate(equationText, answerText);
                    answerNum = Double.parseDouble(answerText.getText());
                    if (answerNum < 0) {
                        //works with i
                        answerNum = answerNum * -1;
                        answerNum = Math.sqrt(answerNum);
                        answerText.setText(reScaleDouble(answerNum) + " i");
                        return;
                    }
                    answerNum = Math.sqrt(answerNum);
                    answerText.setText(reScaleDouble(answerNum));
                }
                case "Cube root ∛" -> {
                    evaluate(equationText, answerText);
                    answerNum = Double.parseDouble(answerText.getText());
                    answerNum = Math.cbrt(answerNum);
                    answerText.setText(reScaleDouble(answerNum));
                }
                case "Custom root ʸ√" -> {
                    String[] splitter = equationText.getText().split(",");
                    equationText.setText(syntaxCheck(splitter[0]) + "," + syntaxCheck(splitter[1]));
                    splitter[0] = getEvaluate(splitter[0]);
                    splitter[1] = getEvaluate(splitter[1]);
                    answerNum = Math.pow(Double.parseDouble(splitter[1]), 1 / Double.parseDouble(splitter[0]));
                    if (Double.isNaN(answerNum)) {
                        //works with i
                        splitter[1] = splitter[1].replace("-", "");
                        answerNum = Math.pow(Double.parseDouble(splitter[1]), 1 / Double.parseDouble(splitter[0]));
                        answerText.setText(reScaleDouble(answerNum) + " i");
                        return;
                    }
                    answerText.setText(reScaleDouble(answerNum));
                }
                case "Abs ||" -> {
                    evaluate(equationText, answerText);
                    answerNum = Double.parseDouble(answerText.getText());
                    answerNum = Math.abs(answerNum);
                    answerText.setText(reScaleDouble(answerNum));
                }
                case "Ceil ⌈⌉" -> {
                    evaluate(equationText, answerText);
                    answerNum = Double.parseDouble(answerText.getText());
                    answerNum = Math.ceil(answerNum);
                    answerText.setText(reScaleDouble(answerNum));
                }
                case "Floor ⌊⌋" -> {
                    evaluate(equationText, answerText);
                    answerNum = Double.parseDouble(answerText.getText());
                    answerNum = Math.floor(answerNum);
                    answerText.setText(reScaleDouble(answerNum));
                }
                case "log" -> {
                    //log is log10
                    evaluate(equationText, answerText);
                    answerNum = Double.parseDouble(answerText.getText());
                    answerNum = Math.log10(answerNum);
                    answerText.setText(reScaleDouble(answerNum));
                }
                case "ln" -> {
                    //ln is log
                    evaluate(equationText, answerText);
                    answerNum = Double.parseDouble(answerText.getText());
                    answerNum = Math.log(answerNum);
                    answerText.setText(reScaleDouble(answerNum));
                }
                case "Exp"->{
                    // EXP is xEy x is number multiplied y(number of times 10 is added. Must be whole)
                    String[] splitter = equationText.getText().split(",");
                    splitter[1] = getEvaluate(splitter[1]);
                    long timesTen=Math.round(Double.parseDouble(splitter[1]));
                    equationText.setText(syntaxCheck(splitter[0]) + "," + timesTen);
                    splitter[0] = getEvaluate(splitter[0]);
                    answerNum=Double.parseDouble(splitter[0]);
                    while (timesTen>0)
                    {
                        answerNum*=10;
                        timesTen--;
                    }
                    while (timesTen<0)
                    {
                        System.out.println("Ding");
                        answerNum/=10;
                        timesTen++;
                    }
                    answerText.setText(reScaleDouble(answerNum));
                }
                case "nPr"->{
                    // nPr = n!/(n-r)! if n=answerNum n,r>=0 and are whole
                    String[] splitter = equationText.getText().split(",");
                    splitter[0] = getEvaluate(splitter[0]);
                    splitter[1] = getEvaluate(splitter[1]);
                    answerNum=Double.parseDouble(splitter[0]);
                    double r=Double.parseDouble(splitter[1]);
                    answerNum=Math.round(answerNum);
                    r=Math.round(r);
                    equationText.setText(answerNum + "," + r);
                    if(answerNum<0 || r<0)
                        throw new Exception();
                    answerNum=Gamma.gamma(answerNum+1)/Gamma.gamma((answerNum-r)+1);
                    answerText.setText(reScaleDouble(answerNum));
                }
                case "nCr"->{
                    //nCr = n!/[r! (n-r)!] if n=answerNum & n,r>=0 and are whole
                    String[] splitter = equationText.getText().split(",");
                    splitter[0] = getEvaluate(splitter[0]);
                    splitter[1] = getEvaluate(splitter[1]);
                    answerNum=Double.parseDouble(splitter[0]);
                    double r=Double.parseDouble(splitter[1]);
                    answerNum=Math.round(answerNum);
                    r=Math.round(r);
                    equationText.setText(answerNum + "," + r);
                    if(answerNum<0 || r<0)
                        throw new Exception();
                    answerNum=Gamma.gamma(answerNum+1)/(Gamma.gamma(r+1)*Gamma.gamma((answerNum-r)+1));
                    answerText.setText(reScaleDouble(answerNum));
                }
                case "decimal ⮞ fraction" -> {
                    evaluate(equationText, answerText);
                    answerNum = Double.parseDouble(answerText.getText());
                    int denominator=1;

                    while (!(((answerNum*denominator) % 1) == 0))
                    {
                        denominator++;
                        if(denominator>=78256779)
                        {
                            //too big, shut it down
                            throw new Exception();
                        }
                    }
                    answerText.setText(reScaleDouble(answerNum*denominator)+"/"+denominator);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            answerText.setText("ERROR!");
        }
    }

    /**
     * Properly evaluates trig functions
     * @param equationText the textField being changed and drawn from
     * @param answerText gets the final answer or error once it's been rescaled
     * @param fromThis determines what trig function is triggered
     * @param rDG determines weather to use degree, radians, or grad
     */
    public void trig(TextField equationText, Text answerText, String fromThis, String rDG)
    {
        try
        {
            evaluate(equationText,answerText);
            double answerNum=Double.parseDouble(answerText.getText());
            switch (fromThis)
            {
                case "Sin":
                    if(rDG.equals("Radians"))
                        answerNum=Math.sin(answerNum);
                    else if (rDG.equals("Degrees"))
                        answerNum=Math.sin(Math.toRadians(answerNum));
                    else
                        answerNum=Math.sin(Math.PI * answerNum / 200);
                    break;
                case "Sin¯¹":
                    if(rDG.equals("Radians"))
                        answerNum=Math.asin(answerNum);
                    else if (rDG.equals("Degrees"))
                        answerNum=Math.toDegrees(Math.asin(answerNum));
                    else
                        answerNum=200/Math.PI *Math.asin(answerNum);
                    break;
                case "Sinh":
                    answerNum=Math.sinh(answerNum);
                    break;
                case "Sinh¯¹":
                    answerNum=Math.log(answerNum + Math.sqrt(answerNum*answerNum + 1.0));
                    break;
                case "Csc":
                    if(rDG.equals("Radians"))
                        answerNum=1/Math.sin(answerNum);
                    else if (rDG.equals("Degrees"))
                        answerNum=1/Math.sin(Math.toRadians(answerNum));
                    else
                        answerNum=1/Math.sin(Math.PI * answerNum / 200);
                    break;
                case "Csc¯¹":
                    if(rDG.equals("Radians"))
                        answerNum=Math.asin(1/answerNum);
                    else if (rDG.equals("Degrees"))
                        answerNum=Math.toDegrees(Math.asin(1/answerNum));
                    else
                        answerNum=200/Math.PI *Math.asin(1/answerNum);
                    break;
                case "Csch":
                    answerNum=1/Math.sinh(answerNum);
                    break;
                case "Csch¯¹":
                    answerNum=Math.log(1/answerNum + Math.sqrt(1/(answerNum*answerNum) + 1.0));
                    break;
                case "Cos":
                    if(rDG.equals("Radians"))
                        answerNum=Math.cos(answerNum);
                    else if (rDG.equals("Degrees"))
                        answerNum=Math.cos(Math.toRadians(answerNum));
                    else
                        answerNum=Math.cos(Math.PI * answerNum / 200);
                    break;
                case "Cos¯¹":
                    //fun fact cos¯¹(-1) in radians is pie
                    if(rDG.equals("Radians"))
                        answerNum=Math.acos(answerNum);
                    else if (rDG.equals("Degrees"))
                        answerNum=Math.toDegrees(Math.acos(answerNum));
                    else
                        answerNum=200/Math.PI *Math.acos(answerNum);
                    break;
                case "Cosh":
                    answerNum=Math.cosh(answerNum);
                    break;
                case "Cosh¯¹":
                    answerNum=Math.log(answerNum+Math.sqrt(answerNum*answerNum-1));
                    break;
                case "Sec":
                    if(rDG.equals("Radians"))
                        answerNum=1/Math.cos(answerNum);
                    else if (rDG.equals("Degrees"))
                        answerNum=1/Math.cos(Math.toRadians(answerNum));
                    else
                        answerNum=1/Math.cos(Math.PI * answerNum / 200);
                    break;
                case "Sec¯¹":
                    //fun fact sec¯¹(-1) in radians is pie
                    if(rDG.equals("Radians"))
                        answerNum=Math.acos(1/answerNum);
                    else if (rDG.equals("Degrees"))
                        answerNum=Math.toDegrees(Math.acos(1/answerNum));
                    else
                        answerNum=200/Math.PI *Math.acos(1/answerNum);
                    break;
                case "Sech":
                    answerNum=1/Math.cosh(answerNum);
                    break;
                case "Sech¯¹":
                    answerNum=Math.log(1/answerNum+Math.sqrt(1/(answerNum*answerNum)-1));
                    break;
                case "Tan":
                    if(rDG.equals("Radians"))
                        answerNum=Math.tan(answerNum);
                    else if (rDG.equals("Degrees"))
                        answerNum=Math.tan(Math.toRadians(answerNum));
                    else
                        answerNum=Math.tan(Math.PI * answerNum / 200);
                    break;
                case "Tan¯¹":
                    if(rDG.equals("Radians"))
                        answerNum=Math.atan(answerNum);
                    else if (rDG.equals("Degrees"))
                        answerNum=Math.toDegrees(Math.atan(answerNum));
                    else
                        answerNum=200/Math.PI *Math.atan(answerNum);
                    break;
                case "Tanh":
                    answerNum=Math.tanh(answerNum);
                    break;
                case "Tanh¯¹":
                    answerNum=(1.0/2)*Math.log((1+answerNum)/(1-answerNum));
                    break;
                case "Cot":
                    if(rDG.equals("Radians"))
                        answerNum=1/Math.tan(answerNum);
                    else if (rDG.equals("Degrees"))
                        answerNum=1/Math.tan(Math.toRadians(answerNum));
                    else
                        answerNum=1/Math.tan(Math.PI * answerNum / 200);
                    break;
                case "Cot¯¹":
                    if(rDG.equals("Radians"))
                        answerNum=Math.atan(1/answerNum);
                    else if (rDG.equals("Degrees"))
                        answerNum=Math.toDegrees(Math.atan(1/answerNum));
                    else
                        answerNum=200/Math.PI *Math.atan(1/answerNum);
                    break;
                case "Coth":
                    answerNum=1/Math.tanh(answerNum);
                    break;
                case "Coth¯¹":
                    answerNum=(1.0/2)*Math.log((answerNum+1)/(answerNum-1));
                    break;
            }
            answerText.setText(reScaleDouble(answerNum));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            answerText.setText("ERROR!");
        }
    }

    /**
     * Evaluates the geometric mathematical functions
     * @param equationText the textField being changed and drawn from
     * @param answerText gets the final answer or error once it's been rescaled
     * @param fromThis determines what Common function is triggered
     */
    public void geometry(TextField equationText, Text answerText, String fromThis)
    {
        //formulas provided by magoosh.com, google, and calculator soup
        try
        {
            double answerNum=0;
            String[] splitter;
            double[] splitterNum=new double[4];
            switch (fromThis) {
                case "Area of square":
                    answerNum=Double.parseDouble(equationText.getText());
                    answerNum=answerNum*answerNum;
                    break;
                case "Area of triangle":
                    splitter = equationText.getText().split(",");
                    equationText.setText(syntaxCheck(splitter[0]) + "," + syntaxCheck(splitter[1]));
                    splitter[0] = getEvaluate(splitter[0]);
                    splitter[1] = getEvaluate(splitter[1]);
                    splitterNum[0]=Double.parseDouble(splitter[0]);
                    splitterNum[1]=Double.parseDouble(splitter[1]);
                    answerNum = (splitterNum[0]*splitterNum[1])/2.0;
                    break;
                case "Area of rectangle":
                    splitter = equationText.getText().split(",");
                    equationText.setText(syntaxCheck(splitter[0]) + "," + syntaxCheck(splitter[1]));
                    splitter[0] = getEvaluate(splitter[0]);
                    splitter[1] = getEvaluate(splitter[1]);
                    splitterNum[0]=Double.parseDouble(splitter[0]);
                    splitterNum[1]=Double.parseDouble(splitter[1]);
                    answerNum = splitterNum[0]*splitterNum[1];
                    break;
                case "Area of trapezoid":
                    splitter = equationText.getText().split(",");
                    equationText.setText(syntaxCheck(splitter[0]) + "," + syntaxCheck(splitter[1])+","+syntaxCheck(splitter[2]));
                    splitter[0] = getEvaluate(splitter[0]);
                    splitter[1] = getEvaluate(splitter[1]);
                    splitter[2] = getEvaluate(splitter[2]);
                    splitterNum[0]=Double.parseDouble(splitter[0]);
                    splitterNum[1]=Double.parseDouble(splitter[1]);
                    splitterNum[2]=Double.parseDouble(splitter[2]);
                    answerNum = (splitterNum[0]+splitterNum[1])/2.0;
                    answerNum=answerNum*splitterNum[2];
                    break;
                case "Area of circle":
                    answerNum=Double.parseDouble(equationText.getText());
                    answerNum=answerNum*answerNum;
                    answerNum=Math.PI*answerNum;
                    break;
                case "Circumference of circle":
                    answerNum=Double.parseDouble(equationText.getText());
                    answerNum=2.0*Math.PI*answerNum;
                    break;
                case "Volume of cube":
                    answerNum=Double.parseDouble(equationText.getText());
                    answerNum=answerNum*answerNum*answerNum;
                    break;
                case "Volume of rectangular solid":
                    splitter = equationText.getText().split(",");
                    equationText.setText(syntaxCheck(splitter[0]) + "," + syntaxCheck(splitter[1])+","+syntaxCheck(splitter[2]));
                    splitter[0] = getEvaluate(splitter[0]);
                    splitter[1] = getEvaluate(splitter[1]);
                    splitter[2] = getEvaluate(splitter[2]);
                    splitterNum[0]=Double.parseDouble(splitter[0]);
                    splitterNum[1]=Double.parseDouble(splitter[1]);
                    splitterNum[2]=Double.parseDouble(splitter[2]);
                    answerNum = splitterNum[0]*splitterNum[1]*splitterNum[2];
                    break;
                case "Volume of cylinder":
                    splitter = equationText.getText().split(",");
                    equationText.setText(syntaxCheck(splitter[0]) + "," + syntaxCheck(splitter[1]));
                    splitter[0] = getEvaluate(splitter[0]);
                    splitter[1] = getEvaluate(splitter[1]);
                    splitterNum[0]=Double.parseDouble(splitter[0]);
                    splitterNum[1]=Double.parseDouble(splitter[1]);
                    answerNum = splitterNum[0]*splitterNum[0];
                    answerNum=(Math.PI*answerNum)*splitterNum[1];
                    break;
                case "Slope M":
                    splitter = equationText.getText().split(",");
                    equationText.setText(syntaxCheck(splitter[0]) + "," + syntaxCheck(splitter[1])+
                            ","+syntaxCheck(splitter[2])+","+syntaxCheck(splitter[3]));
                    splitter[0] = getEvaluate(splitter[0]);
                    splitter[1] = getEvaluate(splitter[1]);
                    splitter[2] = getEvaluate(splitter[2]);
                    splitter[3] = getEvaluate(splitter[3]);
                    splitterNum[0]=Double.parseDouble(splitter[0]);
                    splitterNum[1]=Double.parseDouble(splitter[1]);
                    splitterNum[2]=Double.parseDouble(splitter[2]);
                    splitterNum[3]=Double.parseDouble(splitter[3]);
                    answerNum = (splitterNum[0]-splitterNum[1])/(splitterNum[2]-splitterNum[3]);
                    break;
                case "Slope intercept form B":
                    splitter = equationText.getText().split(",");
                    equationText.setText(syntaxCheck(splitter[0]) + "," + syntaxCheck(splitter[1])+
                            ","+syntaxCheck(splitter[2])+","+syntaxCheck(splitter[3]));
                    splitter[0] = getEvaluate(splitter[0]);
                    splitter[1] = getEvaluate(splitter[1]);
                    splitter[2] = getEvaluate(splitter[2]);
                    splitter[3] = getEvaluate(splitter[3]);
                    splitterNum[0]=Double.parseDouble(splitter[0]);
                    splitterNum[1]=Double.parseDouble(splitter[1]);
                    splitterNum[2]=Double.parseDouble(splitter[2]);
                    splitterNum[3]=Double.parseDouble(splitter[3]);
                    answerNum = (splitterNum[0]-splitterNum[1])/(splitterNum[2]-splitterNum[3]);
                    double x1=-splitterNum[3];
                    double y1=-splitterNum[1];
                    answerNum=(answerNum*x1)-y1;
                    break;

            }
            answerText.setText(reScaleDouble(answerNum));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            answerText.setText("ERROR!");
        }
    }

    /**
     * finds the standard deviation of a string
     * @param equationText the textField being drawn from
     * @param answerText gets the final answer or error
     */
    public void standardDeviation(TextField equationText, Text answerText)
    {
        //Formulas provided by, math is fun
        try {
            String answer="";
            String[] splitter = equationText.getText().split(",");
            double[]splitNum=new double[splitter.length];
            for(int i=0;i<splitter.length;i++)
            {
                answer+=syntaxCheck(splitter[i])+",";
                splitter[i]=getEvaluate(splitter[i]);
                splitNum[i]=Double.parseDouble(splitter[i]);
            }
            answer=answer.substring(0,answer.length()-1);
            equationText.setText(answer);

            double sum=0;
            for(int i=0; i<splitNum.length;i++)
            {
                sum+=splitNum[i];
            }
            double mean=sum/splitNum.length;
            sum=0;

            for(int i=0; i<splitNum.length;i++)
            {
                splitNum[i]=splitNum[i]-mean;
                splitNum[i]=Math.pow(splitNum[i],2);
                sum+=splitNum[i];
            }
            double variance=sum/splitNum.length;

            variance=Math.sqrt(variance);
            answerText.setText(reScaleDouble(variance));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            answerText.setText("ERROR!");
        }
    }

    /**
     * finds the quadratic of a string
     * @param equationText the textField being drawn from
     * @param answerText gets the final answer or error
     */
    public void quadratic(TextField equationText, Text answerText)
    {
        //Formulas provided by, calculator soup
        try {
            String[] splitter = equationText.getText().split(",");
            String copy= syntaxCheck(splitter[0])+","+syntaxCheck(splitter[1])+","+syntaxCheck(splitter[2]);
            splitter[0] = getEvaluate(splitter[0]);
            splitter[1] = getEvaluate(splitter[1]);
            splitter[2] = getEvaluate(splitter[2]);
            double a=Double.parseDouble(splitter[0]);
            double b=Double.parseDouble(splitter[1]);
            double c=Double.parseDouble(splitter[2]);

            equationText.setText(copy);

            //a cannot be 0
            if(a==0)
                throw new Exception();

            double preRoot=Math.pow(b,2)-4*a*c;
            double x1;
            double x2;

            //if preRoot=0 1 root if >0 2 roots if <0 2 imaginary roots
            if(preRoot==0)
            {
                x1=-b/(2*a);
                answerText.setText("x= "+reScaleDouble(x1)+"\n");
            }
            else if (preRoot<0)
            {
                //works with i
                preRoot*=-1;
                preRoot=Math.sqrt(preRoot)/(2*a);
                x1=-b/(2*a);
                answerText.setText("x1= "+reScaleDouble(x1)+" + "+reScaleDouble(preRoot)+" i\nx2= "+
                        reScaleDouble(x1)+" - "+reScaleDouble(preRoot)+" i");
            }
            else
            {
                x1=(-b+Math.sqrt(preRoot))/(2*a);
                x2=(-b-Math.sqrt(preRoot))/(2*a);
                answerText.setText("x1= "+reScaleDouble(x1)+"\nx2= "+ reScaleDouble(x2));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            answerText.setText("ERROR!\n");
        }
    }

    /**
     * finds the cubic of a string (but only the first and too complex to integrate i)
     * @param equationText the textField being drawn from
     * @param answerText gets the final answer or error
     */
    public void cubic(TextField equationText, Text answerText)
    {
        //Formulas provided by, reddit
        try {
            String[] splitter = equationText.getText().split(",");
            String copy= syntaxCheck(splitter[0])+","+syntaxCheck(splitter[1])+","+syntaxCheck(splitter[2])
                    +","+syntaxCheck(splitter[3]);
            splitter[0] = getEvaluate(splitter[0]);
            splitter[1] = getEvaluate(splitter[1]);
            splitter[2] = getEvaluate(splitter[2]);
            splitter[3] = getEvaluate(splitter[3]);
            double a=Double.parseDouble(splitter[0]);
            double b=Double.parseDouble(splitter[1]);
            double c=Double.parseDouble(splitter[2]);
            double d=Double.parseDouble(splitter[3]);

            equationText.setText(copy);

            //a cannot be 0
            if(a==0)
                throw new Exception();

            //the horror
            double cube1=Math.cbrt((1/2.0)*(2*Math.pow(b,3)-9*a*b*c+27*Math.pow(a,2)*d
                    +Math.sqrt(Math.pow((2*Math.pow(b,3)-9*a*b*c+27*Math.pow(a,2)*d),2)-4
                    *Math.pow((Math.pow(b,2)-3*a*c),3))));
            double cube2=Math.cbrt((1/2.0)*(2*Math.pow(b,3)-9*a*b*c+27*Math.pow(a,2)*d
                    -Math.sqrt(Math.pow((2*Math.pow(b,3)-9*a*b*c+27*Math.pow(a,2)*d),2)-4
                    *Math.pow((Math.pow(b,2)-3*a*c),3))));
            double x1=-(b/(3*a))-(1/(3*a))*cube1-(1/(3*a))*cube2;
            System.out.println(cube1);
            answerText.setText("x1= "+reScaleDouble(x1));

        }
        catch (Exception e)
        {
            e.printStackTrace();
            answerText.setText("ERROR!");
        }
    }

    /**
     * Converts decimal(whole numbers) to hex, binary, octal and vise versa
     * (I would do ascii here too but that would require a massive scene change, so it gets its own spot)
     * @param equationText the textField being drawn from
     * @param answerText gets the final answer or error
     * @param fromThis determines what function is triggered
     * @param toThis determines what function is triggered
     */
    public void programmerCalc(TextField equationText, Text answerText, String fromThis, String toThis)
    {
        //radix of 2=binary 8=octal 16=hex 10=dec but its not shown
        try
        {
            String answer=equationText.getText();
            int answerNum;

            switch (fromThis)
            {
                case "Dec":
                    switch (toThis) {
                        case "Bin" -> {
                            answerNum = Integer.parseInt(answer);
                            answer = Integer.toBinaryString(answerNum);
                        }
                        case "Hex" -> {
                            answerNum = Integer.parseInt(answer);
                            answer = Integer.toHexString(answerNum);
                        }
                        case "Oct" -> {
                            answerNum = Integer.parseInt(answer);
                            answer = Integer.toOctalString(answerNum);
                        }
                        default -> answer="";
                    }
                    break;
                case "Bin":
                    switch (toThis) {
                        case "Dec" -> {
                            answerNum = new BigInteger(answer, 2).intValue();
                            answer = String.valueOf(answerNum);
                        }
                        case "Hex" -> answer = new BigInteger(answer, 2).toString(16);
                        case "Oct" -> answer = new BigInteger(answer, 2).toString(8);
                        default -> answer="";
                    }
                    break;
                case "Hex":
                    switch (toThis) {
                        case "Dec" -> {
                            answerNum = new BigInteger(answer, 16).intValue();
                            answer = String.valueOf(answerNum);
                        }
                        case "Bin" -> answer = new BigInteger(answer, 16).toString(2);
                        case "Oct" -> answer = new BigInteger(answer, 16).toString(8);
                        default -> answer="";
                    }
                    break;
                case "Oct":
                    switch (toThis) {
                        case "Dec" -> {
                            answerNum = new BigInteger(answer, 8).intValue();
                            answer = String.valueOf(answerNum);
                        }
                        case "Bin" -> answer = new BigInteger(answer, 8).toString(2);
                        case "Hex" -> answer = new BigInteger(answer, 8).toString(16);
                        default -> answer="";
                    }
                    break;
            }
            answerText.setText(answer);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            answerText.setText("ERROR!");
        }
    }

    /**
     * Turns a string into ascii and adds it all up
     * @param equationText the textField being drawn from
     * @param answerText gets the final answer or error
     */
    public void asciiToDec(TextField equationText, Text answerText)
    {
        try {
            String answer=equationText.getText();
            int toAscii=0;
            char temp;
            for(int i=0;i<answer.length();i++)
            {
                temp=answer.charAt(i);
                toAscii+= temp;
            }
            answerText.setText(String.valueOf(toAscii));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            answerText.setText("ERROR!");
        }
    }

    /**
     * Converts between data types of 2^10
     * @param equationText the textField being drawn from
     * @param answerText gets the final answer or error
     * @param fromThis determines what function is triggered
     * @param toThis determines what function is triggered
     */
    public void dataConverterBase2(TextField equationText, Text answerText, String fromThis, String toThis)
    {
        //I've stumbled across an age-old war of 2^10 or 10^3
        try
        {
            double answerNum=Double.parseDouble(equationText.getText());
            switch (fromThis)
            {
                case "Bit":
                    switch (toThis)
                    {
                        case "Byte":
                            answerNum/=8;
                            break;
                        case "Kilobyte":
                            answerNum/=Math.pow(2,13);
                            break;
                        case "Megabyte":
                            answerNum/=Math.pow(2,23);
                            break;
                        case "Gigabyte":
                            answerNum/=Math.pow(2,33);
                            break;
                        case "Terabyte":
                            answerNum/=Math.pow(2,43);
                            break;
                        case "Petabyte":
                            answerNum/=Math.pow(2,53);
                            break;
                        case "Exabyte":
                            answerNum/=Math.pow(2,63);
                            break;
                        case "Zettabyte":
                            answerNum/=Math.pow(2,73);
                            break;
                        case "Yottabyte":
                            answerNum/=Math.pow(2,83);
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
                case "Byte":
                    switch (toThis)
                    {
                        case "Bit":
                            answerNum*=8;
                            break;
                        case "Kilobyte":
                            answerNum/=Math.pow(2,10);
                            break;
                        case "Megabyte":
                            answerNum/=Math.pow(2,20);
                            break;
                        case "Gigabyte":
                            answerNum/=Math.pow(2,30);
                            break;
                        case "Terabyte":
                            answerNum/=Math.pow(2,40);
                            break;
                        case "Petabyte":
                            answerNum/=Math.pow(2,50);
                            break;
                        case "Exabyte":
                            answerNum/=Math.pow(2,60);
                            break;
                        case "Zettabyte":
                            answerNum/=Math.pow(2,70);
                            break;
                        case "Yottabyte":
                            answerNum/=Math.pow(2,80);
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
                case "Kilobyte":
                    switch (toThis)
                    {
                        case "Bit":
                            answerNum*=Math.pow(2,13);
                            break;
                        case "Byte":
                            answerNum*=Math.pow(2,10);
                            break;
                        case "Megabyte":
                            answerNum/=Math.pow(2,10);
                            break;
                        case "Gigabyte":
                            answerNum/=Math.pow(2,20);
                            break;
                        case "Terabyte":
                            answerNum/=Math.pow(2,30);
                            break;
                        case "Petabyte":
                            answerNum/=Math.pow(2,40);
                            break;
                        case "Exabyte":
                            answerNum/=Math.pow(2,50);
                            break;
                        case "Zettabyte":
                            answerNum/=Math.pow(2,60);
                            break;
                        case "Yottabyte":
                            answerNum/=Math.pow(2,70);
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
                case "Megabyte":
                    switch (toThis)
                    {
                        case "Bit":
                            answerNum*=Math.pow(2,23);
                            break;
                        case "Byte":
                            answerNum*=Math.pow(2,20);
                            break;
                        case "Kilobyte":
                            answerNum*=Math.pow(2,10);
                            break;
                        case "Gigabyte":
                            answerNum/=Math.pow(2,10);
                            break;
                        case "Terabyte":
                            answerNum/=Math.pow(2,20);
                            break;
                        case "Petabyte":
                            answerNum/=Math.pow(2,30);
                            break;
                        case "Exabyte":
                            answerNum/=Math.pow(2,40);
                            break;
                        case "Zettabyte":
                            answerNum/=Math.pow(2,50);
                            break;
                        case "Yottabyte":
                            answerNum/=Math.pow(2,60);
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
                case "Gigabyte":
                    switch (toThis)
                    {
                        case "Bit":
                            answerNum*=Math.pow(2,33);
                            break;
                        case "Byte":
                            answerNum*=Math.pow(2,30);
                            break;
                        case "Kilobyte":
                            answerNum*=Math.pow(2,20);
                            break;
                        case "Megabyte":
                            answerNum*=Math.pow(2,10);
                            break;
                        case "Terabyte":
                            answerNum/=Math.pow(2,10);
                            break;
                        case "Petabyte":
                            answerNum/=Math.pow(2,20);
                            break;
                        case "Exabyte":
                            answerNum/=Math.pow(2,30);
                            break;
                        case "Zettabyte":
                            answerNum/=Math.pow(2,40);
                            break;
                        case "Yottabyte":
                            answerNum/=Math.pow(2,50);
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
                case "Terabyte":
                    switch (toThis)
                    {
                        case "Bit":
                            answerNum*=Math.pow(2,43);
                            break;
                        case "Byte":
                            answerNum*=Math.pow(2,40);
                            break;
                        case "Kilobyte":
                            answerNum*=Math.pow(2,30);
                            break;
                        case "Megabyte":
                            answerNum*=Math.pow(2,20);
                            break;
                        case "Gigabyte":
                            answerNum*=Math.pow(2,10);
                            break;
                        case "Petabyte":
                            answerNum/=Math.pow(2,10);
                            break;
                        case "Exabyte":
                            answerNum/=Math.pow(2,20);
                            break;
                        case "Zettabyte":
                            answerNum/=Math.pow(2,30);
                            break;
                        case "Yottabyte":
                            answerNum/=Math.pow(2,40);
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
                case "Petabyte":
                    switch (toThis)
                    {
                        case "Bit":
                            answerNum*=Math.pow(2,53);
                            break;
                        case "Byte":
                            answerNum*=Math.pow(2,50);
                            break;
                        case "Kilobyte":
                            answerNum*=Math.pow(2,40);
                            break;
                        case "Megabyte":
                            answerNum*=Math.pow(2,30);
                            break;
                        case "Gigabyte":
                            answerNum*=Math.pow(2,20);
                            break;
                        case "Terabyte":
                            answerNum*=Math.pow(2,10);
                            break;
                        case "Exabyte":
                            answerNum/=Math.pow(2,10);
                            break;
                        case "Zettabyte":
                            answerNum/=Math.pow(2,20);
                            break;
                        case "Yottabyte":
                            answerNum/=Math.pow(2,30);
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
                case "Exabyte":
                    switch (toThis)
                    {
                        case "Bit":
                            answerNum*=Math.pow(2,63);
                            break;
                        case "Byte":
                            answerNum*=Math.pow(2,60);
                            break;
                        case "Kilobyte":
                            answerNum*=Math.pow(2,50);
                            break;
                        case "Megabyte":
                            answerNum*=Math.pow(2,40);
                            break;
                        case "Gigabyte":
                            answerNum*=Math.pow(2,30);
                            break;
                        case "Terabyte":
                            answerNum*=Math.pow(2,20);
                            break;
                        case "Petabyte":
                            answerNum*=Math.pow(2,10);
                            break;
                        case "Zettabyte":
                            answerNum/=Math.pow(2,10);
                            break;
                        case "Yottabyte":
                            answerNum/=Math.pow(2,20);
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
                case "Zettabyte":
                    switch (toThis)
                    {
                        case "Bit":
                            answerNum*=Math.pow(2,73);
                            break;
                        case "Byte":
                            answerNum*=Math.pow(2,70);
                            break;
                        case "Kilobyte":
                            answerNum*=Math.pow(2,60);
                            break;
                        case "Megabyte":
                            answerNum*=Math.pow(2,50);
                            break;
                        case "Gigabyte":
                            answerNum*=Math.pow(2,40);
                            break;
                        case "Terabyte":
                            answerNum*=Math.pow(2,30);
                            break;
                        case "Petabyte":
                            answerNum*=Math.pow(2,20);
                            break;
                        case "Exabyte":
                            answerNum*=Math.pow(2,10);
                            break;
                        case "Yottabyte":
                            answerNum/=Math.pow(2,10);
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
                case "Yottabyte":
                    switch (toThis)
                    {
                        case "Bit":
                            answerNum*=Math.pow(2,83);
                            break;
                        case "Byte":
                            answerNum*=Math.pow(2,80);
                            break;
                        case "Kilobyte":
                            answerNum*=Math.pow(2,70);
                            break;
                        case "Megabyte":
                            answerNum*=Math.pow(2,60);
                            break;
                        case "Gigabyte":
                            answerNum*=Math.pow(2,50);
                            break;
                        case "Terabyte":
                            answerNum*=Math.pow(2,40);
                            break;
                        case "Petabyte":
                            answerNum*=Math.pow(2,30);
                            break;
                        case "Exabyte":
                            answerNum*=Math.pow(2,20);
                            break;
                        case "Zettabyte":
                            answerNum*=Math.pow(2,10);
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
            }
            answerText.setText(reScaleDouble(answerNum));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            answerText.setText("ERROR!");
        }
    }

    /**
     * Converts between data types of 10^3
     * @param equationText the textField being drawn from
     * @param answerText gets the final answer or error
     * @param fromThis determines what function is triggered
     * @param toThis determines what function is triggered
     */
    public void dataConverterBase10(TextField equationText, Text answerText, String fromThis, String toThis)
    {
        //I've stumbled across an age-old war of 2^10 or 10^3
        try
        {
            double answerNum=Double.parseDouble(equationText.getText());
            switch (fromThis)
            {
                case "Bit":
                    switch (toThis)
                    {
                        case "Byte":
                            answerNum/=8;
                            break;
                        case "Kilobyte":
                            answerNum/=Math.pow(20,3);
                            break;
                        case "Megabyte":
                            answerNum/=Math.pow(200,3);
                            break;
                        case "Gigabyte":
                            answerNum/=Math.pow(2000,3);
                            break;
                        case "Terabyte":
                            answerNum/=Math.pow(20000,3);
                            break;
                        case "Petabyte":
                            answerNum/=Math.pow(200000,3);
                            break;
                        case "Exabyte":
                            answerNum/=Math.pow(2000000,3);
                            break;
                        case "Zettabyte":
                            answerNum/=Math.pow(20000000,3);
                            break;
                        case "Yottabyte":
                            answerNum/=Math.pow(200000000,3);
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
                case "Byte":
                    switch (toThis)
                    {
                        case "Bit":
                            answerNum*=8;
                            break;
                        case "Kilobyte":
                            answerNum/=Math.pow(10,3);
                            break;
                        case "Megabyte":
                            answerNum/=Math.pow(10,6);
                            break;
                        case "Gigabyte":
                            answerNum/=Math.pow(10,9);
                            break;
                        case "Terabyte":
                            answerNum/=Math.pow(10,12);
                            break;
                        case "Petabyte":
                            answerNum/=Math.pow(10,15);
                            break;
                        case "Exabyte":
                            answerNum/=Math.pow(10,18);
                            break;
                        case "Zettabyte":
                            answerNum/=Math.pow(10,21);
                            break;
                        case "Yottabyte":
                            answerNum/=Math.pow(10,24);
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
                case "Kilobyte":
                    switch (toThis)
                    {
                        case "Bit":
                            answerNum*=Math.pow(20,3);
                            break;
                        case "Byte":
                            answerNum*=Math.pow(10,3);
                            break;
                        case "Megabyte":
                            answerNum/=Math.pow(10,3);
                            break;
                        case "Gigabyte":
                            answerNum/=Math.pow(10,6);
                            break;
                        case "Terabyte":
                            answerNum/=Math.pow(10,9);
                            break;
                        case "Petabyte":
                            answerNum/=Math.pow(10,12);
                            break;
                        case "Exabyte":
                            answerNum/=Math.pow(10,15);
                            break;
                        case "Zettabyte":
                            answerNum/=Math.pow(10,18);
                            break;
                        case "Yottabyte":
                            answerNum/=Math.pow(10,21);
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
                case "Megabyte":
                    switch (toThis)
                    {
                        case "Bit":
                            answerNum*=Math.pow(200,3);
                            break;
                        case "Byte":
                            answerNum*=Math.pow(10,6);
                            break;
                        case "Kilobyte":
                            answerNum*=Math.pow(10,3);
                            break;
                        case "Gigabyte":
                            answerNum/=Math.pow(10,3);
                            break;
                        case "Terabyte":
                            answerNum/=Math.pow(10,6);
                            break;
                        case "Petabyte":
                            answerNum/=Math.pow(10,9);
                            break;
                        case "Exabyte":
                            answerNum/=Math.pow(10,12);
                            break;
                        case "Zettabyte":
                            answerNum/=Math.pow(10,15);
                            break;
                        case "Yottabyte":
                            answerNum/=Math.pow(10,18);
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
                case "Gigabyte":
                    switch (toThis)
                    {
                        case "Bit":
                            answerNum*=Math.pow(2000,3);
                            break;
                        case "Byte":
                            answerNum*=Math.pow(10,8);
                            break;
                        case "Kilobyte":
                            answerNum*=Math.pow(10,6);
                            break;
                        case "Megabyte":
                            answerNum*=Math.pow(10,3);
                            break;
                        case "Terabyte":
                            answerNum/=Math.pow(10,3);
                            break;
                        case "Petabyte":
                            answerNum/=Math.pow(10,6);
                            break;
                        case "Exabyte":
                            answerNum/=Math.pow(10,9);
                            break;
                        case "Zettabyte":
                            answerNum/=Math.pow(10,12);
                            break;
                        case "Yottabyte":
                            answerNum/=Math.pow(10,15);
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
                case "Terabyte":
                    switch (toThis)
                    {
                        case "Bit":
                            answerNum*=Math.pow(20000,3);
                            break;
                        case "Byte":
                            answerNum*=Math.pow(10,12);
                            break;
                        case "Kilobyte":
                            answerNum*=Math.pow(10,9);
                            break;
                        case "Megabyte":
                            answerNum*=Math.pow(10,6);
                            break;
                        case "Gigabyte":
                            answerNum*=Math.pow(10,3);
                            break;
                        case "Petabyte":
                            answerNum/=Math.pow(10,3);
                            break;
                        case "Exabyte":
                            answerNum/=Math.pow(10,6);
                            break;
                        case "Zettabyte":
                            answerNum/=Math.pow(10,9);
                            break;
                        case "Yottabyte":
                            answerNum/=Math.pow(10,12);
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
                case "Petabyte":
                    switch (toThis)
                    {
                        case "Bit":
                            answerNum*=Math.pow(200000,3);
                            break;
                        case "Byte":
                            answerNum*=Math.pow(10,15);
                            break;
                        case "Kilobyte":
                            answerNum*=Math.pow(10,12);
                            break;
                        case "Megabyte":
                            answerNum*=Math.pow(10,9);
                            break;
                        case "Gigabyte":
                            answerNum*=Math.pow(10,6);
                            break;
                        case "Terabyte":
                            answerNum*=Math.pow(10,3);
                            break;
                        case "Exabyte":
                            answerNum/=Math.pow(10,3);
                            break;
                        case "Zettabyte":
                            answerNum/=Math.pow(10,6);
                            break;
                        case "Yottabyte":
                            answerNum/=Math.pow(10,9);
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
                case "Exabyte":
                    switch (toThis)
                    {
                        case "Bit":
                            answerNum*=Math.pow(2000000,3);
                            break;
                        case "Byte":
                            answerNum*=Math.pow(10,18);
                            break;
                        case "Kilobyte":
                            answerNum*=Math.pow(10,15);
                            break;
                        case "Megabyte":
                            answerNum*=Math.pow(10,12);
                            break;
                        case "Gigabyte":
                            answerNum*=Math.pow(10,9);
                            break;
                        case "Terabyte":
                            answerNum*=Math.pow(10,6);
                            break;
                        case "Petabyte":
                            answerNum*=Math.pow(10,3);
                            break;
                        case "Zettabyte":
                            answerNum/=Math.pow(10,3);
                            break;
                        case "Yottabyte":
                            answerNum/=Math.pow(10,6);
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
                case "Zettabyte":
                    switch (toThis)
                    {
                        case "Bit":
                            answerNum*=Math.pow(20000000,3);
                            break;
                        case "Byte":
                            answerNum*=Math.pow(10,21);
                            break;
                        case "Kilobyte":
                            answerNum*=Math.pow(10,18);
                            break;
                        case "Megabyte":
                            answerNum*=Math.pow(10,15);
                            break;
                        case "Gigabyte":
                            answerNum*=Math.pow(10,12);
                            break;
                        case "Terabyte":
                            answerNum*=Math.pow(10,9);
                            break;
                        case "Petabyte":
                            answerNum*=Math.pow(10,6);
                            break;
                        case "Exabyte":
                            answerNum*=Math.pow(10,3);
                            break;
                        case "Yottabyte":
                            answerNum/=Math.pow(10,3);
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
                case "Yottabyte":
                    switch (toThis)
                    {
                        case "Bit":
                            answerNum*=Math.pow(200000000,3);
                            break;
                        case "Byte":
                            answerNum*=Math.pow(10,24);
                            break;
                        case "Kilobyte":
                            answerNum*=Math.pow(10,21);
                            break;
                        case "Megabyte":
                            answerNum*=Math.pow(10,18);
                            break;
                        case "Gigabyte":
                            answerNum*=Math.pow(10,15);
                            break;
                        case "Terabyte":
                            answerNum*=Math.pow(10,12);
                            break;
                        case "Petabyte":
                            answerNum*=Math.pow(10,9);
                            break;
                        case "Exabyte":
                            answerNum*=Math.pow(10,6);
                            break;
                        case "Zettabyte":
                            answerNum*=Math.pow(10,3);
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
            }
            answerText.setText(reScaleDouble(answerNum));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            answerText.setText("ERROR!");
        }
    }

    /**
     * Converts between temperature types
     * @param equationText the textField being drawn from
     * @param answerText gets the final answer or error
     * @param fromThis determines what function is triggered
     * @param toThis determines what function is triggered
     */
    public void tempConverter(TextField equationText, Text answerText, String fromThis, String toThis)
    {
        //Formulas provided by, google
        try {
            double answerNum=Double.parseDouble(equationText.getText());
            switch (fromThis)
            {
                case "Celsius":
                    switch (toThis)
                    {
                        case"Fahrenheit":
                            answerNum=(answerNum*(9.0/5.0)+32.0);
                            break;
                        case"Kelvin":
                            answerNum=answerNum+273.15;
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
                case"Fahrenheit":
                    switch (toThis)
                    {
                        case "Celsius":
                            answerNum=(answerNum-32.0)*(5.0/9.0);
                            break;
                        case"Kelvin":
                            answerNum=(answerNum-32.0)*(5.0/9.0)+273.15;
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
                case"Kelvin":
                    switch (toThis)
                    {
                        case "Celsius":
                            answerNum=answerNum-273.15;
                            break;
                        case"Fahrenheit":
                            answerNum=(answerNum-273.15)*(9.0/5.0)+32.0;
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
            }
            answerText.setText(reScaleDouble(answerNum));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            answerText.setText("ERROR!");
        }
    }

    /**
     * Converts between time types
     * @param equationText the textField being drawn from
     * @param answerText gets the final answer or error
     * @param fromThis determines what function is triggered
     * @param toThis determines what function is triggered
     */
    public void timeConverter(TextField equationText, Text answerText, String fromThis, String toThis)
    {
        //Formulas provided by, google (fun fact, Google does it different from MS)
        try
        {
            double answerNum=Double.parseDouble(equationText.getText());
            switch (fromThis)
            {
                case "Seconds":
                    switch (toThis)
                    {
                        case "Minutes":
                            answerNum/=60.0;
                            break;
                        case "Hours":
                            answerNum/=3600.0;
                            break;
                        case "Days":
                            answerNum/=86400.0;
                            break;
                        case "Weeks":
                            answerNum/=604800.0;
                            break;
                        case "Months":
                            answerNum/=2.628e+6;
                            break;
                        case "Years":
                            answerNum/=3.154e+7;
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
                case "Minutes":
                    switch (toThis)
                    {
                        case "Seconds":
                            answerNum*=60.0;
                            break;
                        case "Hours":
                            answerNum/=60.0;
                            break;
                        case "Days":
                            answerNum/=1440.0;
                            break;
                        case "Weeks":
                            answerNum/=10080.0;
                            break;
                        case "Months":
                            answerNum/=43800.0;
                            break;
                        case "Years":
                            answerNum/=525600.0;
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
                case "Hours":
                    switch (toThis)
                    {
                        case "Seconds":
                            answerNum*=3600.0;
                            break;
                        case "Minutes":
                            answerNum*=60.0;
                            break;
                        case "Days":
                            answerNum/=24.0;
                            break;
                        case "Weeks":
                            answerNum/=168.0;
                            break;
                        case "Months":
                            answerNum/=730.0;
                            break;
                        case "Years":
                            answerNum/=8760.0;
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
                case "Days":
                    switch (toThis) {
                        case "Seconds":
                            answerNum*=86400.0;
                            break;
                        case "Minutes":
                            answerNum*=1440.0;
                            break;
                        case "Hours":
                            answerNum*=24.0;
                            break;
                        case "Weeks":
                            answerNum/=7.0;
                            break;
                        case "Months":
                            answerNum/=30.417;
                            break;
                        case "Years":
                            answerNum/=365.0;
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
                case "Weeks":
                    switch (toThis) {
                        case "Seconds":
                            answerNum*=604800.0;
                            break;
                        case "Minutes":
                            answerNum*=10080.0;
                            break;
                        case "Hours":
                            answerNum*=168.0;
                            break;
                        case "Days":
                            answerNum*=7.0;
                            break;
                        case "Months":
                            answerNum/=4.345;
                            break;
                        case "Years":
                            answerNum/=52.143;
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
                case "Months":
                    switch (toThis) {
                        case "Seconds":
                            answerNum*=2.628e+6;
                            break;
                        case "Minutes":
                            answerNum*=43800.0;
                            break;
                        case "Hours":
                            answerNum*=730.0;
                            break;
                        case "Days":
                            answerNum*=30.417;
                            break;
                        case "Weeks":
                            answerNum*=4.345;
                            break;
                        case "Years":
                            answerNum/=12.0;
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
                case "Years":
                    switch (toThis) {
                        case "Seconds":
                            answerNum*=3.154e+7;
                            break;
                        case "Minutes":
                            answerNum*=525600.0;
                            break;
                        case "Hours":
                            answerNum*=8760.0;
                            break;
                        case "Days":
                            answerNum*=365.0;
                            break;
                        case "Weeks":
                            answerNum*=52.143;
                            break;
                        case "Months":
                            answerNum*=12.0;
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
            }
            answerText.setText(reScaleDouble(answerNum));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            answerText.setText("ERROR!");
        }
    }

    /**
     * Converts between angle types
     * @param equationText the textField being drawn from
     * @param answerText gets the final answer or error
     * @param fromThis determines what function is triggered
     * @param toThis determines what function is triggered
     */
    public void angleConverter(TextField equationText, Text answerText, String fromThis, String toThis)
    {
        //Formulas provided by, google
        try
        {
            double answerNum=Double.parseDouble(equationText.getText());
            switch (fromThis)
            {
                case "Degree":
                    switch (toThis)
                    {
                        case "Radian":
                            answerNum=Math.toRadians(answerNum);
                            break;
                        case "Gradian":
                            answerNum=answerNum*200.0/180.0;
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
                case "Radian":
                    switch (toThis)
                    {
                        case "Degree":
                            answerNum=Math.toDegrees(answerNum);
                            break;
                        case "Gradian":
                            answerNum=answerNum*200.0/Math.PI;
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
                case "Gradian":
                    switch (toThis)
                    {
                        case "Degree":
                            answerNum=answerNum*180.0/200.0;
                            break;
                        case "Radian":
                            answerNum=answerNum*Math.PI/200.0;
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
            }
            answerText.setText(reScaleDouble(answerNum));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            answerText.setText("ERROR!");
        }
    }

    /**
     * Converts between frequency types
     * @param equationText the textField being drawn from
     * @param answerText gets the final answer or error
     * @param fromThis determines what function is triggered
     * @param toThis determines what function is triggered
     */
    public void frequencyConverter(TextField equationText, Text answerText, String fromThis, String toThis)
    {
        //Formulas provided by, google
        try
        {
            double answerNum=Double.parseDouble(equationText.getText());
            switch (fromThis)
            {
                case "Hertz":
                    switch (toThis)
                    {
                        case "Kilohertz":
                            answerNum/=1000.0;
                            break;
                        case "Megahertz":
                            answerNum/=1e+6;
                            break;
                        case "Gigahertz":
                            answerNum/=1e+9;
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
                case "Kilohertz":
                    switch (toThis)
                    {
                        case "Hertz":
                            answerNum*=1000.0;
                            break;
                        case "Megahertz":
                            answerNum/=1000.0;
                            break;
                        case "Gigahertz":
                            answerNum/=1e+6;
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
                case "Megahertz":
                    switch (toThis)
                    {
                        case "Hertz":
                            answerNum*=1e+6;
                            break;
                        case "Kilohertz":
                            answerNum*=1000.0;
                            break;
                        case "Gigahertz":
                            answerNum/=1000.0;
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
                case "Gigahertz":
                    switch (toThis)
                    {
                        case "Hertz":
                            answerNum*=1e+9;
                            break;
                        case "Kilohertz":
                            answerNum*=1e+6;
                            break;
                        case "Megahertz":
                            answerNum*=1000.0;
                            break;
                        default:
                            answerText.setText("");
                            return;
                    }
                    break;
            }
            answerText.setText(reScaleDouble(answerNum));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            answerText.setText("ERROR!");
        }
    }

    /**
     * Makes sure to return an int format if the double has just ".0" at the end and to round
     * @param answerNum the double to be rescaled
     * @return an int or double in the form of a string
     */
    public String reScaleDouble(double answerNum)
    {
        //round to the 10th decimal place (like actual 10th not first decimal)
        //if the number is too high or low it ends as zero which is bad
        System.out.println("Condition1 pass, "+(answerNum<1.0E7 && answerNum>1.0E-7));
        System.out.println("Condition2 pass, "+(answerNum>-1.0E7 && answerNum<-1.0E-7));
        if((answerNum<1.0E7 && answerNum>1.0E-7) || (answerNum>-1.0E7 && answerNum<-1.0E-7))
        {
            answerNum=Math.round(answerNum*10000000000.0)/10000000000.0;
        }

        //if double is just .0 then it's an int otherwise keep it
        if(Double.toString(answerNum).endsWith(".0"))
        {
            return Integer.toString((int)answerNum);
        }
        else
            return Double.toString(answerNum);
    }

    /**
     * Makes sure to get rid of any unwanted input from the text
     * @param equationText the textField used to get the input & what is ultimately changed
     * @param operation determines what kind of input gets replaced
     * @param fromThis determines what kind of input gets replaced
     */
    public void inputSyntaxCheck(TextField equationText, String operation,String fromThis)
    {
        String temp=equationText.getText();
        switch (operation)
        {
            case "Default": case "Trig":
                temp=temp.replaceAll("[^0-9-/+%*.()πeE]", "");
                break;
            case "Common":
                switch (fromThis)
                {
                    case "Custom power ^ʸ":
                        temp=temp.replaceAll("[^0-9-/+%*.()πe^E]", "");
                        break;
                    case "Custom root ʸ√", "Exp", "nPr", "nCr":
                        temp=temp.replaceAll("[^0-9-/+%*.()πe,E]", "");
                        break;
                    default:
                        temp=temp.replaceAll("[^0-9-/+%*.()πeE]", "");
                        break;
                }
                break;
            case "Geometry":
                switch (fromThis)
                {
                    case "Area of square": case "Area of circle": case "Circumference of circle":
                    case "Volume of cube":
                        temp=temp.replaceAll("[^0-9-/+%*.()πeE]", "");
                        break;
                    default:
                        temp=temp.replaceAll("[^0-9-/+%*.()πeE,]", "");

                }
                break;
            case "Standard deviation calc": case "Quadratic calc": case "Cubic calc":
            temp=temp.replaceAll("[^0-9-/+%*.()πeE,]", "");
            break;
            case "int, hex, binary, octal converter":
                temp = switch (fromThis) {
                    case "Dec" -> temp.replaceAll("[^0-9-]", "");
                    case "Bin" -> temp.replaceAll("[^0-1]", "");
                    case "Hex" -> temp.replaceAll("[^0-9a-f]", "");
                    case "Oct" -> temp.replaceAll("[^0-7]", "");
                    default -> temp;
                };
                break;
            case "Ascii converter":
                temp=temp.replaceAll("¦", "");
                break;
            case "Data converter 2¹⁰":  case "Data converter 10³": case "Time converter":
                case "Frequency converter":
                temp=temp.replaceAll("[^0-9.]", "");
            break;
            case "Temp converter": case"Angle converter":
                temp=temp.replaceAll("[^0-9.-]", "");
                break;

        }
        equationText.setText(temp);
    }

    /**
     * Makes sure things are in a proper java-readable format without trying to confuse the user
     * @param equationText a String copy of equationText
     * @return a newly formatted String
     */
    public String syntaxCheck(String equationText)
    {
        String temp=equationText;

        //parenthesis syntax
        if(temp.contains("(") || temp.contains(")"))
        {
            //counting parenthesis
            int parLCounter=temp.length() - temp.replaceAll("\\(","").length();
            int parRCounter=temp.length() - temp.replaceAll("\\)","").length();

            while(parLCounter>parRCounter)
            {
                //add ')' to the end
                temp=temp.concat(")");
                parRCounter++;
            }
            while(parRCounter>parLCounter)
            {
                //add '(' to the beginning
                StringBuilder tempBuilder=new StringBuilder(temp);
                tempBuilder.insert(0,"(");
                temp=tempBuilder.toString();
                parLCounter++;
            }

            //so we don't get these for no reason
            temp=temp.replace("()","");

            String parThing=temp;
            StringBuilder finalPar = new StringBuilder();
            System.out.println(parThing);
            while(parThing.contains("("))
            {
                System.out.println("index of ( "+parThing.indexOf("("));

                if(parThing.indexOf("(")>0)
                {
                    //add '*' before '(' if conditions are met
                    switch (parThing.charAt(parThing.indexOf("(") - 1)) {
                        case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ')', 'e', 'π' -> {
                            System.out.println("DING at before (");
                            StringBuilder pieThingBuilder = new StringBuilder(parThing);
                            pieThingBuilder.insert(parThing.indexOf("("), "*");
                            parThing = pieThingBuilder.toString();
                        }
                    }
                }
                if(parThing.indexOf(")")<parThing.length()-1)
                {
                    //add '*' after ')' if conditions are met
                    switch (parThing.charAt(parThing.indexOf(")") + 1)) {
                        case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '(', 'e', 'π' -> {
                            System.out.println("DING at after )");
                            StringBuilder pieThingBuilder = new StringBuilder(parThing);
                            pieThingBuilder.insert(parThing.indexOf(")") + 1, "*");
                            parThing = pieThingBuilder.toString();
                            System.out.println("check " + parThing);
                        }
                    }
                }
                //cut out corrected piece and read again
                finalPar.append(parThing.substring(0, parThing.indexOf("(") + 1));
                parThing=parThing.substring(parThing.indexOf("(")+1);
            }
            //add remaining piece
            finalPar = new StringBuilder(finalPar.toString().concat(parThing));
            System.out.println(finalPar);
            temp= finalPar.toString();
        }
        //pi syntax similar to parenthesis
        if(temp.contains("π"))
        {
            String pieThing=temp;
            StringBuilder finalPie = new StringBuilder();
            System.out.println(pieThing);
            while(pieThing.contains("π"))
            {
                System.out.println("index of pie "+pieThing.indexOf("π"));

                if(pieThing.indexOf("π")>0)
                {
                    switch (pieThing.charAt(pieThing.indexOf("π") - 1)) {
                        case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ')', 'e', 'π' -> {
                            System.out.println("DING at before π");
                            StringBuilder pieThingBuilder = new StringBuilder(pieThing);
                            pieThingBuilder.insert(pieThing.indexOf("π"), "*");
                            pieThing = pieThingBuilder.toString();
                        }
                    }
                }
                if(pieThing.indexOf("π")<pieThing.length()-1)
                {
                    switch (pieThing.charAt(pieThing.indexOf("π") + 1)) {
                        case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'e', 'π' -> {
                            System.out.println("DING at after π");
                            StringBuilder pieThingBuilder = new StringBuilder(pieThing);
                            pieThingBuilder.insert(pieThing.indexOf("π") + 1, "*");
                            pieThing = pieThingBuilder.toString();
                            System.out.println("check " + pieThing);
                        }
                    }
                }
                finalPie.append(pieThing.substring(0, pieThing.indexOf("π") + 1));
                pieThing=pieThing.substring(pieThing.indexOf("π")+1);
            }
            finalPie = new StringBuilder(finalPie.toString().concat(pieThing));
            System.out.println(finalPie);
            temp= finalPie.toString();
        }
        //Euler's syntax almost identical to Pi's
        if(temp.contains("e"))
        {
            String eThing=temp;
            StringBuilder finalE = new StringBuilder();
            System.out.println(eThing);
            while(eThing.contains("e"))
            {
                System.out.println("index of Euler "+eThing.indexOf("e"));

                if(eThing.indexOf("e")>0)
                {
                    switch (eThing.charAt(eThing.indexOf("e") - 1)) {
                        case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ')', 'e', 'π' -> {
                            System.out.println("DING at before e");
                            StringBuilder pieThingBuilder = new StringBuilder(eThing);
                            pieThingBuilder.insert(eThing.indexOf("e"), "*");
                            eThing = pieThingBuilder.toString();
                        }
                    }
                }
                if(eThing.indexOf("e")<eThing.length()-1)
                {
                    switch (eThing.charAt(eThing.indexOf("e") + 1)) {
                        case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'e', 'π' -> {
                            System.out.println("DING at after e");
                            StringBuilder pieThingBuilder = new StringBuilder(eThing);
                            pieThingBuilder.insert(eThing.indexOf("e") + 1, "*");
                            eThing = pieThingBuilder.toString();
                            System.out.println("check " + eThing);
                        }
                    }
                }
                finalE.append(eThing.substring(0, eThing.indexOf("e") + 1));
                eThing=eThing.substring(eThing.indexOf("e")+1);
            }
            finalE = new StringBuilder(finalE.toString().concat(eThing));
            System.out.println(finalE);
            temp= finalE.toString();
        }
        return temp;
    }

    /**
     * Makes the syntax readable while not adding a clutter of numbers for the user
     * @param equationText the value of the textField used
     * @return corrected string
     */
    public String mathSyntaxCorrector(String equationText)
    {
        String temp=equationText;
        final double PIE=Math.PI;
        final double EULER=Math.E;

        temp=temp.replace("π",Double.toString(PIE));
        temp=temp.replace("e",Double.toString(EULER));

        return temp;
    }
}
