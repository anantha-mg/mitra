package mitra

import java.util.regex.Matcher
import java.util.regex.Pattern
import grails.converters.JSON
/**
 * Created by anantha on 29/8/15.
 */
class TagService {

    /** For every comment in chat, find out one or more tags it belongs to. ex:
     a) Traffic at M.G. Road
     b) India-Pak cricket score
     c) Timing of Volvo Bus from Indiranagar to Airport
     */

    def Set<Tag> getTagForComment(String comment) {

        // 1) Use the comment String and find which category/tag it is likely to belong to
        // 2) Use the user tags to see which topics he usually posts to determine the tag


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

        def tagsUsingHashTag = getTagUsingExactMatch(comment)

        return (finalTags.keySet() + tagsUsingHashTag) as Set

    }

    /* Rules for allowing a tag

           a) Min 5 tags is needed to qualify [ to eliminate new tags - still in learning phase]
           b) If total tags count is "X", then all tags that constitute the first 75% max hits will qualify
              Wankhede -> Mumbai(10000), Cricket(2000) . Here only Mumbai might qualify

         */
    def filterTags(HashMap hashMap){
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

    def void updateMap(HashMap hashMap, Tag key, Integer increment){
        Integer updateValue = (hashMap.get(key) == null) ? increment : hashMap.get(key) + increment
        hashMap.put(key, updateValue);
    }

    // Returns tag along with count for a given word - ex: Sachin Tendulkar might return (Cricket,10000), (Mumbai, 50)
    def HashMap<Tag, Integer> getTagsForWord(String word) {

        HashMap<Tag,Integer> tags = new HashMap<Tag,Integer>();

        Tag.getAll().each {tag ->
            if (word.toUpperCase().contains(tag.name.toUpperCase())) {
                tags.put(tag, 100)
            }

        }

        return tags;
    }

    def List<String> getUniqueWordsFromComment(String comment) {
        String[]tokens = comment.split(" |,");
        List<String> words = new ArrayList<String>();

        for(String token : tokens) {
            if(token?.trim()){
                words.add(token);
            }
        }

        return words;
    }

    def isCommonWord(String word) {
        //TODO load from DB
        def commonWords = ["a", "about", "all", "and", "are", "as", "at", "back", "be",
                           "because", "been", "but", "can", "can't", "come", "could",
                           "did", "didn't", "do", "don't", "for", "from", "get", "go",
                           "going", "good", "got", "had", "have", "he", "her", "here",
                           "he's", "hey", "him", "his", "how", "I", "if", "I'll", "I'm",
                           "in", "is", "it", "it's", "just", "know", "like", "look",
                           "me", "mean", "my", "no", "not", "now", "of", "oh", "OK",
                           "okay", "on", "one", "or", "out", "really", "right", "say",
                           "see", "she", "so", "some", "something", "tell", "that",
                           "that's", "the", "then", "there", "they", "think", "this",
                           "time", "to", "up", "want", "was", "we", "well", "were",
                           "what", "when", "who", "why", "will", "with", "would",
                           "yeah", "yes", "you", "your", "you're"] ;

        if(!word) return false;

        return commonWords.contains(word)

    }

    def getTagUsingExactMatch(String comment) {

        List<Tag> result = new ArrayList<Tag>();

        Pattern HASH_TAG_PATTERN = Pattern.compile("#(\\w+)");
        Matcher match = HASH_TAG_PATTERN.matcher(comment);
        while(match.find()) {
            Tag tag = Tag.findByName(match.group(1))
            if (tag) {
                result.add(tag)
            }
        }
        return result
    }


    def getUsersByTag(tagList) {

        def returnMap = [:]
        returnMap.status = "FAILED"

        List<User> users = User.createCriteria().list{
            tags {
                'in'('name', tagList)
            }
        }

        return users
    }

}
