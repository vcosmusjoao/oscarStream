package br.com.letscode.java;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Aplicacao {

    private List<Oscar> oscarMale;
    private List<Oscar> oscarFemale;


    public static void main(String[] args)  {
        Aplicacao app = new Aplicacao();
        app.prepararLeituraArquivoCsvMALE();
        app.prepararLeituraArquivoCsvFEMALE();
        app.getAtorMaisJovem();
        app.getAtrizQueMaisGanhou();
        app.getAtrizEntreVinteTrinta();
        app.getAtorEAtrizQueGanhouMaisDeuM();
        app.getInfoSobreAtores("Tom Hanks");
        app.getInfoSobreAtores("Audrey Hepburn");


    }



    private void getInfoSobreAtores(String name){
        System.out.println("----------------------------------------------");
        System.out.println("TODAS AS PREMIAÇÕES DE "+name);
        this.oscarMale.stream()
                .filter(i-> Objects.equals(i.getName(),name))
                .forEach(System.out::println);
        this.oscarFemale.stream()
                .filter(m->Objects.equals(m.getName(),name))
                .forEach(System.out::println);
    }


    private void getAtorEAtrizQueGanhouMaisDeuM(){
        System.out.println("----------------------------------------------");
        System.out.println("#ATORES E ATRIZES QUE RECEBERAM MAIS DE UM OSCAR");
        System.out.println("-----------ATRIZES-------------");
        Map<String,Long> atriz= this.oscarFemale.stream()
                .map(Oscar::getName)
                .collect(Collectors.groupingBy(Function.identity(),Collectors.counting()));
        atriz.entrySet().stream()
                .filter(a->a.getValue()>1)
                .forEach(System.out::println);
        System.out.println("-----------ATORES-------------");
        Map<String,Long> ator= this.oscarMale.stream()
                .map(Oscar::getName)
                .collect(Collectors.groupingBy(Function.identity(),Collectors.counting()));
        ator.entrySet().stream()
                .filter(b->b.getValue()>1)
                .forEach(System.out::println);

    }


    private void getAtrizEntreVinteTrinta() {
        System.out.println("----------------------------------------------");
        System.out.println("ATRIZ QUE MAIS VENCEU O OSCAR ENTRE 20 E 30 ANOS.");
        Map<String, Long> nome = this.oscarFemale.stream()
                .filter(a -> a.getAge() > 20 && a.getAge() < 30)
                .map(Oscar::getName)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        nome.entrySet().stream()
                .max(Comparator.comparingLong(Map.Entry::getValue))
                .ifPresent(c -> System.out.println(c.getKey() + " é a atriz entre 20 e 30 anos com mais Oscar com um total de "+c.getValue()+" estatuetas"));
    }

    private void getAtrizQueMaisGanhou() {
        System.out.println("----------------------------------------------");
        System.out.println("ATRIZ QUE MAIS VENCEU O OSCAR.");
        Map<String,Long> nome= this.oscarFemale.stream()
                .map(Oscar::getName)
                .collect(Collectors.groupingBy(Function.identity(),Collectors.counting()));
        nome.entrySet().stream()
                .max(Comparator.comparingLong(Map.Entry::getValue))
                .ifPresent(c-> System.out.println(c.getKey()+" é a que mais possui Oscar de toda a lista com um total de "+c.getValue()+" estatuetas"));
    }

    private void getAtorMaisJovem() {
        System.out.println("----------------------------------------------");
        System.out.println(" ATOR MAIS JOVEM A GANHAR UM OSCAR");
        String filepath = getFilepathFromResourceAsStream("oscarmale.csv");
        try (Stream<String> lines = Files.lines(Path.of(filepath))) {
            lines
                    .skip(1)
                    .map(Oscar::fromLine)
                    .min(Comparator.comparingInt(Oscar::getAge))
                    .ifPresent(c-> System.out.println(c.getName()+" aos "+c.getAge()+" anos"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void prepararLeituraArquivoCsvMALE() {
        String filepath = getFilepathFromResourceAsStream("oscarmale.csv");
        try (Stream<String> lines = Files.lines(Path.of(filepath))) {
            this.oscarMale=lines
                    .skip(1)
                    .map(Oscar::fromLine)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void prepararLeituraArquivoCsvFEMALE() {
        String filepath = getFilepathFromResourceAsStream("oscarfemale.csv");
        try (Stream<String> lines = Files.lines(Path.of(filepath))) {
            this.oscarFemale=
                    lines
                    .skip(1)
                    .map(Oscar::fromLine)
                            .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFilepathFromResourceAsStream(String fileName) {
       URL url = getClass().getClassLoader().getResource(fileName);
        File file = new File(url.getFile());
        return file.getPath();
    }




}




