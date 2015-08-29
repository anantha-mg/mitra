package mitra

class Comment {

    String comment;
    mitra.User user;
    Date createdOn;

    static constraints = {
        user blank:false, nullable:false
    }
}
