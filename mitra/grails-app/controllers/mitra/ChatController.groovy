package mitra

import grails.converters.JSON

class ChatController {

    def index() { }

    def tagService
    def utilService
    def sessionFactory

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

//        comment.save(flush: true, failOnError: true)


        Chat chat = new Chat();
        chat.tags = tagService.getTagForComment(commentString)
        chat.createdBy = user
        chat.status = Status.OPEN
        chat.chatType = Type.TEXT
        chat.updatedOn = new Date()
        chat.save(flush: true, failOnError: true)

        Comment comment = new Comment(comment: commentString, user: user, createdOn: new Date(), chat: chat)
        comment.save(flush: true, failOnError: true)

        List<User> answererList = chat.tags ? tagService.getUsersByTag(chat.tags.collect {it.name}) : tagService.getUsersByTag(Tag.list().collect {it.name})

        for(User answerer : answererList){
            utilService.pushNotif(answerer.deviceId, commentString)
        }

        returnMap = ["chatId" : chat.id, "tags":chat.tags, status:"SUCCESS"]
        render(text: returnMap as JSON, contentType: "application/json", encoding: "UTF-8")

    }

    def getChat = {
        def returnMap = [:]
        returnMap.status = "FAILED"
        def chatId = Integer.parseInt(params.CHAT_ID)

        def chat = Chat.get(chatId)

        List<Comment> comments = Comment.findAllByIdInList(chat.comments.collect{it.id})
        def commentListMap = comments.collect{
            [comment: it.comment, createdOn: it.createdOn, user: User.get(it.user.id).deviceId]
        }

        def commentMap = [chatId: chat.id, comments: commentListMap.sort{it.createdOn}]

        returnMap = ["chat" : commentMap, status:"SUCCESS"]
        render(text: returnMap as JSON, contentType: "application/json", encoding: "UTF-8")
    }

    def getUserChats = {
        def returnMap = [:]
        returnMap.status = "FAILED"
        def deviceId = params.DEVICE_ID
        def user = utilService.getUser(deviceId)

        List<Chat> chats = Chat.findAllByCreatedBy(user)
        chats.findAll{it.status == Status.OPEN}.sort{it.updatedOn}

        def commentMap = chats.collect{chat ->
            return [chatId: chat.id, comments: Comment.findAllByIdInList(chat.comments.collect{it.id}).sort{it.createdOn}]
        }

        returnMap = ["chats" : commentMap, status:"SUCCESS"]
        render(text: returnMap as JSON, contentType: "application/json", encoding: "UTF-8")
    }

    def getOtherChats = {
        def returnMap = [:]
        returnMap.status = "FAILED"
        def tag = params.TAG
        def deviceId = params.DEVICE_ID

        List<Chat> chats = new ArrayList<Chat>();

        if(tag && tag != "") {
            chats = Chat.createCriteria().list {
                tags {
                    'eq'('name', tag)
                }
            }
        } else {
            chats = Chat.getAll()
        }

        chats = chats.findAll{chat -> chat.user.deviceId != deviceId}

        chats.findAll{it.status == Status.OPEN}.sort{it.updatedOn}

        def commentMap = chats.collect{chat ->
            return [chatId: chat.id, comments: Comment.findAllByIdInList(chat.comments.collect{it.id}).sort{it.createdOn}]
        }

        returnMap = ["chats" : commentMap, status:"SUCCESS"]
        render(text: returnMap as JSON, contentType: "application/json", encoding: "UTF-8")
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


        def chat = Chat.get(Integer.parseInt(chatId))
        if (!chat) {
            render(text: returnMap as JSON, contentType: "application/json", encoding: "UTF-8")
            return
        }

        Comment comment = new Comment(comment: commentString, user: user, createdOn: new Date(), chat: chat)
        comment.save(flush: true)

        returnMap.status = "SUCCESS"
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
        if (!chat || utilService.getUser(deviceId) != chat.createdBy) {
            render(text: returnMap as JSON, contentType: "application/json", encoding: "UTF-8")
            return
        }

        chat.status = status
        chat.save(flush: true)

        returnMap.status = "SUCCESS"
        render(text: returnMap as JSON, contentType: "application/json", encoding: "UTF-8")


    }

}
