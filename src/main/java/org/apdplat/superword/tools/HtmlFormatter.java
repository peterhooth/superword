/**
 *
 * APDPlat - Application Product Development Platform Copyright (c) 2013, 杨尚川,
 * yang-shangchuan@qq.com
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.apdplat.superword.tools;

import org.apache.commons.lang.StringUtils;
import org.apdplat.superword.model.SynonymAntonym;
import org.apdplat.superword.model.SynonymDiscrimination;
import org.apdplat.superword.model.Word;
import org.apdplat.superword.rule.PartOfSpeech;

import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.apdplat.superword.tools.WordLinker.Dictionary;

/**
 * HTML格式化工具，将生成的HTML片段发布到网络上的博客、日志中
 * @author 杨尚川
 */
public class HtmlFormatter {
    private HtmlFormatter(){}
    private static final String RED_EM_PRE = "<span style=\"color:red\">";
    private static final String RED_EM_SUF = "</span>";
    private static final String BLUE_EM_PRE = "<span style=\"color:blue\">";
    private static final String BLUE_EM_SUF = "</span>";

    public static String toHtmlFragmentForText(Map<String, AtomicInteger> data, Set<String> fileNames) {
        return toHtmlFragmentForText(data, fileNames, Dictionary.ICIBA);
    }
    public static String toHtmlFragmentForText(Map<String, AtomicInteger> data, Set<String> fileNames, Dictionary dictionary) {
        StringBuilder html = new StringBuilder();
        html.append("统计书籍：<br/>\n");
        AtomicInteger i = new AtomicInteger();
        fileNames.stream()
                .sorted()
                .forEach(fileName -> html.append(i.incrementAndGet())
                        .append("、")
                        .append(Paths.get(fileName).toFile().getName().replace(".txt", ""))
                        .append("<br/>\n"));
        Map<Integer, TextAnalyzer.Stat> stat = TextAnalyzer.distribute(data);
        html.append("共有")
                .append(data.size())
                .append("个单词，出现次数统计：<br/>\n")
                .append("<table  border=\"1\"  bordercolor=\"#00CCCC\"  width=\"850\">\n\t<tr><td>序号</td><td>出现次数</td><td>单词个数</td><td>单词</td></tr>\n");
        AtomicInteger k = new AtomicInteger();
        stat.keySet()
                .stream()
                .sorted((a, b) -> b - a)
                .forEach(s -> {
                    html.append("\t<tr><td>")
                            .append(k.incrementAndGet())
                            .append("</td><td>")
                            .append(s)
                            .append("</td><td>")
                            .append(stat.get(s).count())
                            .append("</td><td>");
                    AtomicInteger z = new AtomicInteger();
                    List<String> list = stat.get(s).getWords();
                    list.stream()
                            .sorted()
                            .forEach(w -> {
                                if (list.size() > 1) {
                                    html.append(z.incrementAndGet())
                                            .append(".")
                                            .append(WordLinker.toLink(w, dictionary))
                                            .append(" ");
                                } else if (list.size() == 1) {
                                    html.append(WordLinker.toLink(w, dictionary));
                                }
                            });
                    html.append("</td></tr>\n");
                });
        html.append("</table>")
                .append("\n共有(")
                .append(data.size())
                .append(")个单词：<br/>\n")
                .append("<table>\n\t<tr><td>序号</td><td>单词</td><td>词频</td></tr>\n");
        AtomicInteger wordCounter = new AtomicInteger();
        data.entrySet()
                .stream()
                .filter(entry -> entry.getKey().length() <= 14)
                .sorted((a, b) -> b.getValue().get() - a.getValue().get())
                .forEach(entry -> {
                    html.append("\t")
                            .append("<tr><td>")
                            .append(wordCounter.incrementAndGet())
                            .append("</td><td>")
                            .append(WordLinker.toLink(entry.getKey(), dictionary))
                            .append("</td><td>")
                            .append(entry.getValue().get())
                            .append("</td></tr>\n");

                });
        html.append("</table>\n")
                .append("长度大于14的词：")
                .append("\n<table>\n\t<tr><td>序号</td><td>单词</td><td>词频</td></tr>\n");
        AtomicInteger j = new AtomicInteger();
        data.entrySet()
                .stream()
                .filter(entry -> entry.getKey().length() > 14)
                .sorted((a, b) ->
                        b.getValue().get() - a.getValue().get())
                .forEach(entry ->
                        html.append("\t")
                                .append("<tr><td>")
                                .append(j.incrementAndGet())
                                .append("</td><td>")
                                .append(WordLinker.toLink(entry.getKey(), dictionary))
                                .append("</td><td>")
                                .append(entry.getValue().get())
                                .append("</td></tr>\n"));

        html.append("</table>\n")
                .append("长度为2的词：")
                .append("\n<table>\n\t<tr><td>序号</td><td>单词</td><td>词频</td></tr>\n");
        AtomicInteger z = new AtomicInteger();
        data.entrySet()
                .stream()
                .filter(entry -> entry.getKey().length() == 2)
                .sorted((a, b) ->
                        b.getValue().get() - a.getValue().get())
                .forEach(entry ->
                        html.append("\t")
                                .append("<tr><td>")
                                .append(z.incrementAndGet())
                                .append("</td><td>")
                                .append(WordLinker.toLink(entry.getKey(), dictionary))
                                .append("</td><td>")
                                .append(entry.getValue().get())
                                .append("</td></tr>\n"));
        html.append("</table>");
        return html.toString();
    }

