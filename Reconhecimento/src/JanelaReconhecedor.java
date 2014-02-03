import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

@SuppressWarnings("serial")
public class JanelaReconhecedor extends JFrame{

	// Atributos
	private JButton btnReconhecer;
	private JLabel labelTitulo, labelLinha;
	private JPanel panelBtn;
	private JPanel panelTitulo;
	private JScrollPane spCodigo;
	private JTextArea txtCodigo;
	private Reconhecedor rec;

	// Construtor
	public JanelaReconhecedor() {
		super("Reconhecedor da linguagem");
		initComponents();
	}

	// Inicializador de componentes
	private void initComponents() {
		GridBagConstraints gridBagConstraints;

		panelTitulo = new JPanel();
		labelTitulo = new JLabel();
		labelLinha = new JLabel("Linha: 1");
		spCodigo = new JScrollPane();
		txtCodigo = new JTextArea();
		panelBtn = new JPanel();
		btnReconhecer = new JButton();
		rec = new Reconhecedor();

		getContentPane().setLayout(new GridBagLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setResizable(true);
		setTitle("Reconhecedor");
		setPreferredSize(new Dimension(600, 400));
		labelTitulo.setText("Digite o C\u00f3digo na \u00e1rea abaixo");
		panelTitulo.add(labelTitulo);
		txtCodigo.setWrapStyleWord(true);
		txtCodigo.setLineWrap(true);
		btnReconhecer.setMnemonic('r');

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		getContentPane().add(panelTitulo, gridBagConstraints);

		spCodigo.setViewportView(txtCodigo);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new Insets(1, 1, 1, 1);
		getContentPane().add(spCodigo, gridBagConstraints);

		btnReconhecer.setText("Reconhecer");
		btnReconhecer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnReconhecerActionPerformed(evt);
			}
		});

		panelBtn.add(btnReconhecer);
		txtCodigo.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				if (arg0.getSource() == txtCodigo) {
					labelLinha.setText("Linha: " + txtCodigo.getLineCount());			
				}			
			}			
		});

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		getContentPane().add(labelLinha, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		getContentPane().add(panelBtn, gridBagConstraints);

		pack();
	}

	// Ação do botão Reconhecer
	private void btnReconhecerActionPerformed(ActionEvent evt) {
    	String texto = txtCodigo.getText() + "\n";
    	
        if (rec.verificar(texto))
        	JOptionPane.showMessageDialog(this,"Linguagem reconhecida!\n" + rec.getMensagem(),"Resultado",JOptionPane.INFORMATION_MESSAGE);        	
        else
        	JOptionPane.showMessageDialog(this,"Linguagem não reconhecida!\n\nErro na linha " + rec.getLinha(),"Resultado",JOptionPane.ERROR_MESSAGE);       
        
    }

}
