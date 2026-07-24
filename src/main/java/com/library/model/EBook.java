package com.library.model;

public class EBook extends Book {
    private String fileFormat; // PDF, EPUB, MOBI
    private double fileSize; // MB
    private String downloadUrl;

    public EBook(String bookId, String title, String author, String isbn, 
                 String fileFormat, double fileSize, String downloadUrl) {
        super(bookId, title, author, isbn, Integer.MAX_VALUE); // Unlimited copies
        this.fileFormat = fileFormat;
        this.fileSize = fileSize;
        this.downloadUrl = downloadUrl;
        this.baseCost = 0.0;
    }

    @Override
    public String getBookType() {
        return "E-Book";
    }

    @Override
    public String getLocation() {
        return "Digital: " + downloadUrl;
    }

    public String getFileFormat() { return fileFormat; }
    public void setFileFormat(String fileFormat) { this.fileFormat = fileFormat; }

    public double getFileSize() { return fileSize; }
    public void setFileSize(double fileSize) { this.fileSize = fileSize; }

    public String getDownloadUrl() { return downloadUrl; }
    public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }

    @Override
    public String toString() {
        return super.toString() + String.format(", Format=%s, Size=%.2fMB", 
                fileFormat, fileSize);
    }
}