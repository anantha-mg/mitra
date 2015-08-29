package mitra;

import java.util.List;

/**
 * Created by anantha on 29/8/15.
 */
public class TagServiceTest {

    public static void main(String[] args) {
        List<String> words = TagService.getUniqueWordsFromComment("Traffic at M.G.Road, now");

        boolean isCommonWord = TagService.isCommonWord("at");

        Comment c = new Comment();
        c.setComment("India SriLanka Latest Cricket Score");
        TagService.getTagForComment(c);
    }

}
