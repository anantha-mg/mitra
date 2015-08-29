package mitra

import grails.converters.JSON

class UserController {

    def index() { }

    def subscribe = {
        def deviceId = params.DEVICE_ID
        def tagList = params.TAG_LIST?.split(",")
        def role = Role.findByName(MitraConstants.ANSWERER_ROLE_NAME)

        User user = new User(deviceId:deviceId, tags: tagList, roles: [role])
        user.save()

        def returnMap = [:]
        returnMap.status = "SUCCESS"
        def jsonResponse = returnMap as JSON
        render(text: jsonResponse, contentType: "application/json", encoding: "UTF-8");
    }

}
