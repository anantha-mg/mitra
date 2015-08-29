package mitra

class Comment {

    String comment;
    User user;
    Date createdOn;
    static belongsTo = [chat: Chat]

    static constraints = {
        user blank:false, nullable:false
    }
}
