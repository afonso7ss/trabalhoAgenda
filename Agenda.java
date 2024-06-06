package trabalho;

import java.util.ArrayList;
import java.util.List;

class Agenda {
    private Arvore<String> root; // Raiz da árvore, que representa a agenda

    public Agenda() {
        root = new Arvore<>("Agenda"); // Inicializa a árvore com a raiz "Agenda"
    }

    public void adicionarEvento(int dia, int mes, int ano, Evento evento) {
        if (!dataValida(dia, mes, ano)) {
            System.out.println("Data inválida: " + dia + "/" + mes + "/" + ano);
            return;
        }

        Arvore<String> anoNode = root.getChild(String.valueOf(ano));
        if (anoNode == null) {
            anoNode = new Arvore<>(String.valueOf(ano));
            root.addChild(anoNode);
        }

        // Verifica se o nó do mês já existe, senão, cria-o
        Arvore<String> mesNode = anoNode.getChild(String.format("%02d", mes));
        if (mesNode == null) {
            mesNode = new Arvore<>(String.format("%02d", mes));
            anoNode.addChild(mesNode);
        }

        // Verifica se o nó do dia já existe, senão, cria-o
        Arvore<String> diaNode = mesNode.getChild(String.format("%02d", dia));
        if (diaNode == null) {
            diaNode = new Arvore<>(String.format("%02d", dia));
            mesNode.addChild(diaNode);
        }

        diaNode.addChild(new Arvore<>(evento.toString()));
    }

    public void desenho() {
        root.printTree("");
    }

    public List<String> listarTodosEventos() {
        List<String> eventos = new ArrayList<>();
        listarEventosRecursivo(root, "", eventos);
        return eventos;
    }

    private void listarEventosRecursivo(Arvore<String> node, String caminho, List<String> eventos) {
        if (node != null) {
            String novoCaminho = caminho;
            if (caminho.isEmpty()) {
                novoCaminho = node.data;
            } else {
                novoCaminho = caminho + " -> " + node.data;
            }

            if (node.children.estaVazia()) {
                eventos.add(novoCaminho); // Adiciona o evento com o caminho completo
            } else {
                for (int i = 0; i < node.children.tamanho(); i++) {
                    Arvore<String> child = node.children.get(i);
                    listarEventosRecursivo(child, novoCaminho, eventos);
                }
            }
        }
    }

	private boolean dataValida(int dia, int mes, int ano) {
        if (mes < 1 || mes > 12) {
            return false;
        }
        if (dia < 1) {
            return false;
        }
        int[] diasPorMes = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (anoBissexto(ano)) {
            diasPorMes[1] = 29;
        }
        return dia <= diasPorMes[mes - 1];
    }

    // Verifica se um ano é bissexto
    private boolean anoBissexto(int ano) {
        if ((ano % 4 == 0 && ano % 100 != 0) || (ano % 400 == 0)) {
            return true;
        }
        return false;
    }
    
   

    
    public void removerEvento(int dia, int mes, int ano, Evento evento) {
        Arvore<String> anoNode = root.getChild(String.valueOf(ano));
        if (anoNode != null) {
            Arvore<String> mesNode = anoNode.getChild(String.format("%02d", mes));
            if (mesNode != null) {
                Arvore<String> diaNode = mesNode.getChild(String.format("%02d", dia));
                if (diaNode != null) {
                    for (int i = 0; i < diaNode.children.tamanho(); i++) {
                        Arvore<String> eventoNode = diaNode.children.get(i);
                        if (eventoNode != null && eventoNode.data.equals(evento.toString())) {
                            diaNode.children.removerPorIndice(i);
                            break;
                        }
                    }
                    // Remove o nó do dia se estiver vazio
                    removerNoSeVazio(mesNode, diaNode);
                }
                // Remove o nó do mês se estiver vazio
                removerNoSeVazio(anoNode, mesNode);
            }
            // Remove o nó do ano se estiver vazio
            removerNoSeVazio(root, anoNode);
        }
    }

