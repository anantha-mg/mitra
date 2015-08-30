import mitra.Role
import mitra.Tag

class BootStrap {

    def init = { servletContext ->
        Role role = Role.findByName("ROLE_ANSWERER")
        if (!role) {
            role = new Role(name:"ROLE_ANSWERER")
            role.save(flush:true)
        }

        // HACK: To be removed

        ["CRICKET", "MOVIE", "WEATHER", "FOOD", "TRAFFIC"].each { tagName ->
            Tag tag = Tag.findByName(tagName)
            if (!tag) {
                tag = new Tag(name: tagName)
                tag.save(flush: true)
            }
        }
    }

    def destroy = {
    }
}
