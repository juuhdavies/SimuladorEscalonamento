package util;

import modelo.Processo;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadCSV {
    public static List<Processo> carregarProcessos(String path) {
        List<Processo> listaProcessos = new ArrayList<>();
        String line;

        try(BufferedReader br = new BufferedReader(new FileReader(path))){
            //descartar cabeçalho
            String cabecalho = br.readLine();

            while ((line = br.readLine()) != null){
                //dividir conteúdo das linhas a cada virgula
                String[] info = line.split(",");

                String pid = info[0].trim();
                String nomeProcesso = info[1].trim();
                int tempoChegada = Integer.parseInt(info[2].trim());
                int tempoCpuTotal = Integer.parseInt(info[3].trim());
                int prioridade = Integer.parseInt(info[4].trim());
                String tipoProcesso = info[5].trim();
                int operacaoEs = Integer.parseInt(info[6].trim());
                double probabilidadeEs = Double.parseDouble(info[7].trim());
                double mediaEs = Double.parseDouble(info[8].trim());
                int duracaoEs = Integer.parseInt(info[9].trim());
                int filaSugerida = Integer.parseInt(info[10].trim());
                int quantumSugerido = Integer.parseInt(info[11].trim());

                //cria novo processo e adiciona na lista
                Processo p = new Processo(
                    pid, nomeProcesso, tempoChegada, tempoCpuTotal,
                    prioridade, tipoProcesso, operacaoEs, probabilidadeEs, 
                    mediaEs, duracaoEs, filaSugerida, quantumSugerido);

                listaProcessos.add(p);
            } 
        }catch(IOException e){
            System.err.println("Erro ao ler arquivo" + e.getMessage());
        }

        return listaProcessos;
    }
}
