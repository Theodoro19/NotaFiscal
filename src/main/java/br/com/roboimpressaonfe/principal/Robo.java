package br.com.roboimpressaonfe.principal;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JProgressBar;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;

public class Robo {

	private PhantomJSDriver driver;
	private Properties p;
	private String paginaInicial;
	private String pastaTempCriada;
	public static int totalNotas;

	public PhantomJSDriver getDriver() {
		return driver;
	}

	public String getPaginaInicial() {
		return paginaInicial;
	}

	public void setPaginaInicial(String paginaInicial) {
		this.paginaInicial = paginaInicial;
	}

	public String getPastaTempCriada() {
		return pastaTempCriada;
	}

	public void setPastaTempCriada(String pastaTempCriada) {
		this.pastaTempCriada = pastaTempCriada;
	}

	public int getTotalNotas() {
		return totalNotas;
	}

	public Robo() {
		try {
			String caminhoPhantomJSEXE = new File("").getAbsolutePath() + "//resources//phantomjs.exe";
			System.setProperty("phantomjs.binary.path", caminhoPhantomJSEXE);
			DesiredCapabilities caps = new DesiredCapabilities();
			caps.setJavascriptEnabled(true);
			caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS,
					new String[] { "--web-security=no", "--ignore-ssl-errors=true", "--ssl-protocol=tlsv1" });
			driver = new PhantomJSDriver(caps);
			driver.manage().window().setSize(new Dimension(1400, 1000));
			p = new Properties();

			FileInputStream in = new FileInputStream(
					new File("").getAbsolutePath() + "//resources//parametros.properties");
			p.load(in);
			in.close();
			this.paginaInicial = p.getProperty("link.pagina.inicial");
			driver.get(this.paginaInicial);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean efetuaLogin(String cnpj, String senha) {
		try {
			WebElement user = driver.findElement(By.name("j_username"));
			user.sendKeys(cnpj);
			WebElement password = driver.findElement(By.xpath("//*[@id='j_password']"));
			password.sendKeys(senha);
			WebElement buttonLogin = driver.findElement(By.xpath("//*[@id='enviar']"));
			buttonLogin.click();
			String verificadorMsgSucessoLogin = "Bem-vindo, " + cnpj;
			if (driver.getPageSource().trim().contains(verificadorMsgSucessoLogin.trim())) {
				return true;
			} else {
				driver.get(this.paginaInicial);
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void abrePaginaNotas(int mes, int ano) {
		try {
			WebElement caixaTipoNotasFiscais = driver
					.findElement(By.xpath("//*[@id='ctl00_wpMenuLateral_mnuRotinasn5']/td/table/tbody/tr/td[1]/a"));
			caixaTipoNotasFiscais.click();
			Select dropdown = new Select(driver.findElement(By.xpath("//*[@id='ctl00_body_ddlContribuinte']")));
			dropdown.selectByIndex(1);
			Select selectAno = new Select(driver.findElement(By.xpath("//*[@id='ctl00_body_ddlExercicio']")));
			selectAno.selectByValue(String.valueOf(ano));
			Select selectMes = new Select(driver.findElement(By.xpath("//*[@id='ctl00_body_ddlMes']")));
			selectMes.selectByValue(String.valueOf(mes));
			WebElement nfsEEmitidas = driver.findElement(By.xpath("//*[@id='ctl00_body_btEmitidas']"));
			nfsEEmitidas.click();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void imprimirNotas() {
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public int baixarNotas(int mes, int ano, JProgressBar progressBar) {
		int notasBaixadas = 0;
		try {
			String pastaTemp = new File("").getAbsolutePath() + "//temp//";
			StringBuilder strB = new StringBuilder();
			strB.append(String.valueOf(mes) + "-" + String.valueOf(ano));
			Path path = Paths.get(pastaTemp + strB.toString());
			Files.createDirectories(path);
			driver = getHandleToWindow(driver);
			int contadorTotal = 1;
			int paginaAtual = 1;
			List<Integer> listaPaginas = new ArrayList<Integer>();
			List<WebElement> tablerow = driver.findElements(By.xpath("//*[@id='ctl00_cphPopUp_dgNotas']/tbody/tr"));
			int tamanhoLista = 0;
			if (tablerow != null) {
				tamanhoLista = tablerow.size();
			}
			WebElement paginas = driver
					.findElement(By.xpath("//*[@id='ctl00_cphPopUp_dgNotas']/tbody/tr[" + tamanhoLista + "]/td"));
			String texto = paginas.getText();
			String[] trimmed = texto.split(" ");
			for (int i = 0; i < trimmed.length; i++) {
				listaPaginas.add(Integer.parseInt(trimmed[i]));
			}
			do {
				tablerow = driver.findElements(By.xpath("//*[@id='ctl00_cphPopUp_dgNotas']/tbody/tr"));
				for (int i = 1; i < tablerow.size(); i++) {
					int porcentagem = (contadorTotal * 100) / totalNotas;
					progressBar.setValue(porcentagem);
					progressBar.setString(porcentagem + "%");
					List<WebElement> cells = tablerow.get(i).findElements(By.tagName("a"));
					long numero = 0;
					try {
						numero = Long.parseLong(cells.get(0).getText());
					} catch (Exception e) {
						// e.printStackTrace();
					}
					if (numero > 0 && i <= tablerow.size() - 2) {
						WebElement link = cells.get(0);
						System.out.println(link.getText());
						link.click();
						driver = getHandleToWindowNota(driver);
						WebElement img = driver.findElement(By.xpath("//*[@id='ctl00_cphBase_img']"));
						String src = img.getAttribute("src");
						URL url = new URL(src);
						BufferedImage image = ImageIO.read(url);
						System.out.println(path.toString());
						this.pastaTempCriada = path.toString();
						ImageIO.write(image, "png", new File(path.toString() + File.separator + numero + ".png"));
						notasBaixadas += 1;
						System.out.println(src);
						driver.close();
						driver = getHandleToWindow(driver);
					}
					contadorTotal += 1;
				}
				mudarPagina(paginaAtual, driver);
				paginaAtual += 1;
			} while (paginaAtual <= listaPaginas.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return notasBaixadas;
	}

	private void mudarPagina(int paginaAtual, WebDriver driver) {
		List<WebElement> tablerow = driver.findElements(By.xpath("//*[@id='ctl00_cphPopUp_dgNotas']/tbody/tr"));
		int tamanhoLista = 0;
		if (tablerow != null) {
			tamanhoLista = tablerow.size();
		}
		List<WebElement> linksPaginas = driver
				.findElements(By.xpath("//*[@id='ctl00_cphPopUp_dgNotas']/tbody/tr[" + tamanhoLista + "]/td/a"));
		for (WebElement aux : linksPaginas) {
			try {
				if (aux.getText().trim().equals(String.valueOf(paginaAtual + 1))) {
					System.out.println("LINK ABERTO: " + String.valueOf(paginaAtual + 1));
					aux.click();
				}
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
	}

	public PhantomJSDriver getHandleToWindow(PhantomJSDriver driver) {
		String title = "Usuário: 63.059.273/0001-21 - NFS-e - Nota Fiscal de Serviços Eletrônica - São Paulo";
		WebDriver popup = null;
		Set<String> windowIterator = driver.getWindowHandles();
		for (String s : windowIterator) {
			String windowHandle = s;
			popup = (PhantomJSDriver) driver.switchTo().window(windowHandle);
			if (popup.getTitle().trim().equals(title.trim())) {
				return (PhantomJSDriver) popup;
			}
		}
		return driver;
	}

	public void voltarPaginaParametrosPesquisa() {
		driver.get("https://nfe.prefeitura.sp.gov.br/contribuinte/consultas.aspx");
	}

	private static PhantomJSDriver getHandleToWindowNota(PhantomJSDriver driver) {
		WebDriver popup = null;
		Set<String> windowIterator = driver.getWindowHandles();
		for (String s : windowIterator) {
			String windowHandle = s;
			popup = driver.switchTo().window(windowHandle);
			if (popup.getCurrentUrl().trim().contains("notaprint")) {
				return (PhantomJSDriver) popup;
			}
		}
		return driver;
	}

	public int contarTotalArquivosAntesDeBaixar() {
		int notasBaixar = 0;
		driver = getHandleToWindow(driver);
		List<WebElement> tablerow = driver.findElements(By.xpath("//*[@id='ctl00_cphPopUp_dgNotas']/tbody/tr"));
		int tamanhoLista = 0;
		if (tablerow != null) {
			tamanhoLista = tablerow.size();
		}
		WebElement paginas = driver
				.findElement(By.xpath("//*[@id='ctl00_cphPopUp_dgNotas']/tbody/tr[" + tamanhoLista + "]/td"));
		String texto = paginas.getText();
		String[] trimmed = texto.split(" ");
		List<Integer> listaPaginas = new ArrayList<Integer>();
		for (int i = 0; i < trimmed.length; i++) {
			listaPaginas.add(Integer.parseInt(trimmed[i]));
		}
		int paginaAtual = 1;
		do {
			notasBaixar += tablerow.size();
			mudarPagina(paginaAtual, driver);
			paginaAtual += 1;
		} while (paginaAtual <= listaPaginas.size());
		mudarPagina(0, driver);
		totalNotas = notasBaixar;
		return notasBaixar;
	}

}
