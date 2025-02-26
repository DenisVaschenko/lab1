import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
public class App {
    public static void main(String[] args) throws Exception {
        String path = "src/text.txt";
        String output = "src/result.txt";
        String stopPath = "src/stop.txt";
        List<String> stopWords = Files.readAllLines(Paths.get(stopPath),StandardCharsets.UTF_8);
        List<String> text = Files.readAllLines(Paths.get(path),StandardCharsets.UTF_8);
        String result = "";
        String PerfectioveGround = "(ив|ивши|ившись)$";
        String Infinitive = "(ти|учи|ячи|вши|ши|ати|яти|ючи)$";
        String Reflexive = "(с[яьи])$";
        String Adjective = "(ими|ій|ий|а|е|ова|ове|ів|є|їй|єє|еє|" +
                        "я|ім|ем|им|ім|их|іх|ою|йми|іми|у|ю|ого|ому|ої)$";
        String Participle = "(ий|ого|ому|им|ім|а|ій|у|ою|ій|і|их|" + 
                        "йми|их)$";
        String Verb = "(сь|ся|ив|ать|ять|у|ю|ав|али|учи|ячи|" +
                        "вши|ши|е|ме|ати|яти|є)$";
        String Noun = "(а|ев|ов|е|ями|ами|еи|и|ей|ой|ий|й|" +
                        "иям|ям|ием|ем|ам|ом|о|у|ах|иях|ях|ы|ь|ию|ью|ю|" +
                        "ия|ья|я|і|ові|ї|ею|єю|ою|є|еві|ем|єм|ів|їв|\\ꞌю)$";
        //String Rvre = "^(.*?[аеиоуюяіїє])(.*)$";
        String Deravational = "[^аеиоуюяіїє][аеиоуюяіїє]+" +
                        "[^аеиоуюяіїє]+[аеиоуюяіїє].*сть?$";
        for (String line : text) {
            String[] words = line.split("[\\pP\\s&&[^’'-]]+");
            for (String word : words) {
                if (stopWords.contains(word)) continue;
                if (Pattern.compile(Infinitive).matcher(word).find()){
                    if (word != "") result += word + ", ";
                    continue;
                }
                if (Pattern.compile(PerfectioveGround).matcher(word).find()) word = word.replaceFirst(PerfectioveGround, "");
                else if (Pattern.compile(Reflexive).matcher(word).find()) word = word.replaceFirst(Reflexive, "");
                else if (Pattern.compile(Participle).matcher(word).find()) word = word.replaceFirst(Participle, "");
                if (Pattern.compile(Adjective).matcher(word).find()) word = word.replaceFirst(Adjective, "");
                else if (Pattern.compile(Noun).matcher(word).find()) word = word.replaceFirst(Noun, "");
                else if (Pattern.compile(Verb).matcher(word).find()) word = word.replaceFirst(Verb, "");
                if (word.matches("^.*і$")) word = "";
                if (Pattern.compile(Deravational).matcher(word).find()) word = word.replaceAll("ість?$", "");
                if (Pattern.compile("ь$").matcher(word).find()) word = word.replaceFirst("ь$", "");
                else{
                    word = word.replaceFirst("ейше?$", "");
                    word = word.replaceFirst("нн?$", "н");
                }
                if (word != "") result += word + ", ";
            }
        }
        result.replaceFirst(", $", ".");
        Files.writeString(Paths.get(output), result);
    }
}
