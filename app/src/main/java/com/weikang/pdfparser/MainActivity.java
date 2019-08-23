package com.weikang.pdfparser;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.shockwave.pdfium.PdfDocument;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.text.PDFTextStripper;
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements OnPageChangeListener,OnLoadCompleteListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String SAMPLE_FILE = "Boeing_737-300_400_500_Aircraft_Maintena.pdf";
    PDFView pdfView;
    Integer pageNumber = 0;
    String pdfFileName;
    AssetManager assetManager;

    // RecyclerView
    ArrayList<Parts> mItemsParts;
    ArrayList<Parts> mIndivPagePages;
    ArrayList<ArrayList<Parts>> mAllPageParts;
    RecyclerView mRecentRecyclerView;
    ArrayList<PartsAdapter> mAllPagePartsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init parts array
        mAllPageParts = new ArrayList<>();
        mItemsParts = new ArrayList<>();
        mAllPagePartsAdapter = new ArrayList<>();
        assetManager = getAssets();
        PDFBoxResourceLoader.init(getApplicationContext());
        extractParts();
        initParts();
        mIndivPagePages = mAllPageParts.get(0);

        // Set up adapter
        mRecentRecyclerView = (RecyclerView) findViewById(R.id.partsList);
        mRecentRecyclerView.setAdapter(mAllPagePartsAdapter.get(0));
        mRecentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        pdfView= (PDFView)findViewById(R.id.pdfView);
        displayFromAsset(SAMPLE_FILE);
    }

    private void initParts() {
        // TODO: Get page number
        for (int i = 0; i < 9; i++) {
            stripText(i);
        }
    }

    public void stripText(int pageNumber) {
        ArrayList<Parts> partsInCurrentPage = new ArrayList<>();
        String parsedText = null;
        PDDocument document = null;
        try {
            document = PDDocument.load(assetManager.open(SAMPLE_FILE));
        } catch(IOException e) {
            Log.e("PdfBox-Android-Sample", "Exception thrown while loading document to strip", e);
        }

        try {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            pdfStripper.setStartPage(pageNumber);
            pdfStripper.setEndPage(pageNumber + 1);
            parsedText = pdfStripper.getText(document);
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

        for (Parts parts : mItemsParts) {
            if (parsedText.contains(parts.getID())) {
                partsInCurrentPage.add(parts);
            }
        }

        mAllPageParts.add(partsInCurrentPage);
        mAllPagePartsAdapter.add(new PartsAdapter(partsInCurrentPage, this));
    }

    private void extractParts() {
        String parsedText = null;
        PDDocument document = null;
        try {
            document = PDDocument.load(assetManager.open(SAMPLE_FILE));
        } catch(IOException e) {
            Log.e("PdfBox-Android-Sample", "Exception thrown while loading document to strip", e);
        }

        try {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            pdfStripper.setStartPage(0);
            parsedText = pdfStripper.getText(document);
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

        boolean extract = false;
        for (String lines : parsedText.split("\\r?\\n")) {
            if (lines.contains("References")) {
                extract = true;
                continue;
            }

            if (extract) {
                if (lines.contains("AMM")) {
                    Parts parts = new Parts(lines.substring(4));
                    if (!mItemsParts.contains(parts)) {
                        mItemsParts.add(parts);
                    }
                } else {
                    extract = false;
                }
            }
        }
    }

    private void displayFromAsset(String assetFileName) {
        pdfFileName = assetFileName;

        pdfView.fromAsset(SAMPLE_FILE)
                .defaultPage(pageNumber)
                .enableSwipe(true)

                .swipeHorizontal(false)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .pageFitPolicy(FitPolicy.BOTH)
                .load();
    }


    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
        mRecentRecyclerView.setAdapter(mAllPagePartsAdapter.get(page));
    }


    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-");

    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }


}