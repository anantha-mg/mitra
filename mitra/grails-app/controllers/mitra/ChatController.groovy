package mitra

import java.util.regex.Matcher
import java.util.regex.Pattern

class ChatController {

    def index() { }


    def addNewChat = {
        String comment = params.COMMENT
        def userId = Integer.parseInt(paramas.USER_ID)
        Chat chat = new Chat();

        chat.comments = [comment]
        chat.tags = processComment(comment)
        chat.createdBy = User.get(userId)
        chat.status = Status.OPEN
        chat.updatedOn = new Date()
        chat.chatType = Type.TEXT
        chat.save()

        return new Expando("chatId" : chat.id, "tags":chat.tags)
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

    def processComment(String comment) {

        List<Tag> result = new ArrayList<Tag>();

        Pattern HASH_TAG_PATTERN = Pattern.compile("#(\\w+)");
        Matcher match = HASH_TAG_PATTERN.matcher(comment);
        while(match.find()) {
            Tag tag = Tag.findByName(match.group(1))
            result.add(tag)
        }
        return result
    }
}
