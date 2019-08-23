package com.weikang.pdfparser;

import java.util.ArrayList;

class Heading {
    private String name;
    private String relevantInformation;
    private ArrayList<Subtask> subtasksList;

    public Heading(String name) {
        this.name = name;
        this.subtasksList = new ArrayList<>();
    }

    public ArrayList<Subtask> getSubtasksList() {
        return subtasksList;
    }

    public void setSubtasksList(ArrayList<Subtask> subtasksList) {
        this.subtasksList = subtasksList;
    }

    public void setRelevantInformation(String relevantInformation) {
        this.relevantInformation = relevantInformation;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(name);
        result.append(relevantInformation);
        for (Subtask sub : subtasksList) {
            result.append(sub.toString());
        }
        return result.toString();
    }
}
