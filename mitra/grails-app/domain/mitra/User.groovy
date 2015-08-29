package mitra

class User {

    String deviceId
    Integer id;
    static hasMany = [tags: Tag, roles: Role]
    Date updatedOn

    static constraints = {
    }

    def beforeUpdate = {
        updatedOn = new Date()
    }

    def beforeInsert = {
        updatedOn = new Date()
    }

}
