package AISquareRoot;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static java.lang.Math.round;
import static java.lang.Math.sqrt;

public class Controller {
    public Button calculateButton;
    public TextField InputTF, AnswerTF;
    public TextArea ShowProcessTF;
    private String inText, outText, ProcessString;
    private double input, result;


    private File file = new File("AgentMemory.txt");

    public void startCalculating() {
        if (!(file.exists())) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Get input and convert it into double
        inText = InputTF.getText();
        outText = inText + ",";

        if (!(inText.isEmpty())) {
            ProcessString = "> Process Started\n";
            ShowProcessTF.setText(ProcessString);
            input = Double.parseDouble(inText);

            percept();
            ProcessString += "> Done.\n";
            ShowProcessTF.setText(ProcessString);
        }

    }

    private void percept() {
        // This method will check the memory for prior occurance of the number
        ProcessString += "> Percepting Input.....\n";
        ShowProcessTF.setText(ProcessString);
        try {
            Scanner scanner = new Scanner(file);
            String temp = "";
            boolean found = false;
            while (scanner.hasNextLine()) {
                temp = scanner.nextLine();
                String[] tester = temp.split(",");
                if (Double.parseDouble(tester[0]) == input) {
                    result = Double.parseDouble(tester[1]);
                    found = true;
                    action("found");
                    break;
                }
            }
            scanner.close();

            if (!found) {
                ProcessString += "> No Prior Percept Found.\n";
                ShowProcessTF.setText(ProcessString);
                action("notFound");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    private void action(String tag) {
        // This method will work based on the output of percept
        switch (tag) {
            case "notFound":
                update(calculate());
                show();
                break;

            case "found":
                ProcessString += "> Prior Action Found.\n";
                ProcessString += "> Getting Result From Memory......\n";
                ShowProcessTF.setText(ProcessString);
                show();
                break;
        }

    }

    private void update(double d) {
        ProcessString += "> Updating Memory......\n";
        ShowProcessTF.setText(ProcessString);

        outText += result + "\n";

        try {
            FileWriter writer = new FileWriter(file, true);
            writer.write(outText);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private double calculate() {
        ProcessString += "> Processing New Percept......\n";
        ShowProcessTF.setText(ProcessString);

        result = sqrt(input);

        result = ((double) round(result * 10000)) / 10000;

        return result;
    }

    private void show() {
        ProcessString += "> Taking Action.....\n";
        ShowProcessTF.setText(ProcessString);

        AnswerTF.setText(String.valueOf(result));

        ProcessString += "> Action Taken\n";
        ShowProcessTF.setText(ProcessString);
    }
}
