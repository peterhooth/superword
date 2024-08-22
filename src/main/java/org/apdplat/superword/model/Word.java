/**
 *
 * APDPlat - Application Product Development Platform
 * Copyright (c) 2013, 杨尚川, yang-shangchuan@qq.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.apdplat.superword.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 英语单词
 * @author 杨尚川
 */
public class Word implements Comparable{
    private String word;
    private String meaning;
    private Set<String> definitions = new HashSet<>();
    private Set<String> partOfSpeeches = new HashSet<>();

    public Word(){}
    public Word(String word, String meaning) {
        this.word = word;
        this.meaning = meaning;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Set<String> getDefinitions() {
        return Collections.unmodifiableSet(definitions);
    }

    public void addDefinition(String definition) {
        this.definitions.add(definition);
    }

    public void removeDefinition(String definition) {
        this.definitions.remove(definition);
    }

    public Set<String> getPartOfSpeeches() {
        return Collections.unmodifiableSet(partOfSpeeches);
    }

    public String getFormatPartOfSpeeches() {
        if(partOfSpeeches.isEmpty()){
            return "";
        }
        StringBuilder text = new StringBuilder();
        partOfSpeeches.forEach(w -> text.append(w).append(":"));
        text.setLength(text.length()-1);
        return text.toString();
    }

    public void addPartOfSpeech(String partOfSpeech) {
        this.partOfSpeeches.add(partOfSpeech);
    }

    public void removePartOfSpeech(String partOfSpeech) {
        this.partOfSpeeches.remove(partOfSpeech);
    }

    @Override
    public String toString() {
        return word;
    }

    @Override
    public int compareTo(Object o) {
        if(this == o){
            return 0;
        }
        if(this.word == null){
            return -1;
        }
        if(o == null){
            return 1;
        }
        if(!(o instanceof Word)){
            return 1;
        }
        String t = ((Word)o).getWord();
        if(t == null){
            return 1;
        }
        return this.word.compareTo(t);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Word)) return false;

        Word word1 = (Word) o;

        if (word != null ? !word.equals(word1.word) : word1.word != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return word != null ? word.hashCode() : 0;
    }
}