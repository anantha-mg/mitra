package mitra

class Chat {

    String comment;
    User createdBy;
    Status status;
    Date updatedOn;
    Type chatType;
    static hasMany = [ comments: Comment, tags: Tag];

    static constraints = {
//        createdBy blank:false, nullable:false
//        status blank:false, nullable:false
//        updatedOn blank:false, nullable:false
    }
}

enum Type{
    TEXT, VOICE
}

enum Status{
    OPEN, CLOSED
}