package com.bubble.simpleword.db;

import org.litepal.crud.DataSupport;

/**
 * <p>Title: Words</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-8-6 下午8:09:23
 */
public class WordsClass extends DataSupport {

	private String word;	//单词
	private String phonetic_symbol;	//音标
	private String definition;	//释义

	public WordsClass(){
		
	}
	
	public WordsClass(String word, String phonetic_symbol, String definition) {
		setWord(word);
		setPhonetic_symbol(phonetic_symbol);
		setDefinition(definition);
	}
	
	public String getWord() {
		return word;
	}
	
	public void setWord(String word) {
		this.word = word;
	}
	
	public String getPhonetic_symbol() {
		return phonetic_symbol;
	}
	
	public void setPhonetic_symbol(String phonetic_symbol) {
		this.phonetic_symbol = phonetic_symbol;
	}
	
	public String getDefinition() {
		return definition;
	}
	
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	
	public String toString(){
		String s;
		s = word + phonetic_symbol + definition;
		return s;
	}

}