    public static String toHtmlForSentence(Map<String, String> data, Map<Word, AtomicInteger> wordFrequence){
        return toHtmlForSentence(data, wordFrequence, Dictionary.ICIBA);
    }
    public static String toHtmlForSentence(Map<String, String> data, Map<Word, AtomicInteger> wordFrequence, Dictionary dictionary){
        StringBuilder text = new StringBuilder();
        text.append("共有 ")
                .append(data.size())
                .append(" 句子，")
                .append(wordFrequence.size())
                .append(" 个单词。<br/>\n")
                .append("<h4>一、句子("+data.size()+")：</h4>\n");
        AtomicInteger i = new AtomicInteger();
        data
            .keySet()
            .stream()
            .sorted((a, b) -> a.length() - b.length())
            .forEach(s -> text
                    .append(i.incrementAndGet())
                    .append("、")
                    .append(processSentence(s, wordFrequence, dictionary))
                    .append("  ")
                    .append(data.get(s))
                    .append("<br/>\n"));
        text
            .append("<br/>\n<h4>二、单词("+wordFrequence.size()+")：</h4>\n")
            .append(HtmlFormatter.toHtmlTableFragment(wordFrequence, 6, dictionary));
        return text.toString();
    }

    private static String processSentence(String sentence, Map<Word, AtomicInteger> wordFrequence){
        return processSentence(sentence, wordFrequence, Dictionary.ICIBA);
    }
    private static String processSentence(String sentence, Map<Word, AtomicInteger> wordFrequence, Dictionary dictionary){
        sentence = sentence.replace(";", "; ")
                .replace(",", ", ")
                .replace(".", ". ")
                .replace("?", "? ")
                .replace("!", "! ");
        StringBuilder s = new StringBuilder();
        for(String w : sentence.split("\\s+")){
            if(w.endsWith(";")
                    || w.endsWith(",")
                    || w.endsWith(".")
                    || w.endsWith("?")
                    || w.endsWith("!")){
                Word word = new Word(w.substring(0, w.length()-1).toLowerCase(), "");
                if(wordFrequence.containsKey(word)
                        && wordFrequence.get(word).get()<10){
                    s.append(WordLinker.toLink(word.getWord(), dictionary)).append(w.substring(w.length()-1)).append(" ");
                }else{
                    s.append(w).append(" ");
                }

            }else {
                Word word = new Word(w.toLowerCase(), "");
                if (wordFrequence.containsKey(word)
                        && wordFrequence.get(word).get() < 10) {
                    s.append(WordLinker.toLink(w, dictionary)).append(" ");
                } else {
                    s.append(w).append(" ");
                }
            }
        }
        return s.toString();
    }

