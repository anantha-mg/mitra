package mitra

import java.util.regex.Matcher
import java.util.regex.Pattern
/**
 * Created by anantha on 29/8/15.
 */
class TagService {

    // For every comment in chat, find out one or more tags it belongs to
    public List<Tag> getTagForComment(Comment comment) {

        // 1) Use the comment String and find which category/tag it is likely to belong to
        // 2) Use the user tags to see which topics he usually posts to determine the tag

        return getTagForComment(comment)

    }

    def getTagUsingExactMatch(String comment) {

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