    private void removerNoSeVazio(Arvore<String> parent, Arvore<String> child) {
        if (child.children.estaVazia()) {
            for (int i = 0; i < parent.children.tamanho(); i++) {
                if (parent.children.get(i) == child) {
                    parent.children.removerPorIndice(i);
                    break;
                }
            }
        }
    }

    public List<String> buscarEventos(int dia, int mes, int ano) {
        List<String> eventos = new ArrayList<>();
        Arvore<String> anoNode = root.getChild(String.valueOf(ano));
        if (anoNode != null) {
            Arvore<String> mesNode = anoNode.getChild(String.format("%02d", mes));
            if (mesNode != null) {
                Arvore<String> diaNode = mesNode.getChild(String.format("%02d", dia));
                if (diaNode != null) {
                    for (int i = 0; i < diaNode.children.tamanho(); i++) {
                        Arvore<String> eventoNode = diaNode.children.get(i);
                        if (eventoNode != null) {
                            eventos.add(eventoNode.data);
                        }
                    }
                }
            }
        }
        return eventos;
    }

    public int contarEventosAno(int ano) {
        int count = 0;
        Arvore<String> anoNode = root.getChild(String.valueOf(ano));
        if (anoNode != null) {
            for (int i = 0; i < anoNode.children.tamanho(); i++) {
                Arvore<String> mesNode = anoNode.children.get(i);
                if (mesNode != null) {
                    for (int j = 0; j < mesNode.children.tamanho(); j++) {
                        Arvore<String> diaNode = mesNode.children.get(j);
                        if (diaNode != null) {
                            count += diaNode.children.tamanho();
                        }
                    }
                }
            }
        }
        return count;
    }

    
public boolean anoBissesto(int ano) {
    if (ano % 4 == 0) {
        if (ano % 100 == 0) {
            return ano % 400 == 0;
        } else {
            return true;
        }
    } else {
        return false;
    }
}

public void editarEvento(int dia, int mes, int ano, Evento eventoAntigo, Evento eventoNovo) {
    Arvore<String> anoNode = root.getChild(String.valueOf(ano));
    if (anoNode != null) {
        Arvore<String> mesNode = anoNode.getChild(String.format("%02d", mes));
        if (mesNode != null) {
            Arvore<String> diaNode = mesNode.getChild(String.format("%02d", dia));
            if (diaNode != null) {
                for (int i = 0; i < diaNode.children.tamanho(); i++) {
                    Arvore<String> eventoNode = diaNode.children.get(i);
                    if (eventoNode != null && eventoNode.data.equals(eventoAntigo.toString())) {
                        eventoNode.data = eventoNovo.toString();
                        break;
                    }
                }
            }
        }
    }
}


	public void limparAgenda() {
		root = new Arvore<>("Agenda");
	}
    public List<String> listarDatasVazias() {
        List<String> datasVazias = new ArrayList<>();
        // Percorre os nós filhos da raiz (anos)
        for (int i = 0; i < root.children.tamanho(); i++) {
            Arvore<String> anoNode = root.children.get(i);
            if (anoNode != null) {
                int ano = Integer.parseInt(anoNode.data);
                // Percorre os meses
                for (int mes = 1; mes <= 12; mes++) {
                    Arvore<String> mesNode = anoNode.getChild(String.format("%02d", mes));
                    // Determina a quantidade máxima de dias para o mês e ano considerando anos bissextos
                    int maxDia = mes == 2 && anoBissexto(ano) ? 29 : new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}[mes - 1];
                    // Percorre os dias
                    for (int dia = 1; dia <= maxDia; dia++) {
                        Arvore<String> diaNode = mesNode == null ? null : mesNode.getChild(String.format("%02d", dia));
                        // Se o nó do dia não existir, adiciona a data na lista de datas vazias
                        if (diaNode == null) {
                            datasVazias.add(String.format("%02d/%02d/%d", dia, mes, ano));
                        }
                    }
                }
            }
        }
        return datasVazias;
    }


}