    public static String toHtmlForCompoundWord(Map<Word, Map<Integer, List<Word>>> data, int rowLength){
        return toHtmlForCompoundWord(data, rowLength, Dictionary.ICIBA);
    }
    public static String toHtmlForCompoundWord(Map<Word, Map<Integer, List<Word>>> data, int rowLength, Dictionary dictionary){
        Set<Word> elements = new HashSet<>();
        StringBuilder html = new StringBuilder();

        html.append("<br/>复合词数(")
                .append(data.size())
                .append("): <br/><br/>\n");

        html.append("<table  border=\"1\">\n");
        AtomicInteger i = new AtomicInteger();
        data
                .entrySet()
                .stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .sorted((a, b) -> b.getValue().size() - a.getValue().size())
                .forEach(entry -> {
                    html.append("\t<tr><td>")
                            .append(i.incrementAndGet())
                            .append("</td><td>")
                            .append(WordLinker.toLink(entry.getKey().getWord(), dictionary))
                            .append("</td>");
                    entry
                            .getValue()
                            .values()
                            .forEach(words -> {
                                words.forEach(word -> {
                                    html.append("<td>")
                                            .append(WordLinker.toLink(word.getWord(), dictionary))
                                            .append("</td>");
                                    elements.add(word);
                                });
                            });
                    html.append("</tr>\n");
                });
        html.append("</table>\n");

        if(elements.isEmpty() || data.size() <= 1){
            return html.toString();
        }

        html.append("\n<br/>不重复的被组合词数(")
                .append(elements.size())
                .append("): <br/><br/>\n");

        List<String> words = elements
                .stream()
                .sorted()
                .map(word -> WordLinker.toLink(word.getWord(), dictionary))
                .collect(Collectors.toList());
        html.append(toHtmlTableFragment(words, rowLength));

        return html.toString();
    }

    public static String toHtmlForPartOfSpeech(Map<String, Set<String>> data){
        return toHtmlForPartOfSpeech(data, Dictionary.ICIBA);
    }
    public static String toHtmlForPartOfSpeech(Map<String, Set<String>> data, Dictionary dictionary){
        StringBuilder html = new StringBuilder();
        html.append("<h4>各大词性广泛度排名：</h4><br/>\n");
        AtomicInteger i = new AtomicInteger();
        data.entrySet().stream().sorted((a, b) -> b.getValue().size() - a.getValue().size()).forEach(e -> {
            String k = e.getKey();
            html.append(i.incrementAndGet())
                    .append("、")
                    .append(RED_EM_PRE)
                    .append(k)
                    .append(RED_EM_SUF)
                    .append("(")
                    .append(BLUE_EM_PRE)
                    .append(PartOfSpeech.getMeaning(k))
                    .append(BLUE_EM_SUF)
                    .append(") (词数:")
                    .append(data.get(k).size())
                    .append(")")
                    .append("<br/>\n");

        });
        html.append("<h4>各大词性及其包括的词：</h4><br/>\n");
        AtomicInteger j = new AtomicInteger();
        data.keySet().stream().sorted().forEach(k -> {
            html.append("<h4>")
                    .append(j.incrementAndGet())
                    .append("、")
                    .append(RED_EM_PRE)
                    .append(k)
                    .append(RED_EM_SUF)
                    .append("(")
                    .append(BLUE_EM_PRE)
                    .append(PartOfSpeech.getMeaning(k))
                    .append(BLUE_EM_SUF)
                    .append(") (词数:")
                    .append(data.get(k).size())
                    .append(")")
                    .append("</h4>\n")
                    .append(
                            toHtmlTableFragment(data.get(k).stream().sorted().map(w -> WordLinker.toLink(w, dictionary)).collect(Collectors.toList()), 5));
        });
        return html.toString();
    }

    public static String toHtmlForPluralFormat(Map<String, String> data){
        return toHtmlForPluralFormat(data, Dictionary.ICIBA);
    }
    public static String toHtmlForPluralFormat(Map<String, String> data, Dictionary dictionary){
        StringBuilder html = new StringBuilder();
        html.append("<table border=\"1\">\n")
            .append("\t<tr><td>单词原型</td><td>单词复数</td></tr>\n");
        data.keySet().forEach(key -> {
            String origin = key.substring(0, key.length() - data.get(key).length());
            html.append("\t<tr><td>").append(WordLinker.toLink(origin, dictionary)).append("</td><td>")
                .append(WordLinker.toLink(key, origin, BLUE_EM_PRE, BLUE_EM_SUF + "-", dictionary)).append("</td></tr>\n");

        });
        html.append("</table>\n");
        return html.toString();
    }

