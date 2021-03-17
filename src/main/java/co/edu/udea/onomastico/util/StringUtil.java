package co.edu.udea.onomastico.util;

public class StringUtil {

	public static String getFirstWord(String sentence) {
		if(sentence.contains(" ")) {
			int stopIndex = sentence.indexOf(" ");
			return sentence.substring(0, stopIndex);
		}
		return sentence;
	}
	public static String capitalizeFirstLetter(String text) {
		  return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
	}
	
	public static StringBuilder replaceText(String target, StringBuilder texto, String replacement) {
		if(texto.toString().contains(target)) {
			int startIndex = texto.indexOf(target);
			int stopIndex = startIndex + target.length();
			texto.replace(startIndex, stopIndex, replacement);
		}
		return texto;
		
	}
}
