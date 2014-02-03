public class Lexema {
	// Atributos
	private Token tipo;				// Categoria de token
	private boolean isValor;		// É valor ou referência da tabela de símbolos?
	private String entrada, valor;	// Valor e entrada na tabela de símbolos são exclusivos
	
	// Construtores
	public Lexema(Token tipo, String valor, boolean isValor) {
		this.tipo = tipo;
		this.isValor = isValor;
		if (isValor)
			this.valor = valor;
		else
			this.entrada = valor;
	}
	
	// Métodos set/get
	public Token getTipo() {
		return tipo;
	}
	public void setTipo(Token tipo) {
		this.tipo = tipo;
	}
	public String getValor() {
		if (isValor)
			return valor;
		else
			return entrada;
	}
	public void setValor(String valor) {
		if (isValor)
			this.valor = valor;
		else
			this.entrada = valor;
	}	
	
}
