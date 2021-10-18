package com.avijitmondal.grpc.server.model;

import java.util.List;

public class Mail {
    private String id;
    private String subject;
    private Integer numAttachments;
    private String body;
    private List<Object> attachments;

    public Mail(String id, String subject, String body, Integer numAttachments) {
        this.id = id;
        this.subject = subject;
        this.numAttachments = numAttachments;
        this.body = body;
        this.attachments = attachments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getNumAttachments() {
        return numAttachments;
    }

    public void setNumAttachments(Integer numAttachments) {
        this.numAttachments = numAttachments;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<Object> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Object> attachments) {
        this.attachments = attachments;
    }
}
