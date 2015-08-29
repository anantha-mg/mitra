package mitra

import grails.converters.JSON

class ChatController {

    def index() { }

    def tagService
    def utilService

    def addNewChat = {
        def returnMap = [:]
        returnMap.status = "FAILED"

        def deviceId = params.DEVICE_ID
        def commentString = params.COMMENT
        if (!deviceId || !commentString) {
            render(text: returnMap as JSON, contentType: "application/json", encoding: "UTF-8")
            return
        }

        def user = utilService.getUser(deviceId)

        Comment comment = new Comment(comment: commentString, user: user)
        comment.save(flush: true)


        Chat chat = new Chat();
        chat.comments = [comment] as Set
        chat.tags = tagService.getTagForComment(commentString)
        chat.createdBy = user
        chat.status = Status.OPEN
        chat.chatType = Type.TEXT
        chat.save(flush: true)

        returnMap = ["chatId" : chat.id, "tags":chat.tags, status:"SUCCESS"]
        render(text: returnMap as JSON, contentType: "application/json", encoding: "UTF-8")

    }

    def getOtherChats = {
        def tag = params.TAG
        /*List<Chats> chats = Chat.createCriteria().list{
            tags {
                'eq'(tag)
            }
        }*/
        List<Chat> chats = Chat.getAll()
        chats.findAll{it.status == Status.OPEN}.sort{it.updatedOn}
        return chats.size() > 5 ? chats[1..5] : chats
    }

    def addComment = {
        def returnMap = [:]
        returnMap.status = "FAILED"

        def deviceId = params.DEVICE_ID
        def commentString = params.COMMENT
        def chatId = params.CHAT_ID

        if (!deviceId || !commentString || !chatId) {
            render(text: returnMap as JSON, contentType: "application/json", encoding: "UTF-8")
            return
        }

        def user = utilService.getUser(deviceId)

        Comment comment = new Comment(comment: commentString, user: user)
        comment.save(flush: true)

        def chat = Chat.get(Integer.parseInt(chatId))
        if (!chat) {
            render(text: returnMap as JSON, contentType: "application/json", encoding: "UTF-8")
            return
        }

        chat.comments.add(comment)
        chat.save(flush: true)

        returnMap.result = "SUCCESS"
        render(text: returnMap as JSON, contentType: "application/json", encoding: "UTF-8")
    }

    def changeStatus = {

        def returnMap = [:]
        returnMap.status = "FAILED"

        def deviceId = params.DEVICE_ID
        def chatId = params.CHAT_ID
        def status = params.STATUS as Status

        if (!deviceId || !status || !chatId) {
            render(text: returnMap as JSON, contentType: "application/json", encoding: "UTF-8")
            return
        }

        def chat = Chat.get(Integer.parseInt(chatId))
        if (!chat) {
            render(text: returnMap as JSON, contentType: "application/json", encoding: "UTF-8")
            return
        }

        chat.status = status
        chat.save(flush: true)

        returnMap.result = "SUCCESS"
        render(text: returnMap as JSON, contentType: "application/json", encoding: "UTF-8")


    }

}
