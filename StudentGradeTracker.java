package com.example.demo;


import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;

public class StudentGradeTracker extends Application {

    private TextField nameField, totalMarksField, obtainedMarksField;
    private TextArea resultArea;
    private ArrayList<Student> students = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Student Grade Tracker");

        // Input Fields
        nameField = new TextField();
        nameField.setPromptText("Student Name");

        totalMarksField = new TextField();
        totalMarksField.setPromptText("Total Marks");

        obtainedMarksField = new TextField();
        obtainedMarksField.setPromptText("Marks Obtained");

        Button addButton = new Button("Add Student");
        addButton.setOnAction(e -> addStudent());

        Button summaryButton = new Button("Show Summary");
        summaryButton.setOnAction(e -> showSummary());

        // Layout
        GridPane inputGrid = new GridPane();
        inputGrid.setPadding(new Insets(10));
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);

        inputGrid.add(new Label("Name:"), 0, 0);
        inputGrid.add(nameField, 1, 0);
        inputGrid.add(new Label("Total Marks:"), 0, 1);
        inputGrid.add(totalMarksField, 1, 1);
        inputGrid.add(new Label("Obtained Marks:"), 0, 2);
        inputGrid.add(obtainedMarksField, 1, 2);
        inputGrid.add(addButton, 0, 3);
        inputGrid.add(summaryButton, 1, 3);

        resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setPrefHeight(300);

        VBox layout = new VBox(10, inputGrid, resultArea);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 600, 450);
        stage.setScene(scene);
        stage.show();
    }

    private void addStudent() {
        String name = nameField.getText().trim();
        String totalStr = totalMarksField.getText().trim();
        String obtainedStr = obtainedMarksField.getText().trim();

        if (name.isEmpty() || totalStr.isEmpty() || obtainedStr.isEmpty()) {
            showAlert("Please fill in all fields.");
            return;
        }

        try {
            double total = Double.parseDouble(totalStr);
            double obtained = Double.parseDouble(obtainedStr);

            if (total <= 0 || obtained < 0 || obtained > total) {
                showAlert("Invalid marks input.");
                return;
            }

            double percentage = (obtained / total) * 100;
            String grade = calculateGrade(percentage);

            students.add(new Student(name, total, obtained, percentage, grade));

            resultArea.appendText("Added: " + name + " | " + obtained + "/" + total +
                    " (" + String.format("%.2f", percentage) + "%, Grade: " + grade + ")\n");

            nameField.clear();
            totalMarksField.clear();
            obtainedMarksField.clear();
        } catch (NumberFormatException e) {
            showAlert("Marks must be numeric.");
        }
    }

    private void showSummary() {
        if (students.isEmpty()) {
            showAlert("No students added yet.");
            return;
        }

        StringBuilder report = new StringBuilder("=== Student Summary ===\n");
        double highest = -1;
        double lowest = 101;
        double totalPercent = 0;

        for (Student s : students) {
            report.append(s.name)
                    .append(" | ")
                    .append(s.obtained)
                    .append("/")
                    .append(s.total)
                    .append(" (")
                    .append(String.format("%.2f", s.percentage))
                    .append("%, Grade: ")
                    .append(s.grade)
                    .append(")\n");

            if (s.percentage > highest) highest = s.percentage;
            if (s.percentage < lowest) lowest = s.percentage;
            totalPercent += s.percentage;
        }

        double average = totalPercent / students.size();
        report.append("\nAverage: ").append(String.format("%.2f", average)).append("%");
        report.append("\nHighest: ").append(String.format("%.2f", highest)).append("%");
        report.append("\nLowest: ").append(String.format("%.2f", lowest)).append("%");

        resultArea.setText(report.toString());
    }

    private String calculateGrade(double percentage) {
        if (percentage >= 85) return "A";
        else if (percentage >= 70) return "B";
        else if (percentage >= 60) return "C";
        else if (percentage >= 50) return "D";
        else return "F";
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    static class Student {
        String name;
        double total;
        double obtained;
        double percentage;
        String grade;

        Student(String name, double total, double obtained, double percentage, String grade) {
            this.name = name;
            this.total = total;
            this.obtained = obtained;
            this.percentage = percentage;
            this.grade = grade;
        }
    }
}
