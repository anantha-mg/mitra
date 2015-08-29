package mitra

class User {

    String deviceId
    Integer id;
    static hasMany = [tags: Tag, roles: Role]

    static constraints = {
    }
}
