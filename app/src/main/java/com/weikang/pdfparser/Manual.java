package com.weikang.pdfparser;

import java.util.ArrayList;

public class Manual {
    private String name;
    private ArrayList<Heading> headings;
    private String filename;
    private MyPDFParser myPDFParser;

    public Manual(String name, String filename, MyPDFParser myPDFParser) {
        this.name = name;
        this.filename = filename;
        this.myPDFParser = myPDFParser;
        this.headings = myPDFParser.getHeading(filename);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Heading> getHeadings() {
        return headings;
    }

    public String getFilename() {
        return filename;
    }
}
