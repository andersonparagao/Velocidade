/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package velocidade;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

/**
 *
 * @author Anderson Aragao
 */
public class Velocidade {

    /**
     *
     * Método para calcular as velocidades iniciais das Partículas. Gera-se um
     * valor aleatório de 0 a 5% do volume máximo da usina para cada mês do
     * período de planejamento, que corresponde a quanto o volume da usina será
     * esvaziado no primeiro mês. Posteriormente, calcula-se o valor do quando
     * aumentará o valor de cada uma das vazões defluentes, dado que a usina
     * esvaziou o seu reservatório.
     *
     * @param volumeVazao matriz com os volumes e as vazões defluentes de cada
     * usina do caso teste
     * 
     * @return o valor das velocidades iniciais das partículas.
     *
     */
    public static double[][] calculaVelocidadesIniciais(double[][] volumeVazao) {
        // para a conversão dos volumes (em hm3) para vazão (em m3/s)
        double fatorConversao = 2628000.0 / 1000000.0;
        // vetor que irá guardar as velocidades iniciais das usinas
        double[][] velocidadesIniciais = new double[volumeVazao.length][volumeVazao[0].length];
        // vetor que irá guardar os números aleatórios gerados para diminuir o volume das usinas (valor gerado entre 0 e 5%)
        double[] numerosAleatorios = new double[volumeVazao[0].length / 2];
        Random r = new Random();

        // for para gerar os números aleatórios de 0 a 5% (valor que será retirado do volume)
        for (int i = 0; i < numerosAleatorios.length; i++) {
            numerosAleatorios[i] = (0.05) * r.nextDouble();
        }

        // ordenação crescente os números aleatórios gerados
        double menor = 0;
        for (int i = 0; i < numerosAleatorios.length; i++) {
            for (int j = 0; j < numerosAleatorios.length; j++) {
                if (numerosAleatorios[i] < numerosAleatorios[j]) {
                    menor = numerosAleatorios[i];
                    numerosAleatorios[i] = numerosAleatorios[j];
                    numerosAleatorios[j] = menor;
                }
            }
        }

        // definir o quanto vai ser defluido em cada usina
        for (int i = 0; i < volumeVazao.length; i++) {
            for (int j = 0; j < volumeVazao[0].length/2; j++) {
                BigDecimal bd = new BigDecimal(numerosAleatorios[j]).setScale(11, RoundingMode.HALF_EVEN);
                numerosAleatorios[j] = bd.doubleValue();
                velocidadesIniciais[i][j] = -numerosAleatorios[j] * volumeVazao[i][j];
            }
        }

        // cálculo das velocidades iniciais das partículas
        for (int i = 0; i < velocidadesIniciais.length; i++) {
            for (int j = volumeVazao[0].length/2; j < velocidadesIniciais[0].length; j++) {
                if (i == 0 && j == volumeVazao[0].length/2) {
                    velocidadesIniciais[i][j] = ((-1) * velocidadesIniciais[i][j - (volumeVazao[0].length/2)] * fatorConversao);
                } else if (j == (volumeVazao[0].length/2)) {
                    velocidadesIniciais[i][j] = ((-1) * velocidadesIniciais[i][j - (volumeVazao[0].length/2)] * fatorConversao) + velocidadesIniciais[i - 1][j];
                }

                if (i == 0 && j > (volumeVazao[0].length/2)) {
                    velocidadesIniciais[i][j] = ((-1) * velocidadesIniciais[i][j - (volumeVazao[0].length/2)] - (-1) * velocidadesIniciais[i][j - (volumeVazao[0].length/2) - 1]) * fatorConversao;
                }

                if (i != 0 && j > (volumeVazao[0].length/2)) {
                    velocidadesIniciais[i][j] = ((-1) * velocidadesIniciais[i][j - (volumeVazao[0].length/2)] - (-1) * velocidadesIniciais[i][j - (volumeVazao[0].length/2) - 1]) * fatorConversao + velocidadesIniciais[i - 1][j];
                }
            }
        }

        // Exibição dos volumes
        for (int i = 0; i < velocidadesIniciais.length; i++) {
            System.out.println("");
            for (int j = 0; j < (volumeVazao[0].length/2); j++) {
                System.out.print(velocidadesIniciais[i][j] + ", ");
            }
        }

        // Exibição das defluências
        System.out.println();
        for (int i = 0; i < velocidadesIniciais.length; i++) {
            System.out.println("");
            for (int j = (volumeVazao[0].length/2); j < velocidadesIniciais[0].length; j++) {
                System.out.print(velocidadesIniciais[i][j] + ", ");
            }
        }
        return velocidadesIniciais;
    }


    public static void main(String[] args) {
        double[][] volumeVazao = {{17190, 17190, 17190, 17190, 17190, 17190, 17190, 17190, 17190, 17190, 17190, 17190,
                                   399, 306, 245, 196, 173, 206, 356, 656, 894, 904, 863, 623}, 
                                  
                                  {17027, 17027, 17027, 17027, 17027, 17027, 17027, 17027, 17027, 17027, 17027, 17027, 
                                   1331, 1024, 817, 655, 584, 704, 1147, 1971, 2727, 2850, 2765, 2043}, 
                                  
                                  {12540, 12540, 12540, 12540, 12540, 12540, 12540, 12540, 12540, 12540, 12540, 12540, 
                                   2084, 1623, 1309, 1067, 974, 1148, 1766, 2951, 4055, 4253, 4183, 3156}};

        double[][] velocidadesIniciais = Velocidade.calculaVelocidadesIniciais(volumeVazao);
    }
}
