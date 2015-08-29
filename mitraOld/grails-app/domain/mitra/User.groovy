package mitra

class User {

    Integer userId;
    String deviceId;
    static hasMany = [tags: Tag, roles: Role]

    static constraints = {
    }
}
