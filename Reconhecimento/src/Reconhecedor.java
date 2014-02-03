import java.util.*;

public class Reconhecedor {
	// Atributos
	private boolean fimDeLinha;			// indica se houve fim de linha na varredura
	private short 	contador,			// contador da posição atual de leitura do código 
					linha;				// contador da linha atual
	private Map<String, Token> tabela;	// tabela de símbolos
	private Queue<Short> inicioLinhas;	// fila de linhas a serem impressas na próxima impressão de linhas
	private String mensagem;			// mensagem de retorno
	
	// Métodos
	
	// Efetua a análise léxica
	public boolean verificar(String codigo) {
		// Variáveis
		char[] cod = codigo.toCharArray();
		Lexema retorno;
		
		// Inicialização de variáveis e atributos
		tabela = new HashMap<String, Token>();
		inicioLinhas = new LinkedList<Short>();
		mensagem = "Reconhecimento dos tokens:\n";
		contador = 0;
		linha = 1;		
		
		// Inserindo as palavras-chave na tabela de símbolos
		tabela.put("programa", Token.PROGRAMA);
		tabela.put("fim", Token.FIM);
		tabela.put("int", Token.INT);
		tabela.put("leia", Token.LEIA);
		tabela.put("escreva", Token.ESCREVA);
		
		// Inicia a fila de impressão de linhas
		inicioLinhas.add((short)0);
		mensagem += imprimeLinha(cod);
		
		// Para cada linha, imprime a mesma e todos os lexemas reconhecidos, ou retorna código de erro
		do {			
			fimDeLinha = false;
			retorno = obterLexema(cod);
			if (retorno == null) return false;
			
			if (retorno.getTipo() == Token.TERMINO) {
				mensagem += "\nComentários após o último lexema podem ser descartados.";
				return true;
			}					

			if (fimDeLinha == true) mensagem += imprimeLinha(cod);			
			
			// Encadeia um lexema reconhecido			
			mensagem += "< " + imprimeLexema(retorno) + " > / ";
			
		} while (contador < cod.length-1 && retorno != null);
		 
		// Se sucesso, retorna true
		return true;
	}
	
	// Imprime um lexema apropriadamente
	private String imprimeLexema(Lexema lexema) {
		// Variáveis		
		String retorno = null;
		
		// Se for identificador
		if (lexema.getTipo() == Token.ID)
			retorno = lexema.getTipo() + ", nome = " + lexema.getValor();
		// Se for número
		else if (lexema.getTipo() == Token.NUM)
			retorno = lexema.getTipo() + ", valor = " + lexema.getValor();
		// Se for palavra-reservada
		else if (lexema.getTipo() == Token.PROGRAMA || lexema.getTipo() == Token.FIM || lexema.getTipo() == Token.INT || lexema.getTipo() == Token.LEIA || lexema.getTipo() == Token.ESCREVA)
			retorno = "palavra reservada: " + lexema.getValor();
		// Se for símbolo
		else retorno = lexema.getValor();
		
		return retorno;
	}

	// Implementa o autômato finito de reconhecimento de lexemas
	private Lexema obterLexema(char[] codigo) {
		// Variáveis		
		short i = contador;
		char c;
		Estado estado = Estado.INICIO;
		Lexema retorno = null;
		Token tipo = null;
		StringBuffer valor = new StringBuffer();
		
		// Implementação do autômato
		while (estado != Estado.FIM && estado != Estado.ERRO && i < codigo.length) {
			switch (estado) {
				// Estado de início
				case INICIO:
					c = codigo[i];
					if (c == '\n') {
						i++;
						fimDeLinha = true;
						inicioLinhas.add(i);						
					}
					else if (c == ' ' || c == '\t') {
						i++;
					}
					else if (c == '#') {
						i++;
						estado = Estado.COM;
					}
					else if (Character.isDigit(c)) {
						tipo = Token.NUM;
						valor.append(c);
						i++;
						estado = Estado.NUM;						
					}					
					else if (Character.isLetter(c)) {
						tipo = Token.ID;
						valor.append(c);
						i++;
						estado = Estado.ID;						
					}
					else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '%' || c == '=' || c == ';' || c == ',' || c == '(' || c == ')') {
						if (c == '+') tipo = Token.MAIS;
						else if (c == '-') tipo = Token.MENOS;
						else if (c == '*') tipo = Token.VEZES;
						else if (c == '/') tipo = Token.DIVIDIR;
						else if (c == '%') tipo = Token.MODULO;
						else if (c == '=') tipo = Token.IGUAL;
						else if (c == ';') tipo = Token.PONTOEVIRGULA;
						else if (c == ',') tipo = Token.VIRGULA;
						else if (c == '(') tipo = Token.ABREP;
						else if (c == ')') tipo = Token.FECHAP;						
						valor.append(c);
						i++;
						estado = Estado.FIM;
					}
					else {
						estado = Estado.ERRO;
					}					
					break;
				
				// Estado de comentário
				case COM:
					c = codigo[i];
					if (c == '#') {
						i++;
						estado= Estado.INICIO;
					}
					else {
						i++;
						if (c == '\n') {
							fimDeLinha = true;
							inicioLinhas.add(i);
						};
					}
					break;

				// Estado de número
				case NUM:
					c = codigo[i];
					if (Character.isDigit(c)) {
						valor.append(c);						
						i++;						
					}
					else {
						estado = Estado.FIM;						
					}
					break;

				// Estado de identificador
				case ID:
					c = codigo[i];
					if (Character.isLetter(c)) {
						valor.append(c);						
						i++;						
					}
					else {			
						estado = Estado.FIM;						
					}
					break;
					
				// Estado de fim
				case FIM:
					//
					break;

				// Estado de erro
				case ERRO:
					//
					break;					
			}
		}		
		
		contador = i;
		
		if (contador == codigo.length && estado == Estado.INICIO) {
			retorno = new Lexema(Token.TERMINO, "", true);			
		}
		
		// Trata o resultado do autômato
		else if (estado == Estado.FIM) {
			String lex = valor.toString();
			switch (tipo) {
				// Cria um lexema de número
				case NUM:
					retorno = new Lexema(tipo, lex, true);
					break;
					
				// Cria um lexema de identificador ou palavra-chave
				case ID:
					Token tipoId = tabela.get(lex);
					if (tipoId == null) tabela.put(lex, tipo);
					else tipo = tipoId;
					retorno = new Lexema(tipo, lex, false);
					break;
					
				// Cria um lexema de símbolo
				default:
					retorno = new Lexema(tipo, lex, true);
					break;					
			}			
		}
		// Retorna fim da varredura
		else if (estado == Estado.ERRO) {
			retorno = null;
		}		
		
		return retorno;		
	}

	// Efetua a impressão de uma linha de código
	private String imprimeLinha(char[] cod) {
		// Variáveis
		StringBuffer lin = new StringBuffer();
		
		// Imprime o número de linhas que estiver na fila
		while (inicioLinhas.size() > 0) {
			short i = inicioLinhas.remove();
			lin.append("\n" + linha + ":   ");
			while (cod[i] != '\n') {
				lin.append(cod[i]);
				i++;
			}
			linha++;
			lin.append("\n      Tokens: ");
		}
			
		return lin.toString();
	}

	// Métodos set/get
	public short getLinha() {
		return linha;
	}

	public String getMensagem() {
		return mensagem;
	}
}
