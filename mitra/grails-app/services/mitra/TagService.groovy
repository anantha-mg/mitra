package mitra

import java.util.regex.Matcher
import java.util.regex.Pattern
/**
 * Created by anantha on 29/8/15.
 */
class TagService {

    /** For every comment in chat, find out one or more tags it belongs to. ex:
     a) Traffic at M.G. Road
     b) India-Pak cricket score
     c) Timing of Volvo Bus from Indiranagar to Airport
     */

    public Set<Tag> getTagForComment(Comment c) {

        // 1) Use the comment String and find which category/tag it is likely to belong to
        // 2) Use the user tags to see which topics he usually posts to determine the tag

        String comment = c.comment;
        User user = c.user;

        // split comment into words and remove common words like "a","the","at" etc
        // for remaining words, hit the comments DB and find out which tag it is likely to belong to

        //Note: This requires a great ML platform and also a ElasticSearch kind of storage to do good job -
        // buts it pretty hard to do those things in a single day in hackathon - hence building a simple algo

        List<String> words = getUniqueWordsFromComment(comment)
        HashMap<Tag, Integer> finalTags = new HashMap<Tag, Integer>();
        for(String word : words){
            if(!isCommonWord(word)){
                HashMap<Tag, Integer> tagsMap = getTagsForWord(word);

                filterTags(tagsMap);

                for(Tag tag :  tagsMap.keySet()){
                    updateMap(finalTags, tag, tagsMap.get(tag))
                }
            }
        }

        filterTags(finalTags);


        return finalTags.keySet()

    }

    /* Rules for allowing a tag

           a) Min 5 tags is needed to qualify [ to eliminate new tags - still in learning phase]
           b) If total tags count is "X", then all tags that constitute the first 75% max hits will qualify
              Wankhede -> Mumbai(10000), Cricket(2000) . Here only Mumbai might qualify

         */
    private static void filterTags(HashMap hashMap){
        Integer totalCount = 0;
        for(Tag tag :  hashMap.keySet()){
            totalCount+= hashMap.get(tag)
        }

        for(Tag tag :  hashMap.keySet()){
            if(hashMap.get(tag) < 5)
                hashMap.remove(tag)
        }

        //TODO complete second part where based on percentages certain tags will be allowed
    }

    private static void updateMap(HashMap hashMap, Tag key, Integer increment){
        Integer updateValue = (hashMap.get(key) == null) ? increment : hashMap.get(key) + increment
        hashMap.put(key, updateValue);
    }

    // Returns tag along with count for a given word - ex: Sachin Tendulkar might return (Cricket,10000), (Mumbai, 50)
    public static List<Tag, Integer> getTagsForWord(String word) {
        //TODO
        null;
    }

    public static List<String> getUniqueWordsFromComment(String comment) {
        String[]tokens = comment.split(" |,");
        List<String> words = new ArrayList<String>();

        for(String token : tokens) {
            if(token?.trim()){
                words.add(token);
            }
        }

        return words;
    }

    public static boolean isCommonWord(String word) {
        //TODO load from DB
        def commonWords = ["a", "at", "the" ] ;

        if(!word) return false;

        return commonWords.contains(word)

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
