package com.avijitmondal.grpc.server.model;

public class Attachment {
    private String id;
    private String name;
    private String attachmentType;
    private String contentType;
    private String content;
    private String attachmentFormat;
    private Integer size;

    public Attachment(String id, String name, String attachmentType, String contentType, String content, String attachmentFormat, Integer size) {
        this.id = id;
        this.name = name;
        this.attachmentType = attachmentType;
        this.contentType = contentType;
        this.content = content;
        this.attachmentFormat = attachmentFormat;
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public String getContentType() {
        return contentType;
    }

    public String getContent() {
        return content;
    }

    public String getAttachment_format() {
        return attachmentFormat;
    }

    public Integer getSize() {
        return size;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAttachment_format(String attachment_format) {
        this.attachmentFormat = attachment_format;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