    public static String toHtmlForWordDefinition(Set<Word> words, int rowLength){
        return toHtmlForWordDefinition(words, rowLength, Dictionary.ICIBA);
    }
    public static String toHtmlForWordDefinition(Set<Word> words, int rowLength, Dictionary dictionary) {
        Map<Integer, AtomicInteger> map = new HashMap<>();
        words.stream().forEach(w -> {
            int count = w.getDefinitions().size();
            map.putIfAbsent(count, new AtomicInteger());
            map.get(count).incrementAndGet();
        });
        List<String> data =
                    words
                        .stream()
                        .sorted((a, b) -> b.getDefinitions().size() - a.getDefinitions().size())
                        .map(word -> WordLinker.toLink(word.getWord(), dictionary)+"-"+word.getDefinitions().size())
                        .collect(Collectors.toList());
        StringBuilder html = new StringBuilder();
        html.append(toHtmlTableFragment(data, rowLength))
            .append("<table border=\"1\">\n")
            .append("\t<tr><td>定义条数</td><td>单词个数</td></tr>\n");
        map.keySet().stream().sorted((a,b)->b-a).forEach(key -> {
            html.append("\t<tr><td>").append(key).append("</td><td>").append(map.get(key)).append("</td></tr>\n");
        });
        html.append("</table>\n");
        return html.toString();
    }

    public static String toHtmlForAntonym(Set<SynonymAntonym> synonymAntonyms, int rowLength){
        return toHtmlForAntonym(synonymAntonyms, rowLength, Dictionary.ICIBA);
    }
    public static String toHtmlForAntonym(Set<SynonymAntonym> synonymAntonyms, int rowLength, Dictionary dictionary){
        StringBuilder html = new StringBuilder();
        AtomicInteger i = new AtomicInteger();
        synonymAntonyms
                .stream()
                .sorted((a, b) -> b.getAntonym().size() - a.getAntonym().size())
                .forEach(sa -> {
                    if (!sa.getAntonym().isEmpty()) {
                        html.append("<h4>")
                                .append(i.incrementAndGet())
                                .append("、")
                                .append(WordLinker.toLink(sa.getWord().getWord(), dictionary))
                                .append("</h4>\n")
                                .append("<b>反义词(")
                                .append(sa.getAntonym().size())
                                .append(")：</b><br/>\n");
                        List<String> sm = sa.getAntonym().stream().sorted().map(w -> WordLinker.toLink(w.getWord(), dictionary)).collect(Collectors.toList());
                        html.append(toHtmlTableFragment(sm, rowLength))
                            .append("<br/>\n");
                    }
                });
        return html.toString();
    }

    public static String toHtmlForSynonymAntonym(Set<SynonymAntonym> synonymAntonyms, int rowLength){
        return toHtmlForSynonymAntonym(synonymAntonyms, rowLength, Dictionary.ICIBA);
    }
    public static String toHtmlForSynonymAntonym(Set<SynonymAntonym> synonymAntonyms, int rowLength, Dictionary dictionary){
        StringBuilder html = new StringBuilder();
        AtomicInteger i = new AtomicInteger();
        synonymAntonyms
                .stream()
                .sorted((a, b) -> b.size() - a.size())
                .forEach(sa -> {
                    html.append("<h4>")
                            .append(i.incrementAndGet())
                            .append("、")
                            .append(WordLinker.toLink(sa.getWord().getWord(), dictionary))
                            .append("</h4>\n");
                    if (!sa.getSynonym().isEmpty()) {
                        html.append("<b>同义词(").append(sa.getSynonym().size()).append(")：</b><br/>\n");
                        List<String> sm = sa.getSynonym().stream().sorted().map(w -> WordLinker.toLink(w.getWord(), dictionary)).collect(Collectors.toList());
                        html.append(toHtmlTableFragment(sm, rowLength));
                    }
                    if (!sa.getAntonym().isEmpty()) {
                        html.append("<b>反义词(").append(sa.getAntonym().size()).append(")：</b><br/>\n");
                        List<String> sm = sa.getAntonym().stream().sorted().map(w -> WordLinker.toLink(w.getWord(), dictionary)).collect(Collectors.toList());
                        html.append(toHtmlTableFragment(sm, rowLength));
                    }
                    html.append("<br/>\n");
                });
        return html.toString();
    }

