package com.weikang.pdfparser;

class Subtask {
    private String text;
    private String description;

    public Subtask(String text) {
        this.text = text;
        this.description = "";
    }

    public String getText() {
        return text;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return text + description;
    }
}
