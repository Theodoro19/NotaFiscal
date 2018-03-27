package br.com.roboimpressaonfe.principal;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.text.MaskFormatter;

import org.joda.time.DateTime;
import org.openqa.selenium.NoSuchElementException;

import edu.emory.mathcs.backport.java.util.Collections;

public class Login {

	private JFrame frame;
	private JPanel loginPanel, parametrosPanel, imprimirPanel;
	private JFormattedTextField cnpjField;
	private JPasswordField passwordField;
	private Robo robo;
	private JLabel statusLabel;
	private JProgressBar progressBar;
	@SuppressWarnings("rawtypes")
	private JComboBox selectMes, selectAno;
	private JButton btnLogin, btnProsseguir;
	private SwingWorker<String, Void> loginWork;
	private SwingWorker<String, Void> prosseguirWork;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Login() {
		try {
			initializeParametrosPanel();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws ParseException
	 */
	private void initializeLoginPanel() throws ParseException {
		frame = new JFrame();

		frame.setTitle("OMA - Impressão de Notas Fiscais");
		frame.setResizable(false);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width / 2 - frame.getWidth() / 2, dim.height / 2 - frame.getHeight() / 2);

		loginPanel = new JPanel();
		loginPanel.setBounds(1, 5, 441, 268);
		frame.getContentPane().add(loginPanel);
		loginPanel.setLayout(null);

		JLabel lblNewLabel = new JLabel("CNPJ:");
		lblNewLabel.setBounds(84, 80, 54, 14);
		loginPanel.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Senha:");
		lblNewLabel_1.setBounds(84, 111, 54, 14);
		loginPanel.add(lblNewLabel_1);

		cnpjField = new JFormattedTextField();
		cnpjField.setBounds(142, 77, 183, 20);
		cnpjField.setValue("63.059.273/0001-21");
		MaskFormatter maskData = new MaskFormatter("##.###.###/####-##");
		maskData.install(cnpjField);
		loginPanel.add(cnpjField);

		passwordField = new JPasswordField();
		passwordField.setBounds(142, 108, 183, 20);
		loginPanel.add(passwordField);

		btnLogin = new JButton("Login");
		btnLogin.setBounds(183, 149, 81, 23);
		loginPanel.add(btnLogin);

		statusLabel = new JLabel("");
		statusLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		statusLabel.setBounds(69, 24, 301, 20);
		loginPanel.add(statusLabel);

		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				statusLabel.setForeground(Color.BLUE);
				statusLabel.setText("Efetuando login. Aguarde...");
				cnpjField.setEnabled(false);
				passwordField.setEnabled(false);
				btnLogin.setEnabled(false);
				frame.revalidate();
				frame.repaint();
				loginStart();
			}
		});
	}

	public void loginStart() {
		loginWork = new SwingWorker<String, Void>() {
			@Override
			protected String doInBackground() {
				try {
					robo = new Robo();
					//boolean sucesso = robo.efetuaLogin(cnpjField.getText(), passwordField.getText());
					boolean sucesso = true;
					if (sucesso) {
						statusLabel.setForeground(Color.BLUE);
						statusLabel.setText("Login efetuado com sucesso!");
						frame.getContentPane().removeAll();
						initializeParametrosPanel();
						frame.revalidate();
						frame.repaint();
					} else {
						statusLabel.setForeground(Color.RED);
						statusLabel.setText("CNPJ ou Senha incorretos!");
					}
				} catch (Exception ex) {
					statusLabel.setForeground(Color.RED);
					statusLabel.setText("Erro ao efetuar o login...");
					ex.printStackTrace();
				}
				return "";
			}

			@Override
			public void done() {
				System.out.println("done");
				cnpjField.setEnabled(true);
				passwordField.setEnabled(true);
				btnLogin.setEnabled(true);
				loginWork.cancel(true);
			}
		};
		loginWork.execute();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initializeParametrosPanel() {
		parametrosPanel = new JPanel();
		frame = new JFrame();

		frame.setTitle("OMA - Impressão de Notas Fiscais");
		frame.setResizable(false);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width / 2 - frame.getWidth() / 2, dim.height / 2 - frame.getHeight() / 2);

		frame.getContentPane().add(parametrosPanel);
		
		List<String> listaAnos = new ArrayList<>();
		String[] meses = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" };
		int anoInicial = 2006;
		int mesAtual = new DateTime().getMonthOfYear();
		int anoAtual = new DateTime().getYear();
		listaAnos.add(String.valueOf(anoInicial));
		while (anoInicial < anoAtual) {
			anoInicial += 1;
			listaAnos.add(String.valueOf(anoInicial));
		}
		Collections.reverse(listaAnos);
		selectMes = new JComboBox(meses);
		selectMes.setBounds(142, 77, 183, 20);
		selectMes.setSelectedItem(String.valueOf(mesAtual));
		selectAno = new JComboBox(listaAnos.toArray());
		selectAno.setBounds(142, 108, 183, 20);
		parametrosPanel = new JPanel();
		parametrosPanel.setLayout(null);
		parametrosPanel.setBounds(1, 5, 441, 268);
		statusLabel = new JLabel("");
		parametrosPanel.add(statusLabel);
		parametrosPanel.add(selectMes);
		parametrosPanel.add(selectAno);
		btnProsseguir = new JButton("Prosseguir");
		btnProsseguir.setBounds(142, 139, 183, 20);
		parametrosPanel.add(btnProsseguir);
		frame.getContentPane().add(parametrosPanel);
		robo = new Robo();
		btnProsseguir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectMes.setEnabled(false);
				selectAno.setEnabled(false);
				btnProsseguir.setEnabled(false);
				statusLabel.setForeground(Color.BLUE);
				statusLabel.setText("Validando as informações. Aguarde...");
				frame.revalidate();
				frame.repaint();
				startProsseguir();
			}
		});
	}

	public void startProsseguir() {
		prosseguirWork = new SwingWorker<String, Void>() {
			@Override
			protected String doInBackground() {
				try {
					robo.abrePaginaNotas(Integer.parseInt(String.valueOf(selectMes.getSelectedItem())),
							Integer.parseInt(String.valueOf(selectAno.getSelectedItem())));
					robo.contarTotalArquivosAntesDeBaixar();
					statusLabel.setText("Baixando os arquivos...");
					progressBar = new JProgressBar(0, 100);
					progressBar.setValue(0);
					progressBar.setStringPainted(true);
					progressBar.setBounds(142, 170, 183, 20);
					parametrosPanel.add(progressBar);
					frame.revalidate();
					frame.repaint();
					int notasBaixadas = robo.baixarNotas(Integer.parseInt(String.valueOf(selectMes.getSelectedItem())),
							Integer.parseInt(String.valueOf(selectAno.getSelectedItem())), progressBar);
					statusLabel.setForeground(Color.BLUE);
					statusLabel.setText("Foram baixadas " + notasBaixadas + " notas fiscais!");
					frame.getContentPane().removeAll();
					initializeImprimirPanel(Integer.parseInt(String.valueOf(selectMes.getSelectedItem())),
							Integer.parseInt(String.valueOf(selectAno.getSelectedItem())));
					frame.revalidate();
					frame.repaint();
					File pastaTempCriada = new File(robo.getPastaTempCriada());
					System.out.println(robo.getPastaTempCriada());
					File[] l = pastaTempCriada.listFiles();
					List<File> listaImagens = new ArrayList<File>();
					for (File aux : l) {
						if (aux.getName().trim().contains("png")) {
							listaImagens.add(aux);
						}
					}
					PngToPDF pngToPDF = new PngToPDF();
					pngToPDF.transformaImagensEmPDF(listaImagens,
							Integer.parseInt(String.valueOf(selectMes.getSelectedItem())),
							Integer.parseInt(String.valueOf(selectAno.getSelectedItem())), robo.getPastaTempCriada());
				} catch (NoSuchElementException e) {
					statusLabel.setForeground(Color.RED);
					statusLabel.setText("Nenhuma nota encontrada para este período!");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				return "";
			}

			@Override
			public void done() {
				selectMes.setEnabled(true);
				selectAno.setEnabled(true);
				btnProsseguir.setEnabled(true);
				robo.voltarPaginaParametrosPesquisa();
				prosseguirWork.cancel(true);
			}
		};
		prosseguirWork.execute();
	}

	private void initializeImprimirPanel(int mes, int ano) {
		imprimirPanel = new JPanel();
		imprimirPanel.setBounds(1, 5, 441, 268);
		imprimirPanel.setLayout(null);
		imprimirPanel.add(statusLabel);
		JButton btnImprimir = new JButton("Imprimir");
		btnImprimir.setBounds(142, 139, 183, 20);
		imprimirPanel.add(btnImprimir);
		frame.getContentPane().add(imprimirPanel);
		btnImprimir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imprimir(mes, ano);
				robo.getDriver().close();
				robo.getDriver().quit();
			}
		});
	}

	public void imprimir(int mes, int ano) {
		try {
			if (Desktop.isDesktopSupported()) {
				try {
					File myFile = new File(this.robo.getPastaTempCriada() + File.separator + "NOTAS-" + mes + "-" + ano + ".pdf");
					Desktop.getDesktop().open(myFile);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}