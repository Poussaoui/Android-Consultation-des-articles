package com.app.elmoussm.casiranaroidprojet.model;

public class Article {

    int id;
    String author;
    String title;
    String urlToImage;
    String sourceName;
    String publishedAt;
    String description;
    String content;

    public Article() {
    }

    public Article(String author, String title, String urlToImage, String sourceName, String publishedAt, String content, String description) {
        this.author = author;
        this.title = title;
        this.urlToImage = urlToImage;
        this.sourceName = sourceName;
        this.publishedAt = publishedAt;
        this.description = description;
        this.content = content;
    }

    public Article(int id, String author, String title, String urlToImage, String sourceName, String publishedAt, String content, String description) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.urlToImage = urlToImage;
        this.sourceName = sourceName;
        this.publishedAt = publishedAt;
        this.description = description;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                ", sourceName='" + sourceName + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
