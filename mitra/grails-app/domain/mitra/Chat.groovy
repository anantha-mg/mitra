package mitra

class Chat {

    Integer id
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

    def beforeInsert = {
        updatedOn = new Date()
    }

    def beforeUpdate = {
        updatedOn = new Date()
    }

}

enum Type{
    TEXT, VOICE
}

enum Status{
    OPEN, CLOSED
}