    public static String toHtmlForSynonymDiscrimination(Set<SynonymDiscrimination> synonymDiscrimination){
        return toHtmlForSynonymDiscrimination(synonymDiscrimination, Dictionary.ICIBA);
    }
    public static String toHtmlForSynonymDiscrimination(Set<SynonymDiscrimination> synonymDiscrimination, Dictionary dictionary){
        StringBuilder html = new StringBuilder();
        AtomicInteger i = new AtomicInteger();
        synonymDiscrimination
        .stream()
        .sorted()
        .forEach(sd -> {
                html.append("<h4>")
                    .append(i.incrementAndGet())
                    .append("、")
                    .append(sd.getTitle())
                    .append("</h4>\n<b>")
                    .append(sd.getDes().replace("“", "“" + BLUE_EM_PRE).replace("”", BLUE_EM_SUF +"”"))
                    .append("</b><br/>\n");
                if (!sd.getWords().isEmpty()) {
                    html.append("<ol>\n");
                }
                sd.getWords()
                    .forEach(w -> {
                        html.append("\t<li>")
                                .append(WordLinker.toLink(w.getWord(), dictionary))
                                .append("：")
                                .append(w.getMeaning())
                                .append("</li>\n");
                    });
                if (!sd.getWords().isEmpty()) {
                    html.append("</ol>\n");
                }
        });
        return html.toString();
    }

    public static String toHtmlTableFragmentForRootAffix(Map<Word, List<Word>> rootAffixToWords, int rowLength){
        return toHtmlTableFragmentForRootAffix(rootAffixToWords, rowLength, Dictionary.ICIBA);
    }
    public static String toHtmlTableFragmentForRootAffix(Map<Word, List<Word>> rootAffixToWords, int rowLength, Dictionary dictionary) {
        StringBuilder html = new StringBuilder();
        AtomicInteger rootCounter = new AtomicInteger();
        Set<Word> unique = new HashSet<>();
        rootAffixToWords
        .keySet()
        .stream()
        .sorted()
        .forEach(rootAffix -> {
            List<Word> words = rootAffixToWords.get(rootAffix);
            html.append("<h4>")
                .append(rootCounter.incrementAndGet())
                .append("、")
                .append(rootAffix.getWord());
            if(StringUtils.isNotBlank(rootAffix.getMeaning())) {
                html.append(" (")
                    .append(rootAffix.getMeaning())
                    .append(") ");
            }
            html.append(" (hit ")
                .append(words.size())
                .append(")</h4>\n");
            List<String> data =
                    words
                        .stream()
                        .sorted()
                        .map(word -> {
                            unique.add(word);
                            return emphasize(word, rootAffix, dictionary);
                        })
                        .collect(Collectors.toList());
            html.append(toHtmlTableFragment(data, rowLength));
        });
        String head = "词根词缀数："+rootAffixToWords.keySet().size()+"，单词数："+unique.size()+"<br/>\n";
        return head+html.toString();
    }

    public static String emphasize(Word word, Word rootAffix){
        return emphasize(word, rootAffix, Dictionary.ICIBA);
    }
    public static String emphasize(Word word, Word rootAffix, Dictionary dictionary){
        String w = word.getWord();
        String r = rootAffix.getWord().replace("-", "").toLowerCase();
        //词就是词根
        if (w.length() == r.length()) {
            return WordLinker.toLink(w, r, dictionary);
        }
        //词根在中间
        if (w.length() > r.length()
                && !w.startsWith(r)
                && !w.endsWith(r)) {
            return WordLinker.toLink(w, r, "-" + RED_EM_PRE, RED_EM_SUF + "-", dictionary);
        }
        //词根在前面
        if (w.length() > r.length() && w.startsWith(r)) {
            return WordLinker.toLink(w, r, "" + RED_EM_PRE, RED_EM_SUF + "-", dictionary);
        }
        //词根在后面面
        if (w.length() > r.length() && w.endsWith(r)) {
            return WordLinker.toLink(w, r, "-" + RED_EM_PRE, RED_EM_SUF + "", dictionary);
        }
        return WordLinker.toLink(w, r, dictionary);
    }

