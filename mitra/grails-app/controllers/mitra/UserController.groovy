package mitra

import grails.converters.JSON

class UserController {

    def utilService
    def index() { }

    def subscribe = {
        def returnMap = [:]
        returnMap.status = "FAILED"

        def deviceId = params.DEVICE_ID
        if (!deviceId) {
            render(text: returnMap as JSON, contentType: "application/json", encoding: "UTF-8");
            return
        }

        def existingUser = User.findByDeviceId(deviceId)

        def tagNameList = params.TAG_LIST?.split(",") ?: []
        def tagList = tagNameList.collect{Tag.findByName(it)}.findAll{it}
        def role = Role.findByName(MitraConstants.ANSWERER_ROLE_NAME)

        if (existingUser) {
            existingUser.tags = tagList
            existingUser.roles = [role]
            existingUser.save()
        } else {
            User user = new User(deviceId:deviceId, tags: tagList, roles: [role])
            def res = user.save(flush: true)
            println res
            println user.properties
        }

        returnMap.status = "SUCCESS"
//        returnMap.user = user
        render(text: returnMap as JSON, contentType: "application/json", encoding: "UTF-8");
    }

    def unsubscribe = {
        def returnMap = [:]
        returnMap.status = "FAILED"
        def deviceId = params.DEVICE_ID
        if (!deviceId) {
            render(text: returnMap as JSON, contentType: "application/json", encoding: "UTF-8");
            return
        }

        def existingUser = User.findByDeviceId(deviceId)
        if (!existingUser) {
            render(text: returnMap as JSON, contentType: "application/json", encoding: "UTF-8");
            return
        }

//        existingUser.tags = []
        existingUser.roles = []
        existingUser.save()
        returnMap.status = "SUCCESS"
//        returnMap.user = user
        def jsonResponse = returnMap as JSON
        render(text: jsonResponse, contentType: "application/json", encoding: "UTF-8");
    }

}
