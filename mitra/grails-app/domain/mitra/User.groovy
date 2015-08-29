package mitra

class User {

    Integer userId;
    static hasMany = [tags: Tag, roles: Role]

    static constraints = {
    }
}
