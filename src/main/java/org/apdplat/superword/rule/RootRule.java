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
package org.apdplat.superword.rule;

import org.apache.commons.lang.StringUtils;
import org.apdplat.superword.model.Word;
import org.apdplat.superword.tools.HtmlFormatter;
import org.apdplat.superword.tools.WordSources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 词根规则
 * @author 杨尚川
 */
public class RootRule {
    private RootRule(){}

    private static final Logger LOGGER = LoggerFactory.getLogger(RootRule.class);

    public static List<Word> getAllRoots(){
        List<Word> roots = new ArrayList<>();
        // 流式解析, 自动关闭资源
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(PrefixRule.class.getResourceAsStream("/root_affix.txt")))){
            String line = null;
            while((line = bufferedReader.readLine()) != null){
                if(StringUtils.isNotBlank(line)
                        && !line.startsWith("#")
                        && line.startsWith("词根：")){
                    String[] attr = line.substring(3).split("杨尚川");
                    if(attr != null && attr.length == 2){
                        String root = attr[0];
                        String meaning = attr[1];
                        roots.add(new Word(root, meaning));
                        LOGGER.debug("词根："+root+meaning);
                    }else{
                        LOGGER.error("解析词根出错："+line);
                    }
                }
            }
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
        }
        return roots;
    }

    public static TreeMap<Word, List<Word>> findByRoot(Collection<Word> words, Collection<Word> roots) {
        TreeMap<Word, List<Word>> map = new TreeMap<>();
        roots.forEach(root -> map.put(root, findByRoot(words, root)));
        return map;
    }

    public static List<Word> findByRoot(Collection<Word> words, Word root) {
        return words
                .parallelStream()
                .filter(word -> {
                    //词区分大小写
                    String w = word.getWord();
                    if(Character.isUpperCase(w.charAt(0))){
                        return false;
                    }
                    //词根不区分大小写
                    String[] rs = root.getWord().toLowerCase().split(",");
                    //词中包含词根即可，不考虑位置和剩余部分
                    for(String s : rs) {
                        if(w.contains(s)){
                            return true;
                        }
                    }
                    return false;
                })
                .sorted()
                .collect(Collectors.toList());
    }

    public static void main(String[] args) throws Exception {
        Set<Word> words = WordSources.getSyllabusVocabulary();
        //List<Word> roots = RootRule.getAllRoots();
        //List<Word> roots = Arrays.asList(new Word("position", ""));
        //List<Word> roots = Arrays.asList(new Word("well", ""));
        List<Word> roots = Arrays.asList(new Word("equ", ""));


        TreeMap<Word, List<Word>> rootToWords = RootRule.findByRoot(words, roots);
        String htmlFragment = HtmlFormatter.toHtmlTableFragmentForRootAffix(rootToWords, 1);

        System.out.println(htmlFragment);
        Files.write(Paths.get("target/root_rule.txt"),htmlFragment.getBytes("utf-8"));
    }
}
