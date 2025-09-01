package view;

import controller.RedesController;

import javax.swing.JOptionPane;

public class Principal {
    public static void main(String[] args) {
        RedesController controller = new RedesController();
        int opcao = 0;
        
        while (opcao != 9) {
            String input = JOptionPane.showInputDialog("1 - IP\n2 - Ping\n9 - Sair");
            if (input == null || input.isEmpty()) {
                break;
            }
            
            try {
                opcao = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Opção inválida. Tente novamente.");
                continue;
            }
            
            switch (opcao) {
                case 1:
                    controller.ip();
                    break;
                case 2:
                    controller.ping();
                    break;
                case 9:
                    JOptionPane.showMessageDialog(null, "Finalizando a aplicação.");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida. Tente novamente.");
            }
        }
    }
}