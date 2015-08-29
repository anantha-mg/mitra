package mitra

class Comment {

    String comment;
    User user;
    Date createdOn;

    static constraints = {
        user blank:false, nullable:false
    }
}