    public static String toHtmlTableFragment(Map<Word, AtomicInteger> words, int rowLength){
        return toHtmlTableFragment(words, rowLength, Dictionary.ICIBA);
    }
    public static String toHtmlTableFragment(Map<Word, AtomicInteger> words, int rowLength, Dictionary dictionary) {
        return toHtmlTableFragment(words.entrySet(), rowLength, dictionary);
    }

    public static String toHtmlTableFragment(Set<Map.Entry<Word, AtomicInteger>> words, int rowLength){
        return toHtmlTableFragment(words, rowLength, Dictionary.ICIBA);
    }
    public static String toHtmlTableFragment(Set<Map.Entry<Word, AtomicInteger>> words, int rowLength, Dictionary dictionary) {

        List<String> data =
        words
            .stream()
            .sorted((a, b) -> b.getValue().get() - a.getValue().get())
            .map(entry -> {
                String link = WordLinker.toLink(entry.getKey().getWord(), dictionary);
                if (entry.getValue().get() > 0) {
                    link = link+"-"+entry.getValue().get();
                }
                return link;
            })
            .collect(Collectors.toList());

        return toHtmlTableFragment(data, rowLength);
    }

    public static List<String> toHtmlTableFragmentForIndependentWord(Map<Word, List<Word>> data, int rowLength, int wordsLength){
        return toHtmlTableFragmentForIndependentWord(data, rowLength, wordsLength, Dictionary.ICIBA);
    }
    public static List<String> toHtmlTableFragmentForIndependentWord(Map<Word, List<Word>> data, int rowLength, int wordsLength, Dictionary dictionary) {
        List<String> htmls = new ArrayList<>();
        StringBuilder html = new StringBuilder();
        AtomicInteger wordCounter = new AtomicInteger();
        data
            .keySet()
            .stream()
            .sorted()
            .forEach(word -> {
                html.append("<h4>")
                        .append(wordCounter.incrementAndGet())
                        .append("、")
                        .append(word.getWord())
                        .append(" (form ")
                        .append(data.get(word).size())
                        .append(")</h4>\n");
                List<String> result = data
                        .get(word)
                        .stream()
                        .map(rootAffix -> emphasize(word, rootAffix, dictionary))
                        .collect(Collectors.toList());
                html.append(toHtmlTableFragment(result, rowLength));
                result.clear();
                result = data
                        .get(word)
                        .stream()
                        .flatMap(rootAffix -> Arrays.asList(rootAffix.getWord(), rootAffix.getMeaning()).stream())
                        .collect(Collectors.toList());
                html.append(toHtmlTableFragment(result, 2));
                result.clear();
                if (wordCounter.get() % wordsLength == 0) {
                    htmls.add(html.toString());
                    html.setLength(0);
                }
            });
        if(html.length() > 0){
            htmls.add(html.toString());
        }
        return htmls;
    }

    public static String toHtmlTableFragment(List<String> data, int rowLength) {
        StringBuilder html = new StringBuilder();

        AtomicInteger rowCounter = new AtomicInteger();
        AtomicInteger wordCounter = new AtomicInteger();
        html.append("<table  border=\"1\">\n");
        data
            .forEach(datum -> {
                if (wordCounter.get() % rowLength == 0) {
                    if (wordCounter.get() == 0) {
                        html.append("\t<tr>");
                    } else {
                        html.append("</tr>\n\t<tr>");
                    }
                    rowCounter.incrementAndGet();
                    html.append("<td>").append(rowCounter.get()).append("</td>");
                }
                wordCounter.incrementAndGet();
                html.append("<td>").append(datum).append("</td>");
            });
        if(html.toString().endsWith("<tr>")){
            html.setLength(html.length()-5);
        }else{
            html.append("</tr>\n");
        }
        html.append("</table>\n");

        return html.toString();
    }
}
