package com.example.ai_mad_numgame;
/*
   App will show your last performance at the start of the activity. New Tournament will start from
   all performance set to -1 again. And your new performance will be visible, when you return back to game
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences,sp1,sp2;
    int matchCounter=0,c=0,ic=0;
    int []performance={-1,-1,-1,-1,-1,-1}; //score of a game is updated in this array
    int []score={-1,-1,-1}; //score of each match is updated in this array. A total of three matches in a game
    String[] operators ={"+","-","*","/"};
    int correctButton=0; //which button will have the correct answer (tag of that button)
    Random random=new Random(); //You will generate random algebra questions
    TextView textView2, correct, incorrect;
    Button button1,button2,button3,button4;
    public void load(View view){
        Button buttonClicked=(Button)view;
        if(buttonClicked.getTag().toString().equals(correctButton+"")){
            score[matchCounter++]=1;
            c++;
            correct.setText("👍"+c );
//            sp1.edit().putString("title","👍"+c + "").apply();
//            sp1.edit().putInt("cor",c).apply();
            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
        }else{
            score[matchCounter++]=0;
            ic++;
            incorrect.setText("👎"+ic);
//            sp2.edit().putString("title","👎"+ic + "").apply();
//            sp2.edit().putInt("cor",ic).apply();
            Toast.makeText(this, "Wrong", Toast.LENGTH_SHORT).show();
        }
        newMatch();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1=findViewById(R.id.button1);
        button2=findViewById(R.id.button2);
        button3=findViewById(R.id.button3);
        button4=findViewById(R.id.button4);
        textView2=findViewById(R.id.textView2);
        correct=findViewById(R.id.cor);
        incorrect=findViewById(R.id.incor);
        newMatch();
        sharedPreferences=this.getSharedPreferences("com.example.ai_mad_numgame", Context.MODE_PRIVATE);
//        sp1=this.getSharedPreferences("com.example.ai_mad_numgame",0);
//        sp2=this.getSharedPreferences("com.example.ai_mad_numgame",0);
//        int p1=sp1.getInt("cor",0);
//        int p2=sp2.getInt("incor",0);
//        correct.setText("👍"+p1+"");
//        incorrect.setText("👎"+p2+"");
        int[][]dataFrame=dataPrep(); //dataPrep function returns a two-dimensional array
        double slope=LR.getSlope(dataFrame); //LR class, which provides slope on invoking getSlope
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_score) //your custom icon
                .setTitle("Performance")
                .setMessage(getInterpretation(dataFrame,slope))
                .setNeutralButton("OK", (dialog, which) -> newMatch()).show();
    }

    @SuppressLint("SetTextI18n")
    public void newMatch() {  //A game is composed of three matches

        int operand1 = random.nextInt(10);
        int operand2= random.nextInt(10);
        //check if operand2 is not zero; otherwise in case of division-divide by zero error will come
        if(operand2==0)
            operand2++;

        correctButton = random.nextInt(4);
        String operator = operators[random.nextInt(4)];
        textView2.setText(operand1 + operator + operand2 + " = ");

        // to generate distinct wrong outputs for incorrect options
        int rnd1 = random.nextInt(13);
        int rnd2 = random.nextInt(17);
        if (rnd1 == 0) rnd1++;
        if (rnd2 == 0) rnd2++;
        if (rnd1 == rnd2) rnd1 += rnd2;
        if (rnd1 == operand1 && rnd2 == operand2) rnd2 += rnd1;

        // Your code here, to display correct and incorrect options on the buttons

        if(correctButton==0) {
            switch (operator) {
                case "-":
                    button1.setText(operand1 - operand2 + " ");
                    break;
                case "+":
                    button1.setText(operand1 + operand2 + " ");
                    break;
                case "*":
                    button1.setText(operand1 * operand2 + " ");
                    break;
                default:
                    button1.setText((float)operand1 / operand2 + " ");
                    break;
            }
            button2.setText(rnd1+rnd2+" ");
            button3.setText(rnd1-rnd2+" ");
            button4.setText(rnd1*rnd2+" ");
        }
        else if(correctButton==1){
            switch (operator) {
                case "-":
                    button2.setText(operand1 - operand2 + " ");
                    break;
                case "+":
                    button2.setText(operand1 + operand2 + " ");
                    break;
                case "*":
                    button2.setText(operand1 * operand2 + " ");
                    break;
                default:
                    button2.setText((float)operand1 / operand2 + " ");
                    break;
            }
            button1.setText(rnd1+rnd2+" ");
            button3.setText(rnd1-rnd2+" ");
            button4.setText(rnd1*rnd2+" ");
        }
        else if(correctButton==2){
            switch (operator) {
                case "-":
                    button3.setText(operand1 - operand2 + " ");
                    break;
                case "+":
                    button3.setText(operand1 + operand2 + " ");
                    break;
                case "*":
                    button3.setText(operand1 * operand2 + " ");
                    break;
                default:
                    button3.setText((float)operand1 / operand2 + " ");
                    break;
            }
            button1.setText(rnd1+rnd2+" ");
            button2.setText(rnd1-rnd2+" ");
            button4.setText(rnd1*rnd2+" ");
        }
        else {
            switch (operator) {
                case "-":
                    button4.setText(operand1 - operand2 + " ");
                    break;
                case "+":
                    button4.setText(operand1 + operand2 + " ");
                    break;
                case "*":
                    button4.setText(operand1 * operand2 + " ");
                    break;
                default:
                    button4.setText((float)operand1 / operand2 + " ");
                    break;
            }
            button2.setText(rnd1+rnd2+" ");
            button3.setText(rnd1-rnd2+" ");
            button1.setText(rnd1*rnd2+" ");
        }


        if(matchCounter==3){    // if three matches are completed update the performance in shared preferences

            matchCounter=0;

            //adjusting the performance array so that last six entries present with the most recent at the last index.
            if (performance.length - 1 >= 0)
                System.arraycopy(performance, 1, performance, 0, performance.length - 1);
            performance[5]=sumOfScore(); //calculating the sum of last three matches (note result of a match is 1 ro 0, and add to performance
            sharedPreferences.edit().putString("data",new Gson().toJson(performance)).apply();

        }
    }

    public int sumOfScore(){
        //Computing the sum of score array, which has the 1 or in each index,depending on correct or incorrect answers
        int sum=0;
       // your code here
        for(int i=0;i<3;i++)
        {
            sum+=score[i];
        }
        return sum;
    }

    public int[][] dataPrep() {
        int[] data = new Gson().fromJson((sharedPreferences.getString("data", null)), performance.getClass());
        Log.i("data", Arrays.toString(data)); //this is how you display arrays in Logcat, for debugging
        int[][] dataFrame = new int[6][2]; //creating a dataframe of two columns and six rows for regression purpose
        if(data==null)
            return null;
        for (int i = 0; i < data.length; i++) {
            dataFrame[i][0] = i + 1;
            dataFrame[i][1] = data[i];
        }
        return dataFrame;
    }

    public String getInterpretation(int [][]dataFrame,double slope){
       //provide interpretation based on your slope analysis
        // Your code here
        if(slope>1)
            return "Good job, keep it up";
        else if(slope<1)
            return "Need to work more";
        else if(slope==1){
            int n=0;
            for(int i=0;i<=5;i++){
                if(dataFrame[i][1]==3) n++;
            }
            if(n==6)
                return "Awesome, Perfect score of 3 in last 6 games";
        }
        return "Constant Performance";
    }
}