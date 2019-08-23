package com.weikang.pdfparser;

import android.content.res.AssetManager;
import android.util.Log;

import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.util.ArrayList;

class MyPDFParser {
    private AssetManager assetManager;

    public MyPDFParser(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public ArrayList<Heading> getHeading(String filename) {
        ArrayList<Heading> newHeadingList = new ArrayList<>();
        String text = parsePdf(filename);

        StringBuilder description = new StringBuilder();
        Heading currentHeading = new Heading("");
        Subtask currentSubtask = new Subtask("");
        ArrayList<Subtask> subtasksList = new ArrayList<>();
        boolean isFirstSub = true;
        for (String line : text.split("\\r?\\n")) {
            if (line.contains("TASK:") && !line.contains("SUBTASK:")) {
                currentHeading.setSubtasksList(subtasksList);
                currentHeading = new Heading(line);
                newHeadingList.add(currentHeading);
                isFirstSub = true;
            } else if (line.contains("SUBTASK:")) {
                if (isFirstSub) {
                    currentHeading.setRelevantInformation(description.toString());
                } else {
                    currentSubtask.setDescription(description.toString());
                    isFirstSub = false;
                }
                currentSubtask = new Subtask(line);
                subtasksList.add(currentSubtask);
                description = new StringBuilder();
            } else {
                description.append(line);
            }
        }
        System.out.println(newHeadingList);
        return newHeadingList;
    }

    private String parsePdf(String filename) {
        String parsedText = null;
        PDDocument document = null;
        try {
            document = PDDocument.load(this.assetManager.open(filename));
        } catch(IOException e) {
            Log.e("PdfBox-Android-Sample", "Exception thrown while loading document to strip", e);
        }

        try {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            pdfStripper.setStartPage(0);
            parsedText = "Parsed text: " + pdfStripper.getText(document);
        }
        catch (IOException e)
        {
            Log.e("PdfBox-Android-Sample", "Exception thrown while stripping text", e);
        } finally {
            try {
                if (document != null) document.close();
            }
            catch (IOException e)
            {
                Log.e("PdfBox-Android-Sample", "Exception thrown while closing document", e);
            }
        }

        return parsedText;
    }
}
