package mitra

class Comment {

    Integer id;
    String comment;
    User user;
    Date createdOn;
    static belongsTo = [chat: Chat]

    static constraints = {
        user blank:false, nullable:false
    }

    def beforeInsert = {
        createdOn = new Date()
    }


}